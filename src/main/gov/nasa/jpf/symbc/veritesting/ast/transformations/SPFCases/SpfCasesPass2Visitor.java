package gov.nasa.jpf.symbc.veritesting.ast.transformations.SPFCases;

import gov.nasa.jpf.symbc.branchcoverage.obligation.Obligation;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;
import gov.nasa.jpf.symbc.veritesting.ast.def.*;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.Environment.DynamicRegion;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.AstVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.ExprMapVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.visitors.StmtPrintVisitor;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.CoverageCriteria;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.CollectOblgInSide;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.VeriObligationMgr;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.Operation;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static gov.nasa.jpf.symbc.VeritestingListener.coverageCriteria;
import static gov.nasa.jpf.symbc.VeritestingListener.verboseVeritesting;
import static gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide.ELSE;
import static gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationSide.THEN;
import static za.ac.sun.cs.green.expr.Operation.Operator.OR;


/**
 * This is the second pass of the SPFCases where the SPFCases nodes are eliminated and instead the dynamic region is populated with the predicates for the SPFCases.
 * Some optimization are done here to maintain a minimal RangerIR for the dynamic Region.
 */

public class SpfCasesPass2Visitor implements AstVisitor<Stmt> {
    private Expression spfCondition = Operation.TRUE;
    private final LinkedHashSet<SPFCaseStmt> spfCaseSet = new LinkedHashSet<>();
    private Expression earlyReturnCondition = null;

    //these are the list of obligations that lay in an SPFCase path, we need to take these out, as their coverage is no longer controlled by the summary but instead it should be collected by SPF
    private List<Obligation> spfcasesOblgList = new ArrayList<>();


    public List<Obligation> getSpfcasesOblgList() {
        return spfcasesOblgList;
    }

    @Override
    public Stmt visit(AssignmentStmt a) {
        return new AssignmentStmt(a.lhs, a.rhs);
    }

    @Override
    public Stmt visit(CompositionStmt a) {

        Stmt s1 = a.s1.accept(this);
        Stmt s2 = a.s2.accept(this);

        if ((s1 instanceof SPFCaseStmt) && (s2 instanceof SPFCaseStmt)) {
            spfCaseSet.remove((SPFCaseStmt) s1);
            spfCaseSet.remove((SPFCaseStmt) s2);
            SPFCaseStmt stmt = new SPFCaseStmt(spfCondition, SPFCaseStmt.SPFReason.MULTIPLE);
            spfCaseSet.add(stmt);
            return stmt;
        }

        if (s1 instanceof SPFCaseStmt)
            return s1;
        else if (s2 instanceof SPFCaseStmt)
            return s2;
        else
            return new CompositionStmt(s1, s2);
    }

    @Override
    public Stmt visit(IfThenElseStmt a) {
        Stmt s;
        Expression oldSPFCondition = spfCondition;
        spfCondition = new Operation(Operation.Operator.AND, spfCondition, a.condition);
        Stmt thenStmt = a.thenStmt.accept(this);
        spfCondition = new Operation(Operation.Operator.AND, oldSPFCondition, new Operation(Operation.Operator.NOT, a.condition));
        Stmt elseStmt = a.elseStmt.accept(this);
        if ((thenStmt instanceof SPFCaseStmt) && (elseStmt instanceof SPFCaseStmt)) { //attempting to collapse unnecessary nodes
            if (coverageCriteria == CoverageCriteria.BRANCHCOVERAGE) { // we need to keep track of obligation statements that are on the path for spf, we should take them out of the summary as their satisfaction is incomplete if one would look only at the summary on this path
                addOblgToSPF(a, THEN);
                addOblgToSPF(a, ELSE);
            }
            s = new SPFCaseStmt(oldSPFCondition, SPFCaseStmt.SPFReason.MULTIPLE);
            spfCaseSet.remove(thenStmt);
            spfCaseSet.remove(elseStmt);
            spfCaseSet.add((SPFCaseStmt) s);
        } else if (thenStmt instanceof SPFCaseStmt) {
            s = elseStmt;
            addOblgToSPF(a, THEN);
        } else if (elseStmt instanceof SPFCaseStmt) {
            s = thenStmt;
            addOblgToSPF(a, ELSE);
        } else
            s = new IfThenElseStmt(a.original, a.condition, thenStmt, elseStmt, a.genuine, a.isByteCodeReversed, a.generalOblg);
        spfCondition = oldSPFCondition;
        return s;
    }

