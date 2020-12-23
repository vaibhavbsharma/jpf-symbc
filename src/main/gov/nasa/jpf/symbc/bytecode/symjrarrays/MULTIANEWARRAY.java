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

package gov.nasa.jpf.symbc.bytecode.symjrarrays;


import gov.nasa.jpf.symbc.numeric.*;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.string.SymbolicLengthInteger;
import gov.nasa.jpf.vm.*;

import java.util.*;

import static gov.nasa.jpf.symbc.veritesting.AdapterSynth.SPFAdapterSynth.getVal;

public class MULTIANEWARRAY extends gov.nasa.jpf.jvm.bytecode.MULTIANEWARRAY {
    ArrayList<Long> values;
    private final int[] smallValues = new int[]{2, 2};

    public MULTIANEWARRAY(String typeName, int dimensions) {
        super(typeName, dimensions);
    }

    @Override
    public Instruction execute(ThreadInfo ti) {
        arrayLengths = new int[dimensions];
        StackFrame sf = ti.getModifiableTopFrame();
        PathCondition pc = null;
        if (allOperandsConcrete(sf))
            return super.execute(ti);

        //one or more operand is symbolic, then we want to fill out arrayLengths by trying to guess what they could be
        HashMap<Integer, SymbolicInteger> guessedDimensions = new HashMap<>();
        ChoiceGenerator<?> cg = ti.getVM().getSystemState().getChoiceGenerator();
        if (cg instanceof PCChoiceGenerator) {
            pc = ((PCChoiceGenerator) (ti.getVM().getSystemState().getChoiceGenerator())).getCurrentPC();
        } else {
            pc = new PathCondition();
            pc._addDet(Comparator.EQ, new IntegerConstant(0), new IntegerConstant(0));
        }
        assert pc != null;
        PathCondition newPC = pc.make_copy();

        int smallValuesIndex = 0;
        for (int i = dimensions - 1; i >= 0; i--) {
            Object attr = sf.getOperandAttr();
            if (attr instanceof SymbolicLengthInteger) {
                long l = ((SymbolicLengthInteger) attr).solution;
                assert (l >= 0 && l <= Integer.MAX_VALUE) : "Array length must be positive integer";
                arrayLengths[i] = (int) l;
                sf.pop();
            } else if (attr instanceof SymbolicInteger) {
                guessedDimensions.put(i, (SymbolicInteger) attr);
                newPC._addDet(Comparator.EQ, (IntegerExpression) attr, new IntegerConstant(smallValues[smallValuesIndex++]));
                sf.pop();
            } else throw new RuntimeException("MULTIANEWARRAY: unsupported array length type");
        }
        if (guessedDimensions.size() > 0) {
            List<SymbolicInteger> symbolicAttrs = new ArrayList<>();
            symbolicAttrs.addAll(guessedDimensions.values());
            Map<String, Object> map = newPC.solveWithValuations(symbolicAttrs, new ArrayList());

            for (Map.Entry e : guessedDimensions.entrySet()) {
                String name = ((SymbolicInteger) e.getValue()).getName();
                assert name != null : "symbolic name cannot be null. Failing";
                Long lastValue = getVal(map, name);
                arrayLengths[(int) e.getKey()] = Math.toIntExact(lastValue);
            }

            ((PCChoiceGenerator) cg).setCurrentPC(newPC);
        }


        //the remainder of the code is identical to the parent class

        // there is no clinit for array classes, but we still have  to create a class object
        // since its a builtin class, we also don't have to bother with NoClassDefFoundErrors
        ClassInfo ci = ClassLoaderInfo.getCurrentResolvedClassInfo(type);
        if (!ci.isRegistered()) {
            ci.registerClass(ti);
            ci.setInitialized();
        }

        int arrayRef = allocateArray(ti.getHeap(), type, arrayLengths, ti, 0);

        // put the result (the array reference) on the stack
        sf.push(arrayRef, true);

        return getNext(ti);
    }

    private boolean allOperandsConcrete(StackFrame sf) {
        for (int i = 0; i < dimensions; i++) {
            if (sf.getOperandAttr(i) != null)
                return false;
        }
        return true;
    }
}