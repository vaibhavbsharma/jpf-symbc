package gov.nasa.jpf.symbc.veritesting.ChoiceGenerator;

import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import za.ac.sun.cs.green.expr.Expression;

import static gov.nasa.jpf.symbc.VeritestingListener.*;
import static gov.nasa.jpf.symbc.veritesting.ChoiceGenerator.StaticBranchChoiceGenerator.isOnlyStaticChoiceSat;

public class SamePathOptimization {

    public static boolean optimize = false;
    static DynamicRegion dynRegion = null;

    /*
    Expression regionPredicate;
    Expression thenPredicate;
    Expression elsePredicate;
    Expression earlyReturnPredicate;*/

    //used to register the flag for optimization to populate the output when the flag of isFirstStep
    public static void registerSamePathOpt(StaticBranchChoiceGenerator cg) {
        optimize = true;
        dynRegion = cg.region;
    }


    //This is the condition under which an optimization is possible.
//TODO:: update the statistics
    public static boolean canOptimize(ThreadInfo ti, Instruction instructionToExecute, StaticBranchChoiceGenerator
            cg)
            throws StaticRegionException {

        if (isOnlyStaticChoiceSat(cg.region)) {
            registerSamePathOpt(cg); // optimize the register the case for next time when we hit.
            ti.setNextPC(instructionToExecute);
            return true;
        } else return false;
    }

    public static void doOptimization(ThreadInfo ti, Instruction instructionToExecute)
            throws StaticRegionException {
        assert optimize = true && dynRegion != null;

        runOnSamePath(ti, instructionToExecute, dynRegion);

        optimize = false;
        dynRegion = null;
    }

    public static void runOnSamePath(ThreadInfo ti, Instruction instructionToExecute, DynamicRegion dynRegion)
            throws StaticRegionException {
        Instruction nextInstruction = setupSPF(ti, instructionToExecute, dynRegion, null);
        ++veritestRegionCount;
        ti.setNextPC(nextInstruction);
        statisticManager.updateVeriSuccForRegion(key);

    }

}
