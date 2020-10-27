package gov.nasa.jpf.symbc.veritesting.VeritestingUtil;

import com.ibm.wala.shrikeBT.IConditionalBranchInstruction;
import com.ibm.wala.ssa.SymbolTable;
import gov.nasa.jpf.symbc.veritesting.ast.def.WalaVarExpr;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.Operation;
import za.ac.sun.cs.green.expr.RealConstant;

/**
 * This class provides some utility methods for Wala.
 */
public class WalaUtil {

    /**
     * This method is used to return a Green expression for a wala var name, based on the type of the constant.
     *
     */
    public static Expression makeConstantFromWala(SymbolTable symbolTable, int walaId) {

        if (symbolTable.isBooleanConstant(walaId) || symbolTable.isIntegerConstant(walaId))
            return new IntConstant((Integer) symbolTable.getConstantValue(walaId));
        else if (symbolTable.isDoubleConstant(walaId))
            return new RealConstant((Double) symbolTable.getConstantValue(walaId));
        else if (symbolTable.isFloatConstant(walaId))
            return new RealConstant(((Float) symbolTable.getConstantValue(walaId)).doubleValue());
        else if (symbolTable.isTrue(walaId))
            return new IntConstant(1);
        else if (symbolTable.isFalse(walaId))
            return new IntConstant(0);
        else if (symbolTable.isNullConstant(walaId))
            return new IntConstant(0);
        else // is a constant that we don't support, then just return it back.
        {
            System.out.println("constant type not supported for @w" + walaId);
            return null;
        }

    }


    // the assumption is that ranger operation is always the negation of the corresponding wala operation representing the bytecode instructionn.
    public static boolean negationOfCondInst(Operation cond, IConditionalBranchInstruction.Operator walaOp) {
        Operation.Operator rangerOp = cond.getOperator();
        if(rangerOp == Operation.Operator.NOT)
            rangerOp = negateRangerOp(((Operation)cond.getOperand(0)).getOperator());
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

    private static Operation.Operator negateRangerOp(Operation.Operator operator) {

        switch (operator) {
            case EQ:
                return Operation.Operator.NE;
            case NE:
                return Operation.Operator.EQ;
            case LT:
                return Operation.Operator.GE;
            case GE:
                return Operation.Operator.LT;
            case GT:
                return  Operation.Operator.LE;
            case LE:
                return Operation.Operator.GT;
        }
        assert false : "this should be unreachable code. Failing.";

        return null;
    }

}
