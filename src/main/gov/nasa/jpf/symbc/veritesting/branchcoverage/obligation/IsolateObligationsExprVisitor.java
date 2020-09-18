package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import gov.nasa.jpf.symbc.veritesting.ast.def.GammaVarExpr;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntVariable;
import za.ac.sun.cs.green.expr.Operation;

import java.util.HashMap;

public class IsolateObligationsExprVisitor extends ExprMapVisitor {

    // this map holds the new symbolic variables to their expression relation. The new Symbolic variables are treated later as an obligation variant.
    // this mirrors exactly the map in IsolateObligationVisitor
    HashMap<Expression, Expression> newSymToExprMap = new HashMap<>();

    @Override
    public Expression visit(Operation expr) {
        if (newSymToExprMap.containsKey(expr))
            return newSymToExprMap.get(expr);
        Expression[] operands = new Expression[expr.getArity()];
        int index = 0;
        for (Expression e : expr.getOperands()) {
            operands[index++] = eva.accept(e);
        }
        return new Operation(expr.getOperator(), operands);
    }

    @Override
    public Expression visit(IntVariable expr) {
        return newSymToExprMap.getOrDefault(expr, expr);
    }

}