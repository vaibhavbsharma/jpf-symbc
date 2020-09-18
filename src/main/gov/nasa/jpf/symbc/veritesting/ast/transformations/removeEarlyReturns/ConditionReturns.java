package gov.nasa.jpf.symbc.veritesting.ast.transformations.removeEarlyReturns;


import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.ssaToAst.StaticRegion;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.Operation;

import java.util.ArrayList;


/**
 * Takes a region with returns and produces a region that have early returns conditioned. It also introduces InternalJRVar to bookmark returns.
 */

public class ConditionReturns extends AstMapVisitor {
    InternalJRVar returnOccurredVar = null;
    Expression innerPC;

    public ConditionReturns(ExprVisitor<Expression> exprVisitor) {
        super(exprVisitor);
    }

    @Override
    public Stmt visit(CompositionStmt a) {

        Stmt newS1 = a.s1.accept(this);

        if ((a.s1 instanceof IfThenElseStmt) && (returnOccurredVar != null)) {
            Expression returnNotOccuredCond = new Operation(Operation.Operator.EQ, returnOccurredVar, new IntConstant(0));
            Stmt newS2 = a.s2.accept(this);
            newS2 = new IfThenElseStmt(null, returnNotOccuredCond, newS2, SkipStmt.skip, false, false, null);

            return new CompositionStmt(newS1, newS2);
        } else {
            Stmt newS2 = a.s2.accept(this);
            return new CompositionStmt(newS1, newS2);
        }
    }

    @Override
    public Stmt visit(IfThenElseStmt a) {

        Expression oldInnerPC = innerPC;
        Expression thenCond = innerPC == null ? a.condition : new Operation(Operation.Operator.AND, innerPC, a.condition);
        innerPC = thenCond;

        Stmt thenStmt = a.thenStmt.accept(this);

        Expression elseCond = innerPC == null ? a.condition : new Operation(Operation.Operator.AND, new Operation(Operation.Operator.NOT, innerPC), a.condition);

        innerPC = elseCond;
        Stmt elseStmt = a.elseStmt.accept(this);

        innerPC = oldInnerPC;

        return new IfThenElseStmt(a.original, eva.accept(a.condition), thenStmt, elseStmt, a.genuine, a.isByteCodeReversed, a.generalOblg);
    }

    @Override
    public Stmt visit(ReturnInstruction c) {
        if (returnOccurredVar == null)
            returnOccurredVar = InternalJRVar.jrVar;

        return new CompositionStmt(new AssignmentStmt(returnOccurredVar, new IntConstant(1)), c);
    }


    public static StaticRegion execute(StaticRegion region) throws StaticRegionException {
        ConditionReturns conditionReturns = new ConditionReturns(new ExprMapVisitor());
        Stmt stmt = region.staticStmt.accept(conditionReturns);

        return new StaticRegion(stmt, region, null);

    }
}
