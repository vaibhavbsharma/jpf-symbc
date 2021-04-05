package gov.nasa.jpf.symbc.branchcoverage.obligation;

import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSACFG;
import com.ibm.wala.ssa.SSAInstruction;
import gov.nasa.jpf.symbc.BranchListener;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;

import java.io.PrintWriter;
import java.util.*;

public class ObligationMgr {

    public static final HashMap<Obligation, Integer> obligationsMap = new HashMap<>();

    public static final HashMap<Obligation, HashSet<Obligation>> reachabilityMap = new HashMap<>();

    private static final HashMap<Obligation, ISSABasicBlock> obligationBBMap = new HashMap<>();

//    private static final HashMap<String, HashSet<Obligation>> methodObligations = new HashMap<>();

    private static int indexSerial = 0;

    private static boolean[] coveredArray;

    public static void finishedCollection() {
        assert (obligationsMap.size() > 0) : "obligation Map cannot be empty";
        coveredArray = new boolean[obligationsMap.size()];
    }

    public static void addOblgMap(String walaPackageName, String className, String methodSig, int instLine, SSAInstruction inst, SSACFG.BasicBlock blockForOblg, Pair<Set<String>, HashSet<Obligation>> reacheableThenOblgs, Pair<Set<String>, HashSet<Obligation>> reacheableElseOblgs) {
        Obligation oblgThen = new Obligation(walaPackageName, className, methodSig, instLine, inst, ObligationSide.THEN, reacheableThenOblgs.getFirst());
        Obligation oblgElse = new Obligation(walaPackageName, className, methodSig, instLine, inst, ObligationSide.ELSE, reacheableElseOblgs.getFirst());

        if (oblgExists(oblgElse)) return;

        obligationsMap.put(oblgThen, indexSerial++);
        obligationsMap.put(oblgElse, indexSerial++);

        reachabilityMap.put(oblgThen, reacheableThenOblgs.getSecond());
        reachabilityMap.put(oblgElse, reacheableElseOblgs.getSecond());

        if (!BranchListener.evaluationMode) obligationBBMap.put(oblgThen, blockForOblg);
    }


