package gov.nasa.jpf.symbc.branchcoverage.obligation;

import com.ibm.wala.ssa.SSAInstruction;
import gov.nasa.jpf.symbc.BranchListener;

import java.util.HashSet;
import java.util.Set;

public class Obligation implements Cloneable, Comparable {
    // packageName has "." qualifications like spf, but when we create them since they are in Wala's notation they have "/" instead, and so we translate them to their "." version.
    String spfPackageName;
    String className;
    String methodSig;
    int instLine;
    ObligationSide oblgSide;
    SSAInstruction inst;
    Set<String> localReachableMethods;


    //used by WALA obligation creation
    //obligations of these type of constructors are the ones that are stored in the ObligationMgr.obligationMap, since this is where localReachableMethods are defined and used
    // later during the guiding and pruning modes.
    public Obligation(String walaPackageName, String className, String methodSig, int instLine, SSAInstruction inst, ObligationSide oblgSide, Set<String> localReachableMethods) {
        this.spfPackageName = toSpfPackageName(walaPackageName);
        this.className = className;
        this.methodSig = methodSig;
        this.instLine = instLine;
        this.oblgSide = oblgSide;
        this.inst = inst;
        if (BranchListener.interproceduralReachability)
            assert localReachableMethods != null : "unexpected null value for localReachableMethods with interprocedural analysis turned on. It can be empty but not null. Failing.";

        this.localReachableMethods = localReachableMethods;
    }

    //used by WALA for collecting reachable obliagtions where localReachableMethods are not needed
    public Obligation(String walaPackageName, String className, String methodSig, int instLine, SSAInstruction inst, ObligationSide oblgSide) {
        this.spfPackageName = toSpfPackageName(walaPackageName);
        this.className = className;
        this.methodSig = methodSig;
        this.instLine = instLine;
        this.oblgSide = oblgSide;
        this.inst = inst;
    }

    // SPF version of creating an obligation
    public Obligation(String spfPackageClassName, String methodSig, int instLine, SSAInstruction inst, ObligationSide oblgSide) {

        if (!spfPackageClassName.contains(".")) {
//            System.out.println("WARNING: Class has no package define.");
            this.spfPackageName = CoverageUtil.UNKNOWN_PACKAGE;
            this.className = spfPackageClassName;
        } else {
            this.spfPackageName = spfPackageClassName.substring(0, spfPackageClassName.lastIndexOf("."));
            this.className = spfPackageClassName.substring(spfPackageClassName.lastIndexOf(".") + 1);
        }
        this.methodSig = methodSig;
        this.instLine = instLine;
        this.oblgSide = oblgSide;
        this.inst = inst;
    }

    /**
     * Assumes wala package name, that has "/" in it and changing that to spf package name with "." in it.
     *
     * @param packageName
     * @return
     */
    private String toSpfPackageName(String packageName) {
        return !packageName.equals(CoverageUtil.UNKNOWN_PACKAGE) ? packageName.replaceAll("/", ".") : CoverageUtil.UNKNOWN_PACKAGE;
    }

    public String toString() {
        return spfPackageName + "." + className + "." + methodSig + "." + instLine + "." + oblgSide.name();
    }


    @Override
    public int compareTo(Object o) {
        if ((spfPackageName.equals(((Obligation) o).spfPackageName)) &&
                (className.equals(((Obligation) o).className)) &&
                (methodSig.equals(((Obligation) o).methodSig)) &&
                (instLine == ((Obligation) o).instLine) && oblgSide == ((Obligation) o).oblgSide)
            return 1;
        else return 0;

    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Obligation))
            return false;

        return ((spfPackageName.equals(((Obligation) o).spfPackageName)) &&
                (className.equals(((Obligation) o).className)) &&
                (methodSig.equals(((Obligation) o).methodSig)) &&
                (instLine == ((Obligation) o).instLine) && oblgSide == ((Obligation) o).oblgSide);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
