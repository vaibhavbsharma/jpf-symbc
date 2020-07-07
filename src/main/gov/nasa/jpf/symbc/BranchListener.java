package gov.nasa.jpf.symbc;


import com.ibm.wala.ipa.callgraph.CallGraphBuilderCancelException;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.util.WalaException;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.report.ConsolePublisher;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.bytecode.branchchoices.util.IFInstrSymbHelper;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.SpfUtil;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.BranchCoverage;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.CoverageStatistics;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.CoverageMode;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.vm.*;

import java.io.*;

import static gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.ObligationMgr.*;

public class BranchListener extends PropertyListenerAdapter implements PublisherExtension {

    boolean firstTime = true;
    public static boolean evaluationMode = false;
    public static String targetClass;
    public static String targetAbsPath;
    public static CoverageMode coverageMode = CoverageMode.COLLECT_PRUNE_GUIDE; //1 for vanilla spf mode, 2 for Branch Coverage mode, 3 for guided SPF

    // used to flag that the executed branch instruction is symbolic or not. In which case the "instructionExecuted" should let the "firstStepInstruction" check in place, i.e., to return to spf to create
    // the appropriate set of choices, otherwise if it isn't symbolic then it will only invoke "instructionExecuted" only once, and thus we shouldn't return then, and we should check and/or collect obligations then
    public static boolean isSymBranchInst = false;

    public static boolean newCoverageFound = false;
    private boolean allObligationsCovered = false;
    private CoverageStatistics coverageStatistics;

    public BranchListener(Config conf, JPF jpf) {
        jpf.addPublisherExtension(ConsolePublisher.class, this);

        if ((conf.getString("targetAbsPath") == null)) {
            System.out.println("target class or its absolute path is undefined in jpf file for coverage. Aborting");
            assert false;
        }
        targetClass = conf.getString("target");
        targetAbsPath = conf.getString("targetAbsPath");

        if (conf.hasValue("evaluationMode")) evaluationMode = conf.getBoolean("evaluationMode");

        if (conf.hasValue("coverageMode")) if (conf.getInt("coverageMode") == 1) coverageMode = CoverageMode.COLLECT_COVERAGE;
        else if (conf.getInt("coverageMode") == 2) coverageMode = CoverageMode.COLLECT_PRUNE;
        else if (conf.getInt("coverageMode") == 3) {
            coverageMode = CoverageMode.COLLECT_GUIDE;
            BranchSymInstructionFactory.GuideBranchExploration = true;
        } else if (conf.getInt("coverageMode") == 4) {
            coverageMode = CoverageMode.COLLECT_PRUNE_GUIDE;
            BranchSymInstructionFactory.GuideBranchExploration = true;
        } else {
            System.out.println("unknown mode. Failing");
            assert false;
        }

        System.out.println("---- CoverageMode = " + coverageMode);

        coverageStatistics = new CoverageStatistics();
    }

    public void executeInstruction(VM vm, ThreadInfo ti, Instruction instructionToExecute) {
        if (coverageMode == CoverageMode.COLLECT_PRUNE || coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE) // pruning only in pruning mode
            if (allObligationsCovered) {
                System.out.println("all obligation covered, ignoring all paths.");
                ti.getVM().getSystemState().setIgnored(true);
                return;
            }

        try {
            if (firstTime) {
                System.out.println("---- CoverageMode = " + coverageMode);
                BranchCoverage.createObligations(ti);
                ObligationMgr.finishedCollection();
                firstTime = false;
                printCoverage();
                printReachability();
                printOblgToBBMap();

                System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-finished obligation collection|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");
            } else {
                if (instructionToExecute instanceof IfInstruction) {
                    isSymBranchInst = SpfUtil.isSymCond(ti, instructionToExecute);
                    if (isSymBranchInst) {
                        if (coverageMode != CoverageMode.COLLECT_COVERAGE) prunOrGuideSPF(ti, instructionToExecute);
                    } //concrete branch will be pruned in instructionExecuted.
                }
            }
        } catch (ClassHierarchyException e) {
            e.printStackTrace();
            assert false;
        } catch (IOException | CallGraphBuilderCancelException | WalaException e) {
            e.printStackTrace();
        }
    }

