package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ssa.*;
import gov.nasa.jpf.symbc.BranchListener;
import gov.nasa.jpf.symbc.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.symbc.numeric.GreenConstraint;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.solvers.IncrementalListener;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ThreadInfo;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.IntVariable;
import za.ac.sun.cs.green.expr.Operation;

import java.util.*;

import static gov.nasa.jpf.symbc.branchcoverage.obligation.CoverageUtil.getWalaInstLineNum;

public class VeriObligationMgr {

    /**
     * used to track the depth of PCChoiceGenerators to keep track of symbolic variables in every path exploration.
     */
    private static int pcDepth = 0;

    /**
     * This is the main symbolicOblgMap that lives throughout. It carries obligations and their corresponding symbolic
     * expressions that we need to find valuations for.
     */
    public static final HashMap<Obligation, PriorityQueue<Pair<Expression, Integer>>> symbolicOblgMap = new HashMap<>();

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
     */
    public static void collectVeritestingCoverage(gov.nasa.jpf.vm.ThreadInfo ti) {
        HashSet<Obligation> oblgsNeedsCoverage = getNeedsCoverageOblg();
        if (oblgsNeedsCoverage.size() > 0) {
            ArrayList<Obligation> coveredOblgsOnPath = askSolverForCoverage(ti, oblgsNeedsCoverage);
            if (!BranchListener.evaluationMode)
                System.out.println("newly covered obligation on the path + " + coveredOblgsOnPath);
        }
    }

    private static HashSet<Obligation> getNeedsCoverageOblg() {
        HashSet<Obligation> oblgNeedsCoverage = new HashSet<>();

        for (Obligation oblg : symbolicOblgMap.keySet()) {
            if (!ObligationMgr.isOblgCovered(oblg)) oblgNeedsCoverage.add(oblg);
        }
        return oblgNeedsCoverage;
    }

    private static ArrayList<Obligation> askSolverForCoverage(ThreadInfo ti, HashSet<Obligation> oblgsNeedCoverage) {
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
                Map<String, Object> solution = null;
                if (sat) {
                    IncrementalListener.solver.push();
                    solution = pcCopy.solveWithValuation(null, null);
                    IncrementalListener.solver.pop();
                    if (solution.size() != 0) {
                        ArrayList<Obligation> newCoveredOblgs = checkSolutionsWithObligations(solution);
                        oblgsNeedCoverage.removeAll(newCoveredOblgs);
                        ObligationMgr.addNewOblgsCoverage(newCoveredOblgs);
                        newCoveredOblgsOnPath.addAll(newCoveredOblgs);
                        System.out.println("");
                    } else
                        sat = false;
                }
                System.out.println("The solution is " + solution.toString());
            }
        } while (sat && oblgsNeedCoverage.size() != 0);

        return newCoveredOblgsOnPath;
    }

    private static ArrayList<Obligation> checkSolutionsWithObligations(Map<String, Object> solution) {
        ArrayList<Obligation> coveredOblg = new ArrayList<>();
        Set<Map.Entry<Obligation, PriorityQueue<Pair<Expression, Integer>>>> oblgsQueue = symbolicOblgMap.entrySet();
        for (Map.Entry oblgQueue : oblgsQueue)
            if (isOblgCoveredInPath((PriorityQueue<Pair<Expression, Integer>>) oblgQueue.getValue(), solution))
                coveredOblg.add((Obligation) oblgQueue.getKey());

        return coveredOblg;
    }

    private static boolean isOblgCoveredInPath(PriorityQueue<Pair<Expression, Integer>> queue, Map<String, Object> solution) {
        Iterator<Pair<Expression, Integer>> queueItr = queue.iterator();
        while (queueItr.hasNext()) {
            Expression expr = queueItr.next().getFirst();
            if (((Operation) expr).getOperator() == Operation.Operator.NOT) {
                expr = ((Operation) expr).getOperand(0);
                Boolean evalValue = evalExpr(expr, solution);
                if ((evalValue != null) && (!evalValue)) return true;
            } else {
                Boolean evalValue = evalExpr(expr, solution);
                if ((evalValue != null) && (evalValue)) return true;
            }
        }
        return false;
    }

    private static Boolean evalExpr(Expression expr, Map<String, Object> solution) {
        Expression oblgName = ((Operation) expr).getOperand(0);
        Integer condValue = ((IntConstant) ((Operation) expr).getOperand(1)).getValue();
        Object solutionVal = solution.get(oblgName.toString());
        if (solutionVal == null) return null;

        int solverVal = ((Long) solutionVal).intValue();
        return (condValue == solverVal);
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
    }

    private static Expression createDisjunctExprPerOblg(PriorityQueue<Pair<Expression, Integer>> exprQueue) {
        assert exprQueue.size() > 0 : "cannot have an empty priority queue of expressions, there must be at least one. Failing";
        Iterator<Pair<Expression, Integer>> queueItr = exprQueue.iterator();
        Expression oblgDisjunctiveExpr = queueItr.next().getFirst();

        while (queueItr.hasNext()) {
            oblgDisjunctiveExpr = new Operation(Operation.Operator.OR, oblgDisjunctiveExpr, queueItr.next().getFirst());
        }
        return oblgDisjunctiveExpr;
    }


}
