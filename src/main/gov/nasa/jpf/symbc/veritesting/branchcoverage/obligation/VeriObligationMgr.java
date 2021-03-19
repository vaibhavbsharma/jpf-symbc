package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ssa.*;
import gov.nasa.jpf.symbc.BranchListener;
import gov.nasa.jpf.symbc.branchcoverage.TestCaseGenerationMode;
import gov.nasa.jpf.symbc.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.symbc.numeric.GreenConstraint;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.solvers.IncrementalListener;
import gov.nasa.jpf.symbc.sequences.VeriSymbolicSequenceListener;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.SimplifyGreenVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.def.GlobalJRVar;
import gov.nasa.jpf.symbc.veritesting.ast.def.GlobalJRVarSSAExpr;
import gov.nasa.jpf.symbc.veritesting.ast.def.IfThenElseStmt;
import gov.nasa.jpf.symbc.veritesting.ast.def.ObligationVar;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa.GlobalVarPsmMap;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;
import za.ac.sun.cs.green.expr.*;

import java.util.*;

import static gov.nasa.jpf.symbc.BranchListener.evaluationMode;
import static gov.nasa.jpf.symbc.branchcoverage.obligation.CoverageUtil.getWalaInstLineNum;

public class VeriObligationMgr {

    /**
     * used to track the depth of PCChoiceGenerators to keep track of symbolic variables in every path exploration.
     */
    private static int pcDepth = 0;
    static HashSet<String> solutionHash = new HashSet<>(); //ensures that every solution we collect must be unique from the pervious one, otherwise there is something wrong.

    /**
     * This is the main symbolicOblgMap that lives throughout. It carries obligations and their corresponding symbolic
     * expressions that we need to find valuations for.
     */
    public static final LinkedHashMap<Obligation, PriorityQueue<Pair<Expression, Integer>>> symbolicOblgMap = new LinkedHashMap<>();

    /**
     * creates an obligation from ssa if-instruction
     *
     * @param inst
     * @param oblgSide
     * @return
     */
    public static Obligation createOblg(SSAConditionalBranchInstruction inst, ObligationSide oblgSide, IR ir) {
        IMethod m = ir.getMethod();
        String walaPackageName = CoverageUtil.getWalaPackageName(m);
        String className = m.getDeclaringClass().getName().getClassName().toString();
        String methodSig = m.getSelector().toString();
        int instLine = getWalaInstLineNum(m, inst);

        return new Obligation(walaPackageName, className, methodSig, instLine, inst, oblgSide);
    }

    public static Obligation createOblgFromGeneral(Obligation instGeneralOblg, ObligationSide side) {

        assert instGeneralOblg.oblgSide == ObligationSide.GENERAL : "JR If statements must contain general obligations, assumption violated. Failing.";

        return new Obligation(instGeneralOblg.spfPackageName, instGeneralOblg.className, instGeneralOblg.methodSig, instGeneralOblg.instLine, instGeneralOblg.inst, side);
    }

