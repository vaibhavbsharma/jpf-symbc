package veritesting;

public class MethodInlining1 extends TestRegionBaseClass {
    public char f1 = 0;
    public char arr[] = new char[1];
    public char otherArr[] = new char[2];
    public int arrInd = 0;
    public static final char ANY = '?';
    public static final char CCL = '[';
    public static final char ENDSTR = '\0';
    public Outputs simpleFunction1(char c0) {
        int output = -1, intermediate = -1;
        arr = new char[1];
        otherArr = new char[2];
        f1 = 0; arrInd = 0;
        arr[0] = 1;
        otherArr[0] = c0;
        otherArr[1] = 'a';
        if(otherArr[arrInd] != ENDSTR){
            intermediate = -2;
        }
        arrInd++;
        if (otherArr[arrInd] == ANY) {
            intermediate = 0;
        } else if (otherArr[arrInd] == CCL) {
            intermediate = simpleFunction2(c0);
        } else intermediate = 3;
        Outputs outputs = new Outputs(new int[3]);
        outputs.intOutputs[0] = arr[0];
        outputs.intOutputs[1] = otherArr[0];
        outputs.intOutputs[2] = arrInd;
        return outputs;
    }

    private int simpleFunction2(int in0) {
        arr[0] = 2;
        arrInd++;
        otherArr[0] = 3;
        return 2;
    }

    public static void main(String[] args) {
        TestVeritesting t = new TestVeritesting();
        MethodInlining1 h = new MethodInlining1();
        t.runTest(h);
    }

    @Override
    Outputs testFunction(int in0, int in1, int in2, int in3, int in4, int in5,
                         boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5,
                         char c0, char c1, char c2, char c3, char c4, char c5) {
        return simpleFunction1(c0);
    }
}
