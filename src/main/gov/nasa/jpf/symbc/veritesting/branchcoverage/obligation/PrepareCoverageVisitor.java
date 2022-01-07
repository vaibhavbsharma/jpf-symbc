package gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation;

import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.symbc.veritesting.StaticRegionException;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.ssaToAst.StaticRegion;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprVisitor;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.Operation;

import static gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr.isOblgCovered;

/**
 * Prepares collection of obligations by inserting jrvar inside source defined if-statements, that is if an if-statement is not generated because of the source,
 * it does not generate a jrvar assignment, as it maps ot no obligations in the source.
 * It should be executed after the EarlyReturns transformations.
 */
public class PrepareCoverageVisitor extends AstMapVisitor {
    //indicates if a particular branch has already a jrvar, like those generated due to the early return transformations.
    // in this case, for these jrvar assignments we want to skip over and not create duplicated jrvars
    boolean branchHasJRVar = false;

    // indicates that an early return in the previous statements possibly exists.
    // This allows us when creating the obligations to specify that ER cannot have happened in the previous statements
    // for this obligation to be covered.
    boolean hasPreviousER = false;

/*
    @Override
    public Stmt visit(AssignmentStmt a) {
        assert eva.theVisitor instanceof PrepareCoverageExprVisitor : "unexpected visitor for expression. assumption violated. Failing";

        if (!branchHasJRVar)
            branchHasJRVar = a.lhs instanceof InternalJRVar || a.lhs instanceof InternalJRSsaVar;

        return a;
    }
*/

    @Override
    public Stmt visit(ReturnInstruction c) {
        hasPreviousER = true;
        return c;
    }


    @Override
    public Stmt visit(StoreGlobalInstruction a) {
        assert eva.theVisitor instanceof PrepareCoverageExprVisitor : "unexpected visitor for expression. assumption violated. Failing";

        branchHasJRVar = true;
        return a;
    }

    @Override
    public Stmt visit(CompositionStmt a) {
        Stmt newS1;
        Stmt newS2;

        if (a.s1 instanceof IfThenElseStmt) {
            boolean oldsHasJRVar = branchHasJRVar;
            branchHasJRVar = false;
            newS1 = a.s1.accept(this);
            branchHasJRVar = oldsHasJRVar;
        } else
            newS1 = a.s1.accept(this);

        if (a.s2 instanceof IfThenElseStmt) {
            boolean oldsHasJRVar = branchHasJRVar;
            branchHasJRVar = false;
            newS2 = a.s2.accept(this);
            branchHasJRVar = oldsHasJRVar;
        } else
            newS2 = a.s2.accept(this);

        return new CompositionStmt(newS1, newS2);

    }

