package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.ast.def.ComplexExpr;
import gov.nasa.jpf.symbc.veritesting.ast.def.IfThenElseStmt;
import gov.nasa.jpf.symbc.veritesting.ast.def.Stmt;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.ssaToAst.StaticRegion;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.Operation;

/**
 * used to separate complex conditions for branch coverage collection.
 */
public class SeperateCmplxCondVisitor extends AstMapVisitor {


    public SeperateCmplxCondVisitor() {
        super(new ExprMapVisitor());
    }

    @Override
    public Stmt visit(IfThenElseStmt a) {
        Expression condition = a.condition;
        //1.check if the condition is complex.
        if (condition instanceof Operation) {//then it is a complex condition
            assert ((Operation) condition).getArity() <= 2 : "Cannot handle operations with more than two operands. Assumption violated. Failing.";
            if (((Operation) condition).getArity() == 1)
                if (condition instanceof ComplexExpr)
                    return new IfThenElseStmt(((ComplexExpr) condition).original, a.condition, a.thenStmt.accept(this), a.elseStmt.accept(this), a.genuine, a.isByteCodeReversed, a.generalOblg);
            if (((Operation) condition).getOperator() == Operation.Operator.OR) { //decompose OR
                Expression lhs = ((Operation) condition).getOperand(0);
                Expression rhs = ((Operation) condition).getOperand(1);
                return new IfThenElseStmt(a.original, lhs, a.thenStmt, new IfThenElseStmt(a.original, rhs, a.thenStmt, a.elseStmt, a.genuine, a.isByteCodeReversed, a.generalOblg).accept(this), a.genuine, a.isByteCodeReversed, a.generalOblg).accept(this);
            } else if (((Operation) condition).getOperator() == Operation.Operator.AND) {
                //decompose AND
                Expression lhs = ((Operation) condition).getOperand(0);
                Expression rhs = ((Operation) condition).getOperand(1);
                return new IfThenElseStmt(a.original, lhs, new IfThenElseStmt(a.original, rhs, a.thenStmt, a.elseStmt, a.genuine, a.isByteCodeReversed, a.generalOblg).accept(this), a.elseStmt, a.genuine, a.isByteCodeReversed, a.generalOblg).accept(this);
            } else return a;

        } else return a;
    }

    public static StaticRegion execute(StaticRegion staticRegion) throws StaticRegionException {

        SeperateCmplxCondVisitor seperateCmplxCondVisitor = new SeperateCmplxCondVisitor();
        Stmt newStmt = staticRegion.staticStmt.accept(seperateCmplxCondVisitor);
        return new StaticRegion(newStmt, staticRegion, staticRegion.earlyReturnResult);
    }
}
