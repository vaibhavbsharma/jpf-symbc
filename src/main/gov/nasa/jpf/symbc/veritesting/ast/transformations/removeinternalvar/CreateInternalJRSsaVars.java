package gov.nasa.jpf.symbc.veritesting.ast.transformations.removeinternalvar;


import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.ssaToAst.StaticRegion;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.Operation;

import java.util.*;


/**
 * finds all references to the same variable and creates at the end of the
 * region a gamma that reflects possible valuations of this same variable.
 */

public class CreateInternalJRSsaVars extends AstMapVisitor {

    //specifies the last InternalJR var to allow creation of chain among these vars in terms of chained gammas
    InternalJRSsaVar lastInternalSsaVar = null;

    //specifies the last perviously created internalSSA var in the order of the code. This is different than lastInternalSsaVar in that it is not used to create the phis of the different internalSsaVar
    //but rather it is used to identify which lastPerviouslySsaVar that we have created on the path, so we'd enforce the obligation coverage to demand that none of the previousSSAVars are true which
    // means no early return has happened before reaching that obligation.
    InternalJRSsaVar lastPerviouslySsaVar = null;


    Expression innerPC;

    public CreateInternalJRSsaVars(ExprVisitor<Expression> exprVisitor) {
        super(exprVisitor);
    }

    @Override
    public Stmt visit(AssignmentStmt a) {
        if (a.lhs instanceof InternalJRVar) {
            lastInternalSsaVar = new InternalJRSsaVar();
            lastPerviouslySsaVar = lastInternalSsaVar;
//            return new AssignmentStmt(lastInternalSsaVar, a.rhs);
            assert a.rhs instanceof IntConstant && ((IntConstant) a.rhs).getValue() == 1 : "unexpected assignment value to an internal JRVar. Failing.";
            return new AssignmentStmt(lastInternalSsaVar, new GammaVarExpr(innerPC, a.rhs, new IntConstant(0)));
        }
        return new AssignmentStmt(eva.accept(a.lhs), eva.accept(a.rhs));
    }

    @Override
    public Stmt visit(StoreGlobalInstruction c) {
        ((CreateInternalJRSsaVarsExpr) eva.theVisitor).lastPerviouslySsaVar = lastPerviouslySsaVar;
        return new StoreGlobalInstruction((GlobalJRVar) eva.accept(c.lhs), eva.accept(c.rhs));
    }


    @Override
    public Stmt visit(IfThenElseStmt a) {
        /*if ((((Operation) a.condition).getOperand(0) instanceof InternalJRVar)) {
            assert lastInternalSsaVar != null : "lastInternalSsaVar cannot be null. Assumptions Violated. Failing.";
            return new IfThenElseStmt(a.original, new Operation(((Operation) a.condition).getOperator(), lastInternalSsaVar, ((Operation) a.condition).getOperand(1)), a.thenStmt.accept(this), a.elseStmt.accept(this), a.genuine, a.isByteCodeReversed, a.generalOblg);
        }
*/
        Expression newCondition = a.condition;
        if ((((Operation) a.condition).getOperand(0) instanceof InternalJRVar))
            newCondition = new Operation(((Operation) a.condition).getOperator(), lastInternalSsaVar, ((Operation) a.condition).getOperand(1));

        Expression oldInnerPC = innerPC;
        InternalJRSsaVar oldlastInternalSsaVar = lastInternalSsaVar;

//        lastInternalSsaVar = null; It is important to visit both sides of the if-statement with the lastInternalSsaVar so we are able to create he newCondition for subsequent if-statments,
        // however, we need to know if the subsequent sides, i.e., then, or else, have created new lastInternalSsaVar, in which case we want to have the new one.
        Expression thenCond = innerPC == null ? newCondition : new Operation(Operation.Operator.AND, innerPC, newCondition);
        innerPC = thenCond;

        Stmt thenStmt = a.thenStmt.accept(this);

        InternalJRSsaVar thenInternalSsaVar = lastInternalSsaVar != null && lastInternalSsaVar.equals(oldlastInternalSsaVar) ? null : lastInternalSsaVar;

//        lastInternalSsaVar = null;
        lastInternalSsaVar = oldlastInternalSsaVar;
        Expression elseCond = innerPC == null ? newCondition : new Operation(Operation.Operator.AND, new Operation(Operation.Operator.NOT, innerPC), newCondition);

        innerPC = elseCond;
        Stmt elseStmt = a.elseStmt.accept(this);

        InternalJRSsaVar elseInternalSsaVar = lastInternalSsaVar != null && lastInternalSsaVar.equals(oldlastInternalSsaVar) ? null : lastInternalSsaVar;

        innerPC = oldInnerPC;

        Expression oldlastInternalSsaVarValue = oldlastInternalSsaVar == null ? new IntConstant(0) : oldlastInternalSsaVar;
        IfThenElseStmt newIfStmt = new IfThenElseStmt(a.original, newCondition, thenStmt, elseStmt, a.genuine, a.isByteCodeReversed, a.generalOblg);
        if ((thenInternalSsaVar == null) && (elseInternalSsaVar == null)) {
            lastInternalSsaVar = oldlastInternalSsaVar;
            return newIfStmt;
        } else if ((thenInternalSsaVar != null) && (elseInternalSsaVar != null)) {
            InternalJRSsaVar newSsaVar = new InternalJRSsaVar();
            lastInternalSsaVar = newSsaVar;
            lastPerviouslySsaVar = newSsaVar;
            AssignmentStmt assignmentStmt = new AssignmentStmt(newSsaVar, new GammaVarExpr(thenCond, thenInternalSsaVar, elseInternalSsaVar));
            return new CompositionStmt(newIfStmt, assignmentStmt);
        } else if (thenInternalSsaVar != null) {
            InternalJRSsaVar newSsaVar = new InternalJRSsaVar();
            lastInternalSsaVar = newSsaVar;
            lastPerviouslySsaVar = newSsaVar;
            AssignmentStmt assignmentStmt = new AssignmentStmt(newSsaVar, new GammaVarExpr(thenCond, thenInternalSsaVar, oldlastInternalSsaVarValue));
            return new CompositionStmt(newIfStmt, assignmentStmt);
        } else { //if (elseInternalSsaVar != null)
            InternalJRSsaVar newSsaVar = new InternalJRSsaVar();
            lastInternalSsaVar = newSsaVar;
            lastPerviouslySsaVar = newSsaVar;
            AssignmentStmt assignmentStmt = new AssignmentStmt(newSsaVar, new GammaVarExpr(elseCond, elseInternalSsaVar, oldlastInternalSsaVarValue));
            return new CompositionStmt(newIfStmt, assignmentStmt);
        }
    }

    @Override
    public Stmt visit(CompositionStmt a) {
        return new CompositionStmt(a.s1.accept(this), a.s2.accept(this));

    }


    public static StaticRegion execute(StaticRegion region) throws StaticRegionException {
        CreateInternalJRSsaVars conditionReturns = new CreateInternalJRSsaVars(new CreateInternalJRSsaVarsExpr());
        Stmt stmt = region.staticStmt.accept(conditionReturns);

        return new StaticRegion(stmt, region, null);

    }
}
