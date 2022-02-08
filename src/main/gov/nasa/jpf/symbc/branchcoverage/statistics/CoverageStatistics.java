package gov.nasa.jpf.symbc.branchcoverage.statistics;

import gov.nasa.jpf.symbc.branchcoverage.CoverageMode;
import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.vm.Instruction;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

import static gov.nasa.jpf.symbc.BranchListener.*;
import static gov.nasa.jpf.symbc.VeriBranchListener.batchCoverage;
import static gov.nasa.jpf.symbc.VeritestingListener.tcgON;
import static gov.nasa.jpf.symbc.VeritestingListener.veritestingMode;

public class CoverageStatistics {

    public static FileWriter statisticFilefw;
    public static BufferedWriter statisticFilebw;
    public static PrintWriter statisticFilepw;

    public static FileWriter executionStatFilefw;
    public static BufferedWriter executionStatFilebw;
    public static PrintWriter executionStatFilepw;


    public static FileWriter threadCoveragefw;
    public static BufferedWriter threadCoveragebw;
    public static PrintWriter threadCoverageout;

    public static FileWriter solvingStatsFilefw;
    public static BufferedWriter solvingStatsFilesbw;
    public static PrintWriter solvingStatsFilepw;


    String statisticFileName;
    String executionStatFileName;
    String coveragePerThreadFileName;
    String solvingStatsFileName;

    int threadCount = 0;

    private static HashMap<Obligation, Long> threadOblgMap = new HashMap<>();
    static int newOblgPerThreadCount = 0;

    //contains the normalized time after which we will record the coverage.
    static Long timeNormVal = null;

    public CoverageStatistics() {
        LocalDateTime time = LocalDateTime.now();
        Integer evnMaxSteps = (System.getenv("MAX_STEPS") != null) ? Integer.valueOf(System.getenv("MAX_STEPS")) : null;

        String folderStr = "../logs/" + benchmarkName.toLowerCase();

        new File(folderStr).mkdirs(); //creating a folder to hold results

//        String JRorSPF = veritestingMode != 0 ? "JR" : "SPF";
        String isBatch = coverageMode == CoverageMode.JR_COLLECT ? (batchCoverage ? "batch" : "single") : "NA";
        String isOnTheGo = tcgON && coverageMode == CoverageMode.JR_PLAIN ? "NA" :
                tcgOnTheGo ? "onTheGo_ON" : "onTheGo_OFF";
        String envStepsStr = evnMaxSteps != null ? "_steps_" + evnMaxSteps : "";

        String spfPath = coverageMode == CoverageMode.SPF && pathCoverage ? "_Path" : "";

        statisticFileName = folderStr + "/" + benchmarkName + "_OblgOnlyStat_" + coverageMode + spfPath + "_mode" + veritestingMode + "_" + isBatch + "_" + isOnTheGo + envStepsStr + ".txt";
        executionStatFileName = folderStr + "/" + benchmarkName + "_ExecStat_" + coverageMode + spfPath + "_mode" + veritestingMode + "_" + isBatch + "_" + isOnTheGo + envStepsStr + ".txt";
        coveragePerThreadFileName = folderStr + "/" + benchmarkName + "_ThreadStat_" + coverageMode + spfPath + "_mode" + veritestingMode + "_" + isBatch + "_" + isOnTheGo + envStepsStr + ".txt";
        solvingStatsFileName = folderStr + "/" + benchmarkName + "_SolvingStat_" + coverageMode + spfPath + "_mode" + veritestingMode + "_" + isBatch + "_" + isOnTheGo + envStepsStr + ".txt";

      /*  if (evnMaxSteps != null) {
            statisticFileName = folderStr + "/" + benchmarkName + "OblgOnlyStat_" + coverageMode + "_mode" + veritestingMode + "_steps" + evnMaxSteps + ".txt";
            executionStatFileName = folderStr + "/" + benchmarkName + "ExecStat_" + coverageMode + "_mode" + veritestingMode + "_steps" + evnMaxSteps + ".txt";
            coveragePerThreadFileName = folderStr + "/" + benchmarkName + "ThreadStat_" + coverageMode + "_mode" + veritestingMode + "_steps" + evnMaxSteps + ".txt";
        } else {
            statisticFileName = folderStr + "/" + benchmarkName + "OblgOnlyStat_" + coverageMode + "_mode" + veritestingMode + ".txt";
            executionStatFileName = folderStr + "/" + benchmarkName + "ExecStat_" + coverageMode + "_mode" + veritestingMode + ".txt";
            coveragePerThreadFileName = folderStr + "/" + benchmarkName + "ThreadStat_" + coverageMode + "_mode" + veritestingMode + ".txt";
        }
*/
        try {

            statisticFilefw = new FileWriter(statisticFileName);
            statisticFilebw = new BufferedWriter(statisticFilefw);
            statisticFilepw = new PrintWriter(statisticFilebw);
            statisticFilepw.println(time + "  Obligation, isEndOfPathCoverage ------> Time(ms)");
            statisticFilepw.close();

            executionStatFilefw = new FileWriter(executionStatFileName);
            executionStatFilebw = new BufferedWriter(executionStatFilefw);
            executionStatFilepw = new PrintWriter(executionStatFilebw);
            executionStatFilepw.println(time + "  Obligation, isEndOfPathCoverage ------> Execution Time(ms)");
            executionStatFilepw.close();


            threadCoveragefw = new FileWriter(coveragePerThreadFileName);
            threadCoveragebw = new BufferedWriter(threadCoveragefw);
            threadCoverageout = new PrintWriter(threadCoveragebw);
            if (!evaluationMode) threadCoverageout.println(time + "  Coverage Per Thread");
            else threadCoverageout.println("ThreadNum , NewOblgCount");
            threadCoverageout.close();

            solvingStatsFilefw = new FileWriter(solvingStatsFileName);
            solvingStatsFilesbw = new BufferedWriter(solvingStatsFilefw);
            solvingStatsFilepw = new PrintWriter(solvingStatsFilesbw);
            solvingStatsFilepw.println("Instruction, Time(ms), #Thread, EndOfThread");
            solvingStatsFilepw.close();


        } catch (IOException e) {
            System.out.println("PROBLEM writing to statistics file");
            assert false;
        }
    }

