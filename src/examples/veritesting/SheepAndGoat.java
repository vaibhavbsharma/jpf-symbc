package veritesting;

public class SheepAndGoat extends TestRegionBaseClass {
    Outputs testFunction(int in0, int in1, int in2, int in3, int in4, int in5,
                         boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5) {
        return simpleRegion(in0);
    }
    public static int sheepAndGoatLeft(int i) {
        int j = 0;
        System.out.println(Integer.toBinaryString(i));

        while(i != 0) {
            int zeroCount = Integer.numberOfTrailingZeros(i);
            if (zeroCount != 0) {
                i >>= zeroCount;
            } else {
                j >>>= 1;
                j ^= Integer.reverse(1);
                i >>= 1;
            }
        }

        System.out.println(Integer.toBinaryString(j));
        return j;
    }

    public static Outputs simpleRegion(int y) {
        int methodCount = 0;
        if (y > 0)
            methodCount = sheepAndGoatLeft(y);
        Outputs o = new Outputs();
        o.intOutputs = new int[1];
        o.intOutputs[0] = methodCount;
        return o;
    }

    public static void main(String[] args) {
        TestVeritesting t = new TestVeritesting();
        SheepAndGoat h = new SheepAndGoat();
        t.runTest(h);
    }

    @Override
    Outputs testFunction(int in0, int in1, int in2, int in3, int in4, int in5, boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, char c0, char c1, char c2, char c3, char c4, char c5) {
        return simpleRegion(in0);
    }
}