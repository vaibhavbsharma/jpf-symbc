package gov.nasa.jpf.symbc.veritesting.ast.transformations.constprop;

import com.ibm.wala.types.TypeName;
import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.StatisticManager;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicTable;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitorAdapter;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.FixedPointAstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.StmtPrintVisitor;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.Variable;

import java.util.Iterator;
import java.util.Map;

import static gov.nasa.jpf.symbc.VeritestingListener.verboseVeritesting;
import static gov.nasa.jpf.symbc.veritesting.StaticRegionException.ExceptionPhase.INSTANTIATION;
import static gov.nasa.jpf.symbc.veritesting.StaticRegionException.throwException;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ClassUtils.getType;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.*;

public class SimplifyStmtVisitor extends FixedPointAstMapVisitor {
    public ExprVisitorAdapter<Expression> eva;
    public DynamicTable<Expression> constantsTable;
    private DynamicRegion dynRegion;
    private boolean somethingChanged;

    public SimplifyStmtVisitor(DynamicRegion dynRegion, DynamicTable<Expression> constantsTable) {
        super(new SimplifyRangerExprVisitor(constantsTable));
        eva = super.eva;
        this.constantsTable = constantsTable;
        this.dynRegion = dynRegion;
        this.somethingChanged = false;
    }

    public IllegalArgumentException getExprException() {
        IllegalArgumentException ret = null;
        if (((SimplifyRangerExprVisitor) exprVisitor).exception != null) {
            ret = (((SimplifyRangerExprVisitor) exprVisitor).exception);
        }
        return ret;
    }

    public boolean getChange() {
        return somethingChanged || ((SimplifyRangerExprVisitor) exprVisitor).somethingChanged;
    }

    @Override
    public Stmt visit(AssignmentStmt a) {
        Expression rhs = eva.accept(a.rhs);
        if (isConstant(rhs) || isVariable(rhs)) {
            constantsTable.add((Variable) a.lhs, rhs);
            if (isVariable(rhs)) {
                String type = getGreenVariableType(rhs);
                if (type == null) type = (String) dynRegion.varTypeTable.lookup(rhs);
                if (type == null) type = dynRegion.fieldRefTypeTable.lookup(rhs);
                if (type != null) {
                    if (a.lhs instanceof WalaVarExpr)
                        dynRegion.varTypeTable.add(a.lhs, type);
                    else if (a.lhs instanceof FieldRefVarExpr || a.lhs instanceof ArrayRefVarExpr)
                        dynRegion.fieldRefTypeTable.add((CloneableVariable) a.lhs, type);
                }
            }
            this.somethingChanged = true;
            /* if are simplifying that assignment, then if it is referring to a GlobalJRVarSSAExpr,
            then we need to remove it from the gpsm as well, this is because we create the disjunction of branch coverage based on the
            last variables in the gpsm, yet if they do not correspond to any variable assignment (because they were removed by simplification) then we
            need to remove them as well from the disjunction and therefore from the query.
            */
            if(a.lhs instanceof GlobalJRVarSSAExpr)
                dynRegion.gpsm.remove((GlobalJRVarSSAExpr) a.lhs);
            return SkipStmt.skip;
        }
        return new AssignmentStmt(a.lhs, rhs);
    }

    @Override
    public Stmt visit(IfThenElseStmt c) {
        Expression cond = eva.accept(c.condition);
        ExprUtil.SatResult satResult;
        satResult = isSatGreenExpression(cond);
        if (satResult == ExprUtil.SatResult.FALSE) {
            StatisticManager.ifRemovedCount++;
            this.somethingChanged = true;
            return c.elseStmt.accept(this);
        } else if (satResult == ExprUtil.SatResult.TRUE) {
            this.somethingChanged = true;
            StatisticManager.ifRemovedCount++;
            return c.thenStmt.accept(this);
        } else {
            return new IfThenElseStmt(c.original, cond, c.thenStmt.accept(this), c.elseStmt.accept(this), c.genuine, c.isByteCodeReversed, c.generalOblg);
        }
    }

    @Override
    public Stmt visit(CheckCastInstruction c) {
        //TODO: Check if this cast can be done correctly. I (Vaibhav) am assuming it must be correct to get the motivating example to work
        if (c.declaredResultTypes.length != 1)
            throwException(new IllegalArgumentException("Cannot handle checkcast with more than 1 declared result type"), INSTANTIATION);
        TypeName typeName = c.declaredResultTypes[0].getName();
        String type = getType(typeName);
        dynRegion.varTypeTable.add(c.result, type);
        dynRegion.varTypeTable.add(c.val, type);
        Expression rhs = c.val;
        Expression lhs = c.result;
        rhs = eva.accept(rhs);
        if (isConstant(rhs) || isVariable(rhs)) {
            constantsTable.add((Variable) lhs, rhs);
        }
        return new AssignmentStmt(lhs, rhs);
    }

