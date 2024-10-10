package veritesting.tcas;

import veritesting.Outputs;

public class TestVeritestingTCAS {


    void testHarness(TCASEqCheck v,
                     int in0, int in1, int in2, int in3, int in4, int in5,
                     int in6, int in7, int in8, int in9, int in10, int in11) {
        Outputs outSPF = SPFWrapper(new TCASEqCheck(), in0, in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11);
        Outputs outJR = JRWrapper(new TCASEqCheck(), in0, in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11);
        checkEquality(v, outSPF, outJR);
    }

    private void checkEquality(TCASEqCheck v, Outputs outSPF, Outputs outJR) {
        if (outSPF.equals(outJR)) System.out.println("Match");
        else {
            System.out.println("Mismatch");
            assert(false);
        }
//        assert(outSPF == outJR);
    }

    private Outputs SPFWrapper(TCASEqCheck v, int in0, int in1, int in2, int in3, int in4, int in5,
                               int in6, int in7, int in8, int in9, int in10, int in11) {
        return NoVeritest(v, in0, in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11);
    }

    // This is a special method. Call this method to prevent SPF from veritesting any regions that appear in any
    // function or method call higher up in the stack. In the future, this call to SPFWrapperInner can be changed to
    // be a generic method call if other no-veritesting methods need to be invoked.
    private Outputs NoVeritest(TCASEqCheck v, int in0, int in1, int in2, int in3, int in4, int in5,
                               int in6, int in7, int in8, int in9, int in10, int in11) {
        return SPFWrapperInner(v, in0, in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11);
    }

    private Outputs SPFWrapperInner(TCASEqCheck v, int in0, int in1, int in2, int in3, int in4, int in5,
                                    int in6, int in7, int in8, int in9, int in10, int in11) {
        Outputs ret = v.testFunction(in0, in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11);
        return ret;
    }

    private Outputs JRWrapper(TCASEqCheck v, int in0, int in1, int in2, int in3, int in4, int in5,
                              int in6, int in7, int in8, int in9, int in10, int in11) {
        return v.testFunction(in0, in1, in2, in3, in4, in5, in6, in7, in8, in9, in10, in11);
    }

    public void runTest(TCASEqCheck t) {
        testHarness(t, 1, 2, 3, 4, 5, 6,
                6, 7, 8, 9, 10, 11);
    }
};
