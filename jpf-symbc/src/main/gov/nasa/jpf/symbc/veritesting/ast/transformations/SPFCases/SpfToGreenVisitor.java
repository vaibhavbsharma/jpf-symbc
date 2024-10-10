package gov.nasa.jpf.symbc.veritesting.ast.transformations.SPFCases;

import gov.nasa.jpf.symbc.VeritestingListener;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.AstToGreen.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.removeEarlyReturns.RemoveEarlyReturns;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.*;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.Operation;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.removeEarlyReturns.RemoveEarlyReturns.ReturnResult;
import za.ac.sun.cs.green.expr.Variable;

import java.util.LinkedHashSet;

import static gov.nasa.jpf.symbc.VeritestingListener.verboseVeritesting;
import static gov.nasa.jpf.symbc.veritesting.StaticRegionException.ExceptionPhase.INSTANTIATION;
import static gov.nasa.jpf.symbc.veritesting.StaticRegionException.throwException;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.isConstant;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.isVariable;


/*
    Preconditions:
        - Only statements are assignment statements and gamma statements

        - All IfThenElse expressions are in "normal form"
            - No IfThenElse/Gamma expressions embedded within expressions other than
              IfThenElse expressions
                e.g.: (if x then y else z) + (if a then b else c)
            - No IfThenElse expressions as conditions of other IfThenElse expressions:
                e.g.: (if (if x then y else z) then a else b)
     */

/**
 * Main visitor that visits all statements and translate them to the appropriate green expression. At the point the expected statements are, assignments, composition and skip.
 */

public class SpfToGreenVisitor implements AstVisitor<Expression> {

    ExprVisitorAdapter<Expression> eva;
    AstToGreenExprVisitor exprVisitor;


    public SpfToGreenVisitor() {
        exprVisitor = new AstToGreenExprVisitor();
        eva = new ExprVisitorAdapter<>(exprVisitor);
    }

    public Expression bad(Object obj) {
        String name = obj.getClass().getCanonicalName();
        throwException(new IllegalArgumentException("Unsupported class: " + name +
                " value: " + obj.toString() + " seen in AstToGreenVisitor"), INSTANTIATION);
        return null;
    }


    public Expression assignStmt(AssignmentStmt stmt) {
        return bad(stmt);
    }

    /**
     * Transform a composition statement into a conjunction in green.
     *
     * @param stmt The composition statement to be translated.
     * @return A green expression that represents the compsition statement.
     */
    public Expression compositionStmt(CompositionStmt stmt) {
        return bad(stmt);
    }

    public Expression transform(Stmt stmt) {
        return bad(stmt);
    }

    @Override
    public Expression visit(SkipStmt a) {
        return bad(a);
    }

    @Override
    public Expression visit(AssignmentStmt a) {
        return bad(a);
    }

    @Override
    public Expression visit(CompositionStmt a) {
        return bad(a);
    }

    @Override
    public Expression visit(IfThenElseStmt a) {
        return bad(a);
    }

    @Override
    public Expression visit(SPFCaseStmt c) {
        return eva.accept(c.spfCondition);
    }

