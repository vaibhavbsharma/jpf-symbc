package gov.nasa.jpf.symbc.interpolation.ast;

import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.symbc.veritesting.ast.def.Stmt;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstVisitor;
import za.ac.sun.cs.green.expr.Expression;

import static gov.nasa.jpf.symbc.interpolation.InterpolationUtil.getUniqueName;

/**
 * this class is used to hold the guard or the condition that the SE decided to take for a particular execution
 */
public class Guard implements Stmt {

    public final Expression condition;
    public final String uniquePgmPoint;  //to identify which instruction is the guard on, to easily attach the corresponding wp with it.

    public Guard(Expression condition, IfInstruction ifInstruction) {
        this.condition = condition;
        this.uniquePgmPoint = getUniqueName(ifInstruction);
    }

    public Guard(Expression condition, String uniquePgmPoint) {
        this.condition = condition;
        this.uniquePgmPoint = uniquePgmPoint;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        assert false : "Guard has interpolant visitors to visit them.";
        return null;
    }

    @Override
    public <T> T accept(gov.nasa.jpf.symbc.interpolation.ast.AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Stmt stmt2) {
        return (stmt2 instanceof Guard && condition.equals(((Guard) stmt2).condition) && uniquePgmPoint.equals(((Guard) stmt2).uniquePgmPoint));
    }

    @Override
    public String toString() {
        return condition.toString();
    }


}
