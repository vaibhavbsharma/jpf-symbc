package gov.nasa.jpf.symbc.veritesting.branchcoverage.reachability;

import com.ibm.wala.ssa.*;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.graph.Graph;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.ObligationSide;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

public class ObligationReachability {

    IR ir;
    private SSACFG cfg;
    private ISSABasicBlock interestingSuccBB;

    //used to reset the state of seen block for each BB being tested.
    private HashSet<ISSABasicBlock> seenBlocks;


    public ObligationReachability(IR ir, SSAConditionalBranchInstruction ifInst, ObligationSide side) {
        this.ir = ir;
        this.cfg = ir.getControlFlowGraph();
        Iterator<ISSABasicBlock> successorItr = cfg.getNormalSuccessors(cfg.getBlockForInstruction(ifInst.iIndex())).iterator();
        if (side == ObligationSide.ELSE) //getting the "then" successor
            interestingSuccBB = successorItr.next();
        else { //getting the "else" successor
            successorItr.next();
            interestingSuccBB = successorItr.next();
        }
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
                if (seenBlocks.contains(bb)) {
                    seenBlocks.clear();
                    return false;
                } else
                    seenBlocks.add(bb);
                if (interestingSuccBB == bb) { // if it is me, then return true, since I am reachable by definition
                    seenBlocks.clear();
                    return true;
                } else if (cfg.getNormalPredecessors(bb).size() >= 1) { //case we have more than one predecessor to check
                    Object[] predecessors = cfg.getNormalPredecessors(bb).toArray();
                    for (Object predecessor : predecessors) {//testing all unseen BBs
                        if (seenBlocks.contains(predecessor)) { // we are visiting the same nodes again indicating a loop is found but we cannot find on this path a predessor that is a successor of the branching instruction.
                            seenBlocks.clear();
                            return false;
                        }
                        if (test((ISSABasicBlock) predecessor)) {
                            seenBlocks.clear();
                            seenBlocks.add(bb);
                            return true;
                        } else seenBlocks.add(bb);
                    }
                    seenBlocks.clear();
                    return false;
                } else if (cfg.getNormalPredecessors(bb).size() == 0) {
                    seenBlocks.clear();
                    return false;
                } else {// unexpected scenario.
                    assert false;
                    seenBlocks.clear();
                    return false;
                }
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
