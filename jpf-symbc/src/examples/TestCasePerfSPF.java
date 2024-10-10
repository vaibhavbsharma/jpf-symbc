import org.sosy_lab.sv_benchmarks.Verifier;

public class TestCasePerfSPF {

    private static int staticField2;
    int sideEffect = 0;
    public static int staticField1 = 0;

    public static void main(String[] args) {
        /*veritesting.test_case_gen.A myA = new veritesting.test_case_gen.A();
        int myVal = myA.getIncA();
        System.out.println("my A value is = " + myVal);*/
//        singleBranchCov2(1, 1);
//        doubleBranchCov(1, 1);
//        doubleRec(1, 1);
//        complexBranchCov(1,1);
//        unoptimalDFS(1, 1);
//        doubleLoopUnoptimalDFS(1, 1);
//        doubleLoop(1, 1);
//        mixOfRegions(1, 1);
//        mixOfRegions2Paths(1, 1);
//        mixOfRegions2Paths2(1, 1);
//        mixOfRegions2Paths2ComplexCond(1, 1);
//        mixOfRegions2Paths2(1,10);
//        mixOfRegions2PathsDepth3(1, 1);
//        arrayLoadStore0(1, 1);
//        testingSoundReach(1, 1);
//        testingComplexConditions1(true, true);
//        testingComplexConditions3(true, true, true, true);

//        (new TestCasePerf()).testingER1(true, 1);
//        (new TestCasePerf()).testERInline(true, 1);
//        simpleRegion(1);
//        testingComplexConditions3(true, true, true, true);
//        (new TestCasePerf()).testingSoundReach2(1);

//        TestCasePerf.staticFieldTest(1, 1);
//        mwwTestAndIte(true, true, 1);
//        (new TestCasePerf()).sheepAndGoat(1);

//        TestCasePerf.separateBits_4(1);
        TestCasePerfSPF.testOnTheGo(1);
//        TestCasePerf.testER(1);
    }



  /*  public int sheepAndGoat(int i) {
        int j = 0;

//        System.out.println("initial value of i =" + Integer.toBinaryString(i));
//        int hasZeroTrail = 0;

        if (i > 1 && i < 4) {
            while (i != 0) {
                int trailHasZero = (i & 1);
                if (trailHasZero == 0) {
                    int numberOfTrailingZeros = Integer.numberOfTrailingZeros(i);
                    i = i >> numberOfTrailingZeros;
                } else {
                    j = j >>> 1;
                    j = j ^ (Integer.reverse(1));
                    i = i >> 1;
//                System.out.println("i value = " + Integer.toBinaryString(i) + "," + Debug.getSymbolicIntegerValue(i));
//                System.out.println("j value = " + Integer.toBinaryString(j) + "," + Debug.getSymbolicIntegerValue(j));
                }
            }
//            System.out.println("i value = " + Integer.toBinaryString(i) + "," + Debug.getSymbolicIntegerValue(i));
//            System.out.println("j value = " + Integer.toBinaryString(j) + "," + Debug.getSymbolicIntegerValue(j));

            return j;
        }
        //Debug.printPC("PC out loop");
        return 1;
    }*/


    public static int testOnTheGo(int i){
        int k = Verifier.nondetInt();

        if (k >= 1000) assert k > 1000 : "k is greater 1000"; // should fail
        return k;
    }


    public static int separateBits_4(int i) {
        int j = 0;

        while (i != 0) {
            int trailHasZero = (i & 1);
            if (trailHasZero == 0) {
                int numberOfTrailingZeros = numberOfTrailingZeros_4(i);
                i = (i >> numberOfTrailingZeros);
            } else {
                j = (j >>> 1);
                j = (j ^ 8);
                i = (i >> 1);
            }
        }
        System.out.println(Integer.toBinaryString(j));
        System.out.println(j);

        return j;
    }

    public static int numberOfTrailingZeros_4(int i) {
        // HD, Figure 5-14
        int y;
        i = i << 28;
        if (i == 0) return 4;
        int n = 3;


        y = (i << 2);
        if (y != 0) {
            n = (n - 2);
            i = y;
        }
        return (n - ((((i << 1)) >>> 3)));
    }

