package gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa;

import gov.nasa.jpf.symbc.veritesting.ast.def.GlobalJRVar;
import gov.nasa.jpf.symbc.veritesting.ast.def.ObligationVar;
import za.ac.sun.cs.green.expr.Expression;

import java.util.HashMap;
import java.util.Set;

import static gov.nasa.jpf.symbc.VeritestingListener.verboseVeritesting;
import static gov.nasa.jpf.symbc.veritesting.StaticRegionException.ExceptionPhase.INSTANTIATION;
import static gov.nasa.jpf.symbc.veritesting.StaticRegionException.throwException;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.createGreenVar;
import static gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa.GlobalVarSSAVisitor.FIELD_SUBSCRIPT_BASE;


public class GlobalVarGsmMap {
    public final HashMap<GlobalJRVar, Integer> table;
    protected final String tableName = "Global Subscript Map";
    protected final String label1 = "GlobalJRVar";
    protected final String label2 = "subscript";

    public GlobalVarGsmMap() {
        this.table = new HashMap<>();
    }

    // returns -1 if the key isn't found
    public int lookup(GlobalJRVar key) {
        int ret = -1;
        if (key != null) {
            for (GlobalJRVar globalJRVar : table.keySet()) {
                if (globalJRVar.equals(key))
                    ret = table.get(globalJRVar);
            }
        } else {
            throwException(new IllegalArgumentException("Cannot lookup the value of a null " + label1 + "."), INSTANTIATION);
        }
        return ret;
    }

    public void add(GlobalJRVar v1, Integer v2) {
        if ((v1 != null) && (v2 != null))
            table.put(v1, v2);
    }

    public void remove(GlobalJRVar key) {
        if (lookup(key) != -1)
            for (GlobalJRVar globalJRVar : table.keySet()) {
                if (globalJRVar.equals(key))
                    table.remove(globalJRVar);
            }
    }

    public void print() {
        if (verboseVeritesting) {
            System.out.println("\nprinting " + tableName + " (" + label1 + "->" + label2 + ")");
            table.forEach((v1, v2) -> System.out.println("!w" + v1 + " --------- " + v2));
        }
    }

    public Set<GlobalJRVar> getKeys() {
        return table.keySet();
    }

    @Override
    protected GlobalVarGsmMap clone() {
        GlobalVarGsmMap map = new GlobalVarGsmMap();
        this.table.forEach(map::add);
        return map;
    }

    public void updateValue(GlobalJRVar globalJRVar, Integer p) {
        for (GlobalJRVar key : table.keySet()) {
            if (key.equals(globalJRVar)) {
                table.put(key, p);
            }
        }
    }

    public Integer createSubscript(GlobalJRVar globalJRVar) {
        if (lookup(globalJRVar) != -1) {
            int ret = lookup(globalJRVar);
            updateValue(globalJRVar, ret + 1);
            return ret + 1;
        } else {
            add(globalJRVar, FIELD_SUBSCRIPT_BASE + 1);
            return FIELD_SUBSCRIPT_BASE + 1;
        }
    }
}

