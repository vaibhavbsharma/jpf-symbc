package veritesting;

public class RecursionTest1 extends TestRegionBaseClass {
// taken from Unsataddition02 of SV-COMP benchmarks
    static int addition(int m, int n) {
        if (n == 0) {
            return m;
        } else if (n > 0) {
            return addition(m + 1, n - 1);
        } else {
            return addition(m - 1, n + 1);
        }
    }

    public static Outputs runAddition(int m, int n) {
        Outputs o = new Outputs();
        o.intOutputs = new int[1];
        o.intOutputs[0] = addition(m, n);
        return o;
        /*if (m < 100 || n < 100 || result >= 200) {
            return;
        } else {
            assert false;
        }*/
    }

    public static void main(String[] args) {
        TestVeritesting t = new TestVeritesting();
        RecursionTest1 s = new RecursionTest1();
        t.runTest(s);
    }

    @Override
    Outputs testFunction(int in0, int in1, int in2, int in3, int in4, int in5,
                         boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5,
                         char c0, char c1, char c2, char c3, char c4, char c5) {
        return runAddition(in0, in1);
    }
}