    public void recordSolving(Instruction instruction, long time, boolean isEndOfThread) {
        try {
            String instructionStr = instruction.getMethodInfo().toString() + " " +
                    instruction.toString();
            solvingStatsFilefw = new FileWriter(solvingStatsFileName, true);
            solvingStatsFilesbw = new BufferedWriter(solvingStatsFilefw);
            solvingStatsFilepw = new PrintWriter(solvingStatsFilesbw);
            solvingStatsFilepw.println(instructionStr + "," + time + "," + threadCount + "," + isEndOfThread);
            solvingStatsFilepw.close();
        } catch (IOException e) {
            System.out.println("PROBLEM writing to statistics file");
            assert false;
        }
    }

    public void recordObligationCovered(Obligation oblg, boolean isEndPathCovered) {
        ++newOblgPerThreadCount;

        long currentTime = System.currentTimeMillis();

        Long coverageTime;
        if (timeNormVal == null) {
            timeNormVal = currentTime;
            coverageTime = 0L;
        } else coverageTime = currentTime - timeNormVal;

        if (!evaluationMode) {
            Long oblgCoverageTime = threadOblgMap.get(oblg);
            assert (oblgCoverageTime == null) : "unexpected obligation being re-covered.";
            threadOblgMap.put(oblg, timeNormVal);
        }

        try {
            statisticFilefw = new FileWriter(statisticFileName, true);
            statisticFilebw = new BufferedWriter(statisticFilefw);
            statisticFilepw = new PrintWriter(statisticFilebw);
            statisticFilepw.println(oblg + "," + isEndPathCovered + "," + coverageTime);
            statisticFilepw.close();


            executionStatFilefw = new FileWriter(executionStatFileName, true);
            executionStatFilebw = new BufferedWriter(executionStatFilefw);
            executionStatFilepw = new PrintWriter(executionStatFilebw);
            executionStatFilepw.println(oblg + "," + isEndPathCovered + "," + (currentTime - timeZero));
            executionStatFilepw.close();
        } catch (IOException e) {
            System.out.println("problem writing to statistics file");
            assert false;
        }
    }

    public void recordCoverageForThread() {
        try {
            threadCoveragefw = new FileWriter(coveragePerThreadFileName, true);
            threadCoveragebw = new BufferedWriter(threadCoveragefw);
            threadCoverageout = new PrintWriter(threadCoveragebw);
//            threadCoverageout.println("Coverage At the End of Thread, total Obligation");
            threadCoverageout.println(++threadCount + "," + newOblgPerThreadCount);
            if (!evaluationMode) {
                assert newOblgPerThreadCount == threadOblgMap.size();
                if (threadOblgMap.size() > 0) {
                    threadCoverageout.println(printThreadOblgMap());
                    threadOblgMap.clear();
                } else {
                    threadCoverageout.println("NONE");
                    threadCoverageout.close();
                }
                newOblgPerThreadCount = 0;
            } else {
                newOblgPerThreadCount = 0;
            }
            threadCoverageout.close();
        } catch (IOException e) {
            System.out.println("problem writing to coverage per thread file");
            assert false;
        }
    }

    public void printOverallStats() {
        float coveragePercent = ObligationMgr.getCoveragePercent();


        try {
            statisticFilefw = new FileWriter(statisticFileName, true);
            statisticFilebw = new BufferedWriter(statisticFilefw);
            statisticFilepw = new PrintWriter(statisticFilebw);
            statisticFilepw.println(coveragePercent);
            statisticFilepw.close();
        } catch (IOException e) {
            System.out.println("problem writing to statistics file");
            assert false;
        }
    }


    public static String printThreadOblgMap() {
        String coverageStr = "";
        Set<Obligation> olgKeySet = threadOblgMap.keySet();
        for (Obligation oblg : olgKeySet) {
            coverageStr += (oblg + "," + threadOblgMap.get(oblg) + " \n");
        }
        return coverageStr;
    }
}
