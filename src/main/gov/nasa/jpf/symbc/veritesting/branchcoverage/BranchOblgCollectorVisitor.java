package gov.nasa.jpf.symbc.veritesting.branchcoverage;

import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisCacheImpl;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.IAnalysisCacheView;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;

import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.*;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.collections.CollectionFilter;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.util.graph.GraphReachability;
import com.ibm.wala.util.graph.GraphSlicer;
import com.ibm.wala.util.graph.traverse.DFS;
import com.ibm.wala.util.intset.OrdinalSet;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.ObligationMgr;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;


/**
 * used to collect obligation. There is an instance of this class for each method. It uses cha static object in BranchCoverage
 * and its side effect is population of obligation in the obligationMgr.
 */
public class BranchOblgCollectorVisitor extends SSAInstruction.Visitor {
    private SSACFG cfg;
    //packageName and className of the currently being analyzed method.
    IR ir;
    String walaPackageName;
    String className;
    String methodSig;
    IMethod iMethod;
    int irInstIndex;
    public static HashSet<String> visitedClassesMethod = new HashSet<>();
    private ISSABasicBlock interestingBlock;
    private HashSet<ISSABasicBlock> seenBlocks;

    public BranchOblgCollectorVisitor(IR ir, String walaPackageName, String className, String methodSig, IMethod iMethod, int irInstIndex) {
        this.ir = ir;
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
        /*ir.getControlFlowGraph()
        SSACFG cfg = ir.getControlFlowGraph();
        DFS.getReachableNodes(cfg, ir.getBasicBlockForInstruction(inst));
*/
        try {
            buildReachableCFG(inst);
        } catch (CancelException e) {
            e.printStackTrace();
        }

        ObligationMgr.addOblgMap(walaPackageName, className, methodSig, instLine, inst, null);
    }


    public Graph<ISSABasicBlock> buildReachableCFG(SSAConditionalBranchInstruction ifInst) throws CancelException {

        cfg = ir.getControlFlowGraph();
        interestingBlock = cfg.getBlockForInstruction(ifInst.iIndex());
        seenBlocks = new HashSet<>();

        Predicate<ISSABasicBlock> isSuccessor = new Predicate<ISSABasicBlock>() {
            @Override
            public boolean test(ISSABasicBlock bb) {
                seenBlocks.add(bb);
                if (cfg.getNormalSuccessors(interestingBlock).contains(bb)) {
                    seenBlocks.clear();
                    return true;
                } else if (cfg.getNormalPredecessors(bb).size() == 1) {
                    ISSABasicBlock predecessor = cfg.getNormalPredecessors(bb).iterator().next();
                    if (seenBlocks.contains(predecessor)) { // we are visiting the same nodes again indicating a loop is found but we cannot find on this path a predessor that is a successor of the branching instruction.
                        seenBlocks.clear();
                        return false;
                    }
                    seenBlocks.add(predecessor);
                    return test(predecessor);
                } else if (cfg.getNormalPredecessors(bb).size() == 2) {
                    ISSABasicBlock predecessor1 = (ISSABasicBlock) cfg.getNormalPredecessors(bb).toArray()[0];
                    ISSABasicBlock predecessor2 = (ISSABasicBlock) cfg.getNormalPredecessors(bb).toArray()[1];
                    if (seenBlocks.contains(predecessor1)) { // we are visiting the same nodes again indicating a loop is found but we cannot find on this path a predessor that is a successor of the branching instruction.
                        seenBlocks.add(predecessor2);
                        return test(predecessor2);
                    } else if (seenBlocks.contains(predecessor2)) {
                        seenBlocks.add(predecessor1);
                        return test(predecessor1);
                    } else {
                        seenBlocks.add(predecessor1);
                        seenBlocks.add(predecessor2);
                        return test(predecessor1) || test(predecessor2);
                    }
                } else if (cfg.getNormalPredecessors(bb).size() == 0) {
                    seenBlocks.clear();
                    return false;
                } else
                    assert false; // unexpected scenario.
                seenBlocks.clear();
                return false;
            }
        };
        Set<ISSABasicBlock> reachableSet = GraphSlicer.slice(cfg, isSuccessor);
        GraphReachability reachableBB = new GraphReachability(cfg, isSuccessor);
        reachableBB.solve(null);
        OrdinalSet reachablilityOutput = reachableBB.getReachableSet(interestingBlock);

        return GraphSlicer.prune(cfg, new CollectionFilter<>(reachableSet));
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