    private void prunOrGuideSPF(ThreadInfo ti, Instruction instructionToExecute) {
        Obligation oblgThen = CoverageUtil.createOblgFromIfInst((IfInstruction) instructionToExecute, ObligationSide.THEN);
        Obligation oblgElse = CoverageUtil.createOblgFromIfInst((IfInstruction) instructionToExecute, ObligationSide.ELSE);

        Obligation[] uncoveredReachThenOblg = ObligationMgr.isReachableOblgsCovered(oblgThen);
        Obligation[] uncoveredReachElseOblg = ObligationMgr.isReachableOblgsCovered(oblgElse);

        //uncoveredReachThenOblg <=> uncoveredReachElseOblg; iff relation
        assert ((!(uncoveredReachThenOblg == null) || uncoveredReachElseOblg == null) && (!(uncoveredReachElseOblg == null)) || uncoveredReachThenOblg == null);

        if ((uncoveredReachThenOblg == null)) //indicating an obligation that we do not care about covering, i.e., not an application code.
            return;

        System.out.println("before: " + instructionToExecute);


        if ((uncoveredReachElseOblg.length == 0) && (uncoveredReachThenOblg.length == 0) && !newCoverageFound) {//EARLY PRUNING, no new obligation can be reached
            if (coverageMode == CoverageMode.COLLECT_PRUNE || coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE) { //prune only in pruning mode.
                ti.getVM().getSystemState().setIgnored(true);
                System.out.println("EARLY PRUNING CASE: path is ignored");
            }
        } else {//GUIDING HERE - this is not needed in concrete branches
            //default setting is "else" exploration then the "then" exploration. flip if needed
            if ((uncoveredReachThenOblg.length > uncoveredReachElseOblg.length) // if then has more reachable obligations
                    || ((ObligationMgr.isOblgCovered(oblgElse) && !ObligationMgr.isOblgCovered(oblgThen))) //if "else" side has been already covered but the "then" is not covered yet
            ) {
                if (coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE || coverageMode == CoverageMode.COLLECT_GUIDE) // GUIDE: only in guiding mode.
                    if (!ti.isFirstStepInsn()) { // first time around
                        IFInstrSymbHelper.flipBranchExploration = true;
                        System.out.println("flipping then and else sides.");
                    } else IFInstrSymbHelper.flipBranchExploration = false;
            }
        }
    }


    // after the instruction is executed we only need to collect the covered obligation.
    public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
        if (executedInstruction instanceof IfInstruction) collectCoverageAndPrune(executedInstruction, currentThread);
    }


    private void collectCoverageAndPrune(Instruction executedInstruction, ThreadInfo currentThread) {
        if (allObligationsCovered) {
            return;
        }


        //used to check if we are in the case of symbolic instruction and we are hitting for the first time. As we want to only intercept
        //either symbolic instruction after isFirstStepInsn has finished or a concrete instruction
        Instruction nextInst = currentThread.getNextPC();
        if (!currentThread.isFirstStepInsn() && (nextInst == executedInstruction)) { // the second condition indicates that the spf listener have not recognoized a single choice being possible, because if it does then the nextPc would have been either its target or its nextInst
            return;
        }

        ObligationSide oblgSide;
        if (((IfInstruction) executedInstruction).getTarget() == nextInst) oblgSide = ObligationSide.THEN;
        else {
            oblgSide = ObligationSide.ELSE;
            assert (executedInstruction).getNext() == nextInst;
        }

        Obligation oblg = CoverageUtil.createOblgFromIfInst((IfInstruction) executedInstruction, oblgSide);
        if (ObligationMgr.oblgExists(oblg)) {
            System.out.println("after: " + executedInstruction + "---- obligation is: " + oblg);

            if (ObligationMgr.isNewCoverage(oblg)) { //has the side effect of creating a new coverage if not already covered.
                coverageStatistics.recordObligationCovered(oblg);
                if (!newCoverageFound) {
                    newCoverageFound = true;
                }
            }
            if (coverageMode == CoverageMode.COLLECT_PRUNE || coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE)  //prune only in pruning mode.
                prunePath(currentThread, oblg);
//                printCoverage();
        }
        isSymBranchInst = false;
    }


    private void prunePath(ThreadInfo ti, Obligation oblg) {

        Obligation[] uncoveredReachableOblg = ObligationMgr.isReachableOblgsCovered(oblg);

        if ((uncoveredReachableOblg == null)) //indicating an obligation that we do not care about covering, i.e., not an application code.
            return;

        if ((uncoveredReachableOblg.length == 0) && !newCoverageFound) {//no new obligation can be reached
            assert (coverageMode == CoverageMode.COLLECT_PRUNE || coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE) : "pruning can only happen in pruning mode. Failing";
            ti.getVM().getSystemState().setIgnored(true);
            System.out.println("SINGLE CHOICE PRUNING: path is ignored");
        }
    }

    public void threadTerminated(VM vm, ThreadInfo terminatedThread) {
        System.out.println("end of thread");
        newCoverageFound = false;
        allObligationsCovered = ObligationMgr.isAllObligationCovered();
        coverageStatistics.recordCoverageForThread();
    }

    public void stateBacktracked(Search search) {
        System.out.println("backtracking now");
    }


    // -------- the publisher interface
    @Override
    public void publishFinished(Publisher publisher) {
        PrintWriter pw = publisher.getOut();
        publisher.publishTopicStart("Branch Coverage report:");

        printCoverage();
        coverageStatistics.printOverallStats();
    }
}