    @Override
    public Stmt visit(IfThenElseStmt a) {
        //having a jrvar inside the condition of the if-then-else means that there is a probably jrvar inside already, and therefore we do not want to add a new jrvar.
        // when collecting the obligations for each jrvar, we also need to ignore those jrvars that reside inside a condition that contains a jrvar
        // because of two things: the if-condition is artificial and is not part of the source, and second we have alread created a jrvar
        // for the else side previously so it is taking care of the case when an early return has not happened.

        ((PrepareCoverageExprVisitor) eva.theVisitor).hasJRVar = false; //ignore early return conditions
        eva.accept(a.condition);

        boolean conditionHasJRVar = ((PrepareCoverageExprVisitor) eva.theVisitor).hasJRVar;
        boolean oldHasPreviousER = this.hasPreviousER;
        boolean thenHasPreviousER;
        boolean elseHasPreviousER;
        Stmt newThen;
        Stmt newElse;

        branchHasJRVar = false;
        newThen = a.thenStmt.accept(this);
        boolean thenHasJRVarAssignment = branchHasJRVar;
        thenHasPreviousER = this.hasPreviousER;


        branchHasJRVar = false;
        newElse = a.elseStmt.accept(this);
        boolean elseHasJRVarAssignment = branchHasJRVar;
        elseHasPreviousER = thenHasPreviousER ||this.hasPreviousER;

        Obligation thenOblg;
        Obligation elseOblg;
        boolean conditionReverseStatus = a.isByteCodeReversed;

        if (a.condition instanceof ComplexExpr) {
            conditionReverseStatus = ((ComplexExpr) a.condition).isRevered;
            if (conditionReverseStatus) {
                thenOblg = VeriObligationMgr.createOblgFromGeneral(((ComplexExpr) a.condition).generalOblg, ObligationSide.NOT_TAKEN);
                elseOblg = VeriObligationMgr.createOblgFromGeneral(((ComplexExpr) a.condition).generalOblg, ObligationSide.TAKEN);
            } else {
                elseOblg = VeriObligationMgr.createOblgFromGeneral(((ComplexExpr) a.condition).generalOblg, ObligationSide.NOT_TAKEN);
                thenOblg = VeriObligationMgr.createOblgFromGeneral(((ComplexExpr) a.condition).generalOblg, ObligationSide.TAKEN);
            }
        } else {
            if (a.generalOblg == null) { //in case of jrInternal variable for early returns
                assert a.condition instanceof Operation && ((Operation) a.condition).getArity() == 2 && (((Operation) a.condition).getOperand(0) instanceof InternalJRVar || ((Operation) a.condition).getOperand(1) instanceof InternalJRVar) : "condition is not in the form of internalJR vairable, the only case where general obligation is expected to be null";
                return new IfThenElseStmt(a.original, a.condition, newThen,
                        newElse, a.genuine, a.isByteCodeReversed, a.generalOblg);
            }
            if (conditionReverseStatus) {
                thenOblg = VeriObligationMgr.createOblgFromGeneral(a.generalOblg, ObligationSide.NOT_TAKEN);
                elseOblg = VeriObligationMgr.createOblgFromGeneral(a.generalOblg, ObligationSide.TAKEN);
            } else {
                elseOblg = VeriObligationMgr.createOblgFromGeneral(a.generalOblg, ObligationSide.NOT_TAKEN);
                thenOblg = VeriObligationMgr.createOblgFromGeneral(a.generalOblg, ObligationSide.TAKEN);
            }
        }

        if (!conditionHasJRVar) {
            if (!thenHasJRVarAssignment) {// then I'll add a JRVar assignment to indicate the branching/obligation
//                newThen = new CompositionStmt(new AssignmentStmt(InternalJRVar.jrVar, new IntConstant(1)), a.thenStmt);
                if (!isOblgCovered(thenOblg))
                    if (!oldHasPreviousER) // no need to account for the possibility of returning early.
                        newThen = new CompositionStmt(new StoreGlobalInstruction(new ObligationVar(thenOblg), new IntConstant(1)), newThen);
                    else // we need to specify that not only that the obligation can be covered here, but it can only be covered if no early return has occured.
                        newThen = new CompositionStmt(new StoreGlobalInstruction(new ObligationVar(thenOblg),
                                     new GammaVarExpr(new Operation(Operation.Operator.EQ,
                                             InternalJRVar.jrVar,
                                             new IntConstant(0)),new IntConstant(1),
                                             new IntConstant(0))),
                                   newThen);
            }
            if (!elseHasJRVarAssignment) {// then I'll add a JRVar assignment to indicate the branching/obligation
                if (!isOblgCovered(elseOblg))
                    if(!thenHasPreviousER)
                        newElse = new CompositionStmt(new StoreGlobalInstruction(new ObligationVar(elseOblg), new IntConstant(1)), newElse);
                else
                        newElse = new CompositionStmt(new StoreGlobalInstruction(new ObligationVar(elseOblg), new GammaVarExpr(new Operation(Operation.Operator.EQ,
                                InternalJRVar.jrVar,
                                new IntConstant(0)),new IntConstant(1),
                                new IntConstant(0))),
                        newElse);
            }
        }
        this.hasPreviousER = elseHasPreviousER;
        return new IfThenElseStmt(a.original, a.condition, newThen,
                newElse, a.genuine, a.isByteCodeReversed, a.generalOblg);
    }


    public PrepareCoverageVisitor(ExprVisitor<Expression> exprVisitor) {
        super(exprVisitor);
    }

    public static StaticRegion execute(StaticRegion staticRegion) throws StaticRegionException {
        if (!staticRegion.ir.getMethod().getDeclaringClass().getClassLoader().getName().toString().equals("Application")) //if it not an application statement we do not want to create obligation for it.
            return staticRegion;
        PrepareCoverageExprVisitor prepareCoverageExprVisitor = new PrepareCoverageExprVisitor();
        PrepareCoverageVisitor prepareCoverageVisitor = new PrepareCoverageVisitor(prepareCoverageExprVisitor);
        Stmt newStmt = staticRegion.staticStmt.accept(prepareCoverageVisitor);
        return new StaticRegion(newStmt, staticRegion, staticRegion.earlyReturnResult);
    }
}
