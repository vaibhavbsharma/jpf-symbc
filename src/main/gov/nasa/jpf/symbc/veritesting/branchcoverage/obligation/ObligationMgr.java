package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.ssa.SSAInstruction;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;

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


    public static boolean oblgExists(Obligation oblg) {
        Integer oblIndex = obligationsMap.get(oblg);
        return oblIndex != null;
    }


    // when encountering an instruction, tries to see its side was already covered or not. depending on that either ignore the choice or continue
    //returns false if obligation is already cover, and true if it was not perviously covered and was covered now.
    public static boolean coverOblgOrIgnore(IfInstruction ifInst, ObligationSide oblgSide) {
        String spfPackageClassName = ifInst.getMethodInfo().getClassInfo().getName();
        String methodSig = ifInst.getMethodInfo().getUniqueName();
        int instLine = ifInst.getPosition();
        SSAInstruction inst = null;
        ArrayList<Obligation> reachableObl = null;

        Obligation oblg = new Obligation(spfPackageClassName, methodSig, instLine, inst, reachableObl, oblgSide);

        Integer oblgIndex = obligationsMap.get(oblg);

        assert (oblgIndex == null) : "obligation not found in the obligation HashMap. Something is wrong. Failing.";

        if (isOblgCovered(oblg))
            return false;
        else {
            coveredArray[oblgIndex] = true;
            return true;
        }
    }

    // SPF methods to manipulate covering at runtime.
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
