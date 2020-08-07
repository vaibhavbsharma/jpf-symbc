package gov.nasa.jpf.symbc.veritesting.ast.transformations.substitutestackinput;

import gov.nasa.jpf.symbc.veritesting.ast.def.WalaVarExpr;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitorAdapter;
import za.ac.sun.cs.green.expr.Expression;

public class SubstituteWalaVarWithNewExpr extends ExprMapVisitor implements ExprVisitor<Expression> {
    protected ExprVisitorAdapter<Expression> eva =
            new ExprVisitorAdapter<>(this);
    private final WalaVarExpr originalVar;
    private final Expression newExpression;

    public SubstituteWalaVarWithNewExpr(WalaVarExpr originalVar, Expression newExpression) {
        this.originalVar = originalVar;
        this.newExpression = newExpression;
    }

    @Override
    public Expression visit(WalaVarExpr expr) {
        return expr.equals(originalVar) ? newExpression : expr;
    }

}