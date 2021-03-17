package gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa;

import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.ast.def.GlobalJRVar;
import gov.nasa.jpf.symbc.veritesting.ast.def.GlobalJRVarSSAExpr;
import gov.nasa.jpf.symbc.veritesting.ast.def.ObligationVar;
import za.ac.sun.cs.green.expr.Expression;

import java.util.*;

import static gov.nasa.jpf.symbc.VeritestingListener.verboseVeritesting;
import static gov.nasa.jpf.symbc.veritesting.StaticRegionException.ExceptionPhase.INSTANTIATION;
import static gov.nasa.jpf.symbc.veritesting.StaticRegionException.throwException;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.createGreenVar;

public final class GlobalVarPsmMap {
    public final HashMap<GlobalJRVar, SubscriptPair> table;
    protected final String tableName = "Path Subscript Map";
    protected final String label1 = "GlobalJRVar";
    protected final String label2 = "subscript";
    private int uniqueNum = -1;

    public GlobalVarPsmMap() {
        this.table = new HashMap<>();
    }

    // returns -1 if the key isn't found
    public SubscriptPair lookup(GlobalJRVar key) {
        SubscriptPair ret = null;
        if (key != null) {
            for (GlobalJRVar globalJRVar : table.keySet()) {
                if (globalJRVar.equals(key))
                    ret = table.get(globalJRVar);
            }
        } else {
            throwException(new IllegalArgumentException("Cannot lookup the value of a null " + "."), INSTANTIATION);
        }
        return ret;
    }

    public void add(GlobalJRVar v1, SubscriptPair v2) {
        if ((v1 != null) && (v2 != null))
            table.put(v1, v2);
    }

    public void remove(GlobalJRVar key) {
        if (lookup(key) != null)
            for (Iterator<Map.Entry<GlobalJRVar, SubscriptPair>> globalVarItr = table.entrySet().iterator();
                 ((Iterator) globalVarItr).hasNext(); ) {
                Map.Entry<GlobalJRVar, SubscriptPair> entry = globalVarItr.next();
                GlobalJRVar globalJRVar = entry.getKey();
                if (globalJRVar.equals(key))
                    globalVarItr.remove();
            }
    }

    public void print() {
        if (verboseVeritesting) {
            System.out.println("\nprinting " + tableName + " (" + label1 + "->" + label2 + ")");
            table.forEach((v1, v2) -> System.out.println("!w" + v1 + " --------- " + v2));
        }
    }

    public void updateKeys(GlobalJRVar oldKey, GlobalJRVar newKey) {
        for (GlobalJRVar key : table.keySet()) {
            SubscriptPair value = table.get(key);
            if (key.equals(oldKey)) {
                table.put(newKey, value);
                table.remove(oldKey);
            }
        }
    }

    public Set<GlobalJRVar> getKeys() {
        return table.keySet();
    }

    @Override
    protected GlobalVarPsmMap clone() {
        GlobalVarPsmMap map = new GlobalVarPsmMap();
        this.table.forEach((key, value) -> {
            map.add(key, value);
        });
        return map;
    }

    public void updateValue(GlobalJRVar fieldRef, SubscriptPair p) {
        for (GlobalJRVar key : table.keySet()) {
            if (key.equals(fieldRef)) {
                table.put(key, p);
            }
        }
    }

    public void setUniqueNum(int uniqueNum) {
        this.uniqueNum = uniqueNum;
    }

    public ArrayList<GlobalJRVarSSAExpr> getUniqueFieldAccess() throws StaticRegionException {
        ArrayList<GlobalJRVarSSAExpr> retList = new ArrayList();
        if (uniqueNum == -1)
            throwException(new StaticRegionException("uniqueNum not set before getting unique field accesses"), INSTANTIATION);
        Iterator itr = this.table.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry pair = (Map.Entry) itr.next();
            GlobalJRVar globalJRVar = (GlobalJRVar) pair.getKey();
            SubscriptPair subscriptPair = (SubscriptPair) pair.getValue();
            GlobalJRVarSSAExpr expr = new GlobalJRVarSSAExpr(globalJRVar, subscriptPair);
            expr = expr.makeUnique(uniqueNum);
            retList.add(expr);
        }
        return retList;
    }

    public Expression getVarExprForKey(ObligationVar oblgVar) {
        return createGreenVar("int", oblgVar.toString() + "." + table.get(oblgVar).toString());
    }
}
