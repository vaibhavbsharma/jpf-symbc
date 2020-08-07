package gov.nasa.jpf.symbc.veritesting.ast.transformations.ssaToAst;

import gov.nasa.jpf.symbc.veritesting.ast.def.ArrayRefVarExpr;
import gov.nasa.jpf.symbc.veritesting.ast.def.AstVarExpr;
import gov.nasa.jpf.symbc.veritesting.ast.def.FieldRefVarExpr;
import gov.nasa.jpf.symbc.veritesting.ast.def.GammaVarExpr;
import gov.nasa.jpf.symbc.veritesting.ast.def.IfThenElseExpr;
import gov.nasa.jpf.symbc.veritesting.ast.def.WalaVarExpr;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.InputTable;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.SlotParamTable;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitorAdapter;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.IntVariable;
import za.ac.sun.cs.green.expr.Operation;
import za.ac.sun.cs.green.expr.RealConstant;
import za.ac.sun.cs.green.expr.RealVariable;
import za.ac.sun.cs.green.expr.StringConstantGreen;
import za.ac.sun.cs.green.expr.StringVariable;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A simple visitor that returns true if an expression reads a given Wala variable.
 */
public class DoesExprReadWalaVarVisitor implements ExprVisitor<Boolean> {

    private final WalaVarExpr walaVarExpr;
    protected ExprVisitorAdapter<Boolean> eva =
            new ExprVisitorAdapter<>(this);

    public DoesExprReadWalaVarVisitor(final WalaVarExpr walaVarExpr) {
        this.walaVarExpr = walaVarExpr;
    }

    @Override
    public Boolean visit(IntConstant expr) {
        return false;
    }

    @Override
    public Boolean visit(IntVariable expr) {
        return false;
    }

    @Override
    public Boolean visit(Operation expr) {
        Boolean operandsHaveTargetWalaVar = false;
        for (Expression e: expr.getOperands()) {
            operandsHaveTargetWalaVar = operandsHaveTargetWalaVar || eva.accept(e);
        }
        return operandsHaveTargetWalaVar;
    }

    @Override
    public Boolean visit(RealConstant expr) {
        return false;
    }

    @Override
    public Boolean visit(RealVariable expr) {
        return false;
    }

    @Override
    public Boolean visit(StringConstantGreen expr) {
        return false;
    }

    @Override
    public Boolean visit(StringVariable expr) {
        return false;
    }

    @Override
    public Boolean visit(IfThenElseExpr expr) {
        return eva.accept(expr.condition) || eva.accept(expr.thenExpr) || eva.accept(expr.elseExpr);
    }

    @Override
    public Boolean visit(ArrayRefVarExpr expr) {
        return false;
    }

    @Override
    public Boolean visit(WalaVarExpr expr) {
        return expr.equals(walaVarExpr);
    }

    @Override
    public Boolean visit(FieldRefVarExpr expr) {
        return false;
    }

    @Override
    public Boolean visit(GammaVarExpr expr) {
        return false;
    }

    @Override
    public Boolean visit(AstVarExpr expr) {
        return false;
    }
}