    public static void addOblgMap(String walaPackageName, String className, String methodSig, int instLine, SSAInstruction inst, SSACFG.BasicBlock blockForOblg) {
        Obligation oblgThen = new Obligation(walaPackageName, className, methodSig, instLine, inst, ObligationSide.THEN);
        Obligation oblgElse = new Obligation(walaPackageName, className, methodSig, instLine, inst, ObligationSide.ELSE);

        if (oblgExists(oblgElse)) return;

        obligationsMap.put(oblgThen, indexSerial++);
        obligationsMap.put(oblgElse, indexSerial++);

        if (!BranchListener.evaluationMode) obligationBBMap.put(oblgThen, blockForOblg);
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


    public static void addNewCoverage(Obligation oblg) {

        Integer oblgIndex = obligationsMap.get(oblg);

        if ((oblgIndex == null)) {
            System.out.println("obligation not found in the obligation HashMap. Assumed none application/user branch. Coverage Ignored for instruction.");
            return;
        }
        assert !coveredArray[oblgIndex] : "obligation is already covered, it is not a new coverage. Assumption violated. Failing.";
        coveredArray[oblgIndex] = true;
    }

    public static void addNewOblgsCoverage(ArrayList<Obligation> newCoveredOblgs) {
        for (Obligation oblg : newCoveredOblgs)
            addNewCoverage(oblg);
    }

    // SPF methods to manipulate covering at runtime. Must always be called with already existing obligation in the map.
    public static boolean isOblgCovered(Obligation oblg) {
        Integer oblgIndex = obligationsMap.get(oblg);

        assert (!(oblgIndex == null)) : ("obligation" + oblg.toString() + "not found in the obligation HashMap. Something is wrong. Failing.");

        assert oblgIndex < coveredArray.length;

        return coveredArray[oblgIndex];
    }

    public static int getOblgIndex(Obligation oblg) {
        return obligationsMap.get(oblg);
    }

    //returns an array of unreachableObligations, empty array if all are already covered and null if the mainOblg is not found in the map indicating it is not an obligation we are tracking for coverage.
    public static Obligation[] isReachableOblgsCovered(Obligation mainOblg) {
        HashSet<Obligation> uncoveredOblgList = new HashSet<>();

        if (!reachabilityMap.containsKey(mainOblg)) {  //if it can't be found in t he reachability map then it must be the that it doesn't exist in the obligationMap as well indicating that it is an obligation that we do not care about tracking its cover, for example, it is not an application users code.
            assert !obligationsMap.containsKey(mainOblg);
            return null;
        }
        HashSet<Obligation> reachableOblgs = reachabilityMap.get(mainOblg);

        //add myself if I am not yet covered.
        if (!isOblgCovered(mainOblg)) uncoveredOblgList.add(mainOblg);

        for (Obligation reachableOblg : reachableOblgs)
            if (!isOblgCovered(reachableOblg)) uncoveredOblgList.add(reachableOblg);

        if (uncoveredOblgList.size() == 0 && BranchListener.interproceduralReachability) { //start looking at interprocedural reachability.
            Obligation mainOblgWithLocalMethodReach = findActualKeyInMap(obligationsMap.keySet(), mainOblg);
            assert mainOblgWithLocalMethodReach.localReachableMethods != null;

            // giving high weight to explore methods. Note that it could be the case that this is an already explored methods,
            // we still guide to SPF to explore it further, ideally it will backtrack when it realizes that nothing can be explored.
            // It is still debatable whether we should give high priority to method exploration without keeping track of those whose
            //obligations have already been covered.
            if (mainOblgWithLocalMethodReach.localReachableMethods.size() > 0) {
                if (!BranchListener.evaluationMode) System.out.println(" ---- method found along reachable path");
                return new Obligation[1];
            }
        }

        if (uncoveredOblgList.size() > 0) return uncoveredOblgList.toArray(new Obligation[uncoveredOblgList.size()]);
        else return new Obligation[]{};
    }

    /**
     * Tries to find the actual oblg in the obligationMap, since there we will see the actual  value of "localReachableMethods" then later deciding if there are more stuff
     * reachable or not.
     *
     * @param obligationKeys
     * @param mainOblg
     * @return
     */
    private static Obligation findActualKeyInMap(Set<Obligation> obligationKeys, Obligation mainOblg) {
        for (Obligation oblg : obligationKeys)
            if (oblg.equals(mainOblg)) return oblg;
        assert false : "this cannot happen. There must be an obligation in obligationMap for this key. Something went wrong. Failing.";
        return null;
    }

    public static boolean isAllObligationCovered() {
        for (boolean coverage : coveredArray)
            if (!coverage) return false;

        return true;
    }

    public static void printCoverage(PrintWriter pw) {
        pw.println("Obligation -----> Coverage:");

        Set<Obligation> olgKeySet = obligationsMap.keySet();
        for (Obligation oblg : olgKeySet) {
            pw.println(oblg + " -----> " + coveredArray[obligationsMap.get(oblg)]);
        }
    }

    public static void printCoverage() {
        System.out.print("Obligation -----> Coverage:\n");

        Set<Obligation> olgKeySet = obligationsMap.keySet();
        for (Obligation oblg : olgKeySet) {
            System.out.print(oblg + " -----> " + coveredArray[obligationsMap.get(oblg)] + " \n");
        }
        System.out.println();
    }

    public static String printCoverageStr() {
        String coverageStr = "";
        Set<Obligation> olgKeySet = obligationsMap.keySet();
        for (Obligation oblg : olgKeySet) {
            coverageStr += (oblg + " -----> " + coveredArray[obligationsMap.get(oblg)] + " \n");
        }
        return coverageStr;
    }

    public static void printReachability() {
        System.out.print("Obligation Reachability-----> Coverage:\n");

        for (Obligation oblg : reachabilityMap.keySet()) {
            System.out.print(oblg + " -----> " + reachabilityMap.get(oblg) + " \n");
        }
    }


    public static void printOblgToBBMap() {
        if (!BranchListener.evaluationMode) {
            System.out.print("Obligation -----> Basic Block:\n");

            for (Obligation oblg : obligationBBMap.keySet()) {
                System.out.print(oblg + " -----> " + obligationBBMap.get(oblg) + " \n");
            }
        }
    }

    public static float getCoveragePercent() {
        int numberOfCoverage = 0;
        for (int i = 0; i < coveredArray.length; i++)
            numberOfCoverage = coveredArray[i] ? ++numberOfCoverage : numberOfCoverage;

        return ((float) numberOfCoverage / coveredArray.length) * 100;
    }

    public static boolean intraproceduralInvokeReachable(Obligation mainOblg) {

        if (!reachabilityMap.containsKey(mainOblg)) {  //if it can't be found in t he reachability map then it must be the that it doesn't exist in the obligationMap as well indicating that it is an obligation that we do not care about tracking its cover, for example, it is not an application users code.
            assert !obligationsMap.containsKey(mainOblg);
            return false;
        }

        if (BranchListener.interproceduralReachability) { //start looking at interprocedural reachability.
            Obligation mainOblgWithLocalMethodReach = findActualKeyInMap(obligationsMap.keySet(), mainOblg);
            assert mainOblgWithLocalMethodReach.localReachableMethods != null;

            if (mainOblgWithLocalMethodReach.localReachableMethods.size() > 0) {
                if (!BranchListener.evaluationMode) System.out.println(" ---- method found along reachable path");
                return true;
            }
        }
        return false;
    }

    /* -- Commenting this for now but we can potientially add it if we want more accurate interreachability analysis.
    public static void addMethOblg(String walaMethodSignature, Pair<Obligation, Obligation> oblgPair) {
        assert BranchListener.interproceduralReachability : "methodObligations data structure should only be used with interprocedural reachability. Violation detected. Failing";
        HashSet<Obligation> oblgs = methodObligations.get(walaMethodSignature);
        if (oblgs == null)
            methodObligations.put(walaMethodSignature, new HashSet<>((List<Obligation>) (List<?>) oblgPair.toList()));
        else
            oblgs.addAll((List<Obligation>) (List<?>) oblgPair.toList());
    }*/
}
