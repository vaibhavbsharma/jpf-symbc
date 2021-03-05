package gov.nasa.jpf.symbc.interpolation.shadow;

import gov.nasa.jpf.symbc.veritesting.ast.transformations.SPFCases.SpfToGreenVisitor;
import gov.nasa.jpf.vm.StackFrame;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;

import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.SPFToGreenExpr;

public class ShadowStackFrame {
    StackFrame sf;

    public ShadowStackFrame(StackFrame sf) {
        this.sf = sf.clone();
    }

    public Expression getSPFSlotExpr(int slot) {
        Object expr = sf.getSlotAttr(slot);
        if (expr != null) {
            assert expr instanceof gov.nasa.jpf.symbc.numeric.Expression : "Weird type for slot attribute. Assumption Violated. Failing.";
            return SPFToGreenExpr((gov.nasa.jpf.symbc.numeric.Expression) expr);
        } else return new IntConstant(sf.getSlot(slot));
    }


}
