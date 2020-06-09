package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

public class CoverageUtil {

    public static String classUniqueName(String packageName, String classsName) {
        return packageName + "_" + classsName;
    }
}
