package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;

import java.util.HashSet;

public class CoverageUtil {

    public static String classUniqueName(String packageName, String classsName, String methodSig) {
        return packageName + "." + classsName + "." + methodSig;
    }

    public static Obligation createOblgFromIfInst(IfInstruction ifInst, ObligationSide oblgSide) {
        String spfPackageClassName = ifInst.getMethodInfo().getClassInfo().getName();
        String methodSig = ifInst.getMethodInfo().getUniqueName();
        int instLine = ifInst.getPosition();
        SSAInstruction inst = null;
        HashSet<Obligation> reachableObl = null;

        return new Obligation(spfPackageClassName, methodSig, instLine, inst, reachableObl, oblgSide);
    }


    public static HashSet<Obligation> createOblgFromWalaInst(IR ir, SSAInstruction inst) {
        HashSet<Obligation> oblgs = new HashSet();
        IMethod m = ir.getMethod();
        String walaPackageName = m.getDeclaringClass().getName().getPackage().toString();
        String className = m.getDeclaringClass().getName().getClassName().toString();
        String methodSig = m.getSelector().toString();
        int instLine = getInstructionLineNum(m, inst);

        Obligation oblgThen = new Obligation(walaPackageName, className, methodSig, instLine, inst, null, ObligationSide.THEN);
        Obligation oblgElse = new Obligation(walaPackageName, className, methodSig, instLine, inst, null, ObligationSide.ELSE);

        oblgs.add(oblgThen);
        oblgs.add(oblgElse);
        return oblgs;
    }

    public static int getInstructionLineNum(IMethod iMethod, SSAInstruction inst) {


        int instLine = 0;
        try {
            instLine = (((IBytecodeMethod) (iMethod)).getBytecodeIndex(inst.iIndex()));
            return instLine;
        } catch (InvalidClassFileException e) {
            System.out.println("exception while getting instruction index from wala. Failing");
            e.printStackTrace();
        }
        assert false;
        return 0;
    }
}
