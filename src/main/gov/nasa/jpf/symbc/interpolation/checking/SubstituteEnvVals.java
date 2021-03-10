package gov.nasa.jpf.symbc.interpolation.checking;

import gov.nasa.jpf.symbc.interpolation.ast.ExprMapVisitor;
import gov.nasa.jpf.symbc.interpolation.ast.SlotVar;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import za.ac.sun.cs.green.expr.Expression;

public class SubstituteEnvVals extends ExprMapVisitor {

    StackFrame sf;

    public SubstituteEnvVals(StackFrame topFrame) {
        super();
        sf = topFrame;
    }

    @Override
    public Expression visit(SlotVar slotVar) {
        return slotVar.instantiate(sf);
    }

    public static Expression execute(ThreadInfo ti, Expression interpolant) {
        SubstituteEnvVals substituteEnvVals = new SubstituteEnvVals(ti.getTopFrame());
        return substituteEnvVals.eva.accept(interpolant);
    }
}
