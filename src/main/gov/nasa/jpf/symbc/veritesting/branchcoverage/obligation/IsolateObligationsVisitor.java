package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import gov.nasa.jpf.symbc.numeric.MinMax;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.IntVariable;

import java.util.HashMap;

/**
 * This class isolates branch coverage obligations as assignments to new symbolic variables
 */
public class IsolateObligationsVisitor extends AstMapVisitor {

    private static int obligationUniqueness = 0;

    // this map holds the conditionExpr -> new symbolic variables. The new Symbolic variables are treated later as an obligation variant.
    HashMap<Expression, Expression> newSymToExprMap = new HashMap<>();

    public IsolateObligationsVisitor(ExprVisitor<Expression> exprVisitor) {
        super(exprVisitor);
    }


    @Override
    public Stmt visit(IfThenElseStmt a) {
//        ObligationVariable oblgVar = new ObligationVariable("");

        String varId = "o$" + obligationUniqueness++;
        IntVariable oblgVar = new IntVariable(varId, (int) MinMax.getVarMinInt(varId), (int) MinMax.getVarMaxInt(varId));

        AssignmentStmt oblgAssign = new AssignmentStmt(oblgVar, new GammaVarExpr(a.condition, new IntConstant(1), new IntConstant((0))));

        newSymToExprMap.put(a.condition, oblgVar);

        Stmt newThen = a.thenStmt.accept(this);
        Stmt newElse = a.elseStmt.accept(this);

        newSymToExprMap.remove(oblgVar);

        return new CompositionStmt(oblgAssign, new IfThenElseStmt(a.original, eva.accept(a.condition), newThen,
                newElse));
    }


    /**
     * This consists of two main visitors, the first one isolates the conditions of the obligations. The second one
     * creates the obligation expression that we need to ask the solver for.
     *
     * @param dynRegion
     * @return
     */
    public static DynamicRegion execute(DynamicRegion dynRegion) {
        IsolateObligationsVisitor isolateObligationsVisitor = new IsolateObligationsVisitor(new ExprMapVisitor());
        Stmt dynStmt = dynRegion.dynStmt.accept(isolateObligationsVisitor);
        CollectObligationsVisitor collectObligationsVisitor = new CollectObligationsVisitor(new ExprMapVisitor(), dynRegion.ir);
        dynStmt.accept(collectObligationsVisitor);
        VeriObligationMgr.addSymbolicOblgMap(collectObligationsVisitor.oblgToExprsMap);

        return new DynamicRegion(dynRegion,
                dynStmt,
                dynRegion.spfCaseList, dynRegion.regionSummary, dynRegion.spfPredicateSummary, dynRegion.earlyReturnResult);

    }
}
