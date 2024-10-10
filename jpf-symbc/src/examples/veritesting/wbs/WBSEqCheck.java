package veritesting.wbs;

public class WBSEqCheck {

    WBS_vt testFunction(int in0, int in1, int in2, int in3, int in4, int in5,
                     boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5) {
        return runWBS(in0, b0, b1, in1, b2, b3);
    }

    WBSEqCheck() {
    }

     WBS_vt runWBS(int pedal1, boolean auto1, boolean skid1, int pedal2, boolean auto2, boolean skid2) {
        WBS_vt wbs = new WBS_vt();
        wbs.update(pedal1, auto1, skid1);
        wbs.update(pedal2, auto2, skid2);
        return wbs;
    }


    public static void main(String[] args) {
        TestVeritestingWBS t = new TestVeritestingWBS();
        WBSEqCheck s = new WBSEqCheck();
        t.runTest(s);
    }
}