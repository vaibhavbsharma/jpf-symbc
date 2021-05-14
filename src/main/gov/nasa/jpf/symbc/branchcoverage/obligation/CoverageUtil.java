package gov.nasa.jpf.symbc.branchcoverage.obligation;

import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSAOptions;
import com.ibm.wala.types.MethodReference;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.symbc.branchcoverage.BranchCoverage;

import java.util.HashSet;

public class CoverageUtil {

    public static String UNKNOWN_PACKAGE = "UNDEFINED";

    public static String classUniqueName(String packageName, String classsName, String methodSig) {
        packageName = packageName == null ? UNKNOWN_PACKAGE : packageName;
        return packageName + "." + classsName + "." + methodSig;
    }

    public static Obligation createOblgFromIfInst(IfInstruction ifInst, ObligationSide oblgSide) {
        String spfPackageClassName = ifInst.getMethodInfo().getClassInfo().getName();
        String methodSig = ifInst.getMethodInfo().getUniqueName();
        int instLine = ifInst.getPosition();
        SSAInstruction inst = null;

        return new Obligation(spfPackageClassName, methodSig, instLine, inst, oblgSide);
    }


    public static HashSet<Obligation> createOblgFromWalaInst(IR ir, SSAInstruction inst) {
        HashSet<Obligation> oblgs = new HashSet();
        IMethod m = ir.getMethod();
        String walaPackageName = getWalaPackageName(m);
        String className = m.getDeclaringClass().getName().getClassName().toString();
        String methodSig = m.getSelector().toString();
        int instLine = getWalaInstLineNum(m, inst);

        Obligation oblgThen = new Obligation(walaPackageName, className, methodSig, instLine, inst, ObligationSide.TAKEN);
        Obligation oblgElse = new Obligation(walaPackageName, className, methodSig, instLine, inst, ObligationSide.NOT_TAKEN);

        oblgs.add(oblgThen);
        oblgs.add(oblgElse);
        return oblgs;
    }

    public static Obligation createOblgFromWalaInst(IR ir, SSAInstruction inst, ObligationSide side) {

        IMethod m = ir.getMethod();
        String walaPackageName = getWalaPackageName(m);
        String className = m.getDeclaringClass().getName().getClassName().toString();
        String methodSig = m.getSelector().toString();
        int instLine = getWalaInstLineNum(m, inst);

        Obligation oblg = new Obligation(walaPackageName, className, methodSig, instLine, inst, side);
        return oblg;
    }

    public static int getWalaInstLineNum(IMethod iMethod, SSAInstruction inst) {


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

    public static String getWalaPackageName(IMethod m) {
        return m.getDeclaringClass().getName().getPackage() != null ? m.getDeclaringClass().getName().getPackage().toString() : UNKNOWN_PACKAGE;
    }

    public static String constructWalaMethodSign(String walaPackageName, String methodSig, String className) {
        String refinedMethodSig = methodSig.replaceAll(";", "");
        if (walaPackageName.equals(UNKNOWN_PACKAGE))
            return "L" + className + "." + refinedMethodSig;
        else
            return "L" + walaPackageName + "/" + className + "." + refinedMethodSig;
    }

    public static CGNode findNodeForInst(CallGraph cg, MethodReference methodRef) {
        int i = 0;
        CGNode node = cg.getNode(i);
        while (node != null) {
//            node.getClass()
            if (node.toString().contains(methodRef.toString()))
                return node;
            node = cg.getNode(++i);
        }
        return null;
    }

    //an abstract method is a method which we cannot have an IR for it.
    public static boolean isAbstractInvoke(SSAInstruction instruction) {
        if (!(instruction instanceof SSAInvokeInstruction)) return false;
        MethodReference mr = ((SSAInvokeInstruction) instruction).getDeclaredTarget();
        IMethod m = BranchCoverage.cha.resolveMethod(mr);
//        assert m != null : "imethod cannot be null for instruction:" + instruction.toString() + "target = "+ mr.toString();
 /*       if (m == null)
            return false;*/
        AnalysisOptions options = new AnalysisOptions();
        options.getSSAOptions().setPiNodePolicy(SSAOptions.getAllBuiltInPiNodes());
        IAnalysisCacheView cache = new AnalysisCacheImpl(options.getSSAOptions());
        IR ir = cache.getIR(m, Everywhere.EVERYWHERE);
        if (ir == null)//case of an abstract method
            return true;

        return false;
    }
}
