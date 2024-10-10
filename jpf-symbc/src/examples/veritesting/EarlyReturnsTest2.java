package veritesting;

public class EarlyReturnsTest2 extends TestRegionBaseClass {
    public static char[] buffer = new char[81];

    public Outputs erTest1(int ch, int ch1) {
        Outputs o = new Outputs();
        o.intOutputs = new int[1];
        o.intOutputs[0] = test(ch, ch1);
        return o;
    }


    public int test(int ch, int ch1) {
        if (ch == 1) return ch1+1;
        else if (ch == 2) return ch1+2;
        else if (ch == 3) return ch1+3;
        else return ch1+4;
    }



    public static void main(String[] args) {
        TestVeritesting t = new TestVeritesting();
        EarlyReturnsTest2 s = new EarlyReturnsTest2();
        t.runTest(s);
    }

    @Override
    Outputs testFunction(int in0, int in1, int in2, int in3, int in4, int in5,
                         boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5,
                         char c0, char c1, char c2, char c3, char c4, char c5) {
        return erTest1(in0, in1);
    }
}