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

package gov.nasa.jpf.symbc.bytecode;


import gov.nasa.jpf.symbc.numeric.*;
import gov.nasa.jpf.symbc.string.SymbolicLengthInteger;
import gov.nasa.jpf.vm.*;

import java.util.ArrayList;
import java.util.Map;

import static gov.nasa.jpf.symbc.veritesting.AdapterSynth.SPFAdapterSynth.getVal;

/**
 * Symbolic version of the MULTIANEWARRAY class from jpf-core. Like NEWARRAY,
 * the difference from the jpf-core version is a snippet to detect if a symbolic
 * variable is being used as the size of the new array, and treat it accordingly.
 * 
 * And someone should review this one too :)
 * TODO: to review
 */

public class MULTIANEWARRAY extends gov.nasa.jpf.jvm.bytecode.MULTIANEWARRAY {
	ArrayList<Long> values;
	private final int[] smallValues = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10};

	public MULTIANEWARRAY(String typeName, int dimensions) {
		super(typeName, dimensions);
	}

	@Override
	public Instruction execute(ThreadInfo ti) {
		arrayLengths = new int[dimensions];
		StackFrame sf = ti.getModifiableTopFrame();
		PathCondition pc = null;
		for (int i = dimensions - 1; i >= 0; i--) {
			Object attr = sf.getOperandAttr();
			
			if(attr instanceof SymbolicLengthInteger) {
				long l = ((SymbolicLengthInteger) attr).solution;
				assert(l>=0 && l<=Integer.MAX_VALUE) : "Array length must be positive integer";
				arrayLengths[i] = (int) l;
				sf.pop();
			} else 	if(attr instanceof IntegerExpression) {
				/* These changes are introduced by Java Ranger */
				if (attr instanceof SymbolicInteger) {
					if (smallValues.length < dimensions)
						throw new RuntimeException("MULTIANEWARRAY: not enough small values available to concretize all dimensions");
					ChoiceGenerator<?> cg = null;
					if (!ti.isFirstStepInsn()) {
						if (ti.getVM().getSystemState().getChoiceGenerator() instanceof PCChoiceGenerator) {
							pc = ((PCChoiceGenerator) (ti.getVM().getSystemState().getChoiceGenerator())).getCurrentPC();
						} else {
							pc = new PathCondition();
							pc._addDet(Comparator.EQ, new IntegerConstant(0), new IntegerConstant(0));
						}
						assert pc != null;
						String name = ((SymbolicInteger) attr).getName();
						if (name == null) {
							ti.getVM().getSystemState().setIgnored(true);
							return getNext(ti);
						}
						values = new ArrayList<>();
						for (int j = 0; j < dimensions; j++) {
							PathCondition newPC = pc.make_copy();
							Object opAttr = sf.getOperandAttr(j);
							if (opAttr instanceof SymbolicInteger) {
								try { values.add(((SymbolicInteger) opAttr).solution()); }
								catch (RuntimeException e) {
									newPC._addDet(Comparator.EQ, (IntegerExpression) opAttr, new IntegerConstant(smallValues[j]));
									Map<String, Object> map = newPC.solveWithValuation((SymbolicInteger) opAttr, null);
									Long lastValue = getVal(map, name);
									if (lastValue == smallValues[j]) values.add(lastValue);
								}
							} else values.add(Long.valueOf(sf.peek(j)));
						}
						if (values.size() < dimensions)
							throw new RuntimeException("MULTIANEWARRAY: could not concretize all dimensions");
						// Explore small values only for every dimension
						cg = new PCChoiceGenerator(1);
						ti.getVM().setNextChoiceGenerator(cg);
						return this;
					} else {
						cg = ti.getVM().getSystemState().getChoiceGenerator();
						assert (cg instanceof PCChoiceGenerator) : "expected PCChoiceGenerator, got:" + cg;
						ChoiceGenerator<?> prev_cg = cg.getPreviousChoiceGeneratorOfType(PCChoiceGenerator.class);

						if(prev_cg == null)
							pc = new PathCondition();
						else
							pc = ((PCChoiceGenerator)prev_cg).getCurrentPC();
						assert pc != null;
						arrayLengths[i] = Math.toIntExact(values.get(i));
						Object opAttr = sf.getOperandAttr(i);
						if (opAttr instanceof IntegerExpression) {
							pc._addDet(Comparator.EQ, (IntegerExpression) opAttr, new IntegerConstant(arrayLengths[i]));
						}
						((PCChoiceGenerator) cg).setCurrentPC(pc);
						sf.pop();
					}
					/* End of Java Ranger changes */
				} else throw new RuntimeException("MULTIANEWARRAY: symbolic array length");
			} else {
				arrayLengths[i] = sf.pop();
			}
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
}
