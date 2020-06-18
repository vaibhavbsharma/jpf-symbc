package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.ssa.SSAInstruction;

import java.io.PrintWriter;
import java.util.*;

public class ObligationMgr {

    public static final HashMap<Obligation, Integer> obligationsMap = new HashMap<>();

    public static final HashMap<Obligation, HashSet<Obligation>> reachabilityMap = new HashMap<>();

    private static int indexSerial = 0;

    private static boolean[] coveredArray;

    public static void finishedCollection() {
        assert (obligationsMap.size() > 0) : "obligation Map cannot be empty";
        coveredArray = new boolean[obligationsMap.size()];
    }

    public static void addOblgMap(String walaPackageName, String className, String methodSig, int instLine, SSAInstruction inst, HashSet<Obligation> reacheableThenOblgs, HashSet<Obligation> reacheableElseOblgs) {
        Obligation oblgThen = new Obligation(walaPackageName, className, methodSig, instLine, inst, ObligationSide.THEN);
        Obligation oblgElse = new Obligation(walaPackageName, className, methodSig, instLine, inst, ObligationSide.ELSE);

        if (oblgExists(oblgElse)) return;

        obligationsMap.put(oblgThen, indexSerial++);
        obligationsMap.put(oblgElse, indexSerial++);

        reachabilityMap.put(oblgThen, reacheableThenOblgs);
        reachabilityMap.put(oblgElse, reacheableElseOblgs);
    }


    public static boolean oblgExists(Obligation oblg) {
        Integer oblIndex = obligationsMap.get(oblg);
        return oblIndex != null;
    }


    // when encountering an instruction, tries to see its side was already covered or not, return true if it is a new coverage otherwise return false.
    public static boolean isNewCoverage(Obligation oblg) {

        Integer oblgIndex = obligationsMap.get(oblg);

        if ((oblgIndex == null)) {
            System.out.println("obligation not found in the obligation HashMap. Assumed none application/user branch. Coverage Ignored for instruction.");
            return false; // no new obligation that we are looking for are covered in this instance
        }
        if (isOblgCovered(oblg))
            return false; //this is an already covered obligation, so nothing new here, returning false.
        else {
            coveredArray[oblgIndex] = true;
            return true; //this is a new coverage, thus returning true
        }
    }

    // SPF methods to manipulate covering at runtime. Must always be called with already existing obligation in the map.
    public static boolean isOblgCovered(Obligation oblg) {
        Integer oblgIndex = obligationsMap.get(oblg);

        assert (!(oblgIndex == null)) : ("obligation not found in the obligation HashMap. Something is wrong. Failing.");

        assert oblgIndex < coveredArray.length;

        return coveredArray[oblgIndex];
    }

    public static int getOblgIndex(Obligation oblg) {
        return obligationsMap.get(oblg);
    }

    //returns an array of unreachableObligations, empty array if all are already covered and null if the mainOblg is not found in the map indicating it is not an obligation we are tracking for coverage.
    public static Obligation[] isReachableOblgsCovered(Obligation mainOblg) {
        ArrayList<Obligation> uncoveredOblgList = new ArrayList<>();

        if (!reachabilityMap.containsKey(mainOblg)) {  //if it can't be found in t he reachability map then it must be the that it doesn't exist in the obligationMap as well indicating that it is an obligation that we do not care about tracking its cover, for example, it is not an application users code.
            assert !obligationsMap.containsKey(mainOblg);
            return null;
        }
        HashSet<Obligation> reachableOblgs = reachabilityMap.get(mainOblg);

        //add myself if I am not yet covered.
        if (!isOblgCovered(mainOblg)) uncoveredOblgList.add(mainOblg);

        for (Obligation reachableOblg : reachableOblgs) {
            if (!isOblgCovered(reachableOblg)) uncoveredOblgList.add(reachableOblg);
        }
        if (uncoveredOblgList.size() > 0) return uncoveredOblgList.toArray(new Obligation[uncoveredOblgList.size()]);
        else return new Obligation[]{};
    }

    public static boolean isAllObligationCovered() {
        for (boolean coverage : coveredArray)
            if (!coverage)
                return false;

        return true;
    }

    public static void printCoverage(PrintWriter pw) {
        pw.println("Obligation -----> Coverage:");

        Set<Obligation> olgKeySet = obligationsMap.keySet();
        for (Obligation oblg : olgKeySet) {
            pw.println(oblg + " -----> " + coveredArray[obligationsMap.get(oblg)]);
        }
    }

    public static String printCoverage() {
        String coverageStr = ("Obligation -----> Coverage:\n");

        Set<Obligation> olgKeySet = obligationsMap.keySet();
        for (Obligation oblg : olgKeySet) {
            coverageStr = coverageStr.concat(oblg + " -----> " + coveredArray[obligationsMap.get(oblg)] + " \n");
        }
        return coverageStr;
    }


}
