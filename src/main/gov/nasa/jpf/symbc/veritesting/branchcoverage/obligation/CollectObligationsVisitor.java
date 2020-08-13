package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import com.ibm.wala.shrikeBT.IConditionalBranchInstruction;
import com.ibm.wala.ssa.IR;
import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

/**
 * This class collects the obligations and their corresponding expressions.
 */
public class CollectObligationsVisitor extends AstMapVisitor {


    public static HashMap<Obligation, ArrayList<Expression>> oblgToExprsMap;
    private ArrayList<Expression> innerPC = new ArrayList<>();
    private static IR ir;

    public CollectObligationsVisitor(ExprVisitor<Expression> exprVisitor, IR ir) {
        super(exprVisitor);
        CollectObligationsVisitor.ir = ir;
        oblgToExprsMap = new HashMap<>();
    }


    @Override
    public Stmt visit(IfThenElseStmt a) {

        if(!a.genuine) // is it a if-stmt related to JR transformation, if so just return.
            return a;

        innerPC.add(a.condition);
        Obligation thenOblg = VeriObligationMgr.createOblg(a.original, ObligationSide.THEN, ir);
        a.thenStmt.accept(this);
        innerPC.remove(innerPC.size() - 1);

        //TODO: only do that for genuine conditions.
        assert (a.condition instanceof Operation && ((Operation) a.condition).getOperator() == Operation.Operator.EQ) : " The assumption that the if condition is of an equality form is not holding. Failing.";
        assert (((Operation) a.condition).getOperand(1) instanceof IntConstant && ((IntConstant) ((Operation) a.condition).getOperand(1)).getValue() == 1) :
                "The assumption that the condition of the if statement is of the form some obligation = 1 is not holding. Failing.";

        Expression negCond = new Operation(Operation.Operator.EQ, ((Operation) a.condition).getOperand(0), new IntConstant(0));
        innerPC.add(negCond);
        a.elseStmt.accept(this);
        Obligation elseOblg = VeriObligationMgr.createOblg(a.original, ObligationSide.ELSE, ir);
        innerPC.remove(innerPC.size() - 1);

        //check if we need to flip the conditions accompanying the obligations since the obligations are mirroring the bytecode, whereas the conditions might be  matching the source code.
        // and since operations of conditions are negated by the compilers, thus negating their form in the source, we consider obligations are sat by looking at the
        // negation of the source condition.

        if (a.isByteCodeReversed) {
            putOblgExprInMap(thenOblg, negCond);
            putOblgExprInMap(elseOblg, a.condition);
        } else{
            putOblgExprInMap(thenOblg, a.condition);
            putOblgExprInMap(elseOblg, negCond);
        }

        return a;
    }

    /**
     * The map has conjunction expression for obligation to reflect the path done to that obligation.
     * And since we can hit the same region multiple times, this obligation can be covered among any of the paths collected
     *
     * @param oblg
     * @param condition
     */
    private void putOblgExprInMap(Obligation oblg, Expression condition) {
        ArrayList<Expression> exprsList = oblgToExprsMap.get(oblg);
        if (exprsList == null)
//            oblgToExprsMap.put(oblg, new ArrayList<>(Arrays.asList(condition)));
            oblgToExprsMap.put(oblg, new ArrayList<>(Arrays.asList(conjunctWithPc(0, condition))));
        else {
            exprsList.add(conjunctWithPc(0, condition));
        }
    }

    private Expression conjunctWithPc(int index, Expression condition) {
        //S.H. I believe this IS Necessary.

        if (index == innerPC.size())
            return condition;
        return new Operation(Operation.Operator.AND, innerPC.get(index), conjunctWithPc(++index, condition));

//        return condition;
    }


    public static DynamicRegion execute(DynamicRegion dynRegion) {
        CollectObligationsVisitor isolateObligationsVisitor = new CollectObligationsVisitor(new ExprMapVisitor(), dynRegion.ir);
        Stmt dynStmt = dynRegion.dynStmt.accept(isolateObligationsVisitor);


        return new DynamicRegion(dynRegion, dynStmt, dynRegion.spfCaseList, dynRegion.regionSummary, dynRegion.spfPredicateSummary, dynRegion.earlyReturnResult);

    }
}
