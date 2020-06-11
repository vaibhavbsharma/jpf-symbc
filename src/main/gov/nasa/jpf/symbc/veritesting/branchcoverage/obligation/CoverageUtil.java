package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import gov.nasa.jpf.jvm.bytecode.IfInstruction;

public class CoverageUtil {

    public static String classUniqueName(String packageName, String classsName, String methodSig) {
        return packageName + "." + classsName + "." + methodSig;
    }
}