    @Override
    public Expression visit(ArrayLoadInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(ArrayStoreInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(SwitchInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(ReturnInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(GetInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(PutInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(NewInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(InvokeInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(ArrayLengthInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(ThrowInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(CheckCastInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(InstanceOfInstruction c) {
        return bad(c);
    }

    @Override
    public Expression visit(PhiInstruction c) {
        return bad(c);
    }

    public DynamicRegion execute(DynamicRegion dynRegion) {

        LinkedHashSet<SPFCaseStmt> greenList = new LinkedHashSet<>();
        for (SPFCaseStmt spfCaseStmt : dynRegion.spfCaseList.casesList) {
            greenList.add(noVars(spfCaseStmt, dynRegion));
        }

        SPFCaseList greenSPFCaseList = new SPFCaseList(greenList);
        Expression spfPredicateSummary = toGreenSinglePredicate(greenSPFCaseList);
        ReturnResult newReturnResult;
        Expression newCond;
        Expression newAssign;
        if (dynRegion.earlyReturnResult.hasER()) {
            boolean isConstOrCopyReturn = false;
            if (VeritestingListener.simplify && dynRegion.constantsTable != null){
                Expression returnVar = dynRegion.earlyReturnResult.retVar;
                isConstOrCopyReturn = isConstant(dynRegion.constantsTable.lookup((Variable) returnVar))
                        || isVariable(dynRegion.constantsTable.lookup((Variable) returnVar));
            }

            newAssign = earlyReturnToGreen(dynRegion.earlyReturnResult.assign, dynRegion);
            newCond = earlyReturnToGreen(dynRegion.earlyReturnResult.condition, dynRegion);
            ReturnResult oldResult = dynRegion.earlyReturnResult;
            RemoveEarlyReturns o = new RemoveEarlyReturns();
            if(!isConstOrCopyReturn) {
                Expression newRetVar = earlyReturnToGreen(dynRegion.earlyReturnResult.retVar, dynRegion);
                newReturnResult = o.new ReturnResult(oldResult.stmt, newAssign, newCond, oldResult.retPosAndType, newRetVar);
            }
            else {
                // This lets the ReturnResult.retVar remain 'lookup'-able in the dynRegion.constantsTable by letting
                // it remain an AstVarExpr. Otherwise it will be turned into a green variable.
                newReturnResult = o.new ReturnResult(oldResult.stmt, newAssign, newCond, oldResult.retPosAndType, oldResult.retVar);
            }

        } else { //if no early return in the region, assign false to the early return condition.
            newReturnResult = dynRegion.earlyReturnResult;
        }


        if(verboseVeritesting) {
            System.out.println("\n--------------- SPFCases GREEN PREDICATE ---------------");
            System.out.println(StmtPrintVisitor.print(spfPredicateSummary));
        }
        DynamicRegion greenDynRegion = new DynamicRegion(dynRegion,
                dynRegion.dynStmt,
                dynRegion.spfCaseList,
                dynRegion.regionSummary,
                spfPredicateSummary, newReturnResult);

        return greenDynRegion;
    }

    private Expression earlyReturnToGreen(Expression earlyReturnExp, DynamicRegion dynRegion) {

        WalaVarToSPFVarVisitor walaVarVisitor = new WalaVarToSPFVarVisitor(dynRegion.varTypeTable);
        ExprVisitorAdapter eva1 = new ExprVisitorAdapter(walaVarVisitor);
        Expression noWalaVarExp = (Expression) eva1.accept(earlyReturnExp);
        FieldArrayVarToSPFVarVisitor fieldRefVisitor = new FieldArrayVarToSPFVarVisitor(dynRegion.fieldRefTypeTable);
        ExprVisitorAdapter eva2 = new ExprVisitorAdapter(fieldRefVisitor);
        Expression noFieldRefVarExp = (Expression) eva2.accept(noWalaVarExp);
        return noFieldRefVarExp;
    }

    private static Expression toGreenSinglePredicate(SPFCaseList greenSPFCaseList) {
        Expression result = Operation.FALSE;
        for (SPFCaseStmt spfStmt : greenSPFCaseList.casesList) {
            result = new Operation(Operation.Operator.OR, result, spfStmt.spfCondition);
        }
        return result;
    }

    private static SPFCaseStmt noVars(SPFCaseStmt spfCaseStmt, DynamicRegion dynRegion) {
        WalaVarToSPFVarVisitor walaVarVisitor = new WalaVarToSPFVarVisitor(dynRegion.varTypeTable);
        AstMapVisitor astMapVisitor = new AstMapVisitor(walaVarVisitor);
        Stmt noWalaVarStmt = spfCaseStmt.accept(astMapVisitor);
        FieldArrayVarToSPFVarVisitor fieldRefVisitor = new FieldArrayVarToSPFVarVisitor(dynRegion.fieldRefTypeTable);
        astMapVisitor = new AstMapVisitor(fieldRefVisitor);
        Stmt noRangerVarStmt = noWalaVarStmt.accept(astMapVisitor);

        assert (noRangerVarStmt instanceof SPFCaseStmt);
        return (SPFCaseStmt) noRangerVarStmt;
    }
}
