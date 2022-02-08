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
import gov.nasa.jpf.symbc.branchcoverage.TestCaseGenerationMode;
import gov.nasa.jpf.symbc.bytecode.branchchoices.optimization.util.IFInstrSymbHelper;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.SpfUtil;
import gov.nasa.jpf.symbc.branchcoverage.BranchCoverage;
import gov.nasa.jpf.symbc.branchcoverage.statistics.CoverageStatistics;
import gov.nasa.jpf.symbc.branchcoverage.CoverageMode;
import gov.nasa.jpf.symbc.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.vm.*;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr.*;
import static gov.nasa.jpf.symbc.sequences.ThreadSymbolicSequenceListener.addOnTheGoMethodSequence;

public class BranchListener extends PropertyListenerAdapter implements PublisherExtension {

    // this is used to hold signature of methods that we know we do not want to cover its oblgations, like the setup for running multiple steps in WBS and TCAS
    //must not have simicolon in it
    public static HashSet<String> coverageExclusions = new HashSet<>();
    //an optimization that allows collection of coverage and of test cases by utilizing solver calls already done during execution
    // as opposed to waiting until the end of the path to ask
    public static boolean tcgOnTheGo = false;
    //holds the solution of a solver query to extract test cases from it. Unfortuntely due to how pathcondition class gets updated with getCurrentPC, and the fact that we do not know the cg during solving a PC,
    //required this sort of hack where we are keeping the solution of the last query here. It gets cleared up once it is used, thus can only be used once, and that one time should also happen here.
    public static Map<String, Object> onTheGoSolution = new HashMap<>();
    //This flag is used to allow evaluation of PlainJR that uses the same solver call for generating the model, though we are not interested in the model at this point.
    public static boolean useSolverModelPath = true;
    boolean firstTime = true;
    public static boolean evaluationMode = false;
    public static String targetClass;
    public static String targetAbsPath;
    public static CoverageMode coverageMode = CoverageMode.JR_PLAIN; //1 for vanilla spf mode, 2 for Branch Coverage mode, 3 for guided SPF
    public static TestCaseGenerationMode testCaseGenerationMode = TestCaseGenerationMode.SYSTEM_LEVEL;
    public static boolean interproceduralReachability = false;

    // used to flag that the executed branch instruction is symbolic or not. In which case the "instructionExecuted" should let the "firstStepInstruction" check in place, i.e., to return to spf to create
    // the appropriate set of choices, otherwise if it isn't symbolic then it will only invoke "instructionExecuted" only once, and thus we shouldn't return then, and we should check and/or collect obligations then
    public static boolean isSymBranchInst = false;

    public static boolean newCoverageFound = false;
    protected static boolean allObligationsCovered = false;
    protected static CoverageStatistics coverageStatistics;
    public static String benchmarkName;
    public static Long timeZero;
    public static boolean pathCoverage = false;

    public static String solver;

    protected static Long startTime = System.currentTimeMillis() / 1000;
    protected static int timeForExperiment = 60 * 60; //180 * 60; //minutes * seconds -- set to 0 if you want to run indefinitely.

    public BranchListener(Config conf, JPF jpf) {
        jpf.addPublisherExtension(ConsolePublisher.class, this);

        if ((conf.getString("targetAbsPath") == null)) {
            System.out.println("target class or its absolute path is undefined in jpf file for coverage. Aborting");
            assert false;
        }
        targetClass = conf.getString("target");
        targetAbsPath = conf.getString("targetAbsPath");

        if (conf.hasValue("evaluationMode")) {
            evaluationMode = conf.getBoolean("evaluationMode");
            if (evaluationMode && timeForExperiment <= 0)
                System.out.println("RUNNING EXPERIMENT WITH TIME BUDGET =  " + timeForExperiment);
        }

        if (conf.hasValue("coverageExclusions")) coverageExclusions = conf.getStringSet("coverageExclusions");

        if (conf.hasValue("symbolic.dp")) solver = conf.getString("symbolic.dp");

        if (conf.hasValue("interproceduralReachability"))
            interproceduralReachability = conf.getBoolean("interproceduralReachability");


        if (conf.hasValue("coverageMode")) {
            if (conf.getInt("coverageMode") == 1) coverageMode = CoverageMode.SPF;
            else if (conf.getInt("coverageMode") == 2) coverageMode = CoverageMode.COLLECT_PRUNE;
            else if (conf.getInt("coverageMode") == 3) coverageMode = CoverageMode.COLLECT_GUIDE;
            else if (conf.getInt("coverageMode") == 4) coverageMode = CoverageMode.COLLECT_PRUNE_GUIDE;
            else if (conf.getInt("coverageMode") == 9) coverageMode = CoverageMode.JR_PLAIN;
            else if (!conf.hasValue("veritestingMode")) {
                System.out.println("unknown mode. Failing");
                assert false;
            } else {
                coverageMode = CoverageMode.JRCOLLECT_PRUNE_GUIDE;
            }

            BranchSymInstructionFactory.GuideBranchExploration = true;
        }
        benchmarkName = setBenchmarkName(conf.getString("target"));

        if (conf.hasValue("pathcoverage"))
            pathCoverage = conf.getBoolean("pathcoverage");

        if (conf.hasValue("tcgOnTheGo"))
            tcgOnTheGo = conf.getBoolean("tcgOnTheGo");

        if (coverageMode.ordinal() < 5 || coverageMode == CoverageMode.JR_PLAIN)
            coverageStatistics = new CoverageStatistics();



    }

