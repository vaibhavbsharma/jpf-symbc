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

    InternalJRSsaVar lastInternalSsaVar = null;

    Expression innerPC;

    public CreateInternalJRSsaVars(ExprVisitor<Expression> exprVisitor) {
        super(exprVisitor);
    }

    @Override
    public Stmt visit(AssignmentStmt a) {
        if (a.lhs instanceof InternalJRVar) {
            lastInternalSsaVar = new InternalJRSsaVar();
            return new AssignmentStmt(lastInternalSsaVar, a.rhs);
        }
        return new AssignmentStmt(eva.accept(a.lhs), eva.accept(a.rhs));
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
        lastInternalSsaVar = null;
        Expression thenCond = innerPC == null ? newCondition : new Operation(Operation.Operator.AND, innerPC, newCondition);
        innerPC = thenCond;

        Stmt thenStmt = a.thenStmt.accept(this);

        InternalJRSsaVar thenInternalSsaVar = lastInternalSsaVar;

        lastInternalSsaVar = null;
        Expression elseCond = innerPC == null ? newCondition : new Operation(Operation.Operator.AND, new Operation(Operation.Operator.NOT, innerPC), newCondition);

        innerPC = elseCond;
        Stmt elseStmt = a.elseStmt.accept(this);

        InternalJRSsaVar elseInternalSsaVar = lastInternalSsaVar;

        innerPC = oldInnerPC;

        Expression oldlastInternalSsaVarValue = oldlastInternalSsaVar == null ? new IntConstant(0) : oldlastInternalSsaVar;
        IfThenElseStmt newIfStmt = new IfThenElseStmt(a.original, newCondition, thenStmt, elseStmt, a.genuine, a.isByteCodeReversed, a.generalOblg);
        if ((thenInternalSsaVar == null) && (elseInternalSsaVar == null)) {
            lastInternalSsaVar = oldlastInternalSsaVar;
            return newIfStmt;
        } else if ((thenInternalSsaVar != null) && (elseInternalSsaVar != null)) {
            InternalJRSsaVar newSsaVar = new InternalJRSsaVar();
            lastInternalSsaVar = newSsaVar;
            AssignmentStmt assignmentStmt = new AssignmentStmt(newSsaVar, new GammaVarExpr(thenCond, thenInternalSsaVar, elseInternalSsaVar));
            return new CompositionStmt(newIfStmt, assignmentStmt);
        } else if (thenInternalSsaVar != null) {
            InternalJRSsaVar newSsaVar = new InternalJRSsaVar();
            lastInternalSsaVar = newSsaVar;
            AssignmentStmt assignmentStmt = new AssignmentStmt(newSsaVar, new GammaVarExpr(thenCond, thenInternalSsaVar, oldlastInternalSsaVarValue));
            return new CompositionStmt(newIfStmt, assignmentStmt);
        } else { //if (elseInternalSsaVar != null)
            InternalJRSsaVar newSsaVar = new InternalJRSsaVar();
            lastInternalSsaVar = newSsaVar;
            AssignmentStmt assignmentStmt = new AssignmentStmt(newSsaVar, new GammaVarExpr(elseCond, elseInternalSsaVar, oldlastInternalSsaVarValue));
            return new CompositionStmt(newIfStmt, assignmentStmt);
        }
    }

    @Override
    public Stmt visit(CompositionStmt a) {
        return new CompositionStmt(a.s1.accept(this), a.s2.accept(this));

    }

    /* private Stmt createJRVarAssignStmts() {
         assert (internalJRVarPCMap.size() > 0) : "size of the internalJRVarPCMap cannot be zero. Assumption Violated. Failing.";

         ArrayList<InternalJRVar> keys = new ArrayList<>(internalJRVarPCMap.keySet());
         Stmt stmt = createJRVarAssignStmt(keys.get(0));

         if (keys.size() == 1)
             return stmt;

         for (int i = 1; i < keys.size(); i++)
             stmt = new CompositionStmt(stmt, createJRVarAssignStmt(keys.get(i)));

         return stmt;
     }

     private Stmt createJRVarAssignStmt(InternalJRVar internalJRVar) {
         List<Expression> condList = internalJRVarPCMap.get(internalJRVar);
         assert condList != null && condList.size() > 0 : "internalJRVar not found in map or is empty. Assumptions violated. Failing.";

         Expression gammaCond = condList.get(0);

         for (int i = 1; i < condList.size(); i++)
             gammaCond = new Operation(Operation.Operator.OR, gammaCond, condList.get(i));


         return new AssignmentStmt(internalJRVar, new GammaVarExpr(gammaCond, new IntConstant(1), new IntConstant(0)));
     }

     public Stmt composeInternalJRVarGamma(Stmt stmt) {
         return new CompositionStmt(stmt, createJRVarAssignStmts());
     }
 */
    public static StaticRegion execute(StaticRegion region) throws StaticRegionException {
        CreateInternalJRSsaVars conditionReturns = new CreateInternalJRSsaVars(new ExprMapVisitor());
        Stmt stmt = region.staticStmt.accept(conditionReturns);

        return new StaticRegion(stmt, region, null);

    }
}
