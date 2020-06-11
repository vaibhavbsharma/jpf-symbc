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

    public int fibB(int n){
        if (n <= 1)
            return n;
        return aRef.fibA(n-1) + aRef.fibA(n-2);
    }
}
