package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ssa.*;
import gov.nasa.jpf.symbc.BranchListener;
import gov.nasa.jpf.symbc.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.Operation;

import java.io.PrintWriter;
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
            } else
                addInQueue(symExprToPcDepthQueue, (ArrayList<Expression>) entry.getValue());
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
        for (Map.Entry entry : symbolicOblgMap.entrySet()) {
            PriorityQueue<Pair<Expression, Integer>> symExprQueue = (PriorityQueue<Pair<Expression, Integer>>) entry.getValue();
            while (!symExprQueue.isEmpty() && symExprQueue.peek().getSecond() == pcDepth)
                symExprQueue.poll();
        }
        decrementPcDepth();
    }
}
