/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * Symbolic Pathfinder (jpf-symbc) is licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//
//Copyright (C) 2007 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
package gov.nasa.jpf.symbc.sequences;

// does not work well for static methods:summary not printed for errors
// SH: Code copied from SymbolicSequenceListener with some modification for branch coverage test case generation for Veritesting.

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.Property;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction;
import gov.nasa.jpf.report.ConsolePublisher;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.BranchListener;
import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.symbc.branchcoverage.TestCaseGenerationMode;
import gov.nasa.jpf.symbc.bytecode.BytecodeUtils;
import gov.nasa.jpf.symbc.bytecode.INVOKESTATIC;
import gov.nasa.jpf.symbc.concolic.PCAnalyzer;
import gov.nasa.jpf.symbc.numeric.*;
import gov.nasa.jpf.symbc.string.StringSymbolic;
import gov.nasa.jpf.vm.*;

import java.io.PrintWriter;
import java.util.*;

/**
 * @author Mithun Acharya
 * with inputs from Corina.
 * <p>
 * Note that all the methods of interest should be specified in +symbolic.method option.
 * if a method is not specified in +symbolic.method it will not be printed.
 * even if the method, foo(int i) is invoked concretely always, we should have
 * foo(con) in +symbolic.method option
 * <p>
 * Algorithm (Works independent of search order):
 * <p>
 * When instructionExecuted->JVMInvokeInstruction, remember the executed method, symbolic attributes, etc.
 * in a SequenceChoiceGenerator
 * <p>
 * The main idea is to exploit the fact that
 * "at each state, the path from start state to the current state has a
 * unique chain of choice generators"
 * <p>
 * During stateBacktracked or propertyViolated, get the chain of choice generators. In this chain look
 * for SequenceChoiceGenerators (which hold information about symbolically executed methods).
 * With the current path condition solution and the symbolic attributes
 * stored in SequenceChoiceGenerators, output the concrete method sequence.
 * <p>
 * <p>
 * KNOWN PROBLEMS:
 * <p>
 * 1) For JUnit test cases, getting class name and object name is not smart.
 */
public class VeriSymbolicSequenceListener extends PropertyListenerAdapter implements PublisherExtension {


    // this set will store all the method sequences.
    // will be printed at last.
    // 'methodSequences' is a set of 'methodSequence's
    // A single 'methodSequence' is a vector of invoked 'method's along a path
    // A single invoked 'method' is represented as a String.
    static Set<Vector> methodSequences = new LinkedHashSet<Vector>();

    // Name of the class under test
    String className = "";

    // custom marker to mark error strings in method sequences
    private final static String exceptionMarker = "##EXCEPTION## ";

    public VeriSymbolicSequenceListener(Config conf, JPF jpf) {
        jpf.addPublisherExtension(ConsolePublisher.class, this);
    }

    /* SH: commenting this out for now.
    //    @Override
    public void propertyViolated(Search search, Map<String, Object> veriSolutionMap) {
        System.out.println("--------->property violated");
        VM vm = search.getVM();
        SystemState ss = vm.getSystemState();
        ChoiceGenerator<?> cg = vm.getChoiceGenerator();
        if (!(cg instanceof PCChoiceGenerator)) {
            ChoiceGenerator<?> prev_cg = cg.getPreviousChoiceGenerator();
            while (!((prev_cg == null) || (prev_cg instanceof PCChoiceGenerator))) {
                prev_cg = prev_cg.getPreviousChoiceGenerator();
            }
            cg = prev_cg;
        }
        Property prop = search.getLastError().getProperty();
        String errAnn = "";
        if (prop instanceof NoUncaughtExceptionsProperty) {
            String exceptionClass = ((NoUncaughtExceptionsProperty) prop).getUncaughtExceptionInfo().getExceptionClassname();
            errAnn = "(expected = " + exceptionClass + ".class)";
        }

        String error = search.getLastError().getDetails();
        error = "\"" + error.substring(0, error.indexOf("\n")) + "...\"";

        if ((cg instanceof PCChoiceGenerator) && ((PCChoiceGenerator) cg).getCurrentPC() != null) {

            PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
            System.out.println("pc " + pc.count() + " " + pc);

            //solve the path condition
            if (SymbolicInstructionFactory.concolicMode) { //TODO: cleaner
                SymbolicConstraintsGeneral solver = new SymbolicConstraintsGeneral();
                PCAnalyzer pa = new PCAnalyzer();
                pa.solve(pc, solver);
            } else pc.solve();

            // get the chain of choice generators.
            ChoiceGenerator<?>[] cgs = ss.getChoiceGenerators();
            Vector<String> methodSequence = getMethodSequence(cgs, veriSolutionMap);
            // Now append the error String and then add methodSequence to methodSequences
            // prefix the exception marker to distinguish this from
            // an invoked method.
            if (errAnn != "") methodSequence.add(0, errAnn);
            methodSequence.add(exceptionMarker + error);
            methodSequences.add(methodSequence);
        }
    }
*/

