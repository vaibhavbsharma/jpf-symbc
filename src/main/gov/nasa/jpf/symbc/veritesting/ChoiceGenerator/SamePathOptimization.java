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
    
    public static boolean canOptimize(ThreadInfo ti, Instruction instructionToExecute, StaticBranchChoiceGenerator
            cg)
            throws StaticRegionException {

        if (isOnlyStaticChoiceSat(cg.region)) {
            dynRegion = cg.region;
            optimize = true;
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
