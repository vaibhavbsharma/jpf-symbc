package gov.nasa.jpf.symbc.interpolation.ast;

import gov.nasa.jpf.symbc.interpolation.shadow.ShadowStackFrame;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.*;

/**
 * binds the value of a slotVar to specific expression based on the valuation of the shadow memory
 */
public class SubstituteVisitor extends ExprMapVisitor implements ExprVisitor<Expression>, gov.nasa.jpf.symbc.interpolation.ast.ExprVisitor<Expression> {

    Expression newExpr;
    SlotVar forVar;

    public SubstituteVisitor(Expression newExpr, SlotVar forVar) {
        this.newExpr = newExpr;
        this.forVar = forVar;
    }


    public static Expression execute(Expression newExpr, SlotVar forVar, Expression interpol) {
        SubstituteVisitor substituteVisitor = new SubstituteVisitor(newExpr, forVar);
        return substituteVisitor.eva.accept(interpol);
    }


    @Override
    public Expression visit(SlotVar slotVar) {
        if (slotVar.equals(forVar))
            return newExpr;
        else return slotVar;
    }
}
