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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class isolates branch coverage obligations as assignments to new symbolic variables
 */
public class IsolateObligationsVisitor extends AstMapVisitor {

    private static int obligationUniqueness = 0;

    private ArrayList<Expression> innerPC = new ArrayList<>();

    // this map holds the conditionExpr -> new symbolic variables. The new Symbolic variables are treated later as an obligation variant.
    HashMap<Expression, Expression> newSymToExprMap = new HashMap<>();

    public IsolateObligationsVisitor(ExprVisitor<Expression> exprVisitor) {
        super(exprVisitor);
        ((IsolateObligationsExprVisitor) exprVisitor).newSymToExprMap = newSymToExprMap;
    }

    private Expression conjunctWithPc(int index, Expression condition) {
        //S.H. I believe this is unnecessary.

        if (index == innerPC.size())
            return condition;
        return new Operation(Operation.Operator.AND, innerPC.get(index), conjunctWithPc(++index, condition));

//        return condition;
    }


    @Override
    public Stmt visit(IfThenElseStmt a) {

        //this lines justifies why we flip the condition for associating the obligations coverage in the oblgQueue when we do CollectObligationVisitor
//        assert negationOf(((Operation) a.condition).getOperator(), (IConditionalBranchInstruction.Operator) a.original.getOperator()) : "The assumption that the condition of the JR IR is the negation of the bytecode branch instruction is violated. Something went wrong. Failing.";

        String varId = "o$" + obligationUniqueness++;
        IntVariable oblgVar = new IntVariable(varId, (int) MinMax.getVarMinInt(varId), (int) MinMax.getVarMaxInt(varId));

        AssignmentStmt oblgAssign;

        if (innerPC.size() == 0) //make sure that the Gamma is copying the inner path inside the region as well as the obligation condition, with the else side negating ONLY the
            //current condition of the if-statement.
            oblgAssign = new AssignmentStmt(oblgVar, new GammaVarExpr(a.condition, new IntConstant(1), new IntConstant(0)));
        else //this case has three way choices depending on the satisfiability of the innerPC and the current condition of the if-statement. Option with const 2, is never used for checking the obligation.
            oblgAssign = new AssignmentStmt(oblgVar, new GammaVarExpr(conjunctWithPc(0, a.condition), new IntConstant(1),
                    new GammaVarExpr(conjunctWithPc(0, new Operation(Operation.Operator.NOT, a.condition)), new IntConstant(0), new IntConstant(2))));

        Operation newCondition = new Operation(Operation.Operator.EQ, oblgVar, new IntConstant(1));
        newSymToExprMap.put(a.condition, newCondition);

        innerPC.add(a.condition);
        Stmt newThen = a.thenStmt.accept(this);
        innerPC.remove(innerPC.size() - 1);

        Expression negCond = new Operation(Operation.Operator.NOT, a.condition);
        innerPC.add(negCond);
        Stmt newElse = a.elseStmt.accept(this);
        innerPC.remove(innerPC.size() - 1);

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
