package gov.nasa.jpf.symbc.veritesting.ast.def;

import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstVisitor;

/**
 * Defines an interface for all statements in the RangerIR.
 */
public interface Stmt extends Ast {
    public abstract <T> T accept(AstVisitor<T> visitor);

    default public <T> T accept(gov.nasa.jpf.symbc.interpolation.ast.AstVisitor<T> visitor) {
        assert false : "unsupported visit.";
        return null;
    }

    public String toString();

    public boolean equals(Stmt stmt2);
}
