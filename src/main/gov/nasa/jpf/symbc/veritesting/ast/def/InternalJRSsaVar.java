package gov.nasa.jpf.symbc.veritesting.ast.def;

import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import za.ac.sun.cs.green.expr.Visitor;
import za.ac.sun.cs.green.expr.VisitorException;

import java.util.List;

/**
 * This defines a type of internal variables for JavaRanger
 */
public final class InternalJRSsaVar extends CloneableVariable {
    /**
     * This number matches the number defined for a specific Wala Variable.
     */
    private static int ssaIndex = 0;
    int mySsaIndex;

    public InternalJRSsaVar() {
        super("jrVar_" + ssaIndex);
        mySsaIndex = ssaIndex++;
    }

    //used only for cloning
    private InternalJRSsaVar(int mySsaIndex) {
        super("jrVar" + "_" + mySsaIndex);
    }


    @Override
    public void accept(Visitor visitor) throws VisitorException {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof InternalJRSsaVar) {
            InternalJRSsaVar other = (InternalJRSsaVar) o;
            return (this.mySsaIndex == other.mySsaIndex);
        }
        return false;
    }

    @Override
    public String toString() {
        return "jrVar" + "_" + mySsaIndex;
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

    public InternalJRSsaVar clone() {
        return new InternalJRSsaVar(mySsaIndex);
    }

    @Override
    public InternalJRSsaVar makeUnique(int unique) {
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