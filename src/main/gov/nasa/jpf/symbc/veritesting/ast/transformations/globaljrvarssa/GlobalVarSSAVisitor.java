package gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa;

import com.ibm.wala.ssa.SSAGetInstruction;
import com.ibm.wala.ssa.SSAThrowInstruction;
import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.SPFCases.SPFCaseList;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.FixedPointAstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.StmtPrintVisitor;
import gov.nasa.jpf.vm.ThreadInfo;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntVariable;
import za.ac.sun.cs.green.expr.RealVariable;

import java.util.Map;

import static gov.nasa.jpf.symbc.VeritestingListener.verboseVeritesting;
import static gov.nasa.jpf.symbc.veritesting.StaticRegionException.ExceptionPhase.INSTANTIATION;
import static gov.nasa.jpf.symbc.veritesting.StaticRegionException.throwException;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.*;

/*

 */


public class GlobalVarSSAVisitor extends FixedPointAstMapVisitor {
    private static int fieldExceptionNumber = 42424242;
    private DynamicRegion dynRegion;
    public GlobalVarPsmMap gpsm;
    private ThreadInfo ti;
    static final int FIELD_SUBSCRIPT_BASE = 0;
    private GlobalVarGsmMap ggsm;
    private boolean somethingChanged;

    public GlobalVarSSAVisitor(ThreadInfo ti, DynamicRegion dynRegion) {
        super(new ExprMapVisitor());
        this.dynRegion = dynRegion;
        this.gpsm = dynRegion.gpsm != null ? dynRegion.gpsm : new GlobalVarPsmMap();
        this.ti = ti;
        this.ggsm = new GlobalVarGsmMap();
        this.somethingChanged = false;
    }

    @Override
    public boolean getChange() {
        return somethingChanged;
    }

    private void populateException(IllegalArgumentException e) {
        this.firstException = e;
    }

    public Stmt bad(Object obj) {
        String name = obj.getClass().getCanonicalName();
//        throwException(new IllegalArgumentException("Unsupported class: " + name +
//                " value: " + obj.toString() + " seen in FieldSSAVisitor"), INSTANTIATION);
        firstException = new IllegalArgumentException("Unsupported class: " + name + " value: " + obj.toString() + " seen in FieldSSAVisitor");
        return (Stmt) obj;
    }

    /*public static DynamicRegion execute(ThreadInfo ti, DynamicRegion dynRegion) {
        FieldSSAVisitor visitor = new FieldSSAVisitor(ti, dynRegion);
        Stmt stmt = dynRegion.dynStmt.accept(visitor);
        if (visitor.exception != null) throwException(visitor.exception, INSTANTIATION);
        dynRegion.psm = visitor.psm;
        return new DynamicRegion(dynRegion, stmt, new SPFCaseList(), null, null);
    }*/

    @Override
    public Stmt visit(ReturnInstruction ret) {
        bad(ret);
        return ret;
    }


    @Override
    public Stmt visit(StoreGlobalInstruction storeGlobalIns) {
        GlobalJRVar globalJRVar;
        try {
            if (storeGlobalIns.lhs instanceof ObligationVar)
                globalJRVar = storeGlobalIns.lhs;
            else
                globalJRVar = GlobalJRVar.makePutFieldRef(storeGlobalIns);
        } catch (StaticRegionException e) {
            return getThrowInstruction();
        }
        GlobalJRVarSSAExpr globalJRVarSSAExpr = new GlobalJRVarSSAExpr(globalJRVar, createSubscript(globalJRVar));
        AssignmentStmt assignStmt = new AssignmentStmt(globalJRVarSSAExpr, storeGlobalIns.rhs);
        String type = null;
        if (WalaVarExpr.class.isInstance(storeGlobalIns.rhs)) {
            if (dynRegion.varTypeTable.lookup(storeGlobalIns.rhs) != null) {
                type = (String) dynRegion.varTypeTable.lookup(storeGlobalIns.rhs);
            } else {
                assert false : "var not found. failing";
            }
        } else if (isConstant(storeGlobalIns.rhs)) {
            type = getConstantType(storeGlobalIns.rhs);
        } else if (IntVariable.class.isInstance(storeGlobalIns.rhs)) {
            type = "int";
        } else if (RealVariable.class.isInstance(storeGlobalIns.rhs)) {
            type = "float";
        }
        if (type != null) dynRegion.fieldRefTypeTable.add(globalJRVarSSAExpr.clone(), type);
        somethingChanged = true;
        return assignStmt;

    }

    public static Stmt getThrowInstruction() {
        return new ThrowInstruction(new SSAThrowInstruction(-1, nextFieldExceptionNumber()) {
        });
    }

    public static int nextFieldExceptionNumber() {
        ++fieldExceptionNumber;
        return fieldExceptionNumber;
    }


