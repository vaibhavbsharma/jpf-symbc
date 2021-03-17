package gov.nasa.jpf.symbc.veritesting.ast.def;

import gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa.SubscriptPair;
import za.ac.sun.cs.green.expr.Visitor;
import za.ac.sun.cs.green.expr.VisitorException;

import java.util.List;

/**
 * A class that carries the ssa version of the GlobalJRVar
 */

public final class GlobalJRVarSSAExpr extends CloneableVariable {
    public final GlobalJRVar globalJRVar;
    public final gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa.SubscriptPair subscript;
    public String varId;
    public int uniqueNum = -1;

    public GlobalJRVarSSAExpr(GlobalJRVar globalJRVar, gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa.SubscriptPair subscript) {
        super("@r" + globalJRVar + "." + subscript);
        this.globalJRVar = globalJRVar.clone();
        this.subscript = subscript.clone();
    }

    public GlobalJRVarSSAExpr(GlobalJRVar globalJRVar, SubscriptPair subscript, int uniqueNum) {
        super("@r" + globalJRVar + "." + subscript);
        this.globalJRVar = globalJRVar.clone();
        this.subscript = subscript.clone();
        this.uniqueNum = uniqueNum;
    }

    @Override
    public void accept(Visitor visitor) throws VisitorException {
        // will call the Variable entry.
        visitor.preVisit(this);
        visitor.postVisit(this);
    }

    @Override

    // I am making class final so that equality works correctly.
    public boolean equals(Object o) {
        if (o instanceof GlobalJRVarSSAExpr) {
            GlobalJRVarSSAExpr other = (GlobalJRVarSSAExpr) o;
            return (this.globalJRVar.equals(other.globalJRVar) &&
                    this.subscript.equals(other.subscript) && this.uniqueNum == other.uniqueNum);
        }
        return false;
    }

    @Override
    public String toString() {
        return getSymName();
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public int getLeftLength() {
        return 1;
    }

    @Override
    public int numVar() {
        return 1;
    }

    @Override
    public int numVarLeft() {
        return 1;
    }

    @Override
    public List<String> getOperationVector() {
        return null;
    }

    public String getSymName() {
        String ret = globalJRVar + "." + subscript.getSymName();
        if (uniqueNum != -1) ret = ret + "." + uniqueNum;
        return ret;
    }

    @Override
    public GlobalJRVarSSAExpr clone() {
        GlobalJRVarSSAExpr ret;
        if (uniqueNum != -1) ret = new GlobalJRVarSSAExpr(globalJRVar.clone(), subscript.clone(), uniqueNum);
        else ret = new GlobalJRVarSSAExpr(globalJRVar.clone(), subscript.clone());
        return ret;
    }

    @Override
    public GlobalJRVarSSAExpr makeUnique(int unique) {
        if (uniqueNum == -1) {
            uniqueNum = unique;
        }
        return this.clone();
    }

    @Override
    public int hashCode() {
        return getSymName().hashCode();
    }
}
