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

public class RemoveInternalJRVar extends AstMapVisitor {

    //carries the list of assignments, all assignments must be setting the variable to 1.
    HashMap<InternalJRVar, List<Expression>> internalJRVarPCMap = new HashMap<>();

    Expression innerPC;

    public RemoveInternalJRVar(ExprVisitor<Expression> exprVisitor) {
        super(exprVisitor);
    }

    @Override
    public Stmt visit(AssignmentStmt a) {
        if (a.lhs instanceof InternalJRVar) {
            assert a.rhs instanceof IntConstant && ((IntConstant) a.rhs).getValue() == 1 : "assignments to InternalJRVars must always assign to true. Assumption violated for assignment stmt. Failing";
            List<Expression> internalVarConds = internalJRVarPCMap.get(a.lhs);
            if (internalVarConds == null) {
                if (innerPC != null)
                    internalJRVarPCMap.put((InternalJRVar) a.lhs, new ArrayList<>(Arrays.asList(new Expression[]{innerPC})));
            } else if (innerPC != null)
                internalVarConds.add(innerPC);
            return SkipStmt.skip;
        }
        return new AssignmentStmt(eva.accept(a.lhs), eva.accept(a.rhs));
    }


    @Override
    public Stmt visit(IfThenElseStmt a) {
        if ((((Operation) a.condition).getOperand(0) instanceof InternalJRVar)) {
            return new IfThenElseStmt(a.original, eva.accept(a.condition), a.thenStmt.accept(this), a.elseStmt.accept(this), a.genuine, a.isByteCodeReversed, a.generalOblg);
        }

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


    private Stmt createJRVarAssignStmts() {
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

    public static StaticRegion execute(StaticRegion region) throws StaticRegionException {
        RemoveInternalJRVar conditionReturns = new RemoveInternalJRVar(new ExprMapVisitor());
        Stmt stmt = region.staticStmt.accept(conditionReturns);

        if (conditionReturns.internalJRVarPCMap.size() > 0)
            stmt = conditionReturns.composeInternalJRVarGamma(stmt);

        return new StaticRegion(stmt, region, null);

    }
}
