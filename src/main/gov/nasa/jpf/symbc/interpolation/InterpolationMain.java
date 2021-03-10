package gov.nasa.jpf.symbc.interpolation;

import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.symbc.interpolation.checking.SubstituteEnvVals;
import gov.nasa.jpf.symbc.interpolation.creation.Trace;
import gov.nasa.jpf.symbc.interpolation.creation.WeakestPreConditionVisitor;
import gov.nasa.jpf.symbc.numeric.GreenConstraint;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.solvers.SolverTranslator;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.SpfUtil;
import gov.nasa.jpf.symbc.veritesting.ast.def.Stmt;
import gov.nasa.jpf.vm.ThreadInfo;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.Operation;

import java.util.Deque;
import java.util.HashMap;

import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.SpfConstraintToGreenExpr;

public class InterpolationMain {

    //We only do the subsumption check if the instruction unique name has an entry here with value "BOTH"
    private static HashMap<String, CoveredSides> coveredInstructionSides = new HashMap<String, CoveredSides>();

    public static void computeInterpolants(ThreadInfo currentThread) {
        Deque<Stmt> stmts = Trace.toAST();
        WeakestPreConditionVisitor.computeInterpolant(stmts, currentThread);
    }

    /**
     * checks if the new path condition is subsumed by the computed interpolant.
     *
     * @param ifInst:    the current symbolic if-statement
     * @param currentPC: the path condition we want to check for subsumption
     * @param ti:        current thread that holds all stack and heap information.
     * @return
     */
    public static boolean checkSubsumption(IfInstruction ifInst, PathCondition currentPC, ThreadInfo ti) {
        /**
         * 1. we check if we have an interpolant for that particular ifInst
         * 2. If yes, we get the interpolant and we bind its slot variables to the particular concrete or symbolic
         * value in the current stack frame. Note that so far we do not handle heap yet, so we do this for the stack slots only.
         * 3. We check statisfiability and we return the result.
         */

        String pgmPoint = InterpolationUtil.getUniqueName(ifInst);

        if (!coveredInstructionSides.containsKey(pgmPoint)) //this is the case when we first hit the instruction through the bytecode, we have not yet created wp for it, i.e., we have not come at the end of the trace.
            return false;

        CoveredSides coveredSide = coveredInstructionSides.get(pgmPoint);

        assert coveredSide != null : "not expecting to check subsumption for an instruction that we have not visited either its then or else sides, one side at least should have been explore until this point. Assumption Violated. Failing";

        if (coveredSide != CoveredSides.BOTH)
            return false;

        Expression interpolant = WeakestPreConditionVisitor.wpMap.get(pgmPoint);
        if (interpolant == null) return false;

        // we have an interpolant for the instruction, so we check subsumption
        Expression instantiatedInterExpr = SubstituteEnvVals.execute(ti, interpolant);

        Expression pcGreenExpr = SpfConstraintToGreenExpr(currentPC.header);
        Operation subsumptionGreenExpr = new Operation(Operation.Operator.IMPLIES, pcGreenExpr, instantiatedInterExpr);
//        PathCondition subsumptionPC = new PathCondition();
        currentPC._addDet(new GreenConstraint(subsumptionGreenExpr));
        if(currentPC.simplify()){
            System.out.println("subsumption occurred");
            return true;
        } return false;
    }

    public static void updateCoveredInstSide(IfInstruction inst, CoveredSides newSide) {
        String pgmPoint = InterpolationUtil.getUniqueName(inst);
        CoveredSides oldSide = coveredInstructionSides.get(pgmPoint);
        if (oldSide == null)
            coveredInstructionSides.put(pgmPoint, newSide);
        else if ((oldSide == CoveredSides.THEN && newSide == CoveredSides.ELSE) || (oldSide == CoveredSides.ELSE && newSide == CoveredSides.THEN))
            coveredInstructionSides.put(pgmPoint, CoveredSides.BOTH);
    }
}
