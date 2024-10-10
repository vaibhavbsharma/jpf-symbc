package veritesting.schedule2_3;

import static veritesting.schedule2_3.Schedule2.block_queue;

public class Schedule2EqCheck {
    Process[] runSchedule2(int in0, int in1, int in2) {
        Schedule2 t = new Schedule2();
        t.mainProcess(in0, in1, in2);
        return block_queue;
    }


    public static void main(String[] args) {
        TestSchedule2 t = new TestSchedule2();
        Schedule2EqCheck s = new Schedule2EqCheck();
        t.runTest(s);
    }

    Process[] testFunction(int in0, int in1, int in2) {
        return runSchedule2(in0, in1, in2);
    }
}