package gov.nasa.jpf.symbc.interpolation.ast;

import gov.nasa.jpf.symbc.veritesting.ast.def.CloneableVariable;
import za.ac.sun.cs.green.expr.Visitor;
import za.ac.sun.cs.green.expr.VisitorException;

import java.util.List;

public class SlotVar extends CloneableVariable {
    public final int slot;
    private static final String name = "slot_";

    public SlotVar(int slot) {
        super(name+slot);
        this.slot = slot;
    }

    @Override
    public CloneableVariable clone() throws CloneNotSupportedException {
        return new SlotVar(slot);
    }

    @Override
    public CloneableVariable makeUnique(int unique) {
        assert false : "not expecting this var to be cloneable. Assumption Violated. Failing";
        return null;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public void accept(Visitor visitor) throws VisitorException {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof SlotVar) {
            SlotVar other = (SlotVar) o;
            return (this.toString().equals(other.toString()));
        }
        return false;
    }

    @Override
    public String toString() {
        return name+slot;
    }

    @Override
    public int getLength() {
        assert false : "not expecting a call for this method on this class. Assumption Violated. Failing";
        return 0;
    }

    @Override
    public int getLeftLength() {
        assert false : "not expecting a call for this method on this class. Assumption Violated. Failing";
        return 0;
    }

    @Override
    public int numVar() {
        assert false : "not expecting a call for this method on this class. Assumption Violated. Failing";
        return 0;
    }

    @Override
    public int numVarLeft() {
        assert false : "not expecting a call for this method on this class. Assumption Violated. Failing";
        return 0;
    }

    @Override
    public List<String> getOperationVector() {
        assert false : "not expecting a call for this method on this class. Assumption Violated. Failing";
        return null;
    }
}
