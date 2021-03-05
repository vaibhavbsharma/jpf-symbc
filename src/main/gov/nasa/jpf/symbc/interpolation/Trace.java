package gov.nasa.jpf.symbc.interpolation;

//holds the trace of instructions along a path, for computing the weakest precondition


import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.symbc.interpolation.bytecode.GCinstruction;
import gov.nasa.jpf.symbc.interpolation.bytecode.GCinstructionType;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.veritesting.ast.def.Ast;
import gov.nasa.jpf.symbc.veritesting.ast.def.Stmt;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;

import java.util.ArrayDeque;
import java.util.Deque;

public class Trace {
    static Deque<Instruction> instTrace = new ArrayDeque<>(); //a queue of all instructions that spf has executed on a particular path

    public static void add(Instruction inst, ChoiceGenerator cg) {
        if (inst instanceof GCinstruction) {
            switch (((GCinstruction) inst).cgInstructionType) {
                case CHOICE_GENERATOR_REGISTERED:
                    if (cg instanceof PCChoiceGenerator)
                        instTrace.removeLast(); //removing isFirstStep call of the branching instruction.
                    instTrace.add(inst);
                    break;
                case CHOICE_GENERATOR_ADVANCED:
                    removeUntilFirst(GCinstructionType.CHOICE_GENERATOR_ADVANCED);
                    break;
                case CHOICE_GENERATOR_PROCESSED:
                    removeUntilFirst(GCinstructionType.CHOICE_GENERATOR_PROCESSED);
                    break;
            }
        } else
            instTrace.add(inst);
    }

    //used to restore the state of the instTrace to the right instruction for the backtracking.
    private static void removeUntilFirst(GCinstructionType cgInstType) {

        boolean done = false;
        do {
            Instruction lastInst = instTrace.peekLast();
            // we stop if we see choice registered case, and remove the registered choice generator if we are being called for CG being processed, vs being called for GC being advanced.
            if (lastInst instanceof GCinstruction) {
                if (((GCinstruction) lastInst).cgInstructionType == GCinstructionType.CHOICE_GENERATOR_REGISTERED && cgInstType == GCinstructionType.CHOICE_GENERATOR_PROCESSED)
                    instTrace.removeLast();
                done = true;
            } else
                instTrace.removeLast();
        } while (!done);
    }

    //This is used to remove all tail instructions until the last if-statement and it following one.
    //This is because it is the last if-statement is what we want to generate interpolant for, and it is the instruction after it that would allow us to know if the then or the else side of the instruction was taken, so we can restore the right condition.

    private static void removeUntilLastBranch() {
        boolean done = false;
        do {
            Instruction lastInst = instTrace.removeLast();
            Instruction nextToLastInst = instTrace.peekLast();
            if (nextToLastInst instanceof IfInstruction) {
                instTrace.add(lastInst);
                done = true;
            }
        } while (!done);
    }


    public static String toStr() {
        StringBuilder str = new StringBuilder();
        for (Instruction i : instTrace)
            str.append("\n" + i.toString());
        return str.toString();
    }

    public static Deque<Stmt> toAST() {
        removeUntilLastBranch();
        return new TraceToAST().execute(instTrace);
    }
}
