package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.ssa.SSAInstruction;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;

import java.util.ArrayList;

public class CoverageUtil {

    public static String classUniqueName(String packageName, String classsName, String methodSig) {
        return packageName + "." + classsName + "." + methodSig;
    }

    public static Obligation createOblgFromIfInst(IfInstruction ifInst, ObligationSide oblgSide){
        String spfPackageClassName = ifInst.getMethodInfo().getClassInfo().getName();
        String methodSig = ifInst.getMethodInfo().getUniqueName();
        int instLine = ifInst.getPosition();
        SSAInstruction inst = null;
        ArrayList<Obligation> reachableObl = null;

        return new Obligation(spfPackageClassName, methodSig, instLine, inst, reachableObl, oblgSide);
    }
}
