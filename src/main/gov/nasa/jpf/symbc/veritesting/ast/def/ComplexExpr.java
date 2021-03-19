package gov.nasa.jpf.symbc.veritesting.ast.def;

import com.ibm.wala.ssa.SSAConditionalBranchInstruction;
import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.Operation;
import za.ac.sun.cs.green.expr.Visitor;
import za.ac.sun.cs.green.expr.VisitorException;

import java.util.List;


/**
 * this class is used to attach expression of complex conditions to their instructions and their reverse status
 */
public class ComplexExpr extends Operation {

    public final SSAConditionalBranchInstruction original;
    public final boolean isRevered;
    public final Obligation generalOblg;

    public ComplexExpr(final Operator operator, SSAConditionalBranchInstruction original, boolean isRevered, Obligation generalOblg, Expression... operands) {
        super(operator, operands);
        this.original = original;
        this.isRevered = isRevered;
        this.generalOblg = generalOblg;

    }

}
