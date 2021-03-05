package gov.nasa.jpf.symbc.interpolation.ast;

import gov.nasa.jpf.symbc.interpolation.ast.SlotVar;
import za.ac.sun.cs.green.expr.IntConstant;

public interface ExprVisitor<T> extends gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor<T> {
    public T visit(SlotVar slotVar);
}
