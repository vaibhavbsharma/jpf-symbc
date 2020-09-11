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
 * Takes a region with returns and produces a region that have early returns conditioned
 */

public class ConditionReturns extends AstMapVisitor {
    static int index = 0;
    ArrayList<WalaVarExpr> returnOccurredVars = new ArrayList<>();
    Expression innerPC;
    WalaVarExpr inPathRetOccurredVar;


    public ConditionReturns(ExprVisitor<Expression> exprVisitor) {
        super(exprVisitor);
    }

    @Override
    public Stmt visit(CompositionStmt a) {

        Stmt newS1 = a.s1.accept(this);
        Expression returnNotOccuredCond = null;
        if (a.s1 instanceof IfThenElseStmt)
            returnNotOccuredCond = constructReturnVarCondition();

        Stmt newS2 = a.s2.accept(this);
        if (returnNotOccuredCond != null)
            newS2 = new IfThenElseStmt(null, returnNotOccuredCond, newS2, SkipStmt.skip, false, false, null);

        return new CompositionStmt(newS1, newS2);

    }

    @Override
    public Stmt visit(IfThenElseStmt a) {
        Expression oldInnerPC = innerPC;
        Expression thenCond = innerPC == null ? a.condition : new Operation(Operation.Operator.AND, innerPC, a.condition);
        innerPC = thenCond;

        Pair<WalaVarExpr, Expression> thenRetOccurredPair = null;
        Pair<WalaVarExpr, Expression> elseReOccurredPair = null;

        Stmt thenStmt = a.thenStmt.accept(this);
        if (inPathRetOccurredVar != null) {
            thenRetOccurredPair = new Pair<>(inPathRetOccurredVar, thenCond);
            returnOccurredVars.add(inPathRetOccurredVar);
        }

        inPathRetOccurredVar = null;


        Expression elseCond = innerPC == null ? a.condition : new Operation(Operation.Operator.AND, new Operation(Operation.Operator.NOT, innerPC), a.condition);

        innerPC = elseCond;
        Stmt elseStmt = a.elseStmt.accept(this);
        if (inPathRetOccurredVar != null) {
            elseReOccurredPair = new Pair<>(inPathRetOccurredVar, elseCond);
            returnOccurredVars.add(inPathRetOccurredVar);
        }

        inPathRetOccurredVar = null;

        innerPC = oldInnerPC;
        IfThenElseStmt newIf = new IfThenElseStmt(a.original, eva.accept(a.condition), thenStmt, elseStmt, a.genuine, a.isByteCodeReversed, a.generalOblg);

        return composeReturnGamma(newIf, thenRetOccurredPair, elseReOccurredPair);
    }

    @Override
    public Stmt visit(ReturnInstruction c) {
        inPathRetOccurredVar = createNewRetOccurredVar();
        return new ReturnInstruction(c.getOriginal(), eva.accept(c.rhs));
    }


    public static Stmt composeReturnGamma(IfThenElseStmt ifStmt, Pair<WalaVarExpr, Expression> thenRetOccurredPair, Pair<WalaVarExpr, Expression> elseRetOccurredPair) {
        if (thenRetOccurredPair == null)
            if (elseRetOccurredPair == null)
                return ifStmt;
            else {
                AssignmentStmt retOccurredStmt = new AssignmentStmt(elseRetOccurredPair.getFirst(), new GammaVarExpr(elseRetOccurredPair.getSecond(), new IntConstant(1), new IntConstant(0)));
                return new CompositionStmt(ifStmt, retOccurredStmt);
            }
        else {
            if (elseRetOccurredPair == null) {
                AssignmentStmt retOccurredStmt = new AssignmentStmt(thenRetOccurredPair.getFirst(), new GammaVarExpr(thenRetOccurredPair.getSecond(), new IntConstant(1), new IntConstant(0)));
                return new CompositionStmt(ifStmt, retOccurredStmt);
            }
            AssignmentStmt thenRetOccurredStmt = new AssignmentStmt(thenRetOccurredPair.getFirst(), new GammaVarExpr(thenRetOccurredPair.getSecond(), new IntConstant(1), new IntConstant(0)));
            AssignmentStmt elseRetOccurredStmt = new AssignmentStmt(elseRetOccurredPair.getFirst(), new GammaVarExpr(elseRetOccurredPair.getSecond(), new IntConstant(1), new IntConstant(0)));
            return new CompositionStmt(ifStmt, new CompositionStmt(thenRetOccurredStmt, elseRetOccurredStmt));
        }

    }

    private Expression constructReturnVarCondition() {
        if (returnOccurredVars.size() == 0)
            return null;

        Expression cond = new Operation(Operation.Operator.EQ, returnOccurredVars.get(0), new IntConstant(0));
        if (returnOccurredVars.size() > 1) {
            for (int i = 1; i < returnOccurredVars.size(); i++) {
                cond = new Operation(Operation.Operator.AND, cond, new Operation(Operation.Operator.EQ, returnOccurredVars.get(1)), new IntConstant(0));
            }
        }

        return cond;
    }

    public static WalaVarExpr createNewRetOccurredVar() {
        return new WalaVarExpr("retOccurred$" + index++);
    }

    public static StaticRegion execute(StaticRegion region) throws StaticRegionException {
        ConditionReturns conditionReturns = new ConditionReturns(new ExprMapVisitor());
        Stmt stmt = region.staticStmt.accept(conditionReturns);


        return new StaticRegion(stmt, region, null);

    }
}
