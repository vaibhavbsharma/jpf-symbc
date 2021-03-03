package gov.nasa.jpf.symbc.interpolation.bytecode;

import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;

/*
used to bookmark exploration tree for jpf, to help later recover the instructions along an explored path.
* */
public class GCinstruction extends Instruction {
    public final GCinstructionType cgInstructionType;

    public GCinstruction(GCinstructionType cgInstructionType) {
        this.cgInstructionType = cgInstructionType;
    }

    @Override
    public int getByteCode() {
        assert false : "this should never be called";
        return 0;
    }

    @Override
    public Instruction execute(ThreadInfo threadInfo) {
        assert false : "this should never be called";
        return null;
    }

    @Override
    public String toString(){
        return "GCinstruction " + cgInstructionType.toString();
    }
}
