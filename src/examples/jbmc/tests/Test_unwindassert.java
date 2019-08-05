package jbmc.tests;
import org.cprover.CProver;

public class Test_unwindassert {

    public static void main(String []args) {
        int a = CProver.nondetInt();
        int ret = 0;
        for (int i = 0; i < 10; i++) {
            ret += a;
        }
        assert ret == a*10;
    }
}
