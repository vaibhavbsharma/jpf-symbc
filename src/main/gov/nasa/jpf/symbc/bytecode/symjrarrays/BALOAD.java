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
import gov.nasa.jpf.symbc.arrays.SelectExpression;
import gov.nasa.jpf.symbc.numeric.*;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.Pair;
import gov.nasa.jpf.symbc.veritesting.ast.def.AssignmentStmt;
import gov.nasa.jpf.symbc.veritesting.ast.def.GammaVarExpr;
import gov.nasa.jpf.symbc.veritesting.ast.transformations.AstToGreen.AstToGreenVisitor;
import gov.nasa.jpf.vm.*;
import za.ac.sun.cs.green.expr.Expression;
import za.ac.sun.cs.green.expr.IntConstant;
import za.ac.sun.cs.green.expr.Operation;

import static gov.nasa.jpf.symbc.bytecode.symjrarrays.ArrayUtil.getNewArrLoadVarName;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.createGreenVar;
import static gov.nasa.jpf.symbc.veritesting.VeritestingUtil.ExprUtil.greenToSPFExpression;


/**
 * Load byte or boolean from array
 * ..., arrayref, index => ..., value
 */
public class BALOAD extends gov.nasa.jpf.jvm.bytecode.BALOAD {
    @Override
    public Instruction execute(ThreadInfo ti) {
        StackFrame frame = ti.getModifiableTopFrame();
        arrayRef = frame.peek(1); // ..., arrayRef, idx

        if (arrayRef == MJIEnv.NULL) {
            return ti.createAndThrowException("java.lang.NullPointerException");
        }

        Object symIndex = peekIndexAttr(ti);
        boolean isSymbolicIndex = symIndex != null && symIndex instanceof IntegerExpression;

        assert (peekArrayAttr(ti) == null || !(peekArrayAttr(ti) instanceof ArrayExpression)) : "unsupported symbolic array object. Failing.";

        //if the value to store is not symbolic and neither is the index, then execute concretely
        if (!isSymbolicIndex)
            return super.execute(ti);


        ChoiceGenerator<?> cg;

        if (!ti.isFirstStepInsn()) { // first time around
            cg = new PCChoiceGenerator(3);
            ((PCChoiceGenerator) cg).setOffset(this.position);
            ((PCChoiceGenerator) cg).setMethodName(this.getMethodInfo().getFullName());
            ti.getVM().setNextChoiceGenerator(cg);
            return this;
        } else { // this is what really returns results
            cg = ti.getVM().getChoiceGenerator();
            assert (cg instanceof PCChoiceGenerator) : "expected PCChoiceGenerator, got: " + cg;
        }

        PathCondition pc;
        ChoiceGenerator<?> prev_cg = cg.getPreviousChoiceGeneratorOfType(PCChoiceGenerator.class);

        if (prev_cg == null)
            pc = new PathCondition();
        else
            pc = ((PCChoiceGenerator) prev_cg).getCurrentPC();

        assert pc != null;
        // We have a concrete array, but a symbolic index. We add all the constraints about the elements of the array, and perform the select
        ElementInfo arrayInfo = ti.getElementInfo(arrayRef);


        if ((Integer) cg.getNextChoice() == 1) { // check bounds of the index
            pc._addDet(Comparator.GE, (IntegerExpression) symIndex, arrayInfo.arrayLength());
            if (pc.simplify()) { // satisfiable
                ((PCChoiceGenerator) cg).setCurrentPC(pc);

                return ti.createAndThrowException("java.lang.ArrayIndexOutOfBoundsException", "index greater than array bounds");
            } else {
                ti.getVM().getSystemState().setIgnored(true);
                return getNext(ti);
            }
        } else if ((Integer) cg.getNextChoice() == 2) {
            pc._addDet(Comparator.LT, (IntegerExpression) symIndex, new IntegerConstant(0));
            if (pc.simplify()) { // satisfiable
                ((PCChoiceGenerator) cg).setCurrentPC(pc);
                return ti.createAndThrowException("java.lang.ArrayIndexOutOfBoundsException", "index smaller than array bounds");
            } else {
                ti.getVM().getSystemState().setIgnored(true);
                return getNext(ti);
            }
        } else {
            pc._addDet(Comparator.LT, (IntegerExpression) symIndex, arrayInfo.arrayLength());
            pc._addDet(Comparator.GE, (IntegerExpression) symIndex, new IntegerConstant(0));
            if (pc.simplify()) { //satisfiable
                ((PCChoiceGenerator) cg).setCurrentPC(pc);
                frame.pop(2); // We pop the array and the index
                frame.push(0, false);         // For symbolic expressions, the concrete value does not matter

                za.ac.sun.cs.green.expr.Expression greenVar = createGreenVar(arrayInfo.getType(), getNewArrLoadVarName());

                AssignmentStmt stmt = new AssignmentStmt(greenVar, createNestedGamma(0, ExprUtil.SPFToGreenExpr((IntegerExpression)symIndex), arrayInfo));

                pc._addDet(new GreenConstraint(stmt.accept(new AstToGreenVisitor())));
                pc.simplify();

                // set the result
                frame.setOperandAttr(greenToSPFExpression(greenVar));
                // We add the select instruction in the PathCondition

                return getNext(ti);
            } else {
                ti.getVM().getSystemState().setIgnored(true);
                return getNext(ti);
            }
        }
    }

    za.ac.sun.cs.green.expr.Expression createNestedGamma(int index, za.ac.sun.cs.green.expr.Expression indexAttr, ElementInfo arrayInfo) {
        Pair<Expression, String> arrayElementAttrOrVal = gov.nasa.jpf.symbc.veritesting.ast.transformations.arrayaccess.ArrayUtil.getArrayElement(arrayInfo, index);
        assert arrayElementAttrOrVal != null : "array element attribute or value cannot be null, something is wrong. Failing.";

        if (index + 1 == arrayInfo.arrayLength()){ // last element
            return arrayElementAttrOrVal.getFirst();
        } else{
            return new GammaVarExpr(new Operation(Operation.Operator.EQ, indexAttr, new IntConstant(index)), arrayElementAttrOrVal.getFirst(),
                    createNestedGamma(index+1, indexAttr, arrayInfo));

        }

    }

}
