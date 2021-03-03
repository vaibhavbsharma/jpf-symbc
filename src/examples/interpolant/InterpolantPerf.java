package interpolant;

public class InterpolantPerf {

    public static void main(String[] args) {
        oneVarSimple3(true,true);
    }

    public static int oneVarSimple1(boolean b) {
        int x = 0;

        if (b)
            x = x + 1;

        if (x == 3)
            assert false;

        return x;
    }

    public static int oneVarSimple2(boolean b) {
        int x = 0;

        if (b)
            x = x + 1;
        else
            x = 4;

        if (x == 3)
            assert false;

        return x;
    }

    public static int oneVarSimple3(boolean b, boolean c) {
        int x = 0;

        if (b)
            x = x + 1;

        if (c)
            x = x + 1;

        if (x == 3)
            assert false;

        return x;
    }
}
