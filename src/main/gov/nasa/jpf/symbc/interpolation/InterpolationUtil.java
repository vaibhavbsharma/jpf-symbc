package gov.nasa.jpf.symbc.interpolation;

import gov.nasa.jpf.jvm.bytecode.IfInstruction;

public class InterpolationUtil {

    //create the unique name of the instruction used to index the guards.
    public static String getUniqueName(IfInstruction ifInstruction) {
        return ifInstruction.getMethodInfo().toString() + "_" + ifInstruction.getPosition() + "_" + ifInstruction.toString();
    }
}
