package gov.nasa.jpf.symbc.branchcoverage;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisCacheImpl;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.IAnalysisCacheView;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;

import com.ibm.wala.ipa.callgraph.impl.ExplicitCallGraph;
import com.ibm.wala.ssa.*;
import com.ibm.wala.types.MethodReference;
import gov.nasa.jpf.symbc.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.symbc.branchcoverage.reachability.ObligationReachability;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static gov.nasa.jpf.symbc.BranchListener.coverageExclusions;
import static gov.nasa.jpf.symbc.BranchListener.coverageMode;
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
    CGNode callerNode;
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
   /*     if (coverageExclusions.contains(constructWalaSign())) // do not look for covering these oblg if they exist in the coverageExclusion.
            return;
*/
        int instLine = CoverageUtil.getWalaInstLineNum(iMethod, inst);


        if (((coverageMode == CoverageMode.COLLECT_GUIDE) ||
                (coverageMode == CoverageMode.COLLECT_PRUNE) ||
                (coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE))) {
            Pair<Set<String>, HashSet<Obligation>> reacheableMethodThenOblgsPair = (new ObligationReachability(ir, inst, ObligationSide.THEN)).reachableObligations();
            Pair<Set<String>, HashSet<Obligation>> reacheableMethodElseOblgsPair = (new ObligationReachability(ir, inst, ObligationSide.ELSE)).reachableObligations();

            ObligationMgr.addOblgMap(walaPackageName, className, methodSig, instLine, inst, ir.getControlFlowGraph().getBlockForInstruction(inst.iIndex()), reacheableMethodThenOblgsPair, reacheableMethodElseOblgsPair);
        } else
            ObligationMgr.addOblgMap(walaPackageName, className, methodSig, instLine, inst, ir.getControlFlowGraph().getBlockForInstruction(inst.iIndex()));

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
        if (m == null) {
            System.out.println(mr + " :is not a user defined method. Ignoring.");
            return;
        }
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
            if (ins instanceof SSAInvokeInstruction && BranchCoverage.cha.resolveMethod(((SSAInvokeInstruction) ins).getDeclaredTarget()) == null) { //method is not defined in the class hierarchy, thus not a user code.
                System.out.println(ins.toString() + " is not defined in the class hierarchy, thus not a user code, ignoring");
            } else {
                if (ins != null) {
                    if (branchOblgCollectorVisitor == null)
                        branchOblgCollectorVisitor = new BranchOblgCollectorVisitor(ir, walaPackageName, className, methodSignature, m, irInstIndex);
                    else branchOblgCollectorVisitor.updateInstIndex(irInstIndex);


                    if (CoverageUtil.isAbstractInvoke(ins)) { //iterating over subtype implementations of abstract functions.
                        CGNode node = CoverageUtil.findNodeForInst(BranchCoverage.cg, instruction.getDeclaredTarget());
                        if (node != null) {// if the node is null then that means we could not find it in the callgraph and thus we will ignore assuming it is calling non application code. There was an incident about this in Siena, in the Event.attributeNamesIterator, it was however seeing the Map.keySet() being an application code, but it isn't. This isn't a great fix, but should work for now.
                            Set<CGNode> targets = BranchCoverage.cg.getPossibleTargets(node, ((SSAInvokeInstruction) ins).getCallSite());
                            Iterator<CGNode> targetItr = targets.iterator();
                            while (targetItr.hasNext()) {
                                CGNode targetNode = targetItr.next();
                                assert targetNode instanceof ExplicitCallGraph.ExplicitNode : "unexpected type for node. Assumption violated. Failing.";
                                IR targetIR = cache.getIR(targetNode.getMethod(), Everywhere.EVERYWHERE);
                                SSAInstruction[] targetInstructions = targetIR.getInstructions();
                                IMethod mTarget = targetIR.getMethod();
                                String targetWalaPackageName = CoverageUtil.getWalaPackageName(mTarget);
                                String targetClassName = mTarget.getDeclaringClass().getName().getClassName().toString();
                                String targetMethodSignature = mTarget.getSelector().toString();
                                String targetClassUniqueName = CoverageUtil.classUniqueName(targetWalaPackageName, targetClassName, targetMethodSignature);

                                //return from collecting obligations from the invoked method if we have already visited it or if it is not loaded by Application class loader.
                                if (visitedClassesMethod.contains(targetClassUniqueName) || !(mTarget.getDeclaringClass().getClassLoader().toString().equals("Application")))
                                    continue; //ignore those methods that are either visited or are not a user defined code.
                                visitedClassesMethod.add(targetClassUniqueName);

                                BranchOblgCollectorVisitor targetBranchOblgCollectorVisitor = null;
                                for (int targetInstIndex = 0; targetInstIndex < targetInstructions.length; targetInstIndex++) {
                                    SSAInstruction targetInst = targetInstructions[targetInstIndex];
                                    if (targetInst != null) {
                                        if (targetBranchOblgCollectorVisitor == null)
                                            targetBranchOblgCollectorVisitor = new BranchOblgCollectorVisitor(targetIR, targetWalaPackageName, targetClassName, targetMethodSignature, mTarget, targetInstIndex);
                                        else
                                            targetBranchOblgCollectorVisitor.updateInstIndex(targetInstIndex);
                                        targetInst.visit(targetBranchOblgCollectorVisitor);
                                    }
                                }
                            }
                        }
                    } else
                        ins.visit(branchOblgCollectorVisitor);

//                ins.visit(branchOblgCollectorVisitor);
                }
            }
        }
    }

    public void updateInstIndex(int irInstIndex) {
        this.irInstIndex = irInstIndex;
    }
}
