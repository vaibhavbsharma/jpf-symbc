package gov.nasa.jpf.symbc.interpolation;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.symbc.bytecode.INVOKEINTERFACE;
import gov.nasa.jpf.symbc.bytecode.INVOKESPECIAL;
import gov.nasa.jpf.symbc.bytecode.INVOKESTATIC;
import gov.nasa.jpf.symbc.bytecode.INVOKEVIRTUAL;
import gov.nasa.jpf.symbc.interpolation.bytecode.GCinstruction;
import gov.nasa.jpf.symbc.interpolation.bytecode.GCinstructionType;
import gov.nasa.jpf.symbc.interpolation.creation.Trace;
import gov.nasa.jpf.vm.*;
import gov.nasa.jpf.vm.bytecode.ReturnInstruction;


public class InterpolationListener extends PropertyListenerAdapter implements PublisherExtension {

    static boolean insideInterestingMethod = false; // indicates whether we are in the method we want to compute the interpolant for.

    static String interpolMainMethod;
    public static boolean interpolationOn = false;

    public InterpolationListener(Config conf, JPF jpf) {
        if (conf.hasValue("interpolation.method")) {
            interpolMainMethod = conf.getString("interpolation.method");
            interpolationOn = true;
        } else assert false : "configuration interpolation.method must be specified. Failing.";
    }


    @Override
    public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction,
                                    Instruction executedInstruction) {

    }

    public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
//        System.out.println(instructionToExecute);

        if (!insideInterestingMethod && instructionToExecute.toString().contains(interpolMainMethod))
            if (instructionToExecute instanceof INVOKESTATIC || instructionToExecute instanceof INVOKESPECIAL ||
                    instructionToExecute instanceof INVOKEVIRTUAL || instructionToExecute instanceof INVOKEINTERFACE) {
                assert !insideInterestingMethod : "multiple entrance of the method is not supported. Failing.";
                insideInterestingMethod = true;

                return;
            }

        if(insideInterestingMethod && instructionToExecute instanceof IfInstruction){
            //check subsumption here
        }
        if (instructionToExecute.toString().contains(interpolMainMethod) && instructionToExecute instanceof ReturnInstruction) { // this is where we want to compute the weakest precondition backward.
            InterpolationMain.computeInterpolants(currentThread);
            return;
        }

        if (insideInterestingMethod) {
            Trace.add(instructionToExecute, null);
        }
    }


    @Override
    public void threadTerminated(VM vm, ThreadInfo terminatedThread) {
        System.out.println("printing trace for thread");
    }


    public void choiceGeneratorRegistered(VM vm, ChoiceGenerator<?> nextCG, ThreadInfo currentThread, Instruction executedInstruction) {
        System.out.println("choiceGeneratorRegistered");
        Trace.add(new GCinstruction(GCinstructionType.CHOICE_GENERATOR_REGISTERED), nextCG);
    }

    public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
        System.out.println("choiceGeneratorAdvanced");
        Trace.add(new GCinstruction(GCinstructionType.CHOICE_GENERATOR_ADVANCED), null);
    }

    public void choiceGeneratorProcessed(VM vm, ChoiceGenerator<?> processedCG) {
        Trace.add(new GCinstruction(GCinstructionType.CHOICE_GENERATOR_PROCESSED), null);
    }


}
