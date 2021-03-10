package gov.nasa.jpf.symbc.interpolation.creation;

import gov.nasa.jpf.jvm.bytecode.*;
import gov.nasa.jpf.symbc.interpolation.CoveredSides;
import gov.nasa.jpf.symbc.interpolation.ast.Guard;
import gov.nasa.jpf.symbc.interpolation.ast.SlotVar;
import gov.nasa.jpf.symbc.veritesting.ast.def.AssignmentStmt;
import gov.nasa.jpf.symbc.veritesting.ast.def.Stmt;
import gov.nasa.jpf.vm.Instruction;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.Operation;

import java.util.*;

import static gov.nasa.jpf.symbc.interpolation.CoveredSides.ELSE;
import static gov.nasa.jpf.symbc.interpolation.CoveredSides.THEN;
import static gov.nasa.jpf.symbc.interpolation.InterpolationMain.updateCoveredInstSide;

public class TraceToAST {

    Stack<Expression> jvmOperands = new Stack<>();

    //output of the translation.
    Deque<Stmt> stmts = new ArrayDeque<>();

    public Deque<Stmt> execute(Deque<Instruction> instTrace) {
        stmts = new ArrayDeque<>();
        jvmOperands = new Stack<>();

        Iterator<Instruction> instItr = instTrace.iterator();
        Instruction inst = instItr.next();
        while (inst != null) {
            Instruction nextInst = instItr.hasNext() ? instItr.next() : null;
            switch (inst.getClass().getSimpleName()) {
                case "GCinstruction":
                    break;
                case "IF_ICMPNE":
                    Expression op1 = jvmOperands.pop();
                    Expression op2 = jvmOperands.pop();
                    Operation.Operator op;
                    if (((IF_ICMPNE) inst).getTarget().equals(nextInst)) {
                        op = Operation.Operator.NE;
                        updateCoveredInstSide((IfInstruction) inst, THEN);
                    } else {
                        op = Operation.Operator.EQ;
                        updateCoveredInstSide((IfInstruction) inst, ELSE);
                    }
                    stmts.add(new Guard(new Operation(op, op1, op2), (IfInstruction) inst));
                    break;
                case "IFEQ":
                    op1 = jvmOperands.pop();
                    if (((IFEQ) inst).getTarget().equals(nextInst)) {
                        op = Operation.Operator.EQ;
                        updateCoveredInstSide((IfInstruction) inst, THEN);
                    } else {
                        op = Operation.Operator.NE;
                        updateCoveredInstSide((IfInstruction) inst, ELSE);
                    }
                    stmts.add(new Guard(new Operation(op, op1, new IntConstant(0)), (IfInstruction) inst));
                    break;
                case "IF_ICMPEQ":
                    assert false : "unsupported instruction";
                    break;
                case "IF_ICMPGT":
                    assert false : "unsupported instruction";
                    break;
                case "IF_ICMPLT":
                    assert false : "unsupported instruction";
                    break;
                case "IFGT":
                    assert false : "unsupported instruction";
                    break;
                case "IFLT":
                    assert false : "unsupported instruction";
                    break;
                case "ICONST":
                    int val = ((ICONST) inst).getValue();
                    jvmOperands.push(new IntConstant(val));
                    break;
                case "IADD":
                    op1 = jvmOperands.pop();
                    op2 = jvmOperands.pop();
                    jvmOperands.push(new Operation(Operation.Operator.ADD, op1, op2));
                    break;
                case "ILOAD":
                    jvmOperands.push(new SlotVar(((ILOAD) inst).getLocalVariableSlot()));
                    break;
                case "ISTORE":
                    Expression operand = jvmOperands.pop();
                    SlotVar var = new SlotVar(((ISTORE) inst).getLocalVariableSlot());
                    stmts.add(new AssignmentStmt(var, operand));
                    break;
                case "IRETURN":
                    assert false : "unsupported instruction";
                    break;
                case "REDIRECTCALLRETURN":
                    assert false : "a trace should not have this bytecode. Assumption violated. Failing";
                    break;
                case "POP":
                    jvmOperands.pop();
                    break;
                default:
                    assert false : "unsupported instruction";
                    break;
            }
            inst = nextInst;
        }
        return stmts;
    }
}
