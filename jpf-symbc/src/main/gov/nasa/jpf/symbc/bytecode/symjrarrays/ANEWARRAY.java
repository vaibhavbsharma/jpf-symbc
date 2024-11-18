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

/**
 * Soha Hussein: This package handles only symbolic index arrays, and concertize creation of symbolic size array
 * to a set of small values. This package creates a disjunctive formula to represent arrayloads and stores, it does not
 * use the solver's array theory.
 */


package gov.nasa.jpf.symbc.bytecode.symjrarrays;


import gov.nasa.jpf.symbc.numeric.*;
import gov.nasa.jpf.symbc.string.SymbolicLengthInteger;
import gov.nasa.jpf.vm.*;

import java.util.ArrayList;
import java.util.Map;

import static gov.nasa.jpf.symbc.veritesting.AdapterSynth.SPFAdapterSynth.getVal;


public class ANEWARRAY extends gov.nasa.jpf.jvm.bytecode.ANEWARRAY {
    private static final int[] smallValues = {1, 2, 3, 4, 5, 1000};
    ArrayList<Long> values;

    public ANEWARRAY(String typeDescriptor) {
        super(typeDescriptor);
    }

    @Override
    public Instruction execute(ThreadInfo ti) {
        StackFrame sf = ti.getModifiableTopFrame();
        Object attr = sf.getOperandAttr();
        PathCondition pc;


        if (attr == null)
            return super.execute(ti);

        if (attr instanceof SymbolicLengthInteger) {
            long l = ((SymbolicLengthInteger) attr).solution;
            if (!(l >= 0 && l <= Integer.MAX_VALUE))
                return ti.createAndThrowException("java.lang.NegativeArraySizeException");
            arrayLength = (int) l;
            sf.pop();
        } else if (attr instanceof IntegerExpression) {
            ChoiceGenerator<?> cg = null;
            if (!ti.isFirstStepInsn()) {
                if (ti.getVM().getSystemState().getChoiceGenerator() instanceof PCChoiceGenerator) {
                    pc = ((PCChoiceGenerator) (ti.getVM().getSystemState().getChoiceGenerator())).getCurrentPC();
                } else {
                    pc = new PathCondition();
                    pc._addDet(Comparator.EQ, new IntegerConstant(0), new IntegerConstant(0));
                }
                assert pc != null;

                String name = attr instanceof SymbolicInteger ? ((SymbolicInteger) attr).getName() : null;
                if (attr instanceof BinaryNonLinearIntegerExpression) {
                    // if attr is BNLIE with same operands, concretize the operand to avoid reasoning over the non-linear arithmetic
                    BinaryNonLinearIntegerExpression attrBNLIE = (BinaryNonLinearIntegerExpression) attr;
                    if (attrBNLIE.left instanceof SymbolicInteger && attrBNLIE.right instanceof SymbolicInteger
                            && attrBNLIE.left.equals(attrBNLIE.right)) {
                        name = ((SymbolicInteger) attrBNLIE.left).getName();
                        attr = attrBNLIE.left;
                    }
                }
                if (name == null) {
                    ti.getVM().getSystemState().setIgnored(true);
                    return getNext(ti);
                }

                values = new ArrayList<>();

                for (int i = 0; i < smallValues.length; i++) {
                    PathCondition newPC = pc.make_copy();
                    newPC._addDet(Comparator.LT, (IntegerExpression) attr, new IntegerConstant(smallValues[i]));
                    if(i>0)
                        newPC._addDet(Comparator.GT, (IntegerExpression) attr, new IntegerConstant(smallValues[i-1]));
                    Map<String, Object> map = newPC.solveWithValuation((SymbolicInteger) attr, null);
                    Long lastValue = getVal(map, name);
                    if (map == null || map.size() == 0 || lastValue == null) continue;
                    else values.add(lastValue);
                }
                if (values.size() == 0)
                    return ti.createAndThrowException("unsupported symbolic size of array length.");

                // First choice is to explore negative array length
                // All the choices in the middle explore small array lengths
                cg = new PCChoiceGenerator(values.size() + 1);
                ti.getVM().setNextChoiceGenerator(cg);
                return this;
            }
            cg = ti.getVM().getSystemState().getChoiceGenerator();
            assert (cg instanceof PCChoiceGenerator) : "expected PCChoiceGenerator, got:" + cg;


            ChoiceGenerator<?> prev_cg = cg.getPreviousChoiceGeneratorOfType(PCChoiceGenerator.class);

            if (prev_cg == null)
                pc = new PathCondition();
            else
                pc = ((PCChoiceGenerator) prev_cg).getCurrentPC();
            assert pc != null;

            if ((Integer) cg.getNextChoice() == 0) {
                pc._addDet(Comparator.LT, (IntegerExpression) attr, new IntegerConstant(0));
                if (pc.simplify()) {
                    ((PCChoiceGenerator) cg).setCurrentPC(pc);
                    return ti.createAndThrowException("java.lang.NegativeArraySizeException");
              } else {
                    ti.getVM().getSystemState().setIgnored(true);
                    return getNext(ti);
                }
            } else { // exploring smallValues choices.
               pc._addDet(Comparator.GE, (IntegerExpression) attr, new IntegerConstant(0));
                if (pc.simplify()) {
                    ((PCChoiceGenerator) cg).setCurrentPC(pc);
                    arrayLength = sf.pop();
                } else {
                    ti.getVM().getSystemState().setIgnored(true);
                    return getNext(ti);
                }
                arrayLength = Math.toIntExact(values.get((Integer) cg.getNextChoice() - 1));
                pc._addDet(Comparator.EQ, (IntegerExpression) attr, new IntegerConstant(arrayLength));
            }

        } else {
            arrayLength = sf.pop();
        }

        //the remainder of the code is almost identical to the parent class

        Heap heap = ti.getHeap();

        if (arrayLength < 0) {
            return ti.createAndThrowException("java.lang.NegativeArraySizeException");
        }

        // there is no clinit for array classes, but we still have  to create a class object
        // since its a builtin class, we also don't have to bother with NoClassDefFoundErrors
        String clsName = "[" + type;

        ClassInfo ci = ClassLoaderInfo.getCurrentResolvedClassInfo(clsName);
        if (!ci.isRegistered()) {
            ci.registerClass(ti);
            ci.setInitialized();
        }

        if (heap.isOutOfMemory()) { // simulate OutOfMemoryError
            return ti.createAndThrowException("java.lang.OutOfMemoryError",
                    "trying to allocate new " +
                            getTypeName() +
                            "[" + arrayLength + "]");
        }

        ElementInfo eiArray = heap.newArray(type, arrayLength, ti);
        int arrayRef = eiArray.getObjectRef();

        sf.pushRef(arrayRef);

        ti.getVM().getSystemState().checkGC(); // has to happen after we push the new object ref

        return getNext(ti);
    }
}