package gov.nasa.jpf.symbc;

import com.ibm.wala.util.WalaException;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.symbc.branchcoverage.BranchCoverage;
import gov.nasa.jpf.symbc.branchcoverage.CoverageMode;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.SpfUtil;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

import java.io.IOException;

import static gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr.*;

public class VeriBranchListener extends BranchListener {

    public VeriBranchListener(Config conf, JPF jpf) {
        super(conf, jpf);
    }


    public void executeInstruction(VM vm, ThreadInfo ti, Instruction instructionToExecute) {
        /*if (coverageMode == CoverageMode.COLLECT_PRUNE || coverageMode == CoverageMode.COLLECT_PRUNE_GUIDE) // pruning only in pruning mode
            if (allObligationsCovered) {
                if (!evaluationMode)
                    System.out.println("all obligation covered, ignoring all paths.");
                ti.getVM().getSystemState().setIgnored(true);
                return;
            }
*/
        try {
            if (firstTime) {
                System.out.println("---- CoverageMode = " + coverageMode + ", solver = " + solver + ", benchmark= " + benchmarkName + (System.getenv("MAX_STEPS") != null ? ", STEPS " + System.getenv("MAX_STEPS") : ""));
                BranchCoverage.createObligations(ti);
                ObligationMgr.finishedCollection();
                BranchCoverage.finishedCollection();
                firstTime = false;
                timeZero = System.currentTimeMillis();
                if (!evaluationMode) {
                    printCoverage();
                    printReachability();
                    printOblgToBBMap();
                }
                System.out.println("|-|-|-|-|-|-|-|-|-|-|-|-finished obligation collection|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-|-");
            } /*else {
                if (instructionToExecute instanceof IfInstruction) {
                    isSymBranchInst = SpfUtil.isSymCond(ti, instructionToExecute);
                    if (isSymBranchInst) {
                        if (coverageMode != CoverageMode.COLLECT_COVERAGE) prunOrGuideSPF(ti, instructionToExecute);
                    } //concrete branch will be pruned in instructionExecuted.
                }
            }*/
        } catch (ClassHierarchyException e) {
            e.printStackTrace();
            assert false;
        } catch (IOException | CallGraphBuilderCancelException | WalaException e) {
            e.printStackTrace();
        }
    }

}
