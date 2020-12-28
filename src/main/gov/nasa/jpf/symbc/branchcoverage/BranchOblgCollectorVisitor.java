package gov.nasa.jpf.symbc.branchcoverage;

import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisCacheImpl;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.IAnalysisCacheView;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;

import com.ibm.wala.ssa.*;
import com.ibm.wala.types.MethodReference;
import gov.nasa.jpf.symbc.BranchListener;
import gov.nasa.jpf.symbc.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.symbc.branchcoverage.reachability.ObligationReachability;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;

import java.util.HashSet;
import java.util.Set;

import static gov.nasa.jpf.symbc.BranchListener.coverageExclusions;
import static gov.nasa.jpf.symbc.branchcoverage.obligation.CoverageUtil.UNKNOWN_PACKAGE;


/**
 * used to collect obligation. There is an instance of this class for each method. It uses cha static object in BranchCoverage
 * and its side effect is population of obligation in the obligationMgr.
 */
public class BranchOblgCollectorVisitor extends SSAInstruction.Visitor {
    //packageName and className of the currently being analyzed method.
    IR ir;
    String walaPackageName;
    String className;
    String methodSig;
    IMethod iMethod;
    int irInstIndex;
    public static HashSet<String> visitedClassesMethod = new HashSet<>();

    public BranchOblgCollectorVisitor(IR ir, String walaPackageName, String className, String methodSig, IMethod iMethod, int irInstIndex) {
        this.ir = ir;
        this.walaPackageName = walaPackageName;
        this.className = className;
        this.methodSig = methodSig;
        this.iMethod = iMethod;
        this.irInstIndex = irInstIndex;
    }

    public static void finishedCollection() {
        visitedClassesMethod = null;
    }

    /**
     * when encountering a branch we need to collect its obligation
     *
     * @param inst
     */
    public void visitConditionalBranch(SSAConditionalBranchInstruction inst) {
        if (coverageExclusions.contains(constructWalaSign())) // do not look for covering these oblg if they exist in the coverageExclusion.
            return;

        int instLine = CoverageUtil.getWalaInstLineNum(iMethod, inst);
        /*inst.iIndex();
        try { //no need for this to find the instruction index, it is already available in the instruction itself.
            instLine = (((IBytecodeMethod) (iMethod)).getBytecodeIndex(irInstIndex));
        } catch (InvalidClassFileException e) {
            System.out.println("exception while getting instruction index from wala. Failing");
            e.printStackTrace();
        }*/

        Pair<Set<String>, HashSet<Obligation>> reacheableMethodThenOblgsPair = (new ObligationReachability(ir, inst, ObligationSide.THEN)).reachableObligations();
        Pair<Set<String>, HashSet<Obligation>> reacheableMethodElseOblgsPair = (new ObligationReachability(ir, inst, ObligationSide.ELSE)).reachableObligations();

        ObligationMgr.addOblgMap(walaPackageName, className, methodSig, instLine, inst, ir.getControlFlowGraph().getBlockForInstruction(inst.iIndex()), reacheableMethodThenOblgsPair, reacheableMethodElseOblgsPair);

    }

    private String constructWalaSign() {
        String refinedMethodSig = methodSig.replaceAll(";", "");
        if (walaPackageName.equals(UNKNOWN_PACKAGE))
            return "L" + className + "." + refinedMethodSig;
        else
            return "L" + walaPackageName + "/" + className + "." + refinedMethodSig;
    }


    /**
     * when encountering an invoke we need to collect obligations of the invoked method, if it is user method.
     *
     * @param instruction
     */
    public void visitInvoke(SSAInvokeInstruction instruction) {
        MethodReference mr = instruction.getDeclaredTarget();
        IMethod m = BranchCoverage.cha.resolveMethod(mr);
        AnalysisOptions options = new AnalysisOptions();
        options.getSSAOptions().setPiNodePolicy(SSAOptions.getAllBuiltInPiNodes());
        IAnalysisCacheView cache = new AnalysisCacheImpl(options.getSSAOptions());
        IR ir = cache.getIR(m, Everywhere.EVERYWHERE);

        String walaPackageName = CoverageUtil.getWalaPackageName(m);
        String className = m.getDeclaringClass().getName().getClassName().toString();
        String methodSignature = m.getSelector().toString();

        String classUniqueName = CoverageUtil.classUniqueName(walaPackageName, className, methodSignature);

        //return from collecting obligations from the invoked method if we have already visited it or if it is not loaded by Application class loader.
        if (visitedClassesMethod.contains(classUniqueName) || !(m.getDeclaringClass().getClassLoader().toString().equals("Application")))
            return;
        visitedClassesMethod.add(classUniqueName);

        BranchOblgCollectorVisitor branchOblgCollectorVisitor = null;

        for (int irInstIndex = 0; irInstIndex < ir.getInstructions().length; irInstIndex++) {
            SSAInstruction ins = ir.getInstructions()[irInstIndex];
            if (ins != null) {
                if (branchOblgCollectorVisitor == null)
                    branchOblgCollectorVisitor = new BranchOblgCollectorVisitor(ir, walaPackageName, className, methodSignature, m, irInstIndex);
                else branchOblgCollectorVisitor.updateInstIndex(irInstIndex);
                ins.visit(branchOblgCollectorVisitor);
            }
        }
    }

    public void updateInstIndex(int irInstIndex) {
        this.irInstIndex = irInstIndex;
    }
}
