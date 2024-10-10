package veritesting;

import gov.nasa.jpf.symbc.Debug;

public class JBMCRegList1 extends TestRegionBaseClass {

    static class LinkedListEntry {
        public LinkedListEntry Next;
        public int Value;
    }

    static class LinkedList {
        public LinkedListEntry Head;

        public int size() {
            int count = 0;
            for (LinkedListEntry entry = Head; entry != null; entry = entry.Next)
                ++count;
            return count;
        }

        public void add(int index, int e) {
            LinkedListEntry newEntry = new LinkedListEntry();
            newEntry.Value = e;
            if (index == 0) {
                Head = newEntry;
                return;
            }
            LinkedListEntry entry = Head;
            for (int i = 1; i < index; ++i)
                entry = entry.Next;
            entry.Next = newEntry;
        }

        public void add(int e) { add(size(), e); }

        public void remove(int index) {
            LinkedListEntry entry = Head;
            for (int i = 1; i < index; ++i)
                entry = entry.Next;
            entry.Next = entry.Next.Next;
        }

        public int get(int index) {
            LinkedListEntry entry = Head;
            for (int i = 0; i < index; ++i)
                entry = entry.Next;
            return entry.Value;
        }
    }

    static class Utils_synthesis {
        public static int accumulator(int aggregated, int e) {
            if (e % 2 == 0)
                if (aggregated < e)
                    return e;
            return aggregated;
        }

        public static boolean predicate(int lhs) { return true; }
    }

    public static class Main {
        private static int stream(LinkedList list) {
            // java.util.stream.Stream.filter(...)
            int index = 0;
            for (LinkedListEntry entry = list.Head; entry != null; entry = entry.Next)
                if (Utils_synthesis.predicate(entry.Value))
                    ++index;
                else
                    list.remove(index);

            // java.util.stream.Stream.reduce(...)
            int aggregated = 0;
            for (LinkedListEntry it = list.Head; it != null; it = it.Next)
                aggregated = Utils_synthesis.accumulator(aggregated, it.Value);

            return aggregated;
        }

        public static Outputs compute(int[] args) {
            LinkedList lhs = new LinkedList();
            LinkedList rhs = new LinkedList();
            int size = 1;
            for (int i = 0; i < size; ++i) {
//                int value = Utils_nondet.nondet_int();
                lhs.add(i, args[i]);
                rhs.add(i, args[i]);
            }

//            int lhs_result = 0;
            /*for (LinkedListEntry it = lhs.Head; it != null; it = it.Next) {
                if (it.Value % 2 == 0)
                    if (lhs_result < it.Value)
                        lhs_result = it.Value;
            }*/

            int rhs_result = stream(rhs);
            Outputs o = new Outputs();
            o.intOutputs = new int[1];
            o.intOutputs[0] = rhs_result;
            return o;

//            assert(lhs_result == rhs_result);
        }
    }

    @Override
    Outputs testFunction(int in0, int in1, int in2, int in3, int in4, int in5, boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, char c0, char c1, char c2, char c3, char c4, char c5) {
        int[] inputs = {in0, in1, in2, in3, in4, in5, Debug.makeSymbolicInteger("in6")};
        return Main.compute(inputs);
    }

    public static void main(String[] args) {
        TestVeritesting t = new TestVeritesting();
        JBMCRegList1 s = new JBMCRegList1();
        t.runTest(s);
    }
}
