package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import za.ac.sun.cs.green.expr.*;

import java.util.Map;

import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.translateNotExpr;

public class SolutionSubstitutionVisitor extends Visitor {

    public Expression returnExp;
    private Map<String, Object> solution;

    public SolutionSubstitutionVisitor(Map<String, Object> solution) {
        this.solution = solution;
    }

    public void postVisit(IntConstant intConstant) throws VisitorException {
        returnExp = intConstant;
    }

    public void postVisit(IntVariable intVariable) throws VisitorException {
        Object solverValue = solution.get(intVariable.toString());
        assert solverValue instanceof Long;
        returnExp = new IntConstant(((Long) solverValue).intValue());
    }


    @Override
    public void postVisit(Operation operation) throws VisitorException {

        Expression e1, e2;

        switch (operation.getOperator()) {
            case AND:
            case OR:
            case EQ:
                operation.getOperand(0).accept(this);
                e1 = returnExp;
                operation.getOperand(1).accept(this);
                e2 = returnExp;
                returnExp = new Operation(operation.getOperator(), e1, e2);
                break;
            case NOT:
                operation.getOperand(0).accept(this);
                e1 = returnExp;
                returnExp = new Operation(Operation.Operator.NOT, e1);
                break;
            default:
                assert false : "unexpected operation for substitution. Failing.";
                break;
        }
    }
}