    @Override
    public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {


        if (!vm.getSystemState().isIgnored()) {
            Instruction insn = executedInstruction;
            SystemState ss = vm.getSystemState();
            ThreadInfo ti = currentThread;
            Config conf = vm.getConfig();

            if (insn instanceof JVMInvokeInstruction && insn.isCompleted(ti)) {
                JVMInvokeInstruction md = (JVMInvokeInstruction) insn;
                String methodName = md.getInvokedMethodName();
                int numberOfArgs = md.getArgumentValues(ti).length;
                MethodInfo mi = md.getInvokedMethod();

                StackFrame sf = ti.getTopFrame();
                String shortName = methodName;
                //String longName = mi.getLongName();
                if (methodName.contains("(")) shortName = methodName.substring(0, methodName.indexOf("("));
                // does not work for recursive invocations of sym methods; should compare MethodInfo instead
                //if(!shortName.equals(sf.getMethodName()))
                //return;
                if (!mi.equals(sf.getMethodInfo())) return;

                if ((BytecodeUtils.isMethodSymbolic(conf, mi.getFullName(), numberOfArgs, null))) {

                    // FIXME: get the object name?
                    // VirtualInvocation virtualInvocation = (VirtualInvocation)insn;
                    // int ref = virtualInvocation.getThis(ti);
                    // DynamicElementInfo d = ss.ks.da.get(ref);
                    // right now I am just getting the class name
                    className = mi.getClassName();


                    // get arg values
                    Object[] argValues = md.getArgumentValues(ti);

                    // get symbolic attributes
                    // concretely executed method will have null attributes.
                    // TODO: fix there

                    byte[] argTypes = mi.getArgumentTypes();
                    Object[] attributes = new Object[numberOfArgs];

                    int count = 1; // we do not care about this
                    if (md instanceof INVOKESTATIC) count = 0;  //no "this" reference
                    for (int i = 0; i < numberOfArgs; i++) {
                        attributes[i] = sf.getLocalAttr(count);
                        count++;
                        if (argTypes[i] == Types.T_LONG || argTypes[i] == Types.T_DOUBLE) count++;
                    }

                    // Create a new SequenceChoiceGenerator.
                    // this is just to store the information
                    // regarding the executed method.
                    SequenceChoiceGenerator cg = new SequenceChoiceGenerator(shortName);
                    cg.setArgValues(argValues);
                    cg.setArgAttributes(attributes);
                    // Does not actually make any choice
                    ss.setNextChoiceGenerator(cg);
                    // nothing to do as there are no choices.
                }
            }
            //else if (insn instanceof JVMReturnInstruction){
            // I don't think we need to do anything  here for printing method sequences...
            //}
        }

    }


    public static void collectVeriTests(VM vm, Map<String, Object> veriSolutionMap) {
/*        Config conf = vm.getConfig();

        Instruction insn = vm.getChoiceGenerator().getInsn();*/
        SystemState ss = vm.getSystemState();
        /*//ThreadInfo ti = vm.getChoiceGenerator().getThreadInfo();
        MethodInfo mi = insn.getMethodInfo();
        String methodName = mi.getFullName();

        int numberOfArgs = mi.getNumberOfArguments();//mi.getArgumentsSize()- 1;// corina: problem here? - 1;

        //	if (BytecodeUtils.isMethodSymbolic(conf, methodName, numberOfArgs, null)){
*/
        ChoiceGenerator<?> cg = vm.getChoiceGenerator();

        if (!(cg instanceof PCChoiceGenerator)) {
            ChoiceGenerator<?> prev_cg = cg.getPreviousChoiceGenerator();
            while (!((prev_cg == null) || (prev_cg instanceof PCChoiceGenerator))) {
                prev_cg = prev_cg.getPreviousChoiceGenerator();
            }
            cg = prev_cg;
        }

        if ((cg instanceof PCChoiceGenerator) && ((PCChoiceGenerator) cg).getCurrentPC() != null) {

//            PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
            //solve the path condition
				/*if (SymbolicInstructionFactory.concolicMode) { //TODO: cleaner
					SymbolicConstraintsGeneral solver = new SymbolicConstraintsGeneral();
					PCAnalyzer pa = new PCAnalyzer();
					pa.solve(pc,solver);
				}
				else
					pc.solve();*/
            // get the chain of choice generators.
            ChoiceGenerator<?>[] cgs = ss.getChoiceGenerators();
            methodSequences.add(getMethodSequence(cgs, veriSolutionMap));
        }
        //	}
    }


