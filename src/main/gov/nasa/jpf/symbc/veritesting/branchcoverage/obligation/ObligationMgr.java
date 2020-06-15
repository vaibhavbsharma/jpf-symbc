package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.ssa.SSAInstruction;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ObligationMgr {

    public static final HashMap<Obligation, Integer> obligationsMap = new HashMap<>();

    private static int indexSerial = 0;

    private static boolean[] coveredArray;

    public static void finishedCollection() {
        assert (obligationsMap.size() > 0) : "obligation Map cannot be empty";
        coveredArray = new boolean[obligationsMap.size()];
    }

    public static void addOblgMap(String walaPackageName, String className, String methodSig, int instLine, SSAInstruction inst, HashSet<Obligation> reachableObl) {
        Obligation oblgThen = new Obligation(walaPackageName, className, methodSig, instLine, inst, null, ObligationSide.THEN);
        Obligation oblgElse = new Obligation(walaPackageName, className, methodSig, instLine, inst, null, ObligationSide.ELSE);

        if (oblgExists(oblgElse))
            return;

        obligationsMap.put(oblgThen, indexSerial++);
        obligationsMap.put(oblgElse, indexSerial++);
    }


    public static boolean oblgExists(Obligation oblg) {
        Integer oblIndex = obligationsMap.get(oblg);
        return oblIndex != null;
    }


    // when encountering an instruction, tries to see its side was already covered or not. depending on that either ignore the choice or continue
    //returns a ignore flag, true if the obligation is already covered, false otherwise.
    public static boolean coverNgetIgnore(Obligation oblg) {

        Integer oblgIndex = obligationsMap.get(oblg);
/*
        Set<Obligation> oblgKeySet = obligationsMap.keySet();
        for (Obligation myOblg : oblgKeySet) {
            System.out.println(myOblg);
        }*/

//        assert (!(oblgIndex == null)) : "obligation not found in the obligation HashMap. Something is wrong. Failing.";

        if ((oblgIndex == null)) {
            System.out.println("obligation not found in the obligation HashMap. Assumed none application/user branch. Coverage Ignored for instruction.");
            return false; //returning ignore flag to false, since it is none user branch that we do not care about and we'd like to resume execution to potientially find something down the line.
        }
        if (isOblgCovered(oblg))
            return true; //returning ignore flag to true, since the oblgation is already covered.
        else {
            coveredArray[oblgIndex] = true;
            return false; //returning ignore flag to false, since the oblgation is NOT covered.
        }
    }

    // SPF methods to manipulate covering at runtime.
    public static boolean isOblgCovered(Obligation oblg) {
        Integer oblgIndex = obligationsMap.get(oblg);
        Set<Obligation> oblgKeySet = obligationsMap.keySet();
        /*for (Obligation myOblg : oblgKeySet) {
            System.out.println(myOblg);
        }*/
       assert (!(oblgIndex == null)) : ("obligation not found in the obligation HashMap. Something is wrong. Failing.");

        assert oblgIndex < coveredArray.length;

        return coveredArray[oblgIndex];
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
