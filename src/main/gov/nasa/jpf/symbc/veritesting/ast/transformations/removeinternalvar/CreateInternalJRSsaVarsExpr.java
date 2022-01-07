package gov.nasa.jpf.symbc.veritesting.ast.transformations.removeinternalvar;

import gov.nasa.jpf.symbc.veritesting.ast.def.GammaVarExpr;
import gov.nasa.jpf.symbc.veritesting.ast.def.InternalJRSsaVar;
import gov.nasa.jpf.symbc.veritesting.ast.def.InternalJRVar;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitorAdapter;
import za.ac.sun.cs.green.expr.Expression;

public class CreateInternalJRSsaVarsExpr extends ExprMapVisitor {

    InternalJRSsaVar lastPerviouslySsaVar;

    protected ExprVisitorAdapter<Expression> eva =
            new ExprVisitorAdapter<>(this);

    @Override
    public Expression visit(GammaVarExpr expr) {
        return new GammaVarExpr(eva.accept(expr.condition),
                eva.accept(expr.thenExpr),
                eva.accept(expr.elseExpr));
    }

    @Override
    public Expression visit(InternalJRVar expr) {
        return lastPerviouslySsaVar;
    }

}
