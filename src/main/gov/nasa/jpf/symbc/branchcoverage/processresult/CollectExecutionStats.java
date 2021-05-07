package gov.nasa.jpf.symbc.branchcoverage.processresult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class CollectExecutionStats {

    static HashMap<String, ArrayList<String>> execStatsHashMap = new HashMap<>();

    public CollectExecutionStats(String filename, String benchmark, String jrOrSpf, String mode, String batchOrSingle, String steps) throws IOException {
        String entryName = benchmark + "_" + jrOrSpf + "_" + mode + "_" + batchOrSingle + "_" + steps;
        ArrayList timeList = new ArrayList<>();
        String[] lines = Files.lines(Paths.get(filename)).toArray(String[]::new);

        for (int i = 1; i < lines.length; i++) { //skipping the title of the file
            String s = lines[i];
            timeList.add(s.substring(s.indexOf(",")));
        }
        execStatsHashMap.put(entryName, timeList);
    }

    public static void execute(CoverageStatsType oblgOrThreadStats, String benchmark, String dir) throws IOException {

        String resultsDir = dir + benchmark;

        Path[] coverageStates = Files.list(Paths.get(resultsDir)).toArray(Path[]::new);
        for (Path file : coverageStates) {
            String filename = file.toString();
            if (filename.contains(oblgOrThreadStats.name())) {
                String[] params = filename.substring(filename.lastIndexOf("/")).split("_");
                assert params.length == 7 : "unexpected file name";
                new CollectExecutionStats(filename, benchmark, params[2], params[3], params[4], params[5].replace(".txt", ""));
            }
        }
        writeToFile(oblgOrThreadStats, resultsDir, execStatsHashMap, benchmark + "_all_exec_stats.txt");

    }

    private static void writeToFile(CoverageStatsType oblgOrThreadStats, String resultsDir, HashMap<String, ArrayList<String>> execStatsHashMap, String outputFileName) {
        String outFile = resultsDir + outputFileName;
        Path path = Paths.get(outFile);
        File mutantTableFile = new File(outFile);

        try {
            if (!Files.exists(path))
                mutantTableFile.createNewFile();
            FileWriter fileWriter = new FileWriter(outFile);
            int maxLength = oblgOrThreadStats == CoverageStatsType.ExecStat ? findMaxTimeLength(execStatsHashMap) : 100; // it is a fixed size of hundred rows for the thread collection
            for (HashMap.Entry entry : execStatsHashMap.entrySet()) //writes the header
                fileWriter.write((String) (entry.getKey()) + ',');

            fileWriter.write("\n");

            for (int i = 0; i < maxLength; i++) { //writes the entries.
                fileWriter.write(i + ","); //writing the index of each entry
                for (HashMap.Entry entry : execStatsHashMap.entrySet()) {
                    ArrayList<String> entryTimeList = (ArrayList<String>) entry.getValue();
                    fileWriter.write(i < entryTimeList.size() ? entryTimeList.get(i) + "'" : ""); //ensuring no out of bout exception
                }
                fileWriter.write("\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            assert false : "exception while trying to write to the file. Failing";
        }
    }

    //finds the longest column for time in all the entries.
    private static int findMaxTimeLength(HashMap<String, ArrayList<String>> execStatsHashMap) {
        assert execStatsHashMap != null && execStatsHashMap.size() > 0 : "nothing in the hashmap. aborting";
        int max = -1;
        for (HashMap.Entry entry : execStatsHashMap.entrySet()) {
            int timeSize = ((ArrayList) entry.getValue()).size();
            max = (timeSize > max) ? timeSize : max;
        }
        return max;
    }
}
