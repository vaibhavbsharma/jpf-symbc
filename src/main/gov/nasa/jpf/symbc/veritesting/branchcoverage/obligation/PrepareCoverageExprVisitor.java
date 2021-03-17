package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import gov.nasa.jpf.symbc.veritesting.ast.def.InternalJRVar;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import za.ac.sun.cs.green.expr.Expression;

/**
 * This visitor is called on an expression and visits its subexpressions while populating the
 * hasJRVar flag to indicate if the expression contains a JRVar
 */
public class PrepareCoverageExprVisitor extends ExprMapVisitor {
    public boolean hasJRVar = false;

    @Override
    public Expression visit(InternalJRVar expr) {

        hasJRVar = true;
        return expr;
    }

}