    public static int mwwTestAndIte(boolean x, boolean y, int a) {
//        int a = 0;
        if (x && y) {
            a = a + 1;
        } else {
            a = a - 1;
        }
        return a;
    }

//    public static int testER(int i){
//        int methodCount = 0;
//        if (i > 0)
//            methodCount = 1;//staticMethod1(y);
//
//        if(i==10)
//            System.out.println("i=10");
//        else
//            System.out.println("i!=10");
//
//        if(i<10)
//            if(i>0)
//                return methodCount;
//            else
//                return staticMethod2(i);
//        return 1;
//    }

//    public static int staticMethod2(int x) {
//        int myCount = 0;
//        if (x > 100) {
//            myCount = 1;
//        } else {
//            return (new A().a);
////            myCount = 3;
//        }
//        return myCount;
//    }

    public static int staticMethod1(int x) {
        int myCount = 0;
        if (x > 10) {
//            myCount = 3; //staticMethod2(x);
            staticField1 = 3;
        } else {
//            myCount = 2;
            staticField2 = 2;
        }
        return myCount;
    }

    public static int simpleRegion(int y) {
        int methodCount = 0;
        if (y > 0)
            methodCount = 1;//staticMethod1(y);

        if (y > 10)
            methodCount = 2;//staticMethod1(y);

        return methodCount;
    }


    public static int testingComplexConditions1(boolean a, boolean b) {
        int z;
        if ((a && b)) {
            z = 1;
        } else {
            z = 0;
        }
        return z;
    }

    public static int testingComplexConditions2(boolean a, boolean b) {
        int z;
        if ((a || b)) {
            z = 1;
        } else {
            z = 0;
        }
        return z;
    }


    public static int testingComplexConditions3(boolean a, boolean b, boolean c, boolean d) {
        int z;
        if ((a && c) || (b)) {
            z = 1;
        } else {
            z = 0;
        }
        return z;
    }

    public static int testingComplexConditions4(boolean a, boolean b, boolean c, boolean d) {
        int z;
        if ((a || c) && (b)) {
            z = 1;
        } else {
            z = 0;
        }
        return z;
    }

    public static int testingComplexConditions5(boolean a, boolean b, boolean c, boolean d) {
        int z;
        if (a && (b)) {
            z = 1;
        } else {
            z = 0;
        }
        return z;
    }

    public static int testingComplexConditions6(boolean a, boolean b, boolean c, boolean d) {
        int z;
        if ((a && b) || (c && d)) {
            z = 1;
        } else {
            z = 0;
        }
        return z;
    }

    private int testMultiplERInline(boolean a, int x) {
        int z = 0;
        if (x == 3)
            z = testingER2(a, a, x);
        return z;
    }

    private int methodER1(boolean a, int x) {
        int z = 0;
        if (a)
            z = testingER2(a, a, x);
        return z;
    }

    private int methodER2(boolean a, int x) {
        int z = 0;
        if (a)
            z = testingER2(a, a, x);
        return z;
    }

    private int testERInline(boolean a, int x) {
        int z = 0;
        if (a)
            z = testingER2(a, a, x);
//        assert !(a && x > 2) || this.sideEffect == 0;
        return z;
    }

    private int testingER1(boolean a, int x) {
        int z = 1;
        if (x > 2) {
//            this.sideEffect = 5;
            return z;
        }

        this.sideEffect = 5;

        z++;


        return z;
    }


    private static int testingER2(boolean a, boolean b, int x) {
        int z = 1;
        if (a) {
            return x + 1;
        } else
            return testingSoundReach2(x);
    }

    private int testingER3(boolean a, boolean b, int x) {
        int z = 1;
        if (x > 2) {
            if (b)
                return z;
            sideEffect = 30;
            return z + x;
        }

        this.sideEffect = 5;
        z++;
        return z;
    }

    public static int singleBranchCov2(int x, int y) {

        if (x == 1)
            x = y;
        else
            x = x + 1;
        //  Debug.printPC("printing pc at the end of the path");
        return x;
    }


    public static int singleBranchCov(int x, int y) {

        for (int i = 0; i < 2; i++) {
            if (x == 1)
                x = y;
            else
                x = x + 1;
        }
        //  Debug.printPC("printing pc at the end of the path");
        return x;
    }

    public static int mixOfRegions(int x, int y) {

        if (x == 1)
            x = y;
        else
            x = x + 1;

//        if (x == y)
//            x = new A().a + y;
//        else
//            x = new B().b + y;

        if (x == 1)
            x = y + 3;
        else if (y > 1)
            x = x + 1;
        else
            x = x + 2;

        return x;
    }


