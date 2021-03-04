package gov.nasa.jpf.symbc.interpolation;

import gov.nasa.jpf.jvm.bytecode.*;
import gov.nasa.jpf.symbc.interpolation.ast.Guard;
import gov.nasa.jpf.symbc.interpolation.ast.SlotVar;
import gov.nasa.jpf.symbc.veritesting.ast.def.AssignmentStmt;
import gov.nasa.jpf.symbc.veritesting.ast.def.Stmt;
import gov.nasa.jpf.vm.Instruction;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.Operation;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class TraceToAST {

    static Stack<Expression> jvmOperands = new Stack<>();

    //output of the translation.
    static LinkedList<Stmt> stmts = new LinkedList<>();


    public static LinkedList<Stmt> execute(Deque<Instruction> instTrace) {
        stmts = new LinkedList<>();
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
                    Operation.Operator op = ((IF_ICMPNE) inst).getTarget().equals(nextInst) ? Operation.Operator.NE : Operation.Operator.EQ;
                    stmts.add(new Guard(new Operation(op, op1, op2)));
                    break;
                case "IFEQ":
                    op1 = jvmOperands.pop();
                    op = ((IFEQ) inst).getTarget().equals(nextInst) ? Operation.Operator.EQ : Operation.Operator.NE;
                    stmts.add(new Guard(new Operation(op, op1, new IntConstant(0))));
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
        /*while (!instTrace.isEmpty()) {
            Instruction inst = instTrace.removeFirst();

            switch (inst.getClass().getSimpleName()) {
                case "GCinstruction":
                    break;
                case "IF_ICMPNE":
                    Expression op1 = jvmOperands.pop();
                    Expression op2 = jvmOperands.pop();
                    Instruction nextInst = instTrace.peekFirst();
                    Operation.Operator op = ((IF_ICMPNE)inst).getTarget().equals(nextInst)? Operation.Operator.NE : Operation.Operator.EQ;
                    stmts.add(new Guard(new Operation(op, op1, op2)));
                    break;
                case "IFEQ":
                    op1 = jvmOperands.pop();
                    nextInst = instTrace.peekFirst();
                    op = ((IFEQ)inst).getTarget().equals(nextInst)? Operation.Operator.EQ : Operation.Operator.NE;
                    stmts.add(new Guard(new Operation(op, op1, new IntConstant(0))));
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
                    jvmOperands.push(new SlotVar(((ILOAD)inst).getLocalVariableSlot()));
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
        }
        return stmts;
}*/
}
