package gov.nasa.jpf.symbc.veritesting.branchcoverage;

import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.Obligation;
import jkind.lustre.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import static gov.nasa.jpf.symbc.BranchListener.coverageMode;

public class CoverageStatistics {

    public static FileWriter fw;
    public static BufferedWriter bw;
    public static PrintWriter out;
    String statisticFileName;


    public CoverageStatistics() {
        LocalDateTime time = LocalDateTime.now();
        statisticFileName = "../logs/CoverageStatistics_" + "mode_" + coverageMode + time + ".txt";

        try {
            fw = new FileWriter(statisticFileName);
            bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
            out.println("Obligation ------> Time ");
            out.close();
        } catch (IOException e) {
            System.out.println("problem writing to statistics file");
            assert false;
        }
    }

    public void recordObligationCovered(Obligation oblg) {
        try {
            fw = new FileWriter(statisticFileName, true);
            bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
            out.println(oblg + "," + LocalDateTime.now());
            out.close();
        } catch (IOException e) {
            System.out.println("problem writing to statistics file");
            assert false;
        }
    }
}
