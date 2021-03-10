package gov.nasa.jpf.symbc.interpolation.ast;

import gov.nasa.jpf.symbc.veritesting.RangerDiscovery.InputOutput.DiscoveryUtil;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil;
import gov.nasa.jpf.symbc.veritesting.ast.def.CloneableVariable;
import gov.nasa.jpf.vm.StackFrame;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.Visitor;
import za.ac.sun.cs.green.expr.VisitorException;

import java.util.List;

public class SlotVar extends CloneableVariable {
    public final int slotNum;
    private static final String name = "slot_";

    public SlotVar(int slotNum) {
        super(name + slotNum);
        this.slotNum = slotNum;
    }

    @Override
    public CloneableVariable clone() throws CloneNotSupportedException {
        return new SlotVar(slotNum);
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

    //returns its symbolic value in the stackframe if found, otherwise it returns its concrete value.
    public Expression instantiate(StackFrame sf) {
        Object slotAttr = sf.getSlotAttr(slotNum);
        if (slotAttr != null) {
            assert slotAttr instanceof gov.nasa.jpf.symbc.numeric.Expression;
            return ExprUtil.SPFToGreenExpr((gov.nasa.jpf.symbc.numeric.Expression) sf.getSlotAttr(slotNum));
        } else
            return new IntConstant(sf.getSlot(slotNum));
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
        return name + slotNum;
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
