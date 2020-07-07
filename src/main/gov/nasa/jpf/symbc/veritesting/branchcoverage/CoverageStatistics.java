package gov.nasa.jpf.symbc.veritesting.branchcoverage;

import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.ObligationMgr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

import static gov.nasa.jpf.symbc.BranchListener.coverageMode;
import static gov.nasa.jpf.symbc.BranchListener.evaluationMode;

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

    String statisticFileName;
    String executionStatFileName;
    String coveragePerThreadFileName;

    int threadCount = 0;

    private static HashMap<Obligation, Long> threadOblgMap = new HashMap<>();

    //contains the normalized time after which we will record the coverage.
    static Long timeNormVal = null;

    public CoverageStatistics() {
        LocalDateTime time = LocalDateTime.now();
        statisticFileName = "../logs/CoverageStatistics_" + "mode_" + coverageMode + ".txt";
        executionStatFileName = "../logs/ExecutionStatistics_" + "mode_" + coverageMode + ".txt";
        coveragePerThreadFileName = "../logs/CoveragePerThread_" + "mode_" + coverageMode + ".txt";

        try {
            statisticFilefw = new FileWriter(statisticFileName);
            statisticFilebw = new BufferedWriter(statisticFilefw);
            statisticFilepw = new PrintWriter(statisticFilebw);
            statisticFilepw.println(time + "  Obligation ------> Time ");
            statisticFilepw.close();

            executionStatFilefw = new FileWriter(executionStatFileName);
            executionStatFilebw = new BufferedWriter(executionStatFilefw);
            executionStatFilepw = new PrintWriter(executionStatFilebw);
            executionStatFilepw.println(time + "  Obligation ------> Execution Time ");
            executionStatFilepw.close();

            if (!evaluationMode) {
                threadCoveragefw = new FileWriter(coveragePerThreadFileName);
                threadCoveragebw = new BufferedWriter(threadCoveragefw);
                threadCoverageout = new PrintWriter(threadCoveragebw);
                threadCoverageout.println(time + "  Coverage Per Thread");
                threadCoverageout.close();
            }
        } catch (IOException e) {
            System.out.println("problem writing to statistics file");
            assert false;
        }
    }

    public void recordObligationCovered(Obligation oblg) {
        long currentTime = System.currentTimeMillis();

        Long coverageTime;
        if (timeNormVal == null) {
            timeNormVal = currentTime;
            coverageTime = 0L;
        } else
            coverageTime = currentTime - timeNormVal;

        Long oblgCoverageTime = threadOblgMap.get(oblg);
        assert (oblgCoverageTime == null) : "unexpected obligation being re-covered.";
        threadOblgMap.put(oblg, timeNormVal);
        try {
            statisticFilefw = new FileWriter(statisticFileName, true);
            statisticFilebw = new BufferedWriter(statisticFilefw);
            statisticFilepw = new PrintWriter(statisticFilebw);
            statisticFilepw.println(oblg + "," + coverageTime);
            statisticFilepw.close();


            executionStatFilefw = new FileWriter(executionStatFileName, true);
            executionStatFilebw = new BufferedWriter(executionStatFilefw);
            executionStatFilepw = new PrintWriter(executionStatFilebw);
            executionStatFilepw.println(oblg + "," + currentTime);
            executionStatFilepw.close();
        } catch (IOException e) {
            System.out.println("problem writing to statistics file");
            assert false;
        }
    }

    public void recordCoverageForThread() {
        if (!evaluationMode)
            try {
                threadCoveragefw = new FileWriter(coveragePerThreadFileName, true);
                threadCoveragebw = new BufferedWriter(threadCoveragefw);
                threadCoverageout = new PrintWriter(threadCoveragebw);
                threadCoverageout.println("Coverage At the End of Thread, total Obligation");
                threadCoverageout.println(++threadCount + "," + threadOblgMap.size());
                if (threadOblgMap.size() > 0)
                    threadCoverageout.println(printThreadOblgMap());
                else
                    threadCoverageout.println("NONE");
                threadOblgMap.clear();
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