    public static void setupAndRecordStats(Instruction instruction, long endTime, boolean terminated) {
        if (coverageStatistics == null)
            coverageStatistics = new CoverageStatistics();

        recordSolvingInStatistics(instruction, endTime, terminated);
    }

    private String setBenchmarkName(String target) {
        int classIndex = target.lastIndexOf(".");
        if (classIndex == -1) return target;
        else return target.substring(classIndex + 1);
    }

    public void executeInstruction(VM vm, ThreadInfo ti, Instruction instructionToExecute) {

        if (timeForExperiment > 0) {
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime - startTime >= timeForExperiment) //ignore and report the results if time budget was hit.
                ti.getVM().getSystemState().setIgnored(true);
        }

        if(coverageMode == CoverageMode.JR_PLAIN)
            return;

        if (coverageMode == CoverageMode.COLLECT_PRUNE || coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE) // pruning only in pruning mode
            if (allObligationsCovered) {
                if (!evaluationMode) System.out.println("all obligation covered, ignoring all paths.");
                ti.getVM().getSystemState().setIgnored(true);
                return;
            }

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
                if (instructionToExecute instanceof IfInstruction) {
                    isSymBranchInst = SpfUtil.isSymCond(ti, instructionToExecute);
                    if (isSymBranchInst) {
                        if ((coverageMode == CoverageMode.COLLECT_GUIDE) ||
                                (coverageMode == CoverageMode.COLLECT_PRUNE) ||
                                (coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE))
                            prunOrGuideSPF(ti, instructionToExecute);
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

    protected void prunOrGuideSPF(ThreadInfo ti, Instruction instructionToExecute) {
        if(coverageMode == CoverageMode.JR_PLAIN)
            return;

        Obligation oblgThen = CoverageUtil.createOblgFromIfInst((IfInstruction) instructionToExecute, ObligationSide.TAKEN);
        Obligation oblgElse = CoverageUtil.createOblgFromIfInst((IfInstruction) instructionToExecute, ObligationSide.NOT_TAKEN);


        Obligation[] uncoveredReachThenOblg = ObligationMgr.isReachableOblgsCovered(oblgThen);
        Obligation[] uncoveredReachElseOblg = ObligationMgr.isReachableOblgsCovered(oblgElse);

        //uncoveredReachThenOblg <=> uncoveredReachElseOblg; iff relation
        assert ((!(uncoveredReachThenOblg == null) || uncoveredReachElseOblg == null) && (!(uncoveredReachElseOblg == null)) || uncoveredReachThenOblg == null);

        if ((uncoveredReachThenOblg == null)) //indicating an obligation that we do not care about covering, i.e., not an application code.
            return;

//        if (!evaluationMode) System.out.println("before: " + instructionToExecute);

        if (ObligationMgr.intraproceduralInvokeReachable(oblgThen) || ObligationMgr.intraproceduralInvokeReachable(oblgElse))// turn off pruning and guiding in case we have a method invocation reachable along the way.
            return;

        if ((uncoveredReachElseOblg.length == 0) && (uncoveredReachThenOblg.length == 0) && !newCoverageFound) {//EARLY PRUNING, no new obligation can be reached
            if (coverageMode == CoverageMode.COLLECT_PRUNE || coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE
                    || coverageMode == CoverageMode.JRCOLLECT_PRUNE || coverageMode == CoverageMode.JRCOLLECT_PRUNE_GUIDE) { //prune only in pruning mode.
                ti.getVM().getSystemState().setIgnored(true);
                if (!evaluationMode) System.out.println("EARLY PRUNING CASE: path is ignored");
            }
        } else {//GUIDING HERE - this is not needed in concrete branches
            //default setting is "else" exploration then the "then" exploration. flip if needed
            if ((uncoveredReachThenOblg.length > uncoveredReachElseOblg.length) // if then has more reachable obligations
                    || ((ObligationMgr.isOblgCovered(oblgElse) && !ObligationMgr.isOblgCovered(oblgThen))) //if "else" side has been already covered but the "then" is not covered yet
            ) {
                if (coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE || coverageMode == CoverageMode.COLLECT_GUIDE
                        || coverageMode == CoverageMode.JRCOLLECT_PRUNE_GUIDE || coverageMode == CoverageMode.JRCOLLECT_GUIDE) // GUIDE: only in guiding mode.
                    if (!ti.isFirstStepInsn()) { // first time around
                        IFInstrSymbHelper.flipBranchExploration = true;
                        if (!evaluationMode) System.out.println("flipping then and else sides.");
                    } else IFInstrSymbHelper.flipBranchExploration = false;
            }
        }
    }


    // after the instruction is executed we only need to collect the covered obligation.
    public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
        if(coverageMode == CoverageMode.JR_PLAIN)
            return;
        if (currentThread.getVM().isIgnoredState()) {
            if (!evaluationMode) System.out.println("Path UNSAT- State Ignored.");
            return;
        }
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
        if (((IfInstruction) executedInstruction).getTarget() == nextInst) oblgSide = ObligationSide.TAKEN;
        else {
            oblgSide = ObligationSide.NOT_TAKEN;
            assert (executedInstruction).getNext() == nextInst;
        }

        Obligation oblg = CoverageUtil.createOblgFromIfInst((IfInstruction) executedInstruction, oblgSide);
        if (ObligationMgr.oblgExists(oblg)) {
            if (!evaluationMode) System.out.println("after: " + executedInstruction + "---- obligation is: " + oblg);

            if (ObligationMgr.isNewCoverage(oblg)) { //has the side effect of creating a new coverage if not already covered.
                assert coverageStatistics != null : "coverageStatistics cannot be null, this is probably a configuration problem. Assumption violated. Failing.";
                if (!evaluationMode) System.out.println("New coverage found -- " + oblg);
                coverageStatistics.recordObligationCovered(oblg, false);
                if (!newCoverageFound) {
                    newCoverageFound = true;
                }
                if (tcgOnTheGo) { //collect test cases for branch coverage
                    if (!instructionExecutedInExeclusions(executedInstruction)) {
                        addOnTheGoMethodSequence(currentThread.getVM(), getOnTheGoSolution());
                        newCoverageFound = false; // if we are in the onTheGo mode then, marking coverage to the end of the path is useless.
                        System.out.println("printing spf testcases");
                    }
                    newCoverageFound = false; // if we are in the onTheGo mode then, marking coverage to the end of the path is useless.
                }
                // else, we'll collect the test cases at the end of the path
            }
            if (coverageMode == CoverageMode.COLLECT_PRUNE || coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE)  //prune only in pruning mode.
                if (ObligationMgr.intraproceduralInvokeReachable(oblg)) return;
                else prunePath(currentThread, oblg);
//                printCoverage();
        }
        isSymBranchInst = false;
    }


    private boolean instructionExecutedInExeclusions(Instruction executedInstruction) {
        return coverageExclusions.stream().anyMatch(x -> executedInstruction.getMethodInfo().toString().contains(x));
    }

    public static void addOnTheGoSolution(Map<String, Object> solution) {
        onTheGoSolution = solution;
    }

    public static Map<String, Object> getOnTheGoSolution() {
        return onTheGoSolution;
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

    public static void recordSolvingInStatistics(Instruction instruction, long time, boolean isEndOfThread) {
        coverageStatistics.recordSolving(instruction, time, isEndOfThread);
    }

    public void threadTerminated(VM vm, ThreadInfo terminatedThread) {
        if(coverageMode == CoverageMode.JR_PLAIN)
            return;
        if (!evaluationMode) System.out.println("end of thread");
        if (VeriBranchListener.ignoreCoverageCollection)
            return;
//        newCoverageFound = false;
        allObligationsCovered = ObligationMgr.isAllObligationCovered();
        coverageStatistics.recordCoverageForThread();
    }

    @Override
    public void stateBacktracked(Search search) {
        if (!evaluationMode) System.out.println("backtracking now");
    }

    @Override
    public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
        if (!evaluationMode) if (currentCG instanceof PCChoiceGenerator) {
//            System.out.println("choiceGeneratorAdvanced: at " + currentCG.getInsn().getMethodInfo() + "#" + currentCG.getInsn().getPosition());
        }
    }

    // -------- the publisher interface
    @Override
    public void publishFinished(Publisher publisher) {
        if(coverageMode == CoverageMode.JR_PLAIN)
            return;
        PrintWriter pw = publisher.getOut();
        publisher.publishTopicStart("Branch Coverage report:");

        printCoverage();
        coverageStatistics.printOverallStats();
    }
}
