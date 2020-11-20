/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * The Java Pathfinder core (jpf-core) platform is licensed under the
 * Apache License, Version 2.0 (the "License"); you may not use this file except
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

// author Aymeric Fromherz aymeric.fromherz@ens.fr 

package gov.nasa.jpf.symbc.bytecode.symjrarrays;

import gov.nasa.jpf.symbc.arrays.ArrayExpression;
import gov.nasa.jpf.symbc.numeric.*;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;
import gov.nasa.jpf.symbc.veritesting.ast.def.AssignmentStmt;
import gov.nasa.jpf.symbc.veritesting.ast.def.GammaVarExpr;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.AstToGreen.AstToGreenVisitor;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.arrayaccess.ArrayUtil;
import gov.nasa.jpf.vm.*;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.Operation;

import static gov.nasa.jpf.symbc.bytecode.symjrarrays.ArrayUtil.getNewElementVarName;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.*;
import static za.ac.sun.cs.green.expr.Operation.Operator.EQ;

/**
 * Store into byte or boolean array
 * ..., arrayref, index, value  => ...
 */
public class CASTORE extends gov.nasa.jpf.jvm.bytecode.CASTORE {

	 @Override
	  public Instruction execute (ThreadInfo ti) {
         StackFrame frame = ti.getModifiableTopFrame();

         int arrayRef = peekArrayRef(ti); // need to be polymorphic, could be LongArrayStore

         ElementInfo arrayInfo = ti.getModifiableElementInfo(arrayRef);
         int arrayLen = ((ArrayFields) arrayInfo.getArrayFields()).arrayLength();
         Object symStoreVal = frame.getOperandAttr(0);
         Object symIndex = peekIndexAttr(ti);
         boolean isSymbolicIndex = symIndex != null && symIndex instanceof IntegerExpression;
         boolean isSymbolicValue = symStoreVal != null && symStoreVal instanceof IntegerExpression;

         if (arrayRef == MJIEnv.NULL)
             return ti.createAndThrowException("java.lang.NullPointerException");

         // If only the value is symbolic, we use the concrete instruction
         assert (peekArrayAttr(ti) == null || !(peekArrayAttr(ti) instanceof ArrayExpression)) : "unsupported symbolic array object. Failing.";
         //if the value to store is not symbolic and neither is the index, then execute concretely
         if (!isSymbolicValue && (!isSymbolicIndex))
             return super.execute(ti);
//             return executeConcretely(ti, arrayInfo);


         ChoiceGenerator<?> cg;

         if (!ti.isFirstStepInsn()) { // first time around
             if (isSymbolicIndex) {
                 cg = new PCChoiceGenerator(3);
                 ((PCChoiceGenerator) cg).setOffset(this.position);
                 ((PCChoiceGenerator) cg).setMethodName(this.getMethodInfo().getFullName());
                 ti.getVM().setNextChoiceGenerator(cg);
                 return this;
             }
         }


         if (isSymbolicIndex) {

             // this is what really returns results
             cg = ti.getVM().getChoiceGenerator();
             assert (cg instanceof PCChoiceGenerator) : "expected PCChoiceGenerator, got: " + cg;


             PathCondition pc;
             ChoiceGenerator<?> prev_cg = cg.getPreviousChoiceGeneratorOfType(PCChoiceGenerator.class);

             if (prev_cg == null)
                 pc = new PathCondition();
             else
                 pc = ((PCChoiceGenerator) prev_cg).getCurrentPC();

             assert pc != null;

             if ((Integer) cg.getNextChoice() == 0) { // check bounds of the index
                 pc._addDet(Comparator.GE, (IntegerExpression) symIndex, arrayLen);
                 if (pc.simplify()) { // satisfiable
                     ((PCChoiceGenerator) cg).setCurrentPC(pc);
                     return ti.createAndThrowException("java.lang.ArrayIndexOutOfBoundsException", "index greater than array bounds");
                 } else {
                     ti.getVM().getSystemState().setIgnored(true);
                     return getNext(ti);
                 }
             } else if ((Integer) cg.getNextChoice() == 1) {
                 pc._addDet(Comparator.LT, (IntegerExpression) symIndex, new IntegerConstant(0));
                 if (pc.simplify()) { // satisfiable
                     ((PCChoiceGenerator) cg).setCurrentPC(pc);
                     return ti.createAndThrowException("java.lang.ArrayIndexOutOfBoundsException", "index smaller than array bounds");
                 } else {
                     ti.getVM().getSystemState().setIgnored(true);
                     return getNext(ti);
                 }
             } else {
                 pc._addDet(Comparator.LT, (IntegerExpression) symIndex, arrayLen);
                 pc._addDet(Comparator.GE, (IntegerExpression) symIndex, new IntegerConstant(0));
                 if (pc.simplify()) { // satisfiable
                     ((PCChoiceGenerator) cg).setCurrentPC(pc);
                     if (!isSymbolicValue) {
                         char value = (char)frame.pop();
                         symStoreVal = new IntegerConstant(value);
                     } else
                         frame.pop();

                     for (int i = 0; i < arrayLen; i++) {
                         Pair<za.ac.sun.cs.green.expr.Expression, String> arrayElementAttrOrVal = ArrayUtil.getArrayElement(arrayInfo, i);
                         za.ac.sun.cs.green.expr.Expression spfSymStoreVal = SPFToGreenExpr((Expression) symStoreVal);
                         assert spfSymStoreVal != null : "spf symbolic value cannot be null if was converted, something is wrong. Failing.";
                         assert arrayElementAttrOrVal != null : "array element attribute or value cannot be null, something is wrong. Failing.";

                         za.ac.sun.cs.green.expr.Expression greenVar = createGreenVar(arrayInfo.getType(), getNewElementVarName());
                         AssignmentStmt stmt = new AssignmentStmt(greenVar, new GammaVarExpr(new Operation(EQ, ExprUtil.SPFToGreenExpr((IntegerExpression) symIndex), new IntConstant(i)), spfSymStoreVal, arrayElementAttrOrVal.getFirst()));
                         pc._addDet(new GreenConstraint(stmt.accept(new AstToGreenVisitor())));
                         arrayInfo.setElementAttr(i, greenToSPFExpression(greenVar));
                     }
                     // We create a new arrayAttr, and inherits information from the previous attribute
                     frame.pop(2); // We pop the array and the index

                     return getNext(ti);
                 } else {
                     ti.getVM().getSystemState().setIgnored(true);
                     return getNext(ti);
                 }
             }
         } else { //isSymbolicValue & !isSymbolicIndex
             int concreteIndex = ti.getTopFrame().peek(1);
             if (concreteIndex < 0 || concreteIndex >= arrayLen)
                 return ti.createAndThrowException("java.lang.ArrayIndexOutOfBoundsException", "index less/greater than array bounds");

             arrayInfo.setElementAttr(concreteIndex, symStoreVal);
             frame.pop(3);
             return getNext(ti);
         }
     }

