package veritesting.printtokens2_3;

public class TestPrintTokens {
    void testHarness(PrintTokensEqCheck v, char c0, char c1, char c2, char c3, char c4, char c5, char c6, char c7) {
//        int in0 = Verifier.nondetInt();
//        int in1 = 0; //Verifier.nondetInt();
//        int in2 = 0; //Verifier.nondetInt();
        int outSPF = SPFWrapper(v, c0, c1, c2, c3, c4, c5, c6, c7);
        int outJR = JRWrapper(v, c0, c1, c2, c3, c4, c5, c6, c7);
        checkEquality(v, outSPF, outJR);
    }

    public void checkEquality(PrintTokensEqCheck v, int outSPF, int outJR) {
        if (isequal(outSPF, outJR)) System.out.println("Match");
        else {
            System.out.println("Mismatch");
            assert(false);
        }
//        assert(outSPF == outJR);
    }

    private boolean isequal(int outSPF, int outJR) {
        return outJR == outSPF;
        /*if (outSPF.length == outJR.length) {
            for (int i = 0; i < outSPF.length; i++) {
                if (outSPF[i] != outJR[i]) {
                    return false;
                }
            }
            return true;
        }
        System.out.println("length mismatch");
        return false;*/
    }

    public int SPFWrapper(PrintTokensEqCheck v, char c0, char c1, char c2, char c3, char c4, char c5, char c6, char c7) {
        return NoVeritest(v, c0, c1, c2, c3, c4, c5, c6, c7);
    }

    // This is a special method. Call this method to prevent SPF from veritesting any regions that appear in any
    // function or method call higher up in the stack. In the future, this call to SPFWrapperInner can be changed to
    // be a generic method call if other no-veritesting methods need to be invoked.
    private int NoVeritest(PrintTokensEqCheck v, char c0, char c1, char c2, char c3, char c4, char c5, char c6, char c7){
        return SPFWrapperInner(v, c0, c1, c2, c3, c4, c5, c6, c7);
    }

    private int SPFWrapperInner(PrintTokensEqCheck v, char c0, char c1, char c2, char c3, char c4, char c5, char c6, char c7) {
        int ret = v.testFunction(c0, c1, c2, c3, c4, c5, c6, c7);
        return ret;
    }

    public int JRWrapper(PrintTokensEqCheck v, char c0, char c1, char c2, char c3, char c4, char c5, char c6, char c7) {
        return v.testFunction(c0, c1, c2, c3, c4, c5, c6, c7);
    }

    public void runTest(PrintTokensEqCheck t) {
        testHarness(t, 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
    }
};