    /**
     * traverses the ChoiceGenerator chain to get the method sequence
     * looks for SequenceChoiceGenerator in the chain
     * SequenceChoiceGenerators have information about the methods
     * executed and hence the method sequence can be obtained.
     * A single 'methodSequence' is a vector of invoked 'method's along a path
     * A single invoked 'method' is represented as a String.
     */
    private static Vector<String> getMethodSequence(ChoiceGenerator[] cgs, Map<String, Object> veriSolutionMap) {
        // A method sequence is a vector of strings
        Vector<String> methodSequence = new Vector<String>();
        ChoiceGenerator cg = null;
        if (BranchListener.testCaseGenerationMode == TestCaseGenerationMode.UNIT_LEVEL)
            // explore the choice generator chain - unique for a given path.
            for (int i = 0; i < cgs.length; i++) {
                cg = cgs[i];
                if ((cg instanceof SequenceChoiceGenerator)) {
                    methodSequence.add(getInvokedMethod((SequenceChoiceGenerator) cg, veriSolutionMap));
                }
            }
        else if (BranchListener.testCaseGenerationMode == TestCaseGenerationMode.SYSTEM_LEVEL) methodSequence.add(getInvokedMethod((SequenceChoiceGenerator) cgs[0], veriSolutionMap));

        return methodSequence;
    }


    /**
     * A single invoked 'method' is represented as a String.
     * information about the invoked method is got from the SequenceChoiceGenerator
     */
    private static String getInvokedMethod(SequenceChoiceGenerator cg, Map<String, Object> solutionMap) {
        String invokedMethod = "";

        // get method name
        String shortName = cg.getMethodShortName();
        invokedMethod += shortName + "(";

        // get argument values
        Object[] argValues = cg.getArgValues();

        // get number of arguments
        int numberOfArgs = argValues.length;

        // get symbolic attributes
        Object[] attributes = cg.getArgAttributes();

        // get the solution
        for (int i = 0; i < numberOfArgs; i++) {
            Object attribute = attributes[i];
            if (attribute != null) { // parameter symbolic
                // here we should consider different types of symbolic arguments
                //IntegerExpression e = (IntegerExpression)attributes[i];
                Object e = attributes[i];
                String solution = "";


                if (e instanceof IntegerExpression) {
                    // trick to print bools correctly
                    if (argValues[i].toString() == "true" || argValues[i].toString() == "false") {
                        if ((int) solutionMap.get(e) == 0) solution = solution + "false";
                        else solution = solution + "true";
                    } else solution = solution + solutionMap.get(e);
                } else assert false : "unsupported type";

                invokedMethod += solution + ",";
            } else { // parameter concrete - for a concrete parameter, the symbolic attribute is null
                invokedMethod += argValues[i] + ",";
            }
        }

        // remove the extra comma
        if (invokedMethod.endsWith(",")) invokedMethod = invokedMethod.substring(0, invokedMethod.length() - 1);
        invokedMethod += ")";

        return invokedMethod;
    }


    /**
     * traverses the ChoiceGenerator chain to get the method sequence
     * looks for SequenceChoiceGenerator in the chain
     * SequenceChoiceGenerators have information about the methods
     * executed and hence the method sequence can be obtained.
     * A single 'methodSequence' is a vector of invoked 'method's along a path
     * A single invoked 'method' is represented as a String.
     */
    public static List<SymbolicInteger> getMethodAttributes(ChoiceGenerator[] cgs) {
        // A method sequence is a vector of strings
        Vector<SymbolicInteger> methodSequence = new Vector<SymbolicInteger>();
        ChoiceGenerator cg = null;
        if (BranchListener.testCaseGenerationMode == TestCaseGenerationMode.UNIT_LEVEL)
            // explore the choice generator chain - unique for a given path.
            for (int i = 0; i < cgs.length; i++) {
                cg = cgs[i];
                if ((cg instanceof SequenceChoiceGenerator)) methodSequence.addAll(getInvokedMethodAttributes((SequenceChoiceGenerator) cg));
            }
        else if (BranchListener.testCaseGenerationMode == TestCaseGenerationMode.SYSTEM_LEVEL) methodSequence.addAll(getInvokedMethodAttributes((SequenceChoiceGenerator) cgs[0]));
        else return methodSequence;
        return methodSequence;
    }

