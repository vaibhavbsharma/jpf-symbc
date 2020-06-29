package gov.nasa.jpf.symbc.veritesting.branchcoverage;

import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.ObligationMgr;
import jkind.lustre.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import static gov.nasa.jpf.symbc.BranchListener.coverageMode;

public class CoverageStatistics {

    public static FileWriter fw1;
    public static BufferedWriter bw1;
    public static PrintWriter out1;

    public static FileWriter fw2;
    public static BufferedWriter bw2;
    public static PrintWriter out2;

    String statisticFileName;
    String coveragePerThreadFileName;
    int threadCount = 0;


    public CoverageStatistics() {
        LocalDateTime time = LocalDateTime.now();
        statisticFileName = "../logs/CoverageStatistics_" + "mode_" + coverageMode + ".txt";
        coveragePerThreadFileName = "../logs/CoveragePerThread_" + "mode_" + coverageMode + ".txt";

        try {
            fw1 = new FileWriter(statisticFileName);
            bw1 = new BufferedWriter(fw1);
            out1 = new PrintWriter(bw1);
            out1.println(time + "  Obligation ------> Time ");
            out1.close();

            fw2 = new FileWriter(coveragePerThreadFileName);
            bw2 = new BufferedWriter(fw2);
            out2 = new PrintWriter(bw2);
            out2.println(time + "  Coverage Per Thread");
            out2.close();
        } catch (IOException e) {
            System.out.println("problem writing to statistics file");
            assert false;
        }
    }

    public void recordObligationCovered(Obligation oblg) {
        try {
            fw1 = new FileWriter(statisticFileName, true);
            bw1 = new BufferedWriter(fw1);
            out1 = new PrintWriter(bw1);
            out1.println(oblg + "," + LocalDateTime.now());
            out1.close();
        } catch (IOException e) {
            System.out.println("problem writing to statistics file");
            assert false;
        }
    }

    public void recordCoverageForThread() {
        try {
            fw2 = new FileWriter(coveragePerThreadFileName, true);
            bw2 = new BufferedWriter(fw2);
            out2 = new PrintWriter(bw2);
            out2.println("Coverage At the End of Thread: " + ++threadCount);
            out2.println(ObligationMgr.printCoverageStr());
            out2.close();
        } catch (IOException e) {
            System.out.println("problem writing to coverage per thread file");
            assert false;
        }
    }
}
