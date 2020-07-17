package gov.nasa.jpf.symbc.branchcoverage;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.Language;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.graph.Graph;
import gov.nasa.jpf.symbc.branchcoverage.obligation.CoverageUtil;
import gov.nasa.jpf.symbc.branchcoverage.obligation.ObligationMgr;
import gov.nasa.jpf.vm.ThreadInfo;

import java.io.File;
import java.io.IOException;

import static gov.nasa.jpf.symbc.BranchListener.targetAbsPath;
import static gov.nasa.jpf.symbc.branchcoverage.CallGraphUtil.pruneForAppLoader;

public class BranchCoverage {
    public static ClassHierarchy cha;


    public static void createObligations(ThreadInfo ti) throws WalaException, IOException, CallGraphBuilderCancelException {

        File exclusionFile = new File("../coverageExclusions.txt");

        AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(targetAbsPath, exclusionFile);
        cha = ClassHierarchyFactory.make(scope);
        Iterable<Entrypoint> entryPoints = Util.makeMainEntrypoints(scope, cha);

        /*List<Entrypoint> filteredEntrypoints = new ArrayList<>();
        for (Entrypoint entry : entryPoints) {
            if (entry.getMethod().getSignature().contains(targetClass))
                filteredEntrypoints.add(entry);
        }
*/
        AnalysisOptions analysisOptions = new AnalysisOptions(scope, entryPoints);
        IAnalysisCacheView cache = new AnalysisCacheImpl(analysisOptions.getSSAOptions());
        CallGraphBuilder cgBuilder = Util.makeZeroCFABuilder(Language.JAVA, analysisOptions, cache, cha, scope);
        CallGraph cg = cgBuilder.makeCallGraph(analysisOptions, null);
//        System.out.println("made the class hierarchy");
        Graph<CGNode> g = pruneForAppLoader(cg);

        //I am not expecting to see multiple entery points at least for now.
        assert cg.getEntrypointNodes().size() == 1;

        IR entryIR = cg.getEntrypointNodes().iterator().next().getIR(); //getting the first entry point
        SSAInstruction[] instructions = entryIR.getInstructions();
        IMethod m = entryIR.getMethod();

        String walaPackageName = CoverageUtil.getWalaPackageName(m);
        String className = m.getDeclaringClass().getName().getClassName().toString();
        String methodSignature = m.getSelector().toString();
        BranchOblgCollectorVisitor branchOblgCollectorVisitor = null;
        for (int irInstIndex = 0; irInstIndex < instructions.length; irInstIndex++) {
            SSAInstruction ins = instructions[irInstIndex];
            if (ins != null) {
                if (branchOblgCollectorVisitor == null)
                    branchOblgCollectorVisitor = new BranchOblgCollectorVisitor(entryIR, walaPackageName, className, methodSignature, m, irInstIndex);
                else
                    branchOblgCollectorVisitor.updateInstIndex(irInstIndex);
                ins.visit(branchOblgCollectorVisitor);
            }
        }
        ObligationMgr.finishedCollection();
    }

    public static void finishedCollection() {
        cha = null;
        BranchOblgCollectorVisitor.finishedCollection();
    }
}
