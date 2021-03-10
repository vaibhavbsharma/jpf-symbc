package gov.nasa.jpf.symbc.interpolation.creation;

import gov.nasa.jpf.symbc.interpolation.ast.*;
import gov.nasa.jpf.symbc.interpolation.creation.shadow.ShadowStackFrame;
import gov.nasa.jpf.symbc.veritesting.ast.def.AssignmentStmt;
import gov.nasa.jpf.symbc.veritesting.ast.def.Stmt;
import gov.nasa.jpf.vm.ThreadInfo;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.Operation;

import java.util.Deque;
import java.util.HashMap;

public class WeakestPreConditionVisitor extends AstMapVisitor {

    //the key should be augmented with the call stack too. The expression part should be in the form of implication where the right-hand side is a summarization of the state.
    public static HashMap<String, Expression> wpMap = new HashMap<>();

    //contains the current interpol expression.
    Expression interpol;
    private final ShadowStackFrame shadowSf;

    public WeakestPreConditionVisitor(ExprVisitor<Expression> exprVisitor, ShadowStackFrame frame, Expression interpol) {
        super(exprVisitor);
        this.interpol = interpol;
        this.shadowSf = frame;
    }


    @Override
    public Stmt visit(AssignmentStmt a) {
        //this should only occur for lhs that are definitions of variables used in the interpol expression. otherwise we should not substitute, but in fact update the shadow memory.
        if (a.lhs instanceof SlotVar)
            interpol = SubstituteVisitor.execute(a.rhs, (SlotVar) a.lhs, interpol);
        else assert false : "unsupported assignment of non-stack slot type";
        return null;
    }

    @Override
    public Stmt visit(Guard guard) {
        Expression guardExpr = eva.accept(guard.condition);
        addWpToInst(guard.uniquePgmPoint, new Operation(Operation.Operator.IMPLIES, guardExpr, interpol));
        return null;
    }

    public static void addWpToInst(String uniquePgmPoint, Expression wp) {
        Expression oldWp = wpMap.get(uniquePgmPoint);
        if (oldWp == null) // add the new wp if not found
            wpMap.put(uniquePgmPoint, wp);
        else // disjunct the wp if one was previously found.
            wpMap.put(uniquePgmPoint, new Operation(Operation.Operator.OR, oldWp, wp));
    }


    public static void computeInterpolant(Deque<Stmt> stmts, ThreadInfo ti) {
        ShadowStackFrame frame = new ShadowStackFrame(ti.getTopFrame());
        Stmt lastGuard = stmts.removeLast();
        assert lastGuard instanceof Guard : "unexpected last statement for interpolation. Assumption Violated. Failing.";
        Expression interpol = ((Guard) lastGuard).condition;

        SlotValBindExprVisitor slotValBindExprVisitor = new SlotValBindExprVisitor(frame);
        WeakestPreConditionVisitor wpVisitor = new WeakestPreConditionVisitor(slotValBindExprVisitor, frame, interpol);
        while (!stmts.isEmpty())
            stmts.removeLast().accept(wpVisitor);
    }
}
