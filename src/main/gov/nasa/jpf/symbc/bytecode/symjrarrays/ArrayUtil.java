package gov.nasa.jpf.symbc.bytecode.symjrarrays;

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
