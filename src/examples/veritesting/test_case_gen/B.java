package veritesting.test_case_gen;

public class B {

    int b;
    veritesting.test_case_gen.A aRef = new veritesting.test_case_gen.A();

    public int getIncB(){
        return ++b;
    }

    public int getIncA(){
        return aRef.getIncB();
    }
}