    //adds the obligations that SPF going to cover in the list, so we can take them out of the summary - we basically want to ignore any obligation down that path
    private void addOblgToSPF(IfThenElseStmt a, ObligationSide side) {
        Obligation thenOblg;
        Obligation elseOblg;
        boolean conditionReverseStatus = a.isByteCodeReversed;

        if (a.condition instanceof ComplexExpr) {
            conditionReverseStatus = ((ComplexExpr) a.condition).isRevered;
            if (conditionReverseStatus) {
                thenOblg = VeriObligationMgr.createOblgFromGeneral(((ComplexExpr) a.condition).generalOblg, ObligationSide.ELSE);
                elseOblg = VeriObligationMgr.createOblgFromGeneral(((ComplexExpr) a.condition).generalOblg, ObligationSide.THEN);
            } else {
                elseOblg = VeriObligationMgr.createOblgFromGeneral(((ComplexExpr) a.condition).generalOblg, ObligationSide.ELSE);
                thenOblg = VeriObligationMgr.createOblgFromGeneral(((ComplexExpr) a.condition).generalOblg, ObligationSide.THEN);
            }
        } else {
            if (a.generalOblg == null) { //in case of jrInternal variable for early returns
                //this assert is not valid at that point because we could hae created if statements from the array transformation, it is however true when we are first instantiating the region before any array transformation takes place
//                assert a.condition instanceof Operation && ((Operation) a.condition).getArity() == 2 && (((Operation) a.condition).getOperand(0) instanceof InternalJRVar || ((Operation) a.condition).getOperand(1) instanceof InternalJRVar) : "condition is not in the form of internalJR vairable, the only case where general obligation is expected to be null";
                return;
            }
            if (conditionReverseStatus) {
                thenOblg = VeriObligationMgr.createOblgFromGeneral(a.generalOblg, ObligationSide.ELSE);
                elseOblg = VeriObligationMgr.createOblgFromGeneral(a.generalOblg, ObligationSide.THEN);
            } else {
                elseOblg = VeriObligationMgr.createOblgFromGeneral(a.generalOblg, ObligationSide.ELSE);
                thenOblg = VeriObligationMgr.createOblgFromGeneral(a.generalOblg, ObligationSide.THEN);
            }
        }
        if (side == THEN)
            spfcasesOblgList.add(thenOblg);
        else
            spfcasesOblgList.add(elseOblg);


        CollectOblgInSide collectOblgInSide = new CollectOblgInSide(new ExprMapVisitor());

        if (side == THEN)
            a.thenStmt.accept(collectOblgInSide);
        else
            a.elseStmt.accept(collectOblgInSide);

        spfcasesOblgList.addAll(collectOblgInSide.getSpfcasesOblgList());
    }


    @Override
    public Stmt visit(SkipStmt a) {
        return SkipStmt.skip;
    }

    @Override
    public Stmt visit(SPFCaseStmt c) {
        if (c.reason != SPFCaseStmt.SPFReason.EARLYRETURN) {
            spfCaseSet.add(c);
            return new SPFCaseStmt(c.spfCondition, c.reason);
        } else {//collect the condition into the region's returnResultCondition, and replace it with a skip
            if (earlyReturnCondition == null) //initial condition
                earlyReturnCondition = c.spfCondition;
            else
                earlyReturnCondition = new Operation(OR, earlyReturnCondition, c.spfCondition);
            return SkipStmt.skip;
        }

    }

    @Override
    public Stmt visit(ArrayLoadInstruction c) {
        return c;
    }

    @Override
    public Stmt visit(ArrayStoreInstruction c) {
        return c;
    }

    @Override
    public Stmt visit(SwitchInstruction c) {
        return new SwitchInstruction(c.getOriginal());
    }

    @Override
    public Stmt visit(ReturnInstruction c) {
        return new ReturnInstruction(c.getOriginal(),
                c.rhs);
    }

    @Override
    public Stmt visit(GetInstruction c) {
        return new GetInstruction(c.getOriginal(),
                c.def,
                c.ref,
                c.field);
    }

    @Override
    public Stmt visit(PutInstruction c) {
        return new PutInstruction(c.getOriginal(),
                c.def,
                c.field,
                c.assignExpr);
    }

    @Override
    public Stmt visit(NewInstruction c) {
        return c;
    }

    @Override
    public Stmt visit(InvokeInstruction c) {
        return new InvokeInstruction(c.getOriginal(),
                c.result,
                c.params);
    }

    @Override
    public Stmt visit(ArrayLengthInstruction c) {
        return new ArrayLengthInstruction(c.getOriginal(), c.arrayref, c.def);
    }

    @Override
    public Stmt visit(ThrowInstruction c) {
        return c;
    }

    @Override
    public Stmt visit(CheckCastInstruction c) {
        return new CheckCastInstruction(
                c.getOriginal(),
                c.result,
                c.val,
                c.declaredResultTypes);
    }

    @Override
    public Stmt visit(InstanceOfInstruction c) {
        return new InstanceOfInstruction(c.getOriginal(),
                c.result,
                c.val,
                c.checkedType);
    }

    @Override
    public Stmt visit(PhiInstruction c) {
        return new PhiInstruction(c.getOriginal(),
                c.def,
                c.rhs);
    }

    @Override
    public Stmt visit(StoreGlobalInstruction c) {
        assert false : "StoreGlobalInstruction cannot appear in spfcases. Failing.";
        return null;
    }

    /**
     * This executes the second path of the SPFCases. It removes all SPFCases nodes and creates a new condition instead onto the spfCaseSet. There is still one more step that SPFCases needs to go through which is actually generating a green expression out of the spfCaseSet, this happens seperately after the green transformation is done.
     *
     * @param dynRegion Dynamic region for which SPFCases nodes are going to be removed from the AST and replaced with a condition onto the spfCaseSet instead.
     * @return Dynamic Region with a new AST and spfCaseSet populated.
     */
    public static Pair<DynamicRegion, List<Obligation>> execute(DynamicRegion dynRegion) {

        SpfCasesPass2Visitor visitor = new SpfCasesPass2Visitor();
        Stmt dynStmt = dynRegion.dynStmt.accept(visitor);

        if (verboseVeritesting) {
            System.out.println("--------------- SPFCases TRANSFORMATION 2ND PASS ---------------");
            System.out.println(StmtPrintVisitor.print(dynStmt));
        }

        SPFCaseList detectedCases = new SPFCaseList(visitor.spfCaseSet);
        if (verboseVeritesting)
            detectedCases.print();

        dynRegion.earlyReturnResult.condition = visitor.earlyReturnCondition;

        if (verboseVeritesting)
            System.out.println("printing early return result condition after spfcases2: " + visitor.earlyReturnCondition);
        return new Pair(new DynamicRegion(dynRegion,
                dynStmt,
                detectedCases, null, null, dynRegion.earlyReturnResult), visitor.spfcasesOblgList);
    }
}
