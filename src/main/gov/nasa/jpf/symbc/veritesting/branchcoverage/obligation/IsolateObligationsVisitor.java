package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.shrikeBT.IConditionalBranchInstruction;
import gov.nasa.jpf.symbc.numeric.MinMax;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.PrettyPrintVisitor;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.IntVariable;
import za.ac.sun.cs.green.expr.Operation;

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
        ((IsolateObligationsExprVisitor) exprVisitor).newSymToExprMap = newSymToExprMap;
    }


    @Override
    public Stmt visit(IfThenElseStmt a) {

        //this lines justifies why we flip the condition for associating the obligations coverage in the oblgQueue when we do CollectObligationVisitor
        assert negationOf(((Operation) a.condition).getOperator(), (IConditionalBranchInstruction.Operator) a.original.getOperator()) : "The assumption that the condition of the JR IR is the negation of the bytecode branch instruction is violated. Something went wrong. Failing.";

        String varId = "o$" + obligationUniqueness++;
        IntVariable oblgVar = new IntVariable(varId, (int) MinMax.getVarMinInt(varId), (int) MinMax.getVarMaxInt(varId));

        AssignmentStmt oblgAssign = new AssignmentStmt(oblgVar, new GammaVarExpr(a.condition, new IntConstant(1), new IntConstant((0))));

        Operation newCondition = new Operation(Operation.Operator.EQ, oblgVar, new IntConstant(1));
        newSymToExprMap.put(a.condition, newCondition);

        Stmt newThen = a.thenStmt.accept(this);
        Stmt newElse = a.elseStmt.accept(this);

        newSymToExprMap.remove(oblgVar);

        return new CompositionStmt(oblgAssign, new IfThenElseStmt(a.original, eva.accept(a.condition), newThen,
                newElse));
    }


    // the assumption is that ranger operation is always the negation of the corresponding wala operation representing the bytecode instructionn.
    private boolean negationOf(Operation.Operator rangerOp, IConditionalBranchInstruction.Operator walaOp) {
        switch (walaOp) {
            case EQ:
                return rangerOp == Operation.Operator.NE;
            case NE:
                return rangerOp == Operation.Operator.EQ;
            case LT:
                return rangerOp == Operation.Operator.GE;
            case GE:
                return rangerOp == Operation.Operator.LT;
            case GT:
                return rangerOp == Operation.Operator.LE;
            case LE:
                return rangerOp == Operation.Operator.GT;
        }
        assert false : "this should be unreachable code. Failing.";

        return false;
    }


    /**
     * This consists of two main visitors, the first one isolates the conditions of the obligations. The second one
     * creates the obligation expression that we need to ask the solver for.
     *
     * @param dynRegion
     * @return
     */
    public static DynamicRegion execute(DynamicRegion dynRegion) {
        IsolateObligationsVisitor isolateObligationsVisitor = new IsolateObligationsVisitor(new IsolateObligationsExprVisitor());
//        IsolateObligationsVisitor isolateObligationsVisitor = new IsolateObligationsVisitor(new ExprMapVisitor());
        System.out.println("\n---------- Stmt before Isolation: " +
                "\n" + PrettyPrintVisitor.print(dynRegion.dynStmt) + "\n");

        Stmt dynStmt = dynRegion.dynStmt.accept(isolateObligationsVisitor);

        System.out.println("\n---------- Stmt After Isolation: " +
                "\n" + PrettyPrintVisitor.print(dynStmt) + "\n");

        CollectObligationsVisitor collectObligationsVisitor = new CollectObligationsVisitor(new ExprMapVisitor(), dynRegion.ir);
        dynStmt.accept(collectObligationsVisitor);
//        VeriObligationMgr.addSymbolicOblgMap(collectObligationsVisitor.oblgToExprsMap);
        return new DynamicRegion(dynRegion,
                dynStmt,
                dynRegion.spfCaseList, dynRegion.regionSummary, dynRegion.spfPredicateSummary, dynRegion.earlyReturnResult);

    }
}
