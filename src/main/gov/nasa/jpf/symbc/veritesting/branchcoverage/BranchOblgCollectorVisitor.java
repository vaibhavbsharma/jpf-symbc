package gov.nasa.jpf.symbc.veritesting.branchcoverage;

import com.ibm.wala.classLoader.ClassLoaderImpl;
import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisCacheImpl;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.IAnalysisCacheView;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;

import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.*;
import com.ibm.wala.types.MethodReference;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.ObligationMgr;

import java.util.HashSet;


/**
 * used to collect obligation. There is an instance of this class for each method. It uses cha static object in BranchCoverage
 * and its side effect is population of obligation in the obligationMgr.
 */
public class BranchOblgCollectorVisitor extends SSAInstruction.Visitor {
    //packageName and className of the currently being analyzed method.
    String walaPackageName;
    String className;
    String methodSig;
    IMethod iMethod;
    int irInstIndex;
    public static HashSet<String> visitedClasses = new HashSet<>();

    public BranchOblgCollectorVisitor(String walaPackageName, String className, String methodSig, IMethod iMethod, int irInstIndex) {
        this.walaPackageName = walaPackageName;
        this.className = className;
        this.methodSig = methodSig;
        this.iMethod = iMethod;
        this.irInstIndex = irInstIndex;
    }

    /**
     * when encountering a branch we need to collect its obligation
     *
     * @param inst
     */
    public void visitConditionalBranch(SSAConditionalBranchInstruction inst) {
        int instLine = 0;
        try {
            instLine = (((IBytecodeMethod) (iMethod)).getBytecodeIndex(irInstIndex));
        } catch (InvalidClassFileException e) {
            System.out.println("exception while getting instruction index from wala. Failing");
            e.printStackTrace();
        }
        ObligationMgr.addOblgMap(walaPackageName, className, methodSig, instLine, inst, null);
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

        String walaPackageName = m.getDeclaringClass().getName().getPackage().toString();
        String className = m.getDeclaringClass().getName().getClassName().toString();
        String methodSignature = m.getSelector().toString();

        String classUniqueName = CoverageUtil.classUniqueName(walaPackageName, className);

        //return from collecting obligations from the invoked method if we have already visited it or if it is not loaded by Application class loader.
        if (visitedClasses.contains(classUniqueName) || !(m.getDeclaringClass().getClassLoader().toString().equals("Application")))
            return;
        visitedClasses.add(classUniqueName);

        BranchOblgCollectorVisitor branchOblgCollectorVisitor = null;

        for (int irInstIndex = 0; irInstIndex < ir.getInstructions().length; irInstIndex++) {
            SSAInstruction ins = ir.getInstructions()[irInstIndex];
            if (ins != null) {
                if (branchOblgCollectorVisitor == null)
                    branchOblgCollectorVisitor = new BranchOblgCollectorVisitor(walaPackageName, className, methodSignature, m, irInstIndex);
                else
                    branchOblgCollectorVisitor.updateInstIndex(irInstIndex);
                ins.visit(branchOblgCollectorVisitor);
            }
        }
    }

    public void updateInstIndex(int irInstIndex) {
        this.irInstIndex = irInstIndex;
    }
}
