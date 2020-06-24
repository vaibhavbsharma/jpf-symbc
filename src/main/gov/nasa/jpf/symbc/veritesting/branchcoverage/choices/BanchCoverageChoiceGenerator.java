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
// Copyright (C) 2007 United States Government as represented by the
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
//
package gov.nasa.jpf.symbc.veritesting.branchcoverage.choices;

import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.IntegerExpression;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.veritesting.VeritestingUtil.SpfUtil;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;

public class BanchCoverageChoiceGenerator extends PCChoiceGenerator {


    private final Instruction instruction;

    public enum Kind {UNARYIF, BINARYIF, NULLIF, OTHER}

    //To indicate which precedence this CG is taking, could be different from one to another
    boolean flip;

    public BanchCoverageChoiceGenerator(int count, IfInstruction instruction, boolean flip) {
        super(0, count);
        this.flip = flip;
        this.instruction = instruction;
    }

    public static void execute(ThreadInfo ti, IfInstruction instructionToExecute, boolean flip) {
        Kind kind = getKind(instructionToExecute);
        if (kind == Kind.BINARYIF) ti.setNextPC(executeBinaryIf(ti, instructionToExecute, flip));
        else if (kind == Kind.UNARYIF) ti.setNextPC(executeUnaryIf(ti, instructionToExecute, flip));
        else assert false : "unsupported branching instruction";
    }


    public static Instruction executeBinaryIf(ThreadInfo ti, IfInstruction instructionToExecute, boolean flip) {
        Comparator trueComparator = SpfUtil.getComparator(instructionToExecute);
        Comparator falseComparator = SpfUtil.getNegComparator(instructionToExecute);
        StackFrame sf = ti.getModifiableTopFrame();

        IntegerExpression sym_v1 = (IntegerExpression) sf.getOperandAttr(1);
        IntegerExpression sym_v2 = (IntegerExpression) sf.getOperandAttr(0);

        if ((sym_v1 == null) && (sym_v2 == null)) { // both conditions are concrete
            assert false : "unexpected concrete instruction in BranchCovPCChoiceGenerator";
            return instructionToExecute;
        } else { // at least one condition is symbolic
            Instruction nxtInstr = getNextInstructionAndSetPCChoice(ti, instructionToExecute, sym_v1, sym_v2, trueComparator, falseComparator, flip);
            /* Not sure if these are necessary.
            if (nxtInstr == getTarget()) conditionValue = true;
            else conditionValue = false;*/
            return nxtInstr;
        }
    }

    public static Instruction executeUnaryIf(ThreadInfo ti, IfInstruction instructionToExecute, boolean flip) {

        StackFrame sf = ti.getModifiableTopFrame();
        IntegerExpression sym_v = (IntegerExpression) sf.getOperandAttr();

        Comparator trueComparator = SpfUtil.getComparator(instructionToExecute);
        Comparator falseComparator = SpfUtil.getNegComparator(instructionToExecute);

        if (sym_v == null) { // the condition is concrete
            assert false : "unexpected concrete instruction in BranchCovPCChoiceGenerator";
            return instructionToExecute;
        } else { // the condition is symbolic
            Instruction nxtInstr = getNextInstructionAndSetPCChoice(ti, instructionToExecute, sym_v, trueComparator, falseComparator, flip);
            /*if(nxtInstr==getTarget())
                conditionValue=true;
            else
                conditionValue=false;*/
            return nxtInstr;
        }
    }

    public static Kind getKind(Instruction instruction) {
        switch (instruction.getMnemonic()) {
            case "ifeq":
            case "ifge":
            case "ifle":
            case "ifgt":
            case "iflt":
            case "ifne":
                return Kind.UNARYIF;
            case "if_icmpeq":
            case "if_icmpge":
            case "if_icmpgt":
            case "if_icmple":
            case "if_icmplt":
            case "if_icmpne":
                return Kind.BINARYIF;
            case "ifnull":
                return Kind.NULLIF;
            default:
                return Kind.OTHER;
        }
    }


