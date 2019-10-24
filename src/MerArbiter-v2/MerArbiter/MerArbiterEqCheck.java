package MerArbiter;

import edu.vanderbilt.isis.sm.Event;
import edu.vanderbilt.isis.sm.Region;
import org.sosy_lab.sv_benchmarks.Verifier;

import java.util.ArrayList;

public class MerArbiterEqCheck {

    ArrayList<Region> runMerArbiter() {
        MerArbiter t = new MerArbiter();
        t.setUser1Input(Verifier.nondetInt(),Verifier.nondetBoolean());
        t.setUser2Input(Verifier.nondetInt(),Verifier.nondetBoolean());
        Event e = new Event("");
        t.runOneStep(e);
        return t.arbiter.sm.regions;
    }


    public static void main(String[] args) {
        TestMerArbiter t = new TestMerArbiter();
        MerArbiterEqCheck s = new MerArbiterEqCheck();
        t.runTest(s);
    }

    ArrayList<Region> testFunction() {
        return runMerArbiter();
    }
}