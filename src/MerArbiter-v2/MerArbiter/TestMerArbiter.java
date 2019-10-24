package MerArbiter;

import edu.vanderbilt.isis.sm.Region;

import java.util.ArrayList;

public class TestMerArbiter {

    void testHarness(MerArbiterEqCheck v) {
        ArrayList<Region> outSPF = SPFWrapper(v);
        ArrayList<Region> outJR = JRWrapper(v);
        checkEquality(v, outSPF, outJR);
    }

    public void checkEquality(MerArbiterEqCheck v, ArrayList<Region> outSPF, ArrayList<Region> outJR) {
        if (isequal(outSPF,outJR)) System.out.println("Match");
        else {
            System.out.println("Mismatch");
            assert(false);
        }
//        assert(outSPF == outJR);
    }

    private boolean isequal(ArrayList<Region> outSPF, ArrayList<Region> outJR) {
        if (outJR.size() != outSPF.size()) {
            System.out.println("list sizes mismatch: " + outSPF.size() + ", " + outJR.size());
            return false;
        }
        for (int i = 0; i < outJR.size(); i++) {
            Region rJR = outJR.get(i);
            Region rSPF = outSPF.get(i);
            if (rJR instanceof TopLevelArbiter.REGION_T && rSPF instanceof TopLevelArbiter.REGION_T) {
                TopLevelArbiter.REGION_T.STATE_T s_spf = ((TopLevelArbiter.REGION_T) rSPF).Arbiter3;
                TopLevelArbiter.REGION_T.STATE_T s_jr = ((TopLevelArbiter.REGION_T) rJR).Arbiter3;
                if (!s_spf.equals(s_jr)) return false;
            } else if (!rJR.getClass().toString().equals(rSPF.getClass().toString())) {
                System.out.println("class mismatch: " + rJR.getClass().toString() + ", " + rSPF.getClass().toString());
                return false;
            }
        }
        return true;
    }

    public ArrayList<Region> SPFWrapper(MerArbiterEqCheck v) {
        return NoVeritest(v);
    }

    // This is a special method. Call this method to prevent SPF from veritesting any regions that appear in any
    // function or method call higher up in the stack. In the future, this call to SPFWrapperInner can be changed to
    // be a generic method call if other no-veritesting methods need to be invoked.
    private ArrayList<Region> NoVeritest(MerArbiterEqCheck v){
        return SPFWrapperInner(v);
    }

    private ArrayList<Region> SPFWrapperInner(MerArbiterEqCheck v) {
        ArrayList<Region> ret = v.testFunction();
        return ret;
    }

    public ArrayList<Region> JRWrapper(MerArbiterEqCheck v) {
        return v.testFunction();
    }

    public void runTest(MerArbiterEqCheck t) {
        testHarness(t);
    }
};
