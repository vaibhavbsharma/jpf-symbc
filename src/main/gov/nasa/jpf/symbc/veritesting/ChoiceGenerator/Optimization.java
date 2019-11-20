package gov.nasa.jpf.symbc.veritesting.ChoiceGenerator;

import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import za.ac.sun.cs.green.expr.Expression;

import static gov.nasa.jpf.symbc.VeritestingListener.*;
import static gov.nasa.jpf.symbc.veritesting.ChoiceGenerator.StaticBranchChoiceGenerator.isOnlyStaticChoiceSat;

public class Optimization {

    //This creates the possible to be predicates of the cg and check if only the region predicate or the early return
    // predicate are feasible while both the then and the else predicates being false. If this is the case then the
    // right setup is invoked for each case. The only viable action after this, should be to abort the
    // VeritestingListner and resume execution.
    public static boolean optimizedCG(ThreadInfo ti, Instruction instructionToExecute, StaticBranchChoiceGenerator cg) throws StaticRegionException {
        /*
    Expression regionPredicate;
    Expression thenPredicate;
    Expression elsePredicate;
    Expression earlyReturnPredicate;*/


        if (isOnlyStaticChoiceSat(cg.region)) {
            /*Instruction nextInstruction = setupSPF(ti, instructionToExecute, cg.region, null);
            ++veritestRegionCount;
            ti.setNextPC(nextInstruction);
            statisticManager.updateVeriSuccForRegion(key);
            System.out.println("------------- Region was successfully veritested --------------- ");*/

            runOnSamePath(ti, instructionToExecute, cg.region);
            return true;
        }
        return false;
    }
}
