package veritesting.test_case_gen;

import veritesting.Outputs;

public class TestCasePerf {

    int sideEffect = 0;

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
//        testingComplexConditions2(true, true);
//        testingComplexConditions3(true, true, true, true);

//        (new TestCasePerf()).testingER1(true, 1);
        (new TestCasePerf()).testERInline(true, 1);
//        simpleRegion(1);
    }


    public static int staticMethod2(int x) {
        int myCount = 0;
        if (x > 100) {
            myCount = 1;
        } else {
            myCount = 3;
        }
        return myCount;
    }
    public static int staticMethod1(int x) {
        int myCount = 0;
        if (x > 10) {
            myCount = staticMethod2(x);
        } else {
            myCount = 2;
        }
        return myCount;
    }

    public static int simpleRegion(int y) {
        int methodCount = 0;
        if (y > 0)
            methodCount = staticMethod1(y);

        return methodCount;
    }


    private static int testingComplexConditions1(boolean a, boolean b) {
        int z;
        if ((a && b)) {
            z = 1;
        } else {
            z = 0;
        }
        return z;
    }

    private static int testingComplexConditions2(boolean a, boolean b) {
        int z;
        if ((a || b)) {
            z = 1;
        } else {
            z = 0;
        }
        return z;
    }


    private static int testingComplexConditions3(boolean a, boolean b, boolean c, boolean d) {
        int z;
        if ((a && c) || (b)) {
            z = 1;
        } else {
            z = 0;
        }
        return z;
    }

    private int testERInline(boolean a, int x) {
        int z = 0;
        if (a)
            z = testingER1(a, x);
        assert !(a && x > 2) || this.sideEffect == 0;
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


    private int testingER2(boolean a, boolean b, int x) {
        int z = 1;
        if (a) {
            return x + 1;
        } else if (b)
            return z;
        else
            return 5;
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

        if (x == y)
            x = new A().a + y;
        else
            x = new B().b + y;

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

        if (x == y)
            z = new A().a;

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

    public static int branchMethodCov(int x, int y) {

        veritesting.test_case_gen.A a = new veritesting.test_case_gen.A();
        for (int i = 0; i < 2; i++) {
            if (x == 1)
                x = y;
            else
                x = x + a.branchInA(true);
        }
        //  Debug.printPC("printing pc at the end of the path");
        return x;
    }

    public static int c(int x, int y) {

        veritesting.test_case_gen.A a = new veritesting.test_case_gen.A();
        for (int i = 0; i < 2; i++) {
            if (x == 1)
                x = y;
            else
                x = x + a.fib(x);
        }
        //  Debug.printPC("printing pc at the end of the path");
        return x;
    }

    public static int doubleRec(int x, int y) {

        veritesting.test_case_gen.A a = new veritesting.test_case_gen.A();
        for (int i = 0; i < 2; i++) {
            if (x == 1)
                x = y;
            else
                x = x + a.fibA(3);
        }
//          Debug.printPC("printing pc at the end of the path");
        return x;
    }

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

    private static int testingSoundReach2(int y) {
        if (y > 100)
            return y + 10;
        else
            return y + 20;
    }
}