    //binary branching instruction
    public static Instruction getNextInstructionAndSetPCChoice(ThreadInfo ti, IfInstruction instr, IntegerExpression sym_v1, IntegerExpression sym_v2, Comparator trueComparator, Comparator falseComparator, boolean flip) {


        if (!ti.isFirstStepInsn()) { // first time around
            PCChoiceGenerator prevPcGen;
            ChoiceGenerator<?> cg = ti.getVM().getChoiceGenerator();
            if (cg instanceof PCChoiceGenerator) prevPcGen = (PCChoiceGenerator) cg;
            else prevPcGen = (PCChoiceGenerator) cg.getPreviousChoiceGeneratorOfType(PCChoiceGenerator.class);

            PathCondition pc;
            if (prevPcGen != null) pc = prevPcGen.getCurrentPC();
            else pc = new PathCondition();

            PathCondition eqPC = pc.make_copy();
            PathCondition nePC = pc.make_copy();

            int v2 = ti.getModifiableTopFrame().peek();
            int v1 = ti.getModifiableTopFrame().peek(1);

            if (sym_v1 != null) {
                if (sym_v2 != null) { //both are symbolic values
                    eqPC._addDet(trueComparator, sym_v1, sym_v2);
                    nePC._addDet(falseComparator, sym_v1, sym_v2);
                } else {
                    eqPC._addDet(trueComparator, sym_v1, v2);
                    nePC._addDet(falseComparator, sym_v1, v2);
                }
            } else {
                eqPC._addDet(trueComparator, v1, sym_v2);
                nePC._addDet(falseComparator, v1, sym_v2);
            }

            boolean eqSat = eqPC.simplify();
            boolean neSat = nePC.simplify();

            if (eqSat) {
                if (neSat) {
                    BanchCoverageChoiceGenerator newPCChoice = new BanchCoverageChoiceGenerator(2, instr, flip);
                    newPCChoice.setOffset(instr.getPosition());
                    newPCChoice.setMethodName(instr.getMethodInfo().getFullName());
                    ti.getVM().getSystemState().setNextChoiceGenerator(newPCChoice);
                    return instr;
                } else {
                    ti.getModifiableTopFrame().pop();
                    ti.getModifiableTopFrame().pop();
                    return instr.getTarget();
                }
            } else {
                ti.getModifiableTopFrame().pop();
                ti.getModifiableTopFrame().pop();
                return instr.getNext(ti);
            }
        } else { //This branch will only be taken if there is a choice

            int v2 = ti.getModifiableTopFrame().pop();
            int v1 = ti.getModifiableTopFrame().pop();
            PathCondition pc;
            PCChoiceGenerator curCg = (PCChoiceGenerator) ti.getVM().getSystemState().getChoiceGenerator();
            assert curCg instanceof BanchCoverageChoiceGenerator : "unexpected type for choice generator. Failing";

            PCChoiceGenerator prevCg = curCg.getPreviousChoiceGeneratorOfType(PCChoiceGenerator.class);

            if (prevCg == null) pc = new PathCondition();
            else pc = prevCg.getCurrentPC();

            boolean conditionValue = (Integer) curCg.getNextChoice() == 1 ? true : false;

            //else side is always executed first, flip the order if needed
            conditionValue = ((BanchCoverageChoiceGenerator) curCg).flip ? !conditionValue : conditionValue;

            if (conditionValue) {
                if (sym_v1 != null) {
                    if (sym_v2 != null) { //both are symbolic values
                        pc._addDet(trueComparator, sym_v1, sym_v2);
                    } else pc._addDet(trueComparator, sym_v1, v2);
                } else pc._addDet(trueComparator, v1, sym_v2);
                ((PCChoiceGenerator) curCg).setCurrentPC(pc);
                return instr.getTarget();
            } else {
                if (sym_v1 != null) {
                    if (sym_v2 != null) { //both are symbolic values
                        pc._addDet(falseComparator, sym_v1, sym_v2);
                    } else pc._addDet(falseComparator, sym_v1, v2);
                } else pc._addDet(falseComparator, v1, sym_v2);
                ((PCChoiceGenerator) curCg).setCurrentPC(pc);
                return instr.getNext(ti);
            }
        }
    }


    //unary branching instruction
    public static Instruction getNextInstructionAndSetPCChoice(ThreadInfo ti,
                                                               IfInstruction instr,
                                                               IntegerExpression sym_v,
                                                               Comparator trueComparator,
                                                               Comparator falseComparator, boolean flip) {

        if (!ti.isFirstStepInsn()) { // first time around
            PCChoiceGenerator prevPcGen;
            ChoiceGenerator<?> cg = ti.getVM().getChoiceGenerator();
            if (cg instanceof PCChoiceGenerator)
                prevPcGen = (PCChoiceGenerator) cg;
            else
                prevPcGen = (PCChoiceGenerator) cg.getPreviousChoiceGeneratorOfType(PCChoiceGenerator.class);

            PathCondition pc;
            if (prevPcGen != null)
                pc = prevPcGen.getCurrentPC();
            else
                pc = new PathCondition();

            PathCondition eqPC = pc.make_copy();
            PathCondition nePC = pc.make_copy();
            eqPC._addDet(trueComparator, sym_v, 0);
            nePC._addDet(falseComparator, sym_v, 0);

            boolean eqSat = eqPC.simplify();
            boolean neSat = nePC.simplify();

            if (eqSat) {
                if (neSat) {
                    BanchCoverageChoiceGenerator newPCChoice = new BanchCoverageChoiceGenerator(2, instr, flip);
                    newPCChoice.setOffset(instr.getPosition());
                    newPCChoice.setMethodName(instr.getMethodInfo().getFullName());
                    ti.getVM().getSystemState().setNextChoiceGenerator(newPCChoice);
                    return instr;
                } else {
                    ti.getModifiableTopFrame().pop();
                    return instr.getTarget();
                }
            } else {
                ti.getModifiableTopFrame().pop();
                return instr.getNext(ti);
            }
        } else {
            ti.getModifiableTopFrame().pop();
            PathCondition pc;
            PCChoiceGenerator curCg = (PCChoiceGenerator) ti.getVM().getSystemState().getChoiceGenerator();
            assert curCg instanceof BanchCoverageChoiceGenerator : "unexpected type for choice generator. Failing";

            PCChoiceGenerator prevCg = curCg.getPreviousChoiceGeneratorOfType(PCChoiceGenerator.class);

            if (prevCg == null)
                pc = new PathCondition();
            else
                pc = prevCg.getCurrentPC();
            boolean conditionValue = (Integer) curCg.getNextChoice() == 1 ? true : false;

            //else side is always executed first, flip the order if needed
            conditionValue = ((BanchCoverageChoiceGenerator) curCg).flip ? !conditionValue : conditionValue;

            if (conditionValue) {
                pc._addDet(trueComparator, sym_v, 0);
                ((PCChoiceGenerator) curCg).setCurrentPC(pc);
                return instr.getTarget();
            } else {
                pc._addDet(falseComparator, sym_v, 0);
                ((PCChoiceGenerator) curCg).setCurrentPC(pc);
                return instr.getNext(ti);
            }
        }
    }

}