    public static DynamicTable<Expression> makeConstantsTableUnique(DynamicTable<Expression> constantsTable, int uniqueNum) throws StaticRegionException {
        // Alpha-renaming on the constants table
        Iterator<Map.Entry<Variable, Expression>> itr = constantsTable.table.entrySet().iterator();
        DynamicTable<Expression> newConstantsTable = new DynamicTable<>("Constants Table", "Expression", "Constant Value");
        while (itr.hasNext()) {
            Map.Entry<Variable, Expression> entry = itr.next();
            Variable newKey;
            Expression newValue;
            if (entry.getKey() instanceof FieldRefVarExpr) {
                FieldRefVarExpr newExpr = ((FieldRefVarExpr) entry.getKey()).clone();
                newExpr = newExpr.makeUnique(uniqueNum);
                newKey = newExpr;
            } else if (entry.getKey() instanceof ArrayRefVarExpr) {
                ArrayRefVarExpr newExpr = ((ArrayRefVarExpr) entry.getKey()).clone();
                newExpr = newExpr.makeUnique(uniqueNum);
                newKey = newExpr;
            } else if (entry.getKey() instanceof AstVarExpr) {
                assert ((AstVarExpr) entry.getKey()).getUniqueNum() != -1;
                newKey = entry.getKey(); // AstVarExpr are assumed to be alpha-renamed by this point
            } else if (entry.getKey() instanceof WalaVarExpr) {
                assert ((WalaVarExpr) entry.getKey()).getUniqueNum() != -1;
                newKey = entry.getKey(); // WalaVarExpr are assumed to be alpha-renamed by this point
            } else newKey = entry.getKey();
            if (entry.getValue() instanceof FieldRefVarExpr) {
                FieldRefVarExpr newExpr = ((FieldRefVarExpr) entry.getValue()).clone();
                newExpr = newExpr.makeUnique(uniqueNum);
                newValue = newExpr;
            } else if (entry.getValue() instanceof ArrayRefVarExpr) {
                ArrayRefVarExpr newExpr = ((ArrayRefVarExpr) entry.getValue()).clone();
                newExpr = newExpr.makeUnique(uniqueNum);
                newValue = newExpr;
            } else if (entry.getValue() instanceof AstVarExpr) {
                assert ((AstVarExpr) entry.getValue()).getUniqueNum() != -1;
                newValue = entry.getValue(); // AstVarExpr are assumed to be alpha-renamed by this point
            }else if (entry.getValue() instanceof WalaVarExpr) {
                assert ((WalaVarExpr) entry.getValue()).getUniqueNum() != -1;
                newValue = entry.getValue(); // WalaVarExpr are assumed to be alpha-renamed by this point
            } else newValue = entry.getValue();
            newConstantsTable.add(newKey, newValue);
        }
        return newConstantsTable;
    }

    public static SimplifyStmtVisitor create(DynamicRegion dynRegion) {
        DynamicTable<Expression> constantsTable = new DynamicTable<>("Constants Table", "Expression", "Constant Value");
        SimplifyStmtVisitor simplifyVisitor = new SimplifyStmtVisitor(dynRegion, constantsTable);
        return simplifyVisitor;
    }

    public DynamicRegion execute() {
        Stmt simplifiedStmt = dynRegion.dynStmt.accept(this);
        this.instantiatedRegion = new DynamicRegion(dynRegion, simplifiedStmt, dynRegion.spfCaseList, dynRegion.regionSummary,
                dynRegion.spfPredicateSummary, dynRegion.earlyReturnResult);

        if (instantiatedRegion.constantsTable == null)
            instantiatedRegion.constantsTable = this.constantsTable;
        else dynRegion.constantsTable.addAll(this.constantsTable);
//            simplifyArrayOutputs(dynRegion);
        if(verboseVeritesting) {
            System.out.println("\n--------------- AFTER SIMPLIFICATION ---------------\n");
            System.out.println(StmtPrintVisitor.print(instantiatedRegion.dynStmt));
        }
        Iterator<Map.Entry<Variable, Expression>> itr = instantiatedRegion.constantsTable.table.entrySet().iterator();
        if(verboseVeritesting){
            System.out.println("Constants Table:");
            while (itr.hasNext()) {
                Map.Entry<Variable, Expression> entry = itr.next();
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }

        return instantiatedRegion;
    }
}