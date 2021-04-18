package gov.nasa.jpf.symbc.branchcoverage;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.Language;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.cha.ClassHierarchy;
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
import java.util.Iterator;

import static gov.nasa.jpf.symbc.BranchListener.targetAbsPath;
import static gov.nasa.jpf.symbc.BranchListener.targetClass;
import static gov.nasa.jpf.symbc.branchcoverage.CallGraphUtil.pruneForAppLoader;

public class BranchCoverage {
    public static ClassHierarchy cha;
    public static CallGraph cg;


    public static void createObligations(ThreadInfo ti, String exclusionFilePath) throws WalaException, IOException, CallGraphBuilderCancelException {

        File exclusionFile = new File(exclusionFilePath);

        String classPath = targetAbsPath.substring(0, targetAbsPath.lastIndexOf("/"));
        AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(classPath, exclusionFile);
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
        cg = cgBuilder.makeCallGraph(analysisOptions, null);
//        System.out.println("made the class hierarchy");
        Graph<CGNode> g = pruneForAppLoader(cg);

        //I am not expecting to see multiple entery points at least for now.
//        assert cg.getEntrypointNodes().size() == 1;
        boolean specificEntryNodeFound = false;
        IR entryIR = null;
        Iterator<CGNode> entryItr = cg.getEntrypointNodes().iterator();
        while (!specificEntryNodeFound && entryItr.hasNext()) {
            CGNode node = entryItr.next();
            if (node.toString().contains(targetClass.replaceAll("\\.", "/"))) {
                entryIR = node.getIR();
                specificEntryNodeFound = true;
            }
        }

        assert entryIR != null : "entry node cannot be null. Assumption Violated. Failing.";

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
        cg = null;
    }
}
