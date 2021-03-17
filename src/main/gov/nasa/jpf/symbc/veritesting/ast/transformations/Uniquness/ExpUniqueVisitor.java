package gov.nasa.jpf.symbc.veritesting.ast.transformations.Uniquness;

import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.Expression;

/**
 * Unique Expression Visitor that ensures the uniqueness of vars used inside the region.
 */
public class ExpUniqueVisitor extends ExprMapVisitor implements ExprVisitor<Expression> {

    int uniqueNum;

    public ExpUniqueVisitor(int uniqueNum) {
        super();
        this.uniqueNum = uniqueNum;
    }

    /**
     * A visit to all WalaVariables to create a new-unique object for the same var number.
     *
     * @param expr WalaVariable that is visited
     * @return either a new unique wala var object or already an existing unique wala var object.
     */
    @Override
    public Expression visit(WalaVarExpr expr) {
        return expr.makeUnique(uniqueNum);
    }

    @Override
    public Expression visit(AstVarExpr expr) {
        return expr.makeUnique(uniqueNum);
    }

    /**
     * A visit to all FieldRefVarExpr to create a new-unique object for the same var number.
     *
     * @param expr FieldRefVarExpr that is visited
     * @return either a new unique FieldRefVarExpr or already an existing unique FieldRefVarExpr object.
     */
    @Override
    public Expression visit(FieldRefVarExpr expr) {
        return expr.makeUnique(uniqueNum);
    }

    @Override
    public Expression visit(GlobalJRVarSSAExpr expr) {
        return expr.makeUnique(uniqueNum);
    }

    /**
     * A visit to all ArrayRefVarExpr to create a new-unique object for the same var number.
     *
     * @param expr ArrayRefVarExpr that is visited
     * @return either a new unique ArrayRefVarExpr or already an existing unique ArrayRefVarExpr object.
     */
    @Override
    public Expression visit(ArrayRefVarExpr expr) {
        return expr.makeUnique(uniqueNum);
    }
}
