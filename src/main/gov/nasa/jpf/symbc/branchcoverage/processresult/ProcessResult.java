package gov.nasa.jpf.symbc.branchcoverage.processresult;

import java.io.IOException;

//used to process results from the TCG
public class ProcessResult {

    //takes as input the benchmark name, and it assumes results exists in the logs directory.
    public static void main(String[] args) throws IOException {

        assert args.length == 1 : "unexpected input";
        String benchmark = args[0];
        String dir = args[1];

        CollectExecutionStats.execute(CoverageStatsType.ExecStat, benchmark, dir);
        CollectExecutionStats.execute(CoverageStatsType.ThreadStat, benchmark, dir);
        CollectPerfStats.execute(benchmark);
    }
}
