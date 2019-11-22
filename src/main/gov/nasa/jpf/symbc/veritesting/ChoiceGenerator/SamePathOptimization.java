package gov.nasa.jpf.symbc.veritesting.ChoiceGenerator;

import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.Operation;

import static gov.nasa.jpf.symbc.VeritestingListener.*;
import static gov.nasa.jpf.symbc.veritesting.ChoiceGenerator.StaticBranchChoiceGenerator.isOnlyStaticChoiceSat;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.isSatGreenExpression;

public class SamePathOptimization {

    public static boolean optimizedRegionPath = false;
    public static boolean optimizedReturnPath = false;
    static DynamicRegion dynRegion = null;


    static Expression regionPredicate;
    static Expression thenPredicate;
    static Expression elsePredicate;
    public static Expression earlyReturnPredicate;

    public static boolean optimizedChoices(ThreadInfo ti, Instruction instructionToExecute, StaticBranchChoiceGenerator
            cg)
            throws StaticRegionException {
        dynRegion = cg.region;

        if (isOnlyStaticChoiceSat(dynRegion)) { // checking if no spfcases or early returns exists.

            runOnSamePath(ti, instructionToExecute, dynRegion);

            System.out.println("region summary single path optimization applied.");
            return true;
        } else { // checking if only the region summary or the early return path is true.

            //have the side effect of populating field predicates.
            collectPredicates(ti);

            //simplifyPredicates
            ExprUtil.SatResult isRegionPredSat = isSatGreenExpression(regionPredicate);
            ExprUtil.SatResult isThenPredSat = isSatGreenExpression(thenPredicate);
            ExprUtil.SatResult isElsePredSat = isSatGreenExpression(elsePredicate);
            ExprUtil.SatResult isReturnPredSat = isSatGreenExpression(earlyReturnPredicate);

            //checking if either the region path or the return path are the only sat paths.
            if ((isRegionPredSat == ExprUtil.SatResult.TRUE) &&
                    (isThenPredSat == ExprUtil.SatResult.FALSE) &&
                    (isElsePredSat == ExprUtil.SatResult.FALSE) &&
                    (isReturnPredSat == ExprUtil.SatResult.FALSE)) { //region summary is only true
                runOnSamePath(ti, instructionToExecute, dynRegion);
                System.out.println("region summary single path optimization applied.");
                return true;
            } else if ((isRegionPredSat == ExprUtil.SatResult.FALSE) &&
                    (isThenPredSat == ExprUtil.SatResult.FALSE) &&
                    (isElsePredSat == ExprUtil.SatResult.FALSE) &&
                    (isReturnPredSat == ExprUtil.SatResult.TRUE)) {//early return is only true.
                runReturnSamePath(ti, instructionToExecute, dynRegion);
                System.out.println("early return summary single path optimization applied.");
                return true;
            }

            dynRegion = null;
            return false;
        }
    }

    //has the side effect of populating predicate fields of the class.
    private static void collectPredicates(ThreadInfo ti) throws StaticRegionException {

        ExprUtil.SatResult isSPFPredSat = isSatGreenExpression(dynRegion.spfPredicateSummary);

        if (dynRegion.earlyReturnResult.hasER()) {
            regionPredicate = new Operation(Operation.Operator.AND, dynRegion.regionSummary,
                    (new Operation(Operation.Operator.AND,
                            new Operation(Operation.Operator.NOT, dynRegion.spfPredicateSummary),
                            new Operation(Operation.Operator.NOT, dynRegion.earlyReturnResult.condition))));
            if (isSPFPredSat != ExprUtil.SatResult.FALSE) {
                thenPredicate = new Operation(Operation.Operator.AND, dynRegion.regionSummary, dynRegion.spfPredicateSummary);
                elsePredicate = new Operation(Operation.Operator.AND, dynRegion.regionSummary, dynRegion.spfPredicateSummary);
            } else {
                thenPredicate = Operation.FALSE;
                elsePredicate = Operation.FALSE;
            }
            earlyReturnPredicate = new Operation(Operation.Operator.AND, dynRegion.regionSummary,
                    (new Operation(Operation.Operator.AND,
                            new Operation(Operation.Operator.NOT, dynRegion.spfPredicateSummary),
                            dynRegion.earlyReturnResult.condition)));
        } else {
            regionPredicate = new Operation(Operation.Operator.AND, dynRegion.regionSummary, new Operation(Operation
                    .Operator.NOT, dynRegion.spfPredicateSummary));
            if (isSPFPredSat != ExprUtil.SatResult.FALSE) {
                thenPredicate = new Operation(Operation.Operator.AND, dynRegion.regionSummary, dynRegion.spfPredicateSummary);
                elsePredicate = new Operation(Operation.Operator.AND, dynRegion.regionSummary, dynRegion.spfPredicateSummary);
            } else {
                thenPredicate = Operation.FALSE;
                elsePredicate = Operation.FALSE;
            }
            earlyReturnPredicate = Operation.FALSE;
        }


    }

    public static void runOnSamePath(ThreadInfo ti, Instruction instructionToExecute, DynamicRegion dynRegion)
            throws StaticRegionException {

        assert !optimizedReturnPath;
        optimizedRegionPath = true;

        Instruction nextInstruction = setupSPF(ti, instructionToExecute, dynRegion, null);
        ++veritestRegionCount;
        ti.setNextPC(nextInstruction);
        statisticManager.updateVeriSuccForRegion(key);

        optimizedRegionPath = false;
        dynRegion = null;

    }


    public static void runReturnSamePath(ThreadInfo ti, Instruction instructionToExecute, DynamicRegion dynRegion)
            throws StaticRegionException {

        assert !optimizedRegionPath;

        optimizedReturnPath = true;
        Instruction nextInstruction = setupSPF(ti, instructionToExecute, dynRegion, null);
        ++veritestRegionCount;
        ti.setNextPC(nextInstruction);
        statisticManager.updateVeriSuccForRegion(key);

        optimizedReturnPath = false;
        dynRegion = null;
    }
}
