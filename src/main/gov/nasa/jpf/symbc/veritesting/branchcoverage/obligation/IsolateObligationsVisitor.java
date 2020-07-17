package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.Expression;

public class IsolateObligationsVisitor extends AstMapVisitor {
    public IsolateObligationsVisitor(ExprVisitor<Expression> exprVisitor) {
        super(exprVisitor);
    }
}
