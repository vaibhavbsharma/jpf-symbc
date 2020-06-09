package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.ssa.SSAInstruction;

import java.util.ArrayList;
import java.util.HashMap;

public class ObligationMgr {

    public static final HashMap<Obligation, Integer> obligationsMap = new HashMap<>();

    private static int indexSerial = 0;

    private static boolean[] coveredArray;

    public static void finishedCollection() {
        if (obligationsMap.size() == 0) {
            System.out.println("obligation Map cannot be empty");
            assert false;
        }
        coveredArray = new boolean[obligationsMap.size()];
    }

    public static void addOblgMap(String walaPackageName, String className, String methodSig, int instLine, SSAInstruction inst, ArrayList<Obligation> reachableObl) {
        Obligation oblgTrue = new Obligation(walaPackageName, className, methodSig, instLine, inst, null, ObligationSide.TRUE);
        Obligation oblgFalse = new Obligation(walaPackageName, className, methodSig, instLine, inst, null, ObligationSide.FALSE);

        if (oblgExists(oblgFalse))
            return;

        obligationsMap.put(oblgTrue, indexSerial++);
        obligationsMap.put(oblgFalse, indexSerial++);
    }

    public static void coverOblg(Obligation oblg) {
        Integer oblgIndex = obligationsMap.get(oblg);

        if (oblgIndex == null) {
            System.out.println("obligation not found in the obligation HashMap. Something is wrong. Failing.");
            assert false;
        }
        coveredArray[oblgIndex] = true;
    }

    public static boolean oblgExists(Obligation oblg) {
        Integer oblIndex = obligationsMap.get(oblg);
        return oblIndex != null;
    }

    public static boolean isOblgCovered(Obligation oblg) {
        Integer oblIndex = obligationsMap.get(oblg);
        if (oblIndex == null) {
            System.out.println("obligation not found in the obligation HashMap. Something is wrong. Failing.");
            assert false;
        }
        assert oblIndex < coveredArray.length;

        return coveredArray[oblIndex];
    }
}