    private Instruction executeConcretely(ThreadInfo ti, ElementInfo arrayInfo) {

        StackFrame frame = ti.getModifiableTopFrame();
        index = peekIndex(ti);

        // original code for concrete execution

        // int idx = peekIndex(ti);
        int aref = peekArrayRef(ti); // need to be polymorphic, could be LongArrayStore

        arrayOperandAttr = peekArrayAttr(ti);
        indexOperandAttr = peekIndexAttr(ti);

        // --- shared access CG
        /*
         * ignore POR for now TODO Scheduler scheduler = ti.getScheduler(); if
         * (scheduler.canHaveSharedArrayCG(ti, this, eiArray, idx)){ eiArray =
         * scheduler.updateArraySharedness(ti, eiArray, idx); if
         * (scheduler.setsSharedArrayCG(ti, this, eiArray, idx)){ return this; } } }
         */
        // System.out.println("len "+len+" index "+index);
        try {
            // setArrayElement(ti, frame, eiArray); // this pops operands
            int esize = getElementSize();
            Object attr = esize == 1 ? frame.getOperandAttr() : frame.getLongOperandAttr();

            popValue(frame);
            frame.pop();
            // don't set 'arrayRef' before we do the CG checks (would kill loop
            // optimization)
            arrayRef = frame.pop();

            arrayInfo = arrayInfo.getModifiableInstance();
            setField(arrayInfo, index);
            arrayInfo.setElementAttrNoClone(index, attr); // <2do> what if the value is the same but not the attr?

        } catch (ArrayIndexOutOfBoundsExecutiveException ex) { // at this point, the AIOBX is already processed
            return ex.getInstruction();
        }

        return getNext(ti);
    }

}
