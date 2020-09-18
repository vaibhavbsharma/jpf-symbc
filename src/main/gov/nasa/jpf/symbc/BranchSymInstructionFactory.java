package gov.nasa.jpf.symbc;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.vm.Instruction;

public class BranchSymInstructionFactory extends SymbolicInstructionFactory {


    public static boolean GuideBranchExploration = true;

    public BranchSymInstructionFactory(Config conf) {
        super(conf);
    }

    public Instruction if_icmpeq(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IF_ICMPEQ(targetPc): super.if_icmpeq(targetPc);
    }

    public Instruction if_icmpge(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IF_ICMPGE(targetPc): super.if_icmpge(targetPc);
    }

    public Instruction if_icmpgt(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IF_ICMPGT(targetPc): super.if_icmpgt(targetPc);
    }

    public Instruction if_icmple(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IF_ICMPLE(targetPc): super.if_icmple(targetPc);
    }

    public Instruction if_icmplt(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IF_ICMPLT(targetPc): super.if_icmplt(targetPc);
    }

    public Instruction if_icmpne(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IF_ICMPNE(targetPc): super.if_icmpne(targetPc);
    }

    public Instruction ifeq(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IFEQ(targetPc): super.ifeq(targetPc);
    }

    public Instruction ifge(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IFGE(targetPc): super.ifge(targetPc);
    }

    public Instruction ifgt(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IFGT(targetPc): super.ifgt(targetPc);
    }

    public Instruction ifle(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IFLE(targetPc): super.ifle(targetPc);
    }

    public Instruction iflt(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IFLT(targetPc): super.iflt(targetPc);
    }

    public Instruction ifne(int targetPc) {
        return (GuideBranchExploration) ? new gov.nasa.jpf.symbc.bytecode.branchchoices.IFNE(targetPc): super.ifne(targetPc);
    }

}
