package veritesting.test_case_gen;

public class A {

    int a;
    veritesting.test_case_gen.B bRef = new veritesting.test_case_gen.B();

    public int getIncA() {
        return ++a;
    }

    public int getIncB() {
        return bRef.getIncB();
    }

    public int getIncAThroughB(){
        return bRef.getIncA();
    }
}
