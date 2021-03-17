package gov.nasa.jpf.symbc.veritesting.ast.def;

import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import za.ac.sun.cs.green.expr.*;

import java.util.List;


/**
 * This class is used to represent Global internal JR variables.
 */

public class GlobalJRVar extends Variable {
    String varName;
    public static final Expression defaultValue = new IntConstant(0);

    public GlobalJRVar(String varName) {
        super(varName);
        this.varName = varName;
    }

    @Override
    public String toString() {
        return varName;
    }

    @Override
    public int getLength() {
        assert false : "unimplemented.";
        return 0;
    }

    @Override
    public int getLeftLength() {
        assert false : "unimplemented.";
        return 0;
    }

    @Override
    public int numVar() {
        assert false : "unimplemented.";
        return 0;
    }

    @Override
    public int numVarLeft() {
        assert false : "unimplemented.";
        return 0;
    }

    @Override
    public List<String> getOperationVector() {
        assert false : "unimplemented.";
        return null;
    }

    @Override
    public void accept(Visitor visitor) throws VisitorException {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GlobalJRVar) {
            GlobalJRVar f = (GlobalJRVar) obj;
            return varName.equals(((GlobalJRVar) obj).varName);
        } else return false;
    }

    @Override
    protected GlobalJRVar clone() {
        return new GlobalJRVar(varName);
    }

    public static GlobalJRVar makePutFieldRef(Object obj) throws StaticRegionException {
        return new GlobalJRVar(obj.toString());
    }
}
