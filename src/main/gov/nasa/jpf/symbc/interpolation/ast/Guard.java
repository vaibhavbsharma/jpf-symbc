package gov.nasa.jpf.symbc.interpolation.ast;

import gov.nasa.jpf.symbc.veritesting.ast.def.Stmt;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstVisitor;
import za.ac.sun.cs.green.expr.Expression;

/**
 * this class is used to hold the guard or the condition that the SE decided to take for a particular execution
 */
public class Guard implements Stmt {

    Expression condition;

    public Guard(Expression condition) {
        this.condition = condition;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        assert false : "unsupported";
        return null;
    }

    @Override
    public boolean equals(Stmt stmt2) {
        return (stmt2 instanceof Guard && condition.equals(((Guard) stmt2).condition));
    }

    @Override
    public String toString(){
        return condition.toString();
    }
}
