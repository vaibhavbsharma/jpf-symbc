package gov.nasa.jpf.symbc.veritesting.branchcoverage;

import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa.GlobalVarPsmMap;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa.GlobalVarSSAVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.Expression;

import java.util.List;

public class ClearSPFCasesOblgVisitor extends AstMapVisitor {


    private final List<Obligation> spfcasesOblgList;

    public ClearSPFCasesOblgVisitor(ExprVisitor<Expression> exprVisitor, List<Obligation> spfcasesOblgList) {
        super(exprVisitor);
        this.spfcasesOblgList = spfcasesOblgList;
    }

    @Override
    public Stmt visit(AssignmentStmt a) {
        if (a.lhs instanceof GlobalJRVarSSAExpr && ((GlobalJRVarSSAExpr) a.lhs).globalJRVar instanceof ObligationVar) { // it is really an instance of an obligation variable
            for (Obligation oblg : spfcasesOblgList)
                if (((ObligationVar) ((GlobalJRVarSSAExpr) a.lhs).globalJRVar).oblg.equals(oblg)) //ignore the statement if the left hand side is based on a global variable of the same base obligation.
                    return SkipStmt.skip;
        }
        return new AssignmentStmt(eva.accept(a.lhs), eva.accept(a.rhs));
    }

    //used to clear all obligations on the path for spfcases
    public static DynamicRegion execute(DynamicRegion dynRegion, List<Obligation> spfcasesOblgList) {
        ClearSPFCasesOblgVisitor clearSPFCasesOblgVisitor = new ClearSPFCasesOblgVisitor(new ExprMapVisitor(), spfcasesOblgList);
        Stmt dynStmt = dynRegion.dynStmt.accept(clearSPFCasesOblgVisitor);

        GlobalVarPsmMap spfFilteredGpsm = dynRegion.gpsm.clone();

        for (Obligation oblg : spfcasesOblgList) //clearing obligations from GPSM as these are going to be added to VeriSymbolicMap, used to find the disjunction of obligations
            spfFilteredGpsm.remove(new ObligationVar(oblg));


        return new DynamicRegion(dynRegion, dynStmt, spfFilteredGpsm);
    }
}
