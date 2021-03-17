package gov.nasa.jpf.symbc.veritesting.ast.transformations.globaljrvarssa;

import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.vm.ThreadInfo;

public class GlobalVarAccessVisitor extends AstMapVisitor {
    public GlobalVarAccessVisitor() {
        super(new ExprMapVisitor());
    }

    @Override
    public Stmt visit(StoreGlobalInstruction c) {
        return super.visit(c);
    }

    public static DynamicRegion doTransform(ThreadInfo ti, DynamicRegion dynRegion) {

        return dynRegion;
    }
}
