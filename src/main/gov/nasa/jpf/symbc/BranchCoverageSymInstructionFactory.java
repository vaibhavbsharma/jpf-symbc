package gov.nasa.jpf.symbc;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.vm.Instruction;

public class BranchCoverageSymInstructionFactory extends SymbolicInstructionFactory {

    public static boolean flipBranchExploration = true;
    public BranchCoverageSymInstructionFactory(Config conf) {
        super(conf);
    }

    public Instruction if_icmpne(int targetPc) {
        return (flipBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchcoverage.IF_ICMPNE(targetPc): super.if_icmpne(targetPc);
    }

    public Instruction if_icmpge(int targetPc) {
        return (flipBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchcoverage.IF_ICMPGE(targetPc): super.if_icmpge(targetPc);
    }

    public Instruction if_icmple(int targetPc) {
        return (flipBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchcoverage.IF_ICMPLE(targetPc): super.if_icmple(targetPc);
    }

}
