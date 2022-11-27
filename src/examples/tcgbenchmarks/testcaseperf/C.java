package tcgbenchmarks.testcaseperf;

public class C {

    int a = 100;
    tcgbenchmarks.testCasePerf.B bRef = new tcgbenchmarks.testCasePerf.B();

    public int getIncA() {
        return ++a;
    }

    public int getIncB() {
        return bRef.getIncB();
    }

    public int getIncAThroughB() {
        return bRef.getIncA(this);
    }

    public int branchInA(boolean x) {
        if (x)
            return 1;
        else return 2;
    }

    public int fib(int n) {
        if (n <= 1)
            return n;
        return fib(n - 1) + fib(n - 2);
    }

    public int fibA(int n) {
        if (n <= 1)
            return n;
        return fibA(n - 1) + fibA(n - 2);
    }
}