    /**
     * populates the symbolicOblgMap with the current map of obligations and symbolic expression, ideally obtained from veritesting
     * right before linearization.
     *
     * @param oblgToExprsMap
     */
    public static void addSymbolicOblgMap(HashMap<Obligation, ArrayList<Expression>> oblgToExprsMap) {
        for (Map.Entry entry : oblgToExprsMap.entrySet()) {
            PriorityQueue<Pair<Expression, Integer>> symExprToPcDepthQueue = symbolicOblgMap.get(entry.getKey());
            if (symExprToPcDepthQueue == null) {
                PriorityQueue<Pair<Expression, Integer>> newSymExprToPcDepthQueue = new PriorityQueue(new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return -Integer.compare(((Pair<Expression, Integer>) o1).getSecond(), ((Pair<Expression, Integer>) o2).getSecond());
                    }
                });
                addInQueue(newSymExprToPcDepthQueue, (ArrayList<Expression>) entry.getValue());
                symbolicOblgMap.put((Obligation) entry.getKey(), newSymExprToPcDepthQueue);
            } else addInQueue(symExprToPcDepthQueue, (ArrayList<Expression>) entry.getValue());
        }
    }

    private static void addInQueue(PriorityQueue<Pair<Expression, Integer>> queue, ArrayList<Expression> symExprs) {
        for (Expression expr : symExprs)
            queue.add(new Pair(expr, pcDepth));
    }

    private static void decrementPcDepth() {
        --pcDepth;
    }

    public static void incrementPcDepth() {
        ++pcDepth;
    }

    public static void popDepth() {

        ArrayList<Obligation> emptyOblgList = new ArrayList<>();

        for (Map.Entry entry : symbolicOblgMap.entrySet()) {
            PriorityQueue<Pair<Expression, Integer>> symExprQueue = (PriorityQueue<Pair<Expression, Integer>>) entry.getValue();
            while (!symExprQueue.isEmpty() && symExprQueue.peek().getSecond() == pcDepth) symExprQueue.poll();
            if (symExprQueue.size() == 0) emptyOblgList.add((Obligation) entry.getKey());
        }

        //clearing obligations with empty expressions.
        for (Obligation oblg : emptyOblgList)
            symbolicOblgMap.remove(oblg);

        decrementPcDepth();
    }

    public static int getPcDepth() {
        return pcDepth;
    }


    /**
     * collects new coverage by doing the following
     * 1. first it finds out which of the obligations encountered in path merging is not yet covered.
     * 2. utilizes symbolicOblgMap, pc and the solver to ask for coverage for these obligations.
     *
     * @return
     */
    public static ArrayList<Obligation> collectVeritestingCoverage(ThreadInfo ti, LinkedHashSet<Obligation> oblgsNeedsCoverage) {
//        HashSet<Obligation> oblgsNeedsCoverage = getNeedsCoverageOblg();
        ArrayList<Obligation> coveredOblgsOnPath = new ArrayList<>();
        if (oblgsNeedsCoverage.size() > 0) {
            coveredOblgsOnPath = askSolverForCoverage(ti, oblgsNeedsCoverage);
            if (!evaluationMode)
                System.out.println("newly covered obligation on the path: " + coveredOblgsOnPath);
        }
        return coveredOblgsOnPath;
    }

    public static LinkedHashSet<Obligation> getVeriNeedsCoverageOblg() {
        LinkedHashSet<Obligation> oblgNeedsCoverage = new LinkedHashSet<>();

        for (Obligation oblg : symbolicOblgMap.keySet()) {
            if (!ObligationMgr.isOblgCovered(oblg)) oblgNeedsCoverage.add(oblg);
        }
        return oblgNeedsCoverage;
    }

    private static ArrayList<Obligation> askSolverForCoverage(ThreadInfo ti, LinkedHashSet<Obligation> oblgsNeedCoverage) {
        boolean sat = true;
        ArrayList<Obligation> newCoveredOblgsOnPath = new ArrayList<>();
        do {
            Expression disjunctiveOblgExpr = createDisjunctiveExpr(new ArrayList(oblgsNeedCoverage), 0);
            ChoiceGenerator<?> cg = ti.getVM().getChoiceGenerator();
            if (!(cg instanceof PCChoiceGenerator)) {
                ChoiceGenerator<?> prev_cg = cg.getPreviousChoiceGenerator();
                while (!((prev_cg == null) || (prev_cg instanceof PCChoiceGenerator))) {
                    prev_cg = prev_cg.getPreviousChoiceGenerator();
                }
                cg = prev_cg;
            }
            GreenConstraint greenConstraint = new GreenConstraint(disjunctiveOblgExpr);
            if ((cg instanceof PCChoiceGenerator) && ((PCChoiceGenerator) cg).getCurrentPC() != null) {

                PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
                PathCondition pcCopy = pc.make_copy();

                pcCopy._addDet(greenConstraint);

                //this is the place we want to get the attributes of the method calls that has occured so far in SequenceListener.
                List<String> attributes = new ArrayList<>();
                attributes = VeriSymbolicSequenceListener.getMethodAttributes(ti.getVM().getChoiceGenerators());

                Map<String, Object> solution = null;
                if (sat) {
                    if (IncrementalListener.solver != null) IncrementalListener.solver.push();
                    assert (attributes.size() != 0);

                    /*List<Expression> greenExprs = ExprUtil.spfToGreenExpr((List<gov.nasa.jpf.symbc.numeric.Expression>) (List<?>) attributes);
                    for (Expression e : greenExprs)
                        assert e instanceof IntVariable;*/

                    solution = pcCopy.solveWithValuations(attributes);
                    if (IncrementalListener.solver != null) IncrementalListener.solver.pop();
                    if (solution.size() != 0) {
                        ArrayList<Obligation> newCoveredOblgs = checkSolutionsWithObligations(ti.getVM(), oblgsNeedCoverage, solution);
                        oblgsNeedCoverage.removeAll(newCoveredOblgs);
                        newCoveredOblgsOnPath.addAll(newCoveredOblgs);
                        ObligationMgr.addNewOblgsCoverage(newCoveredOblgs);
                        System.out.println("");
                    } else sat = false;
                }
                System.out.println("The solution is " + solution.toString());
            }
        } while (sat && oblgsNeedCoverage.size() != 0);

        return newCoveredOblgsOnPath;
    }

    private static ArrayList<Obligation> checkSolutionsWithObligations(VM vm, HashSet<Obligation> oblgsNeedCoverage, Map<String, Object> solution) {
        ArrayList<Obligation> coveredOblgs = new ArrayList<>();
        for (Obligation oblg : oblgsNeedCoverage) {
            PriorityQueue<Pair<Expression, Integer>> oblgQueue = symbolicOblgMap.get(oblg);
            if (isOblgCoveredInPath(oblgQueue, solution)) coveredOblgs.add(oblg);
        }
        // if we have any new coverage then
        if (!evaluationMode)
            if (solutionHash.contains(solution.toString()))
                assert false : "solution/test case is duplicated, something went wrong. Failing.";
            else
                solutionHash.add(solution.toString());

        if (coveredOblgs.size() == 0) {
            System.out.println("---> useless execution for covering new JR obligations. SPF must have new branch obligation then.");
        }

        //for a single solver output there can't exists multiple valuations for the arguments.
        else
            //generate system test at this point
            if (BranchListener.testCaseGenerationMode != TestCaseGenerationMode.NONE)
                VeriSymbolicSequenceListener.collectVeriTests(vm, solution);

        return coveredOblgs;
    }

    private static boolean isOblgCoveredInPath(PriorityQueue<Pair<Expression, Integer>> queue, Map<String, Object> solution) {
        Iterator<Pair<Expression, Integer>> queueItr = queue.iterator();
        while (queueItr.hasNext()) {
            Expression expr = queueItr.next().getFirst();
            Object solutionValue = solution.get(expr.toString());
            assert (solutionValue == null || solutionValue instanceof Long) : "unexpected type in solution. Assumption Violated. Failing.";

            if (solutionValue != null) return (Long) solutionValue == 1;
            /*SolutionSubstitutionVisitor solutionSubstitutionVisitor = new SolutionSubstitutionVisitor(solution);
            try {
                expr.accept(solutionSubstitutionVisitor);
                Expression substitutedExpr = solutionSubstitutionVisitor.returnExp;
                SimplifyGreenVisitor exprEvalVisitor = new SimplifyGreenVisitor();
                substitutedExpr.accept(exprEvalVisitor);
                if (Operation.TRUE == exprEvalVisitor.returnExp)
                    return true;
            } catch (VisitorException e) {
                e.printStackTrace();
                assert false : "something went wrong during evaluating the green expression. Failing.";
            }*/

            /*if (((Operation) expr).getOperator() == Operation.Operator.NOT) {
                expr = ((Operation) expr).getOperand(0);
                Boolean evalValue = evalExpr(expr, solution);
                if ((evalValue != null) && (!evalValue)) return true;
            } else {
                Boolean evalValue = evalExpr(expr, solution);
                if ((evalValue != null) && (evalValue)) return true;
            }*/
        }
        return false;
    }


    private static Expression createDisjunctiveExpr(ArrayList<Obligation> oblgsNeedCoverage, int index) {
        assert (oblgsNeedCoverage.size() > 0) : "cannot get to this point with no obligation needed to be covered. Failing";

        Expression disjunctExpr = createDisjunctExprPerOblg(symbolicOblgMap.get(oblgsNeedCoverage.get(index++)));

        if (index == oblgsNeedCoverage.size()) {
            assert disjunctExpr != null;
            return disjunctExpr;
        } else {
            return new Operation(Operation.Operator.OR, disjunctExpr, createDisjunctiveExpr(oblgsNeedCoverage, index));
        }

        /*List<Expression> allOblgExprs = new ArrayList<>();
        for (Obligation oblg : oblgsNeedCoverage) {
            PriorityQueue<Pair<Expression, Integer>> queue = symbolicOblgMap.get(oblg);
            for (Pair<Expression, Integer> pair : queue) {
                pair.getFirst().toString();
                allOblgExprs.add(pair.getFirst());
            }
        }

        return new Operation(Operation.Operator.OR, allOblgExprs.toArray(new Expression[allOblgExprs.size()]));*/
    }

    private static Expression createDisjunctExprPerOblg(PriorityQueue<Pair<Expression, Integer>> exprQueue) {
        assert exprQueue.size() > 0 : "cannot have an empty priority queue of expressions, there must be at least one. Failing";
        Iterator<Pair<Expression, Integer>> queueItr = exprQueue.iterator();
        Expression oblgDisjunctiveExpr = new Operation(Operation.Operator.EQ, queueItr.next().getFirst(), new IntConstant(1));

        while (queueItr.hasNext()) {
            oblgDisjunctiveExpr = new Operation(Operation.Operator.OR, oblgDisjunctiveExpr, new Operation(Operation.Operator.EQ, queueItr.next().getFirst(), new IntConstant(1)));
        }
        return oblgDisjunctiveExpr;
    }

    public static void addSymbolicOblgMap(GlobalVarPsmMap gpsm) {
        for (Map.Entry entry : gpsm.table.entrySet()) {
            assert (entry.getKey() instanceof ObligationVar) : "unexpected type for key in gpsm. Assumption Violated. Failing.";
            ObligationVar oblgVar = ((ObligationVar) entry.getKey());
            Obligation oblg = (((ObligationVar) entry.getKey()).oblg);
            PriorityQueue<Pair<Expression, Integer>> symExprToPcDepthQueue = symbolicOblgMap.get(oblg);
            if (symExprToPcDepthQueue == null) {
                PriorityQueue<Pair<Expression, Integer>> newSymExprToPcDepthQueue = new PriorityQueue(new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return -Integer.compare(((Pair<Expression, Integer>) o1).getSecond(), ((Pair<Expression, Integer>) o2).getSecond());
                    }
                });
                newSymExprToPcDepthQueue.add(new Pair(gpsm.getVarExprForKey(oblgVar), pcDepth));
//                addInQueue(newSymExprToPcDepthQueue, gpsm.getVarExprForKey(oblg));
                symbolicOblgMap.put(oblg, newSymExprToPcDepthQueue);
            } else
                symExprToPcDepthQueue.add(new Pair(gpsm.getVarExprForKey(oblgVar), pcDepth));
//            addInQueue(symExprToPcDepthQueue, gpsm.getVarExprForKey(oblg));
        }
    }
}
