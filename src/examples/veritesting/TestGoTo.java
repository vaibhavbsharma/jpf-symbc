package veritesting;

public class TestGoTo extends TestRegionBaseClass {
    Outputs testFunction(int in0, int in1, int in2, int in3, int in4, int in5,
                         boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5) {
        return simpleRegion(in0);
    }

    public static int testGoTo(int i) {
        int j = 0;

        while (j <= 1) {
            if (i == 0) {
                i >>= 2;
            } else ++i;
            ++j;
        }
        System.out.println(Integer.toBinaryString(j));
        return j;
    }

    public int testWhileProblem1(int i, int j) {
        int x = 0;
        while (i != 0) {
            if (j != 0) {
                x = 5;
            } else {
                x = 6;
            }
        }
        return x;

    }


    public static Outputs simpleRegion(int y) {
        int methodCount = 0;
        if (y > 0)
            methodCount = testGoTo(y);
        Outputs o = new Outputs();
        o.intOutputs = new int[1];
        o.intOutputs[0] = methodCount;
        return o;
    }

    public static void main(String[] args) {
        TestVeritesting t = new TestVeritesting();
        TestGoTo h = new TestGoTo();
        t.runTest(h);
    }

    @Override
    Outputs testFunction(int in0, int in1, int in2, int in3, int in4, int in5, boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, char c0, char c1, char c2, char c3, char c4, char c5) {
        return simpleRegion(in0);
    }
}