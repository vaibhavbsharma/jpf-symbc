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

import static com.ibm.wala.cfg.Util.getNotTakenSuccessor;
import static com.ibm.wala.cfg.Util.getTakenSuccessor;

public class ObligationReachability {

    IR ir;
    private SSACFG cfg;
    private ISSABasicBlock interestingSuccBB;

    //used identify loops in traversing predecessors, thus failing in this case.
    private HashSet<ISSABasicBlock> seenBlocks;


    public ObligationReachability(IR ir, SSAConditionalBranchInstruction ifInst, ObligationSide side) {
        this.ir = ir;
        this.cfg = ir.getControlFlowGraph();
        SSACFG.BasicBlock branchBB = cfg.getBlockForInstruction(ifInst.iIndex());
        if (side == ObligationSide.ELSE) //getting the "then" successor - they are filliped in WALA
            interestingSuccBB = getNotTakenSuccessor(cfg, branchBB);
        else { //getting the "else" successor
            interestingSuccBB = getTakenSuccessor(cfg, branchBB);
        }
        this.seenBlocks = new HashSet<>();
    }


    //borrowed from GraphSlicer from WALA
    public <T> Set<T> reachableSubset(Graph<T> g, Predicate<T> p) {
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
                seenBlocks = new HashSet<>();
            }
            return roots;
        }
    }


    public Set<ISSABasicBlock> buildReachableBB() {

        Predicate<ISSABasicBlock> isSuccessor = new Predicate<ISSABasicBlock>() {
            @Override
            public boolean test(ISSABasicBlock bb) {
                if (interestingSuccBB == bb) { // if it is me, then return true, since I am reachable by definition
                    return true;
                } else {
                    HashSet<ISSABasicBlock> allPredecessors = collectPredecessors(new HashSet<>(), bb);
                    if (allPredecessors == null)
                        return false;
                    else
                        return allPredecessors.contains(interestingSuccBB);
                }
            }
        };
        Set<ISSABasicBlock> reachableSet = reachableSubset(cfg, isSuccessor);
        return reachableSet;
    }

    /**
     * collects all predecessors for BasicBlock bb and check if the interestingBB is one of them.
     *
     * @param collectedPred
     * @param bb
     * @return
     */
    private HashSet<ISSABasicBlock> collectPredecessors(HashSet<ISSABasicBlock> collectedPred, ISSABasicBlock bb) {

        HashSet<ISSABasicBlock> predecessors = new HashSet<>(cfg.getNormalPredecessors(bb));
        if (predecessors.size() == 0)
            return collectedPred;
        else if (collectedPred.contains(interestingSuccBB)) //save collecting predecessors if we already have the interestingBB collected already, in which case we just want to return.
            return collectedPred;
        else
            for (ISSABasicBlock predecessor : predecessors) {
                if (!collectedPred.contains(predecessor)) {
                    collectedPred.add(predecessor);
                    HashSet<ISSABasicBlock> parentPred = (collectPredecessors(collectedPred, predecessor));
                    collectedPred.addAll(parentPred);
                }
            }
        return (HashSet<ISSABasicBlock>) collectedPred;
    }

/*

    public Set<ISSABasicBlock> buildReachableBB() {

        Predicate<ISSABasicBlock> isSuccessor = new Predicate<ISSABasicBlock>() {
            @Override
            public boolean test(ISSABasicBlock bb) {
                if (seenBlocks.contains(bb)) {
                    return false;
                } else
                    seenBlocks.add(bb);
                if (interestingSuccBB == bb) { // if it is me, then return true, since I am reachable by definition
                    return true;
                } else if (cfg.getNormalPredecessors(bb).size() >= 1) { //case we have more than one predecessor to check
                    Object[] predecessors = cfg.getNormalPredecessors(bb).toArray();
                    for (Object predecessor : predecessors) {//testing all unseen BBs
                        if (test((ISSABasicBlock) predecessor)) {
                            return true;
                        } else {
                            seenBlocks.clear();
                            seenBlocks.add(bb);
                        }
                    }
                    return false;
                } else if (cfg.getNormalPredecessors(bb).size() == 0) {
                    return false;
                } else {// unexpected scenario.
                    assert false;
                    return false;
                }
            }
        };
        Set<ISSABasicBlock> reachableSet = reachableSubset(cfg, isSuccessor);
        return reachableSet;
    }
*/

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
