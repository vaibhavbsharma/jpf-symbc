package veritesting.wbs;

public class TestVeritestingWBS {


    void testHarness(WBSEqCheck v,
                     int in0, int in1, int in2, int in3, int in4, int in5,
                     boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5) {
        WBS_vt outSPF = SPFWrapper(new WBSEqCheck(), in0, in1, in2, in3, in4, in5, b0, b1, b2, b3, b4, b5);
        WBS_vt outJR = JRWrapper(new WBSEqCheck(), in0, in1, in2, in3, in4, in5, b0, b1, b2, b3, b4, b5);
        checkEquality(v, outSPF, outJR);
    }

    private void checkEquality(WBSEqCheck v, WBS_vt outSPF, WBS_vt outJR) {
        if (outSPF.equals(outJR)) System.out.println("Match");
        else {
            System.out.println("Mismatch");
            assert(false);
        }
//        assert(outSPF == outJR);
    }

    private WBS_vt SPFWrapper(WBSEqCheck v, int in0, int in1, int in2, int in3, int in4, int in5,
                           boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5) {
        return NoVeritest(v, in0, in1, in2, in3, in4, in5, b0, b1, b2, b3, b4, b5);
    }

    // This is a special method. Call this method to prevent SPF from veritesting any regions that appear in any
    // function or method call higher up in the stack. In the future, this call to SPFWrapperInner can be changed to
    // be a generic method call if other no-veritesting methods need to be invoked.
    private WBS_vt NoVeritest(WBSEqCheck v, int in0, int in1, int in2, int in3, int in4, int in5,
                           boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5){
        return SPFWrapperInner(v, in0, in1, in2, in3, in4, in5, b0, b1, b2, b3, b4, b5);
    }

    private WBS_vt SPFWrapperInner(WBSEqCheck v, int in0, int in1, int in2, int in3, int in4, int in5,
                                boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5) {
        WBS_vt ret = v.testFunction(in0, in1, in2, in3, in4, in5, b0, b1, b2, b3, b4, b5);
        return ret;
    }

    private WBS_vt JRWrapper(WBSEqCheck v, int in0, int in1, int in2, int in3, int in4, int in5,
                          boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5) {
        return v.testFunction(in0, in1, in2, in3, in4, in5, b0, b1, b2, b3, b4, b5);
    }

    public void runTest(WBSEqCheck t) {
        testHarness(t, 1, 2, 3, 4, 5, 6,
                true, true, true, true, true, true);
    }
};
