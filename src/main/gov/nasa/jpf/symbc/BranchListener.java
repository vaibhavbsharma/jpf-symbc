package gov.nasa.jpf.symbc;


import com.ibm.wala.ipa.callgraph.CallGraphBuilderCancelException;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.util.WalaException;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.SpfUtil;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.BranchCoverage;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.ObligationSide;
import gov.nasa.jpf.vm.*;

import java.io.*;

public class BranchListener extends PropertyListenerAdapter implements PublisherExtension {

    boolean firstTime = true;

    public static String targetClass;
    public static String targetAbsPath;

    // used to flag that the executed branch instruction is symbolic or not. In which case the "instructionExecuted" should let the "firstStepInstruction" check in place, i.e., to return to spf to create
    // the appropriate set of choices, otherwise if it isn't symbolic then it will only invoke "instructionExecuted" only once, and thus we shouldn't return then, and we should check and/or collect obligations then
    public static boolean isSymBranchInst = false;

    public BranchListener(Config conf, JPF jpf) {
        if ((conf.getString("targetAbsPath") == null)) {
            System.out.println("target class or its absolute path is undefined in jpf file for coverage. Aborting");
            assert false;
        }

        targetClass = conf.getString("target");
        targetAbsPath = conf.getString("targetAbsPath");

    }

    public void executeInstruction(VM vm, ThreadInfo ti, Instruction instructionToExecute) {
        try {
            if (firstTime) {
                BranchCoverage.createObligations(ti);
                ObligationMgr.finishedCollection();
                firstTime = false;
                System.out.println(ObligationMgr.printCoverage());
                System.out.println("finished obligation collection");
            } else
                if (instructionToExecute instanceof IfInstruction)
                isSymBranchInst = SpfUtil.isSymCond(ti, instructionToExecute);

            /*else {
                if (instructionToExecute instanceof IfInstruction) {
                    //if (!ti.isFirstStepInsn() && SpfUtil.isSymCond(ti, instructionToExecute))
                    if (!ti.isFirstStepInsn())
                        return;
                    ChoiceGenerator<?> cg = ti.getVM().getSystemState().getChoiceGenerator();
                    assert (cg instanceof PCChoiceGenerator) : "expected PCChoiceGenerator, got " + cg;

                    ObligationSide oblgSide = (Integer) cg.getNextChoice() == 0 ? ObligationSide.FALSE : ObligationSide.TRUE;
                    if ((ObligationMgr.coverNgetIgnore((IfInstruction) instructionToExecute, oblgSide)))
                        ti.getVM().getSystemState().setIgnored(true);
                    System.out.println("in instruction: " + instructionToExecute);
                    System.out.println(ObligationMgr.printCoverage());
                }
            }*/
        } catch (ClassHierarchyException e) {
            e.printStackTrace();
            assert false;
        } catch (IOException | CallGraphBuilderCancelException | WalaException e) {
            e.printStackTrace();
        }
    }


    public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
        if (executedInstruction instanceof IfInstruction) {

            //used to check if we are in the case of symbolic instruction and we are hitting for the first time. As we want to only intercept
            //either symbolic instruction after isFirstStepInsn has finished or a concerte instruction
            if (!currentThread.isFirstStepInsn() && isSymBranchInst) {
                isSymBranchInst = false;
                return;
            }
            System.out.println("after execution of  instruction: " + executedInstruction);

            isSymBranchInst = false;

            ObligationSide oblgSide;
            if (((IfInstruction) executedInstruction).getTarget() == currentThread.getNextPC())
                oblgSide = ObligationSide.ELSE;
            else {
                oblgSide = ObligationSide.THEN;
                assert ((IfInstruction) executedInstruction).getNext() == currentThread.getNextPC();
            }

            if ((ObligationMgr.coverNgetIgnore((IfInstruction) executedInstruction, oblgSide))) {
                /*currentThread.getVM().getSystemState().setIgnored(true);
                currentThread.setNextPC()*/
            }
            System.out.println(ObligationMgr.printCoverage());
        }
    }

    @Override
    public void publishFinished(Publisher publisher) {
        PrintWriter pw = publisher.getOut();
        publisher.publishTopicStart("Branch Coverage report:");

        pw.println(ObligationMgr.printCoverage());
    }
}
