package gov.nasa.jpf.symbc;


import com.ibm.wala.ipa.callgraph.CallGraphBuilderCancelException;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.util.WalaException;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.BranchCoverage;
import gov.nasa.jpf.symbc.veritesting.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.vm.*;

import java.io.*;

public class BranchListener extends PropertyListenerAdapter implements PublisherExtension {

    public static String targetClass;
    public static String targetAbsPath;
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
            BranchCoverage.createObligations(ti);
            ObligationMgr.finishedCollection();
        } catch (ClassHierarchyException e) {
            e.printStackTrace();
            assert false;
        } catch (IOException | CallGraphBuilderCancelException | WalaException e) {
            e.printStackTrace();
        }
    }


}
