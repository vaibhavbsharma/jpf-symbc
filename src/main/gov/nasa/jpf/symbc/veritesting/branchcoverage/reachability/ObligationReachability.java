package gov.nasa.jpf.symbc.veritesting.branchcoverage.reachability;

import com.ibm.wala.ssa.*;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.collections.CollectionFilter;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.util.graph.GraphReachability;
import com.ibm.wala.util.graph.GraphSlicer;
import com.ibm.wala.util.graph.impl.GraphInverter;
import com.ibm.wala.util.graph.traverse.DFS;
import com.ibm.wala.util.intset.OrdinalSet;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.Obligation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

public class ObligationReachability {

    IR ir;
    private SSACFG cfg;
    private ISSABasicBlock interestingBlock;
    private HashSet<ISSABasicBlock> seenBlocks;


    public ObligationReachability(IR ir, SSAConditionalBranchInstruction ifInst) {
        this.ir = ir;
        this.cfg = ir.getControlFlowGraph();
        this.interestingBlock = cfg.getBlockForInstruction(ifInst.iIndex());
        this.seenBlocks = new HashSet<>();
    }


    //borrowed from GraphSlicer from WALA
    public static <T> Set<T> reachableSubset(Graph<T> g, Predicate<T> p) {
        if (g == null) {
            throw new IllegalArgumentException("g is null");
        } else {
            HashSet<T> roots = HashSetFactory.make();
            Iterator var4 = g.iterator();

            while (var4.hasNext()) {
                T o = (T) var4.next();
                if (p.test(o)) {
                    roots.add(o);
                }
            }
            return roots;
        }
    }


    public Set<ISSABasicBlock> buildReachableBB() {

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
        Set<ISSABasicBlock> reachableSet = reachableSubset(cfg, isSuccessor);
        /*GraphReachability reachableBB = new GraphReachability(cfg, isSuccessor);
        reachableBB.solve(null);
        OrdinalSet reachablilityOutput = reachableBB.getReachableSet(interestingBlock);

        return GraphSlicer.prune(cfg, new CollectionFilter<>(reachableSet));*/
        return reachableSet;
    }


    /**
     * Collects BasicBlocks that have BranchInstructions.
     *
     * @return
     */
    public Set<ISSABasicBlock> buildReachableBranchBB(Set<ISSABasicBlock> intrestingBlocks) {
        Predicate<ISSABasicBlock> isSuccessor = new Predicate<ISSABasicBlock>() {
            @Override
            public boolean test(ISSABasicBlock bb) { //if the last instruction in a basic block is branch then return true
                if ((intrestingBlocks.contains(bb)) && ((SSACFG.BasicBlock) bb).getAllInstructions().size() > 0)
                    return bb.getLastInstruction() instanceof SSAConditionalBranchInstruction;
                else return false;
            }
        };
        Set<ISSABasicBlock> reachableBranchBB = reachableSubset(cfg, isSuccessor);

        return reachableBranchBB;
    }

    public HashSet<Obligation> reachableObligations() {
        Set<ISSABasicBlock> reachableBB = buildReachableBB();
        Set<ISSABasicBlock> reachableBranchBB = buildReachableBranchBB(reachableBB);
        return getObligationsFromBB(reachableBranchBB);
    }

    private HashSet<Obligation> getObligationsFromBB(Set<ISSABasicBlock> reachableBB) {
        HashSet<Obligation> obligations = new HashSet<>();

        Iterator<ISSABasicBlock> bbItr = reachableBB.iterator();
        while (bbItr.hasNext()) {
            ISSABasicBlock bb = bbItr.next();
            SSAInstruction ifInst = bb.getLastInstruction();
            obligations.addAll(CoverageUtil.createOblgFromWalaInst(ir, ifInst));
        }
        return obligations;
    }


}
