package veritesting.schedule2_3;

import org.sosy_lab.sv_benchmarks.Verifier;

import java.util.Objects;

public class TestSchedule2 {
    void testHarness(Schedule2EqCheck v, int in0, int in1, int in2) {
//        int in0 = Verifier.nondetInt();
//        int in1 = 0; //Verifier.nondetInt();
//        int in2 = 0; //Verifier.nondetInt();
        Process[] outSPF = SPFWrapper(v, in0, in1, in2);
        Process[] outJR = JRWrapper(v, in0, in1, in2);
        checkEquality(v, outSPF, outJR);
    }

    public void checkEquality(Schedule2EqCheck v, Process[] outSPF, Process[] outJR) {
        if (isequal(outSPF, outJR)) System.out.println("Match");
        else {
            System.out.println("Mismatch");
            assert(false);
        }
//        assert(outSPF == outJR);
    }

    private boolean isequal(Process[] outSPF, Process[] outJR) {
        if (outSPF.length == outJR.length) {
            for (int i = 0; i < outSPF.length; i++) {
                if (!Objects.equals(outSPF[i], outJR[i])) {
                    return false;
                }
            }
            return true;
        }
        System.out.println("length mismatch");
        return false;
    }

    public Process[] SPFWrapper(Schedule2EqCheck v, int in0, int in1, int in2) {
        return NoVeritest(v, in0, in1, in2);
    }

    // This is a special method. Call this method to prevent SPF from veritesting any regions that appear in any
    // function or method call higher up in the stack. In the future, this call to SPFWrapperInner can be changed to
    // be a generic method call if other no-veritesting methods need to be invoked.
    private Process[] NoVeritest(Schedule2EqCheck v, int in0, int in1, int in2){
        return SPFWrapperInner(v, in0, in1, in2);
    }

    private Process[] SPFWrapperInner(Schedule2EqCheck v, int in0, int in1, int in2) {
        Process[] ret = v.testFunction(in0, in1, in2);
        return ret;
    }

    public Process[] JRWrapper(Schedule2EqCheck v, int in0, int in1, int in2) {
        return v.testFunction(in0, in1, in2);
    }

    public void runTest(Schedule2EqCheck t) {
        testHarness(t, 0, 0, 0);
    }
};
