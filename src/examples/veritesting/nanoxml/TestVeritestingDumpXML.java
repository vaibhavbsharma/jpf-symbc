package veritesting.nanoxml;

import java.util.ArrayList;

public class TestVeritestingDumpXML {

    void testHarness(DumpXMLEqCheck v, char c0, char c1, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
        int outSPF = SPFWrapper(v, c0, c1, c2, c3, c4, c5, c6, c7, c8);
        int outJR = JRWrapper(v, c0, c1, c2, c3, c4, c5, c6, c7, c8);
        checkEquality(outSPF, outJR);
    }

    public void checkEquality(int outSPF, int outJR) {
        if (outSPF == outJR) System.out.println("Match");
        else {
            System.out.println("Mismatch");
            assert(false);
        }
//        assert(outSPF == outJR);
    }

    public int SPFWrapper(DumpXMLEqCheck v, char c0, char c1, char c2, char c3, char c4, char c5,
                                        char c6, char c7, char c8) {
        return NonVeritest(v, c0, c1, c2, c3, c4, c5, c6, c7, c8);
    }

    // This is a special method. Call this method to prevent SPF from veritesting any regions that appear in any
    // function or method call higher up in the stack. In the future, this call to SPFWrapperInner can be changed to
    // be a generic method call if other no-veritesting methods need to be invoked.
    private int NonVeritest(DumpXMLEqCheck v, char c0, char c1, char c2, char c3, char c4, char c5,
                                         char c6, char c7, char c8){
        return SPFWrapperInner(v, c0, c1, c2, c3, c4, c5, c6, c7, c8);
    }

    private int SPFWrapperInner(DumpXMLEqCheck v, char c0, char c1, char c2, char c3, char c4, char c5,
                                              char c6, char c7, char c8) {
        int ret = v.testFunction(c0, c1, c2, c3, c4, c5, c6, c7, c8);
        return ret;
    }

    public int JRWrapper(DumpXMLEqCheck v, char c0, char c1, char c2, char c3, char c4, char c5, char c6,
                                       char c7, char c8) {
        return v.testFunction(c0, c1, c2, c3, c4, c5, c6, c7, c8);
    }

    public void runTest(DumpXMLEqCheck t) {
        testHarness(t, '1', '2', '3', '4', '5', '6', '7', '8', '9');
    }
};
