package gov.nasa.jpf.symbc.veritesting.ast.transformations.substitutestackinput;

import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.ast.def.Stmt;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.StmtPrintVisitor;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.RealConstant;

import static gov.nasa.jpf.symbc.veritesting.StaticRegionException.throwException;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.SPFToGreenExpr;

public class SubstituteStackInput {
    public static DynamicRegion execute(final ThreadInfo threadInfo, final DynamicRegion dynRegion) throws StaticRegionException {
        if (dynRegion.stackInput == null) {
            return dynRegion;
        }
        final StackFrame stackFrame = threadInfo.getTopFrame();
        final Object operandAttrObj = stackFrame.getOperandAttr();
        final gov.nasa.jpf.symbc.numeric.Expression spfExpression = operandAttrObj != null ? (gov.nasa.jpf.symbc.numeric.Expression) operandAttrObj : null;
        final String stackInputType = (String) dynRegion.varTypeTable.lookupByName(dynRegion.stackInput.getSymName());
        Expression stackInputExpression = spfExpression != null ? SPFToGreenExpr(spfExpression) : getConcreteStackInputValue(stackFrame, stackInputType);
        final Stmt stmtWithStackInputSubstitutedWithValue =
                dynRegion.dynStmt.accept(
                        new AstMapVisitor(
                                new SubstituteWalaVarWithNewExpr(dynRegion.stackInput, stackInputExpression)));
        System.out.println("\n--------------- SubstituteStackInput Complete---------------\n");
        System.out.println(StmtPrintVisitor.print(stmtWithStackInputSubstitutedWithValue));
        return new DynamicRegion(dynRegion, stmtWithStackInputSubstitutedWithValue, dynRegion.spfCaseList, dynRegion.regionSummary,
                dynRegion.spfPredicateSummary, dynRegion.earlyReturnResult);
    }

    private static Expression getConcreteStackInputValue(StackFrame stackFrame, String stackInputType) throws StaticRegionException {
        if (stackInputType != null) {
            switch (stackInputType) {
                case "double":
                    return new RealConstant(stackFrame.popDouble());
                case "float":
                    return new RealConstant(stackFrame.popFloat());
                case "long":
                    return new IntConstant((int) stackFrame.popLong());
                case "int":
                case "short":
                case "boolean":
                default: //assume int for now
                    return new IntConstant(stackFrame.pop());
            }
        } else {
            throwException(new StaticRegionException("Unknown type of expression to be read off the stack"), StaticRegionException.ExceptionPhase.INSTANTIATION);
        }
        assert(false);
        return null;
    }
}
