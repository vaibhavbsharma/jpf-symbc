package veritesting;

public class ERWithSideEffect {
    int sideEffect = 0;

    public static void main(String[] args) {

        (new ERWithSideEffect()).testERInline(true, 1);
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
            return z;
        }

        this.sideEffect = 5;

        z++;


        return z;
    }
}
