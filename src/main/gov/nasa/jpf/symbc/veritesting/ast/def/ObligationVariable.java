package gov.nasa.jpf.symbc.veritesting.ast.def;

import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import za.ac.sun.cs.green.expr.Visitor;
import za.ac.sun.cs.green.expr.VisitorException;

import java.util.List;

/**
 * This are new symbolic variables that indicates that will later refer to obligations. Their type must always be bool.
 */
public class ObligationVariable extends CloneableVariable {
    private static int globalUniqueCounter = 0;
    public final String type;
    public final int unique;

    public ObligationVariable(String name) {
        super("o");
        type = "bool";
        this.unique = globalUniqueCounter++;
    }

    @Override
    public CloneableVariable clone() throws CloneNotSupportedException {
        return new ObligationVariable(getName());
    }

    @Override
    public CloneableVariable makeUnique(int unique) throws StaticRegionException {
        return null;
    }

    public String getSymName() {
        String ret = getName();
        ret += "$" + globalUniqueCounter;
        return ret;
    }

    @Override
    public int hashCode() {
        return getSymName().hashCode();
    }

    @Override
    public void accept(Visitor visitor) throws VisitorException {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (o.getClass() != this.getClass()) { return false; }
        return (this.getName().equals(((ObligationVariable)o).getName()) && (this.unique == ((ObligationVariable) o).unique) &&
                this.type.equals(((ObligationVariable)o).type));
    }

    @Override
    public String toString() {
        return this.getSymName();
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public int getLeftLength() {
        return 0;
    }

    @Override
    public int numVar() {
        return 0;
    }

    @Override
    public int numVarLeft() {
        return 0;
    }

    @Override
    public List<String> getOperationVector() {
        return null;
    }
}
