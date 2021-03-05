package gov.nasa.jpf.symbc.interpolation.ast;

import gov.nasa.jpf.symbc.interpolation.ast.ExprMapVisitor;
import gov.nasa.jpf.symbc.interpolation.ast.SlotVar;
import gov.nasa.jpf.symbc.interpolation.shadow.ShadowStackFrame;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.*;

/**
 * binds the value of a slotVar to specific expression based on the valuation of the shadow memory
 */
public class SlotValBindExprVisitor extends ExprMapVisitor implements ExprVisitor<Expression>, gov.nasa.jpf.symbc.interpolation.ast.ExprVisitor<Expression> {
    private final ShadowStackFrame frame;

    public SlotValBindExprVisitor(ShadowStackFrame frame) {
        this.frame = frame;
    }

    @Override
    public Expression visit(IfThenElseExpr expr) {
        assert false : "unsupported expression";
        return null;
    }

    @Override
    public Expression visit(ArrayRefVarExpr expr) {
        assert false : "unexpected expression";
        return null;
    }

    @Override
    public Expression visit(WalaVarExpr expr) {
        assert false : "unexpected expression";
        return null;
    }

    @Override
    public Expression visit(InternalJRVar expr) {
        assert false : "unexpected expression";
        return null;
    }

    @Override
    public Expression visit(InternalJRSsaVar expr) {
        assert false : "unexpected expression";
        return null;
    }

    @Override
    public Expression visit(FieldRefVarExpr expr) {
        assert false : "unexpected expression";
        return null;
    }

    @Override
    public Expression visit(GammaVarExpr expr) {
        assert false : "unexpected expression";
        return null;
    }

    @Override
    public Expression visit(AstVarExpr expr) {
        assert false : "unexpected expression";
        return null;
    }

    //dereference the stack slot variable.
    @Override
    public Expression visit(SlotVar slotVar) {
        int slot = slotVar.slot;
        return frame.getSPFSlotExpr(slot);
    }
}
