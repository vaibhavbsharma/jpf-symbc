package gov.nasa.jpf.symbc;

import com.ibm.wala.util.WalaException;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.branchcoverage.BranchCoverage;
import gov.nasa.jpf.symbc.branchcoverage.CoverageMode;
import gov.nasa.jpf.symbc.branchcoverage.CoverageStatistics;
import gov.nasa.jpf.symbc.branchcoverage.TestCaseGenerationMode;
import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.SpfUtil;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.VeriObligationMgr;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

import com.ibm.wala.ipa.callgraph.*;

import java.io.IOException;
import java.util.HashSet;

import static gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr.*;
import static gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.VeriObligationMgr.collectVeritestingCoverage;
import static gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.VeriObligationMgr.getVeriNeedsCoverageOblg;

public class VeriBranchListener extends BranchListener {
    public static HashSet<Obligation> newCoveredOblg = new HashSet<>();

    public VeriBranchListener(Config conf, JPF jpf) {
        super(conf, jpf);
        if (conf.hasValue("coverageMode")) {
            int coverageNum = conf.getInt("coverageMode");
            assert coverageNum > 4 : "coverageMode must be greater that 4 to support Veritesting";
            if (conf.getInt("coverageMode") == 5) coverageMode = CoverageMode.JRCOLLECT_COVERAGE;
            else if (conf.getInt("coverageMode") == 6) coverageMode = CoverageMode.JRCOLLECT_PRUNE;
            else if (conf.getInt("coverageMode") == 7) coverageMode = CoverageMode.JRCOLLECT_GUIDE;
            else if (conf.getInt("coverageMode") == 8) coverageMode = CoverageMode.JRCOLLECT_PRUNE_GUIDE;
            else { //default setting for coverage
                coverageMode = CoverageMode.JRCOLLECT_PRUNE_GUIDE;
            }
            VeritestingListener.simplify = false;
        }

        if (conf.hasValue("TestCaseGenerationMode")) {
            int coverageNum = conf.getInt("TestCaseGenerationMode");

            if (conf.getInt("TestCaseGenerationMode") == 1)
                testCaseGenerationMode = TestCaseGenerationMode.SYSTEM_LEVEL;
            else if (conf.getInt("TestCaseGenerationMode") == 2)
                testCaseGenerationMode = TestCaseGenerationMode.UNIT_LEVEL;
            else testCaseGenerationMode = TestCaseGenerationMode.NONE;
        }
        coverageStatistics = new CoverageStatistics();
    }


    public void executeInstruction(VM vm, ThreadInfo ti, Instruction instructionToExecute) {
        /*if (coverageMode == CoverageMode.COLLECT_PRUNE || coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE) // pruning only in pruning mode
            if (allObligationsCovered) {
                if (!evaluationMode)
                    System.out.println("all obligation covered, ignoring all paths.");
                ti.getVM().getSystemState().setIgnored(true);
                return;
            }
*/
        try {
            if (firstTime) {
                System.out.println("---- CoverageMode = " + coverageMode + ", solver = " + solver + ", benchmark= " + benchmarkName + (System.getenv("MAX_STEPS") != null ? ", STEPS " + System.getenv("MAX_STEPS") : ""));
                BranchCoverage.createObligations(ti);
                ObligationMgr.finishedCollection();
                BranchCoverage.finishedCollection();
                firstTime = false;
                timeZero = System.currentTimeMillis();
                if (!evaluationMode) {
                    printCoverage();
                    printReachability();
                    printOblgToBBMap();
                }
                System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-finished obligation collection|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");
            } else {
                if (instructionToExecute instanceof IfInstruction && (!VeritestingListener.veritestingSuccessful)) {
                    isSymBranchInst = SpfUtil.isSymCond(ti, instructionToExecute);
                    if (isSymBranchInst) {
                        if ((coverageMode == CoverageMode.JRCOLLECT_GUIDE) ||
                                (coverageMode == CoverageMode.JRCOLLECT_PRUNE) ||
                                (coverageMode == CoverageMode.JRCOLLECT_PRUNE_GUIDE))
                            prunOrGuideSPF(ti, instructionToExecute);
                    } //concrete branch will be pruned in instructionExecuted.
                }
            }
        } catch (IOException | CallGraphBuilderCancelException | WalaException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {

        //if veritesting was not successful then we must have encountered a branch and so there is a pcDepth at that point that we need to account for.
        if ((executedInstruction instanceof IfInstruction)) {
//            isSymBranchInst = SpfUtil.isSymCond(currentThread, instructionToExecute);
//            if (isSymBranchInst && !VeritestingListener.veritestingSuccessful)
            if (!VeritestingListener.veritestingSuccessful)
                super.instructionExecuted(vm, currentThread, nextInstruction, executedInstruction);
        }
    }

    @Override
    public void threadTerminated(VM vm, ThreadInfo terminatedThread) {
        if (!evaluationMode) System.out.println("end of thread");
//        newCoverageFound = false;
        newCoveredOblg.clear();
        HashSet<Obligation> veriOblgsNeedsCoverage = getVeriNeedsCoverageOblg();
        if (veriOblgsNeedsCoverage.size() > 0) {
            newCoveredOblg = new HashSet<>(collectVeritestingCoverage(terminatedThread, veriOblgsNeedsCoverage));
            for (Obligation oblg : newCoveredOblg)
                coverageStatistics.recordObligationCovered(oblg);
        }

        //the case where the path contains no veriObligations, the computation of allObligationCovered might not be
        //reflective of the actual coverage, because the actually coverage by assumption is going to be checked with
        //ThreadSymbolicSequenceListener, which is - according to recommended jpf setup- is fired after this listener.
        // thus we need to provide VeriSymbolicSequenceListener to do the the same behaviour.
        /*allObligationsCovered = ObligationMgr.isAllObligationCovered();
        coverageStatistics.recordCoverageForThread();*/
        updateCoverageEndOfPath();
        newCoverageFound = false;
    }

    public static void updateCoverageEndOfPath() {
        allObligationsCovered = ObligationMgr.isAllObligationCovered();
        coverageStatistics.recordCoverageForThread();
    }

    @Override
    public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
        if (currentCG instanceof PCChoiceGenerator) {
            if (!evaluationMode) {
//                System.out.println("choiceGeneratorAdvanced: at " + currentCG.getInsn().getMethodInfo() + "#" + currentCG.getInsn().getPosition());
                System.out.println("pcDepth = " + VeriObligationMgr.getPcDepth());
            }
            VeriObligationMgr.incrementPcDepth();
        }
    }


    @Override
    public void stateBacktracked(Search search) {
        if (search.getVM().getSystemState().getChoiceGenerator() instanceof PCChoiceGenerator) {
            VeriObligationMgr.popDepth();
            if (!evaluationMode) {
//                System.out.println("backtracking now");
                System.out.println("pcDepth = " + VeriObligationMgr.getPcDepth());
            }
        }
    }
}