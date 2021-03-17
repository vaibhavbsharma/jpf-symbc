package gov.nasa.jpf.symbc.veritesting.ast.def;

import com.ibm.wala.ssa.SSAPutInstruction;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstVisitor;
import za.ac.sun.cs.green.expr.Expression;

/**
 * This is an artificial internal instruction in JR that is used to put assignments to GlobalJRVars.
 */

public class StoreGlobalInstruction extends Instruction {

    public final GlobalJRVar lhs;
    public final Expression rhs;

    //actual constructor
    public StoreGlobalInstruction(GlobalJRVar lhs, Expression rhs) {
        super(null);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    //fake constructor
    public StoreGlobalInstruction(SSAPutInstruction ins, GlobalJRVar lhs, Expression rhs) {
        super(ins);
        assert false : "This instruction maps to no Wala instruction. Invalid Constructor. Assumption Violated. Failing.";
        this.lhs = lhs;
        this.rhs = rhs;
    }

    //fake constructor
    public StoreGlobalInstruction(Instruction ins) {
        super(null);
        assert false : "This instruction maps to no Wala instruction. Invalid Constructor. Assumption Violated. Failing.";
        lhs = null;
        rhs = null;
    }

    public Instruction getOriginal() {
        assert false : "No original Instruction for StoreGlobalInstruction. Assumption Violated. Failing.";
        return null;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "\n globalStore(" + lhs + ") = " + rhs;
    }

    @Override
    public boolean equals(Stmt stmt2) {
        if (!(stmt2 instanceof StoreGlobalInstruction))
            return false;
        else {
            String assignExp2 = ((StoreGlobalInstruction) stmt2).rhs.toString();
            return (this.lhs.equals(((StoreGlobalInstruction) stmt2).lhs)
                    && this.rhs.toString().equals(assignExp2));
        }
    }
}
