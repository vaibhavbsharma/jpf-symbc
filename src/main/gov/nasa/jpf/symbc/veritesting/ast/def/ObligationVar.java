package gov.nasa.jpf.symbc.veritesting.ast.def;

import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAConditionalBranchInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.symbc.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;
import gov.nasa.jpf.vm.ThreadInfo;

/**
 * This class is used to create obligations variables, which are instance of Global Vars,
 * whose names are slightly different.
 */

public class ObligationVar extends GlobalJRVar {
    public final Obligation oblg;

    public ObligationVar(Obligation oblg) {
        super(oblg.toString());
        this.oblg = oblg;
    }

    @Override
    protected ObligationVar clone() {
        return new ObligationVar(this.oblg);
    }

    @Override
    public String toString() {
        return oblg.methodSig + "_" + oblg.instLine + "_" + oblg.oblgSide;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ObligationVar) {
            return oblg.toString().equals(((ObligationVar) obj).oblg.toString());
        } else return false;
    }

    @Override
    public int hashCode() {
        return oblg.hashCode();
    }

/*
    public static ObligationVar makePutFieldRef(Object obj) throws StaticRegionException {
        assert obj instanceof Pair : "unexpected instruction type for creating obligation. Assumption Violated. Failing";
        assert ((Pair<?, ?>) obj).getFirst() instanceof Pair : "unexpected instruction type for creating obligation. Assumption Violated. Failing";
        assert ((Pair<?, ?>) obj).getSecond() instanceof ObligationSide : "unexpected instruction type for creating obligation. Assumption Violated. Failing";
        assert ((Pair) ((Pair<?, ?>) obj).getFirst()).getFirst() instanceof IR : "unexpected instruction type for creating obligation. Assumption Violated. Failing";
        assert ((Pair) ((Pair<?, ?>) obj).getFirst()).getSecond() instanceof SSAConditionalBranchInstruction : "unexpected instruction type for creating obligation. Assumption Violated. Failing";

        ObligationSide side = (ObligationSide) ((Pair<?, ?>) obj).getSecond();
        IR ir = (IR) ((Pair) ((Pair<?, ?>) obj).getFirst()).getFirst();
        SSAConditionalBranchInstruction inst = (SSAConditionalBranchInstruction) ((Pair) ((Pair<?, ?>) obj).getFirst()).getSecond();
        Obligation oblg = CoverageUtil.createOblgFromWalaInst(ir, inst, side);
        return new ObligationVar(oblg);
    }*/
}