    public static int mixOfRegions2Paths(int x, int y) {
        int z = 10;

//        if (x == y)
//            z = new A().a;

        if (x == y)
            x = y + 3 + z;

        if (x >= y + 3)
            x = y + 3 + z;

        return x;
    }

    public static int mixOfRegions2Paths2(int x, int y) {
        int z = 10;

        if ((x == y))
            if (x < 10)
                z = 3 + x;
            else
                z = 3;
        return z;
    }


    public static int mixOfRegions2Paths2ComplexCond(int x, int y) {
        int z = 10;

        if ((x == y) && (x < 20))
            if (x < 10)
                z = 3 + x;
            else
                z = 3;
        return z;
    }


    public static int mixOfRegions2PathsDepth3(int x, int y) {
        int z = 10;

        if (x == y)
            if (x < 10)
                z = 3 + x;
            else if (x < 20)
                z = 3;
        return z;
    }

    public static int doubleBranchCov(int x, int y) {

        for (int i = 0; i < 2; i++) {
            if (x == 1)
                x = y;
            else
                x = x + 1;
        }
        if (x == y)
            return 1;
        else
            return 0;
    }

    public static int unoptimalDFS(int x, int y) {
        if (x == y)
            return 1;

        for (int i = 0; i < 2; i++) {
            if (x == 1)
                x = y + 3;
            else if (y > 1)
                x = x + 1;
            else
                x = x + 2;
        }
        return x;
    }


    public static int doubleLoopUnoptimalDFS(int x, int y) {
        int j = 0;
        if (x == y)
            return 1;

        for (int i = 0; i < 2; i++) {
            if (x == 1)
                x = y + 3;
            else if (y > 1) {
                while (j < 4) {
                    x = x + 1;
                    j++;
                }
            } else
                x = x + 2;
        }
        return x;
    }


    public static int doubleLoop(int x, int y) {

        int j = 0;
        for (int i = 0; i < 2; i++) {
            while (j < 4) {
                x = x + 1;
                j++;
            }
        }
        return x;
    }


    public static int complexBranchCov(int x, int y) {

        for (int i = 0; i < 2; i++) {
            if (x == 1)
                if (x == y)
                    x = 0;
                else
                    x = x + 1;
        }
        return x;
    }

//    public static int branchMethodCov(int x, int y) {
//
//        A a = new A();
//        for (int i = 0; i < 2; i++) {
//            if (x == 1)
//                x = y;
//            else
//                x = x + a.branchInA(true);
//        }
//        //  Debug.printPC("printing pc at the end of the path");
//        return x;
//    }
//
//    public static int c(int x, int y) {
//
//        A a = new A();
//        for (int i = 0; i < 2; i++) {
//            if (x == 1)
//                x = y;
//            else
//                x = x + a.fib(x);
//        }
//        //  Debug.printPC("printing pc at the end of the path");
//        return x;
//    }

//    public static int doubleRec(int x, int y) {
//
//        A a = new A();
//        for (int i = 0; i < 2; i++) {
//            if (x == 1)
//                x = y;
//            else
//                x = x + a.fibA(3);
//        }
////          Debug.printPC("printing pc at the end of the path");
//        return x;
//    }

    public static int arrayLoadStore0(int index, int length) {
        int[] x = {300, 400};
        int temp = 1;
        if (index >= 0 && index < 2) {
            if (length <= 0) {
                temp = 2;
            } else {
                x[index] = temp + 2;
                temp = x[index] + 2;
                x[index] = temp + 2;
            }
            return x[index];
        }
        return 1;
    }

    public static int testingSoundReach(int x, int y) {

        int z = 5;

        for (int i = 0; i < 2; i++)
            if (x > y)
                z = z + 1;
            else
                z = testingSoundReach2(y) + z;


        return z;
    }

    public static int testingSoundReach2(int y) {
        if (y > 100)
            return y + 10;
        else
            return y + 20;
    }

//    public static int staticFieldTest(int x, int y) {
//        A a = new A();
//        int output = 0;
//        if (x > 1) {
////            staticField = x;
//            inlineForStatic(x, x);
//        } else {
//            a.bRef.inlineInB(y, y);
//        }
//        /*else
//            inlineForStatic(y, y);*/
////            staticField = 1;
//
//        return 1;
//    }

    public static void inlineForStatic(int x, int y) {
        if (x > 2)
            staticField1 = 1;
        else staticField1 = x + 1;

        /*if (y > 1)
            staticField = y + 1;*/
    }


}
