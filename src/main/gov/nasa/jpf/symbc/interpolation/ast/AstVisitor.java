package gov.nasa.jpf.symbc.interpolation.ast;


import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;

public interface AstVisitor<T> extends gov.nasa.jpf.symbc.veritesting.ast.visitors.AstVisitor<T> {

    T visit(Guard guard);
}
