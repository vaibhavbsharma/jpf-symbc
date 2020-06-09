package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.ssa.SSAInstruction;

import java.util.ArrayList;

public class Obligation implements Cloneable, Comparable {
    // packageName has "." qualifications like spf, but when we create them since they are in Wala's notation they have "/" instead, and so we translate them to their "." version.
    String spfPackageName;
    String className;
    String methodSig;
    int instLine;
    ObligationSide oblSide;
    SSAInstruction inst;
    ArrayList<Obligation> reachableObl;


    public Obligation(String walaPackageName, String className, String methodSig, int instLine, SSAInstruction inst, ArrayList<Obligation> reachableObl, ObligationSide oblSide) {
        this.spfPackageName = toSpfPackageName(walaPackageName);
        this.className = className;
        this.methodSig = methodSig;
        this.instLine = instLine;
        this.oblSide = oblSide;
        this.inst = inst;
        this.reachableObl = reachableObl;
    }

    /**
     * Assumes wala package name, that has "/" in it and changing that to spf package name with "." in it.
     *
     * @param packageName
     * @return
     */
    private String toSpfPackageName(String packageName) {
        return packageName != null ? packageName.replaceAll("/", ".") : null;
    }

    public String toString() {
        return spfPackageName + "_" + className + "_" + methodSig + "_" + instLine + "_" + oblSide.name();
    }


    @Override
    public int compareTo(Object o) {
        if ((spfPackageName.equals(((Obligation) o).spfPackageName)) &&
                (className.equals(((Obligation) o).className)) &&
                (methodSig.equals(((Obligation) o).methodSig)) &&
                (inst == ((Obligation) o).inst) &&
                (reachableObl.size() == (((Obligation) o).reachableObl.size())))
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
                (inst == ((Obligation) o).inst) &&
                (reachableObl.size() == (((Obligation) o).reachableObl.size())));
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
