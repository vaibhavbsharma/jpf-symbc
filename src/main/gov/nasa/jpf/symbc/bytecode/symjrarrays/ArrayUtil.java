package gov.nasa.jpf.symbc.bytecode.symjrarrays;
/**
 * Soha Hussein: This package handles only symbolic index arrays, and concertize creation of symbolic size array
 * to a set of small values. This package creates a disjunctive formula to represent arrayloads and stores, it does not
 * use the solver's array theory.
 */

public class ArrayUtil {
    private static int loadVarIndex = 0;

    private static int elementIndexVar = 0;

    public static String getNewArrLoadVarName(){
        return "lvar"+loadVarIndex++;
    }

    public static String getNewElementVarName(){
        return "e" + elementIndexVar++;
    }
}
