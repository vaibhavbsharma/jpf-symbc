package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa.GlobalVarPsmMap;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.Expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectOblgInSide extends AstMapVisitor {


    private final List<Obligation> spfcasesOblgList = new ArrayList<>();

    public CollectOblgInSide(ExprVisitor<Expression> exprVisitor) {
        super(exprVisitor);
    }

    @Override
    public Stmt visit(AssignmentStmt a) {
        if (a.lhs instanceof GlobalJRVarSSAExpr && ((GlobalJRVarSSAExpr) a.lhs).globalJRVar instanceof ObligationVar)  // it is really an instance of an obligation variable
            spfcasesOblgList.add(((ObligationVar) ((GlobalJRVarSSAExpr) a.lhs).globalJRVar).oblg);

        return a;
    }

    public Collection<? extends Obligation> getSpfcasesOblgList() {
        return spfcasesOblgList;
    }
}
