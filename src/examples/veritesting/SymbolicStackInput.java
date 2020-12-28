package veritesting;

public class SymbolicStackInput  extends TestRegionBaseClass {
    public static void main (String args[]) {
        TestVeritesting t = new TestVeritesting();
        SymbolicStackInput h = new SymbolicStackInput();
        t.runTest(h);
    }

    private static int symIntBranching(int x) {
        Integer x1 = x;
        return x1 == 0 ? 1: 0;
    }

    @Override
    Outputs testFunction(int in0, int in1, int in2, int in3, int in4, int in5, boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, char c0, char c1, char c2, char c3, char c4, char c5) {
        return new Outputs(new int[]{symIntBranching(in0)});
    }
}