    private SubscriptPair createSubscript(GlobalJRVar globalJRVar) {
        SubscriptPair subscript = gpsm.lookup(globalJRVar);
        if (subscript == null) {
            subscript = new SubscriptPair(FIELD_SUBSCRIPT_BASE + 1, ggsm.createSubscript(globalJRVar));
            gpsm.add(globalJRVar, subscript);
        } else {
            subscript = new SubscriptPair(subscript.pathSubscript + 1, ggsm.createSubscript(globalJRVar));
            gpsm.updateValue(globalJRVar, subscript);
        }
        return subscript;
    }


    @Override
    public Stmt visit(IfThenElseStmt stmt) {
        GlobalVarPsmMap oldMap = gpsm.clone();
        Stmt newThen = stmt.thenStmt.accept(this);
        GlobalVarPsmMap thenMap = gpsm.clone();
        gpsm = oldMap.clone();
        Stmt newElse = stmt.elseStmt.accept(this);
        GlobalVarPsmMap elseMap = gpsm.clone();
        gpsm = oldMap.clone();
        Stmt gammaStmt = mergePSM(stmt.condition, thenMap, elseMap);
        if (gammaStmt != null) {
            somethingChanged = true;
            return new CompositionStmt(new IfThenElseStmt(stmt.original, stmt.condition, newThen, newElse, stmt.genuine, stmt.isByteCodeReversed, stmt.generalOblg), gammaStmt);
        } else
            return new IfThenElseStmt(stmt.original, stmt.condition, newThen, newElse, stmt.genuine, stmt.isByteCodeReversed, stmt.generalOblg);
    }

    private Stmt mergePSM(Expression condition, GlobalVarPsmMap thenMap, GlobalVarPsmMap elseMap) {
        Stmt compStmt = null;
        for (Map.Entry<GlobalJRVar, SubscriptPair> entry : thenMap.table.entrySet()) {
            GlobalJRVar thenGlobalVar = entry.getKey();
            SubscriptPair thenSubscript = entry.getValue();
            SubscriptPair elseSubscript = elseMap.lookup(thenGlobalVar);
            if (elseSubscript != null) {
                if (!thenSubscript.equals(elseSubscript))
                    compStmt = compose(compStmt, createGammaStmt(condition, thenGlobalVar, thenSubscript, elseMap.lookup(thenGlobalVar)), false);
                elseMap.remove(thenGlobalVar);
            } else {
                compStmt = compose(compStmt, createGammaStmt(condition, thenGlobalVar, thenSubscript, new SubscriptPair(FIELD_SUBSCRIPT_BASE, ggsm.createSubscript(thenGlobalVar))), false);
            }
        }

        for (Map.Entry<GlobalJRVar, SubscriptPair> entry : elseMap.table.entrySet()) {
            GlobalJRVar elseGlobalVar = entry.getKey();
            SubscriptPair elseSubscript = entry.getValue();
            if (thenMap.lookup(elseGlobalVar) != null) {
                throwException(new IllegalArgumentException("invariant failure: something in elseMap should not be in thenMap at this point in FieldSSAVisitor"), INSTANTIATION);
            } else {
                compStmt = compose(compStmt, createGammaStmt(condition, elseGlobalVar, new SubscriptPair(FIELD_SUBSCRIPT_BASE, ggsm.createSubscript(elseGlobalVar)), elseSubscript), false);
            }
        }

        return compStmt;
    }


    private Stmt createGammaStmt(Expression condition, GlobalJRVar globalJRVar, SubscriptPair thenSubscript, SubscriptPair elseSubscript) {
        if (thenSubscript.pathSubscript == FIELD_SUBSCRIPT_BASE && elseSubscript.pathSubscript == FIELD_SUBSCRIPT_BASE) {
            throwException(new IllegalArgumentException("invariant failure: ran into a gamma between subscripts that are both base subscripts in FieldSSAVisitor"), INSTANTIATION);
        }

        GlobalJRVarSSAExpr globalJRVarSSAExpr = new GlobalJRVarSSAExpr(globalJRVar, createSubscript(globalJRVar));

        Expression thenExpr = thenSubscript.pathSubscript != FIELD_SUBSCRIPT_BASE ? new GlobalJRVarSSAExpr(globalJRVar, thenSubscript) : GlobalJRVar.defaultValue;
        Expression elseExpr = elseSubscript.pathSubscript != FIELD_SUBSCRIPT_BASE ? new GlobalJRVarSSAExpr(globalJRVar, elseSubscript) : GlobalJRVar.defaultValue;
        return new AssignmentStmt(globalJRVarSSAExpr, new GammaVarExpr(condition, thenExpr, elseExpr));
    }


    public DynamicRegion execute() {
        Stmt fieldStmt = dynRegion.dynStmt.accept(this);

        instantiatedRegion = new DynamicRegion(dynRegion, fieldStmt, new SPFCaseList(), null, null, dynRegion.earlyReturnResult);
        instantiatedRegion.gpsm = this.gpsm;

        if (verboseVeritesting)
            System.out.println(StmtPrintVisitor.print(instantiatedRegion.dynStmt));

        return instantiatedRegion;
    }
}
