package gov.nasa.jpf.symbc.veritesting.ast.def;

import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import za.ac.sun.cs.green.expr.Visitor;
import za.ac.sun.cs.green.expr.VisitorException;

import java.util.List;

/**
 * This defines a single variable for Internal JavaRanger Variable.
 */
public class InternalJRVar extends CloneableVariable {

    //main internal JR variable to be used.
    public static InternalJRVar jrVar = new InternalJRVar();

    //only a single variable is allowed to be used.
    private InternalJRVar() {
        super("jrVar");
    }

    //used only for cloning
    private InternalJRVar(int myIndex) {
        super("jrVar" + myIndex);
    }

    @Override
    public void accept(Visitor visitor) throws VisitorException {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof InternalJRVar) {
            InternalJRVar other = (InternalJRVar) o;
            return (this.toString().equals(other.toString()));
        }
        return false;
    }

    @Override
    public String toString() {
        return "jrVar";
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

    public InternalJRVar clone() {
        return new InternalJRVar();
    }

    @Override
    public InternalJRVar makeUnique(int unique) {
        /*if (uniqueNum != -1 && unique != uniqueNum)
            throw new StaticRegionException("Attempting to make a already-unique WalaVarExpr unique");
        return new InternalJRVar(number, unique);*/

        //the assumption here is that this variable is already unique.
        return this;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /*public int getUniqueNum() {
        return uniqueNum;
    }*/
/*
    public static WalaVarExpr getUniqueWalaVarExpr(WalaVarExpr expr, int uniqueNum) {
        String varId = Integer.toString(expr.number);
        varId = varId.concat("$");
        varId = varId.concat(Integer.toString(uniqueNum));
        return new WalaVarExpr(expr.number, varId);
    }*/

}