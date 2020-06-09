package veritesting.test_case_gen;

public class TestCasePerf {

    public static void main(String[] args) {
        /*veritesting.test_case_gen.A myA = new veritesting.test_case_gen.A();
        int myVal = myA.getIncA();
        System.out.println("my A value is = " + myVal);*/
        singleBranchCov(1,1);
    }

    public static int singleBranchCov(int x, int y) {

        for (int i = 0; i < 3; i++) {
            if ((x & 1) == 1)
                x = y;
            else x = x + 2;
        }
        return x;
    }

}