    /*
     */
/**
 * This gets only the attributes of the last choice.
 * @param cg
 * @return
 *//*

    private static Collection<? extends SymbolicInteger> getInvokedSystemMethodAttributes(SequenceChoiceGenerator cg) {
        return Arrays.asList((SymbolicInteger) cg.getArgAttributes()[0]);
    }
*/


    /**
     * A single invoked 'method' is represented as a String.
     * information about the invoked method is got from the SequenceChoiceGenerator
     *
     * @return
     */
    private static List<SymbolicInteger> getInvokedMethodAttributes(SequenceChoiceGenerator cg) {

        SymbolicInteger[] attributeNames = new SymbolicInteger[cg.getArgAttributes().length];

        for (int i = 0; i < cg.getArgAttributes().length; i++) {
            assert cg.getArgAttributes()[i] instanceof SymbolicInteger;
            attributeNames[i] = (SymbolicInteger) cg.getArgAttributes()[i];
        }

        return Arrays.asList(attributeNames);
    }


    //	-------- the publisher interface
    public void publishFinished(Publisher publisher) {


        PrintWriter pw = publisher.getOut();
        // here just print the method sequences
        publisher.publishTopicStart("Method Sequences");
        printMethodSequences(pw);

        // print JUnit4.0 test class
        publisher.publishTopicStart("JUnit 4.0 test class");
        printJUnitTestClass(pw);

    }


    /**
     * @author Mithun Acharya
     * <p>
     * prints the method sequences
     */
    private void printMethodSequences(PrintWriter pw) {
        Iterator<Vector> it = methodSequences.iterator();
        while (it.hasNext()) {
            pw.println(it.next());
        }
    }


    /**
     * @author Mithun Acharya
     * Dumb printing of JUnit 4.0 test class
     * FIXME: getting class name and object name is not smart.
     */
    private void printJUnitTestClass(PrintWriter pw) {
        // imports
        pw.println("import static org.junit.Assert.*;");
        pw.println("import org.junit.Before;");
        pw.println("import org.junit.Test;");

        String objectName = (className.toLowerCase()).replace(".", "_");

        pw.println();
        pw.println("public class " + className.replace(".", "_") + "Test {"); // test class
        pw.println();
        pw.println("	private " + className + " " + objectName + ";"); // CUT object to be tested
        pw.println();
        pw.println("	@Before"); // setUp method annotation
        pw.println("	public void setUp() throws Exception {"); // setUp method
        pw.println("		" + objectName + " = new " + className + "();"); // create object for CUT
        pw.println("	}"); // setUp method end
        // Create a test method for each sequence
        int testIndex = 0;
        Iterator<Vector> it = methodSequences.iterator();
        while (it.hasNext()) {
            Vector<String> methodSequence = it.next();
            pw.println();
            Iterator<String> it1 = methodSequence.iterator();
            if (it1.hasNext()) {
                String errAnn = (String) (it1.next());

                if (errAnn.contains("expected")) {
                    pw.println("	@Test" + errAnn); // Corina: added @Test annotation with exception expected
                } else {
                    pw.println("	@Test"); // @Test annotation
                    it1 = methodSequence.iterator();
                }
            } else it1 = methodSequence.iterator();

            //pw.println("	@Test"); // @Test annotation
            pw.println("	public void test" + testIndex + "() {"); // begin test method
            //Iterator<String> it1 = methodSequence.iterator();
            while (it1.hasNext()) {
                String invokedMethod = it1.next();
                if (invokedMethod.contains(exceptionMarker)) { // error-string. not a method
                    // add a comment about the exception
                    pw.println("		" + "//should lead to " + invokedMethod);
                } else { // normal method
                    pw.println("		" + objectName + "." + invokedMethod + ";"); // invoke a method in the sequence
                }
            }
            pw.println("	}"); // end test method
            testIndex++;
        }
        pw.println("}"); // test class end
    }

}
