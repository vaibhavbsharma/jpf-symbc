import javax.management.modelmbean.XMLParseException;
import gov.nasa.jpf.symbc.Debug;

public class VeritestingPerf {

    public int count = 0;
    public static char[] buffer = new char[81];

    public static int patParaIndex = 1;

    public static void main(String[] args) {

        /**************** create New Object Tests ************/
        //(new VeritestingPerf()).createObjectTC8(true, true);
        //(new VeritestingPerf()).createObjectTC08(true, true,1);
        //(new VeritestingPerf()).createObjectComplexRegionTC8(true, true);
        //(new VeritestingPerf()).arrayLoad1(null, 0, 2);
        //(new VeritestingPerf()).createObjectTC5(false, false);

        //(new VeritestingPerf()).regionTest(1);
/*

        String string = "test the example in the paper";
        ArrayList<Character> textList = new ArrayList<Character>();
        for (char c : string.toCharArray()) {
            textList.add(c);
        }
        (new VeritestingPerf()).paperExample(textList);

*/


        //int y = (new VeritestingPerf()).fixPointExample1(1);
        //int y = (new VeritestingPerf()).fixPointExample2(1);
        //int y = (new VeritestingPerf()).fixPointExample3(1);
        //int y = (new VeritestingPerf()).fixPointExample4(1);
/*
        if ((new VeritestingPerf()).isalnum('a'))
            System.out.println("finished then");
        else
            System.out.println("finished else");*/

        //(new VeritestingPerf()).isalnum2('a');


/*

        if ((new VeritestingPerf()).isint1(1) )
            System.out.println("finished then");
        else
            System.out.println("finished else");
*/
/*

        if ((new VeritestingPerf()).erTest1('a'))
            System.out.println("finished then");
        else
            System.out.println("finished else");

*/



/*

        if((new VeritestingPerf()).isint2(1)==1)
            System.out.println("finished then");
        else
            System.out.println("finished else");
*/


/*

        if((new VeritestingPerf()).isint3(1)==1)
            System.out.println("finished then");
        else
            System.out.println("finished else");
*/

//        (new VeritestingPerf()).paperExampleNum(1, 1, 1, 1, 1, 1);


        /************** early return ************/
        /*int val = (new VeritestingPerf()).earlyReturn2(1, 2, 3);
        if(Debug.isSymbolicInteger(val))
            System.out.println("return is symbolic :) value after early return is : " + val);
        else
            System.out.println("return is NOT symbolic :(");*/
        /************** high order region ************/
        //VeritestingPerf.staticMethod1(1);
        //(new VeritestingPerf()).method1(1);
        //(new VeritestingPerf()).highOrd3(1);


        /****************** arrayLoad Tests ********************/
        //(new VeritestingPerf()).testSegment2(true, true, 2);
        //(new VeritestingPerf()).inRangeloadArrayTC(22, 2);
//        (new VeritestingPerf()).innerCatchOutRangeloadArrayTC(22, 2);
//        (new VeritestingPerf()).outRangeloadArrayTC( 22, 2);
        // (new VeritestingPerf()).catchOutRangeloadArrayTC(22, 2);
        //(new VeritestingPerf()).boundedOutRangeloadArrayTC(22, 2);
        //(new VeritestingPerf()).segmantTest(22, 2);
        //(new VeritestingPerf()).assertRegions(true,true);

        /****************** arrayStore Tests ********************/
        //int a[] = {200, 300};
        //(new VeritestingPerf()).arrayStoreTC1(1, 2);
        //(new VeritestingPerf()).arrayStoreTC2(1, 2, a);
        //(new VeritestingPerf()).arrayStoreTC3(1, 2, a);
        //(new VeritestingPerf()).arrayStoreTC4(1, 2, a);
        //(new VeritestingPerf()).arrayStoreTC5(1, 2, a);
        //(new VeritestingPerf()).arrayStoreTC6(1, 2);
        //(new VeritestingPerf()).arrayStoreTC7(1, 2);
        //(new VeritestingPerf()).arrayLoadTC1(1,2);

        //(new VeritestingPerf()).arrayLoad0(1,2);

        /****************** getFieldSPFCases Tests ********************/
        //(new VeritestingPerf()).getFieldSPFCaseTC1(true);
        //(new VeritestingPerf()).fieldTest1(1);
        //(new VeritestingPerf()).fieldTest00(1);

        /****************** ir Tests ********************/
        //(new VeritestingPerf()).irTest1(1);
        //(new VeritestingPerf()).irTest2(1);
        //(new VeritestingPerf()).irTest3(1);
        //(new VeritestingPerf()).irTest4(1);
        //(new VeritestingPerf()).irTest5(1);
        //(new VeritestingPerf()).irTest6(1);


        /*************** highOrderRegionTest **********************/
        //VeritestingPerf.staticMethod1(1);
        //(new VeritestingPerf()).countBitsSet1(1);

        //(new VeritestingPerf()).nestedRegionThrowsException(0);
//        (new VeritestingPerf()).simpleRegionThrowsException(0);


//        (new VeritestingPerf()).cfgTest(1);
//        (new VeritestingPerf()).countBitsSet(1);
        //(new VeritestingPerf()).nestedRegion1(true, true);
        // (new VeritestingPerf()).fieldDoubleTest(1);
//        (new VeritestingPerf()).nestedRegion1(true, true);
//        (new VeritestingPerf()).testNestedMiddle(1);
//        (new VeritestingPerf()).testNested(1);
//        (new VeritestingPerf()).testDynObject(false, 1);
//        int x[] = {1, 2};
//        (new VeritestingPerf()).ifNull("Test");
//        (new VeritestingPerf()).foo(true);
//
//        System.out.println("!!!!!!!!!!!!!!! Start Testing! ");
//        (new VeritestingPerf()).testMe2(0,true);
//        (new VeritestingPerf()).readAfterWriteTest(1);
//        (new VeritestingPerf()).testSimple(1);
//
//        (new VeritestingPerf()).testSimple1(1);
        //(new VeritestingPerf()).staticMethod2(1);
        //(new VeritestingPerf()).mwwNestedIfBranch(1, 2);
        //(new VeritestingPerf()).boundedOutRangeloadArrayTC(1, 1);
        //(new VeritestingPerf()).simpleRegion(1);
        //(new VeritestingPerf()).mwwTestOrIte(true, true, 1);
//        (new VeritestingPerf()).fieldWriteTestBranch2(1);
//        (new VeritestingPerf()).fieldWriteTestBranch1(1);
//        (new VeritestingPerf()).testSimple2(1);
//        (new VeritestingPerf()).testSimpleFail(1);

        //(new VeritestingPerf()).earlyReturn0(1, 1, 1);
        //(new VeritestingPerf()).isint2(1);
//
//        int x[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
//        (new VeritestingPerf()).testMe5(x, 1);
//        (new VeritestingPerf()).testMe6(x, 12, -1, 1);
//        (new VeritestingPerf()).testMe4(x, 12, -1, 1);
//        (new VeritestingPerf()).arrayTest(x, 6);
//        (new VeritestingPerf()).checkOperator();
//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(Debug.makeSymbolicInteger("a1"));
//        list.add(Debug.makeSymbolicInteger("a2"));
//        (new VeritestingPerf()).countArrayList(list);
//
        //(new VeritestingPerf()).earlyReturn4(1, 2, true);

        // (new VeritestingPerf()).wbsConditions(1, 1);

        //(new VeritestingPerf()).testWhileProblem1(1, 1);

        //(new VeritestingPerf()).testLocalOutputs(1, 1, 1);

        //(new VeritestingPerf()).recursiveIsFun(1);

        (new VeritestingPerf()).sheepAndGoat(1);
    }

    public int testLocalOutputs(int _patParaIndex, int _patIndex, int _lastj) {
        int lastj = _lastj; // stack slot = 3
        int lj = _patIndex; // stack slot = 4
        boolean done = false; // stack slot = 5
//        patIndex = _patIndex;
        char[] pat = {'a', 'b'}; // stack slot = 6
        patParaIndex = _patParaIndex;
        if (patParaIndex > 0) {
//            _lastj++; // stack slot = 2
//            lastj++; // stack slot = 3
            lj = lastj; // stack slot = 4
            if (lj >= 0 && lj < 2) {
//                char tempChar = pat[lj]; // stack slot = 7
//                if (pat[lj] == 'a') {
//                    done = true;
//                }
            }
        } else {
//            lj++;
        }

        return 1;
    }

    public int sheepAndGoat(int i) {
        int j = 0;

        //System.out.println(Integer.toBinaryString(i));
        int hasZeroTrail = 0;

        if(i>1 && i<2){
        while (i != 0) {
            hasZeroTrail = (i & 1);
            if (hasZeroTrail == 0) {
             //   System.out.println("inside if-then side with i=" + Debug.getSymbolicIntegerValue(i));
               int numberOfTrailingZeros = Integer.numberOfTrailingZeros(i);
                i = i >> 1;
            } else {
                //System.out.println("inside if-else side with i=" + Debug.getSymbolicIntegerValue(i));
               // Debug.getSymbolicIntegerValue(i);
                j = j >>> 1;
                j = j ^ (Integer.reverse(1));
                i = i >> 1;
                //System.out.println("i value = " + i);
            }
        }
        //System.out.println(Integer.toBinaryString(j));
            //Debug.printPC("PC in loop");
        return j;}
        //Debug.printPC("PC out loop");
        return 1;
    }

    public int testGoTo(int i) {
        int j = 0;

        while (j <= 1) {
            if (i == 0) {
                i >>= 2;
            } else
                ++i;
            ++j;
        }
        System.out.println(Integer.toBinaryString(j));
        return j;
    }


    private int recursiveIsFun(int n) { //implementing a
        int x = 0;
        if (n > 0 && n < 3)
            x = fib2(n);

        return x;
    }


    public int fib2(int n) {
        if (n != 0)
            return 0;
        else if (n == 1)
            return 1;
        else {
            int n1 = fib1(n - 1);
            //int n2 = fibMediator(n - 2);
            //return n1 + n2;
            return n + n1;
        }
    }


    public int fib1(int n) {
        if (n != 0)
            return 0;
        else if (n == 1)
            return 1;
        else {
            int n1 = fib2(n - 1);
            //int n2 = fibMediator(n - 2);
            //return n1 + n2;
            return n + n1;
        }
    }

    private int fibMediator(int n) {
        return fib2(n);
    }

    public int testWhileProblem1(int i, int j) {
        int x = 0;
        while (i != 0) {
            if (j != 0) {
                x = 5;
            } else {
                x = 6;
            }
        }
        return x;

    }


    public int testWhileProblem2(int i, int j) {
        int x = 0;
        while (i != 0) {
            if (j != 0) {
                x = 5;
            } else {
                x = 6;
            }
            ;
        }
        return x;

    }


    public int testWhileProblem3(int i, int j) {
        int x = 0;
        while (i != 0) {
            if (j != 0) {
                x = 5;
            } else {
                x = 6;
            }
            System.out.println("after the if statement");
        }
        return x;

    }

    public int wrapper(boolean x, int y) {

        if (x) {
            try {
                errorExpectedInput("hi");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return y;
        } else
            return y + 1;

    }

    public int wbsConditions(int WBS_Node_WBS_BSCU_Switch3, int WBS_Node_WBS_AccumulatorValve_Switch) {

        int WBS_Node_WBS_AS_MeterValve_Switch;

        if ((WBS_Node_WBS_BSCU_Switch3 == 0)) {
            WBS_Node_WBS_AS_MeterValve_Switch = 0;
        } else {
            if ((WBS_Node_WBS_AccumulatorValve_Switch == 1)) {
                WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 4);
            } else {
                if ((WBS_Node_WBS_BSCU_Switch3 == 2)) {
                    WBS_Node_WBS_AS_MeterValve_Switch = (WBS_Node_WBS_AccumulatorValve_Switch / 2);
                } else {
                    if ((WBS_Node_WBS_BSCU_Switch3 == 3)) {
                        WBS_Node_WBS_AS_MeterValve_Switch = ((WBS_Node_WBS_AccumulatorValve_Switch / 4) * 3);
                    } else {
                        if ((WBS_Node_WBS_BSCU_Switch3 == 4)) {
                            WBS_Node_WBS_AS_MeterValve_Switch = WBS_Node_WBS_AccumulatorValve_Switch;
                        } else {
                            WBS_Node_WBS_AS_MeterValve_Switch = 0;
                        }
                    }
                }
            }
        }
        return WBS_Node_WBS_AS_MeterValve_Switch;
    }

    protected void errorExpectedInput(String expectedString) throws NullPointerException {
        String str = "Expected: " + expectedString;
//    	int nr = this.reader.getLineNr();
//      throw new XMLParseException(nr,s);
        throw new NullPointerException(str);
    }

    public boolean erTest1(char ch) {
        char[] tokens = get_token(ch);
        boolean is_spec_symbol = tokens[0] == '(';
        System.out.print("finished the program.");
        return is_spec_symbol;
    }

    public char[] get_token(char ch) {
        int id = 0, i = 0;

        if (ch == '"') {
            id = 1;
        } else if (ch == 59) {
            id = 2;
        }
        if (id == 1) {
            i = i + 1;
            if (i <= 80) {
                buffer[i] = ch;
            }
            return buffer;
        } else if (id == 0) {
            if (ch == 59) {
                return buffer;
            }
        }
        return buffer;
    }


    private static boolean isalnum(char ch) {
        if (ch >= '0') {
            if (ch <= '9') {
                return true;
            } else if (ch >= 'A') {
                if (ch <= 'Z') {
                    return true;
                } else if (ch >= 'a') {
                    if (ch <= 'z') {
                        return true;
                    }
                }
            }
        }
        return false;
    }


/*

    private static boolean isalnum(int ch) {
        if (ch >= 1 ) {
            if (ch <= 9) {
                return true;
            } else if (ch >= 4 ) {
                if (ch <= 6) {
                    return true;
                } else if (ch >= 7 ) {
                    if (ch <= 6 ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
*/


    private static boolean isalnum2(char ch) {
        if (ch >= 'A') {
            if (ch <= 'Z') {
                return true;
            } else {
                if (ch >= 'a') {
                    if (ch <= 'z') {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
/*

    private static int isint1(int a) {
        int val = 100;

        if (a >= 1) {
            if (a > 5)
                return 1;
            else
                return 2;
        } else {
            val = 200;
        }
        return val;
    }
*/


    private static boolean isint1(int a) {
        int val = 100;

        if (a >= 1) {
            return isalnum('a');
        } else {
            val = 200;
        }


        return true;
    }


    private static int isint2(int a) {
        int val;

        if (a >= 1) {
            val = 200;
        } else {
            val = 500;

        }
        return val;
    }


    private static int isint3(int a) {
        int val = 100;

        if (a >= 1) {
            return 200;
        } else {
            return 1;

        }
    }
/*
    private int regionTest(int i) {
        int a = 1;
        if (i != 0) {
            if (i < 10)
                i = i + 5;
        } else a = 10 + i;


        ArrayList<Integer> list = new ArrayList<Integer>(200);
        list.add(i);
        list.get(1);
        return a;
    }*/
/*
        public int simpleRegionThrowsException(int i) {
            int ret = -1;
            TempClass tempClass = new TempClass();
            if (i < 0) {
                throw new NullPointerException("negative");
    //            new NullPointerException("negative");
    //            assert true;
            }
            else ret = ret + tempClass.getTempInt();
            ret += 1;
            return ret;
        }

        public int simpleRegionEarlyReturn(int i) {
            int ret = -1;
            TempClass tempClass = new TempClass();
            if (i < 0)
                return -1;
            else ret = ret + tempClass.getTempInt();
            ret += 1;
            return ret;
        }

*/

    //Tests no early return case.

    public static int earlyReturn0(int x, int y, int z) {
        if (x > 1) {
            if (y > 2)
                return x = x * y;
            else
                x = 4;
        } else {
            if (y == 4)
                return x = x + y;
            x = x + y;
        }
        return x;
    }

/*    // tests early return on one side of the branch, with some continuation after the nominal case.
    public static int earlyReturn1(int x, int y, int z) {
        if (x > 1) {
            return x * y;
        } else {
            x = x + y;
        }
        if (x > 10)
            x = x + 3;
        return x;
    }
*/

/*

    public int paperExampleNum(int x1, int x2, int x3, int x4, int x5, int x6) {

        ArrayList<Integer> numberList = new ArrayList<Integer>(Arrays.asList(x1, x2, x3, x4, x5, x6));

        int wordCount = 0;
        boolean inWord;

        if (numberList.size() > 0) { //would be nice if we have early return here.
            int firstEement = numberList.get(0);
            if (x1 == 0)
                inWord = false;
            else
                inWord = true;

            for (int i = 0; i < numberList.size(); i++) {
                if (inWord) {
                    if (numberList.get(i) == 0) { //0 means there is a whitespace
                        ++wordCount;
                        inWord = false;
                    }
                } else {
                    if (numberList.get(i) != 0) { // non whitespace.
                        inWord = true;
                    }
                }
            }
        }
        System.out.println("Number of words is:" + wordCount);
        return wordCount;
    }

*/
/*

    public int paperExample(List<Character> textList) {
        boolean inWord = false;
        int wordCount = 0;

        for (int i = 0; i < textList.size(); i++) {
            if (inWord) {
                if (Character.isWhitespace(textList.get(i))) {
                    ++wordCount;
                    inWord = false;
                }
            } else {
                if (!Character.isWhitespace(textList.get(i))) {
                    inWord = true;
                }
            }
        }

        System.out.println("Number of words is:" + wordCount);

        return wordCount;
    }

*/

/*
    public int fixPointExample1(int x) {
        int wordCount;
        TempClass5 obj1 = new TempClass5();
        TempClass5 obj2 = new TempClass5();

        TempClass5[] class5Array = {obj1, obj2};

        if (x == 0) {
            wordCount = class5Array[0].tempInt2;
        } else
            wordCount = class5Array[1].tempInt2 + 1;


        if (wordCount == 1)
            System.out.println("--fix point result is 1 ");
        else
            System.out.println("--fix point result is:" + wordCount);


        return wordCount;
    }
*/
/*

    public int fixPointExample2(int x) {
        int wordCount;
        TempClass5 obj1 = new TempClass5();
        TempClass5 obj2 = new TempClass5();

        TempClass5[] class5Array = {obj1, obj2};

        if (x == 0) {
            wordCount = class5Array[0].tempMethod();
        } else
            wordCount = class5Array[2].tempInt2 + 1;


        if (wordCount == 2)
            System.out.println("--else side is taken ");
        else
            System.out.println("--then side is taken");

        return wordCount;
    }
*/

/*
    public int fixPointExample3(int x) {
        int wordCount;
        TempClass5 obj1 = new TempClass5();
        TempClass5 obj2 = new TempClass5();

        TempClass5[] class5Array = {obj1, obj2};

        if (x == 0) {
            wordCount = class5Array[0].getTempClasses()[0].method2(1);
        } else
            wordCount = class5Array[1].tempInt2 + 1;

        return wordCount;
    }
*/

/*

    public int fixPointExample4(int x) {
        int wordCount;
        MyTempClass obj1 = new TempClass5();
        MyTempClass obj2 = new MyTempClass();

        MyTempClass[] class5Array = {obj1, obj2};

        if (x == 0) { //calls the method in the drived class
            wordCount = class5Array[0].method2(1);
        } else //calls the method in the base class
            wordCount = class5Array[1].method2(2) + 1;


        if (wordCount == 1)
            System.out.println("--fix point result is 1 ");
        else
            System.out.println("--fix point result is:" + wordCount);


        return wordCount;
    }
*/


    // tests early retrun on both sides of the branch
    public static int earlyReturn1(int x, int y, int z) {
        if (x > 1) {
            return x * y;
        } else {
            return x + y;
        }
    }


    public static int earlyReturn2(int x, int y, int z) {
        if (x > 1) {
            return x * y;
        } else {
            return x + y;
        }
    }

    public static int earlyReturn3(int x, int y, int z) {
        if (x > 1) {
            return x * y;
        } else {
            return x + y;
        }
    }

    //tests inner early return inside a larger if.
    public static int earlyReturn5(int x, int y, int z) {
        if (x > 1) {
            if (y > 1)
                return x * y;
            else
                return x = x * y;
        } else {
            x = x + y;
        }
        x = 3 + y;
        return x;
    }


    // tests early retrun on one side of the branch followed by another early return on both sides.
    public static int earlyReturn4(int x, int y, boolean z) {
        if (x > 1) {
            x = x * y;
            System.out.println("hi then");
        } else {
            System.out.println("hi else");
            x = x + y;
        }

        z = (x != x);

        if (z) {

            return y + x;

        } else {
            return y;
        }
    }





/*
        public static void testMe2(int x, boolean b) {
            System.out.println("!!!!!!!!!!!!!!! First step! ");
            //System.out.println("x = " + x);
            int[] y = {1, 2};
            if (b) {
                x++;
                System.out.println("Program then branch");
            } else {
                x--;
                System.out.println("Program else branch");
            }
            x++;
        }

        private void testNested(int x) {
            testNestedMiddle(x);
            assert (x != 0 && x > 0 ? count == 3 : true);
            assert (x != 0 && x <= 0 ? count == 4 : true);
            assert (x == 0 ? count == 5 : true);
        }

        private int testNestedMiddle(int x) {
            int retval = 0;
            retval += nestedRegion(x);
    //        assert(x != 0 && x > 0 ? count == 3 : true);
    //        assert(x != 0 && x <= 0 ? count == 4 : true);
    //        assert(x ==0 ? count == 5 : true);
            return retval;
        }


    public int nestedRegion(int x) {
        int count = 0;
        int a = 8;
        if (x != 0) {
                count = a / 8;
        } else {
            count = a / 2;
        }
  /*          assert(x != 0 && x > 0 ? count == (a/8) : true);
            assert(x != 0 && x <= 0 ? count == (a/4) : true);
            assert(x ==0 ? count == (a/2) : true);
*/
      /*
        if((x != 0) && (x > 0) && (count == (a/8)))
            System.out.println("right constraint");
        else
            System.out.println("something is wrong");

        if((x != 0) && (x <= 0) && (count == (a/4)))
            System.out.println("right constraint");
        else
            System.out.println("something is wrong");

        if((x != 0) && (count == a/8))
            System.out.println("right constraint");
        else if((x == 0) && (count == a/2))
          System.out.println("constraint is right");

        return count;
    }
/*
        // from ProbExample1
        public int test(int x, int y, int z) {
            if (x > z) {
                //calcProb();
            }
            if (z > 0) {
                //calcProb();
            }
            System.out.println("now");
            if (y > 0) {
                System.out.println("now1");

                //calcProb();
            }
            return 0;

        }
*/
      /*
    public int nestedRegion1(boolean x, boolean y) {
        int a = 0;
        if (y) {
            a = 1;
            if (x) {
                a = 2;
            } else {
                a = 3;
            }
        } else {
            a = 5;
            if (x) a = 6;
            else a = 7;
        }
        if ((y) && a == 4)
            System.out.println("true");

        return a;
    }


*/

/* my copies of test cases
    public int intermediateVarHgRegion(int x) {
        int val = -1;
        if (x != 0)
            val = staticMethod1(x + 1    );

        assert(x==0? val==-1: true);
        assert(x==9? val==2: true);
        assert(x==10? val==1: true);
        assert(x==99? val==3: true);

        return val;
    }

    public static int staticMethod3() {
        int myCount = 3;
        return myCount;
    }

    public static int staticMethod2(int x) {
        int myCount = 0;
        if (x != 100) {
            myCount = 1;
        } else {
            myCount = 3;
        }
        return myCount;
    }


    public static int staticMethod1(int x) {
        int myCount = 0;
        if (x != 10) {
            myCount = staticMethod2(x);
        } else {
            myCount = 2;
        }
        return myCount;
    }


    public int intermediateVarHgRegion2(int x) {
        int val = -1;
        if (x != 0)
            val = staticMethod3();

        assert(x==0? val==-1: true);
        assert(x!=0? val==3: true);
        return val;
    }

    public static int simpleRegion(int y) {
        int methodCount = 0;
        if (y != 100)
            methodCount = staticMethod2(y);
        return methodCount;
    }


*/
    /*
     */

    public static int staticMethod2(int x) {
        int myCount = 0;
        if (x > 100) {
            myCount = 1;
        } else {
            myCount = 3;
        }
        return myCount;
    }

    public static int staticMethod1(int x) {
        int myCount = 0;
        if (x > 10) {
            myCount = staticMethod2(x);
        } else {
            myCount = 2;
        }
        return myCount;
    }


    public int method2(int x) {
        int myCount = 0;
        if (x > 100) {
            myCount = 1;
        } else {
            myCount = 3;
        }
        return myCount;
    }

    public int method1(int x) {
        int myCount = 0;
        TempClass5 myTemp = new TempClass5();

        if (x > 10) {
            myCount = myTemp.method2(x);
        } else {
            myCount = 2;
        }
/*

        assert(x<=10? myCount==2: true);
        assert(x>10 && x<=100? myCount==3: true);
        assert(x>100? myCount==1: true);
*/


        return myCount;
    }


    public int fieldTest1(int x) {
        count = x;
        for (int i = 0; i < 2; i++)
            if (count % 2 != 0) count += 1;
        return count;
    }
//
/*
    public static int simpleRegion(int y) {
        int methodCount = 0;
        if (y != 100)
            methodCount = staticMethod1(y);
        System.out.println("after caller if-condition.");
        return methodCount;
    }
*/

/*
    public int mwwNestedIfBranch(int x, int y) {
        if (x < y) {
            if (y < 100) {
                y = 100;
            } else {
                y = y * 2;
            }
        } else {
            y = x;
        }
        return y;
    }*/

//TODO: Need work here SOHA
/*
    public static int staticMethod2(int x) {
        int myCount = 0;
        if (x != 100) {
            myCount = 1;
        } else {
            myCount = 3;
        }
        if (myCount == 1)
            System.out.println("taken the then side.");
        return myCount;
    }

    public static int staticMethod1(int x) {
        int myCount = 0;
        if (x != 10) {
            myCount = staticMethod2(x);
        } else {
            myCount = 2;
        }
        return myCount;
    }

    public static int simpleRegion(int y) {
        int methodCount = 0;
        if (y != 100)
            methodCount = staticMethod1(y);
        System.out.println("after if-condition");
        assert ((y == 100) ? methodCount == 0 : true);
        assert ((y == 10) ? methodCount == 2 : true);
        return methodCount;
    }
*/
/*
    public int mwwTestOrIte(boolean x, boolean y, int a) {
        if (x || y) {
            a = a + 1;
        } else {
            a = a - 1;
        }
        return a;
    }*/


    /*
    public int fieldWriteTestBranch2(int x) {
        if (x != 0) count = 1;
        else count = 2;
        return count;
    }
*/



/*


    public int countBitsSetSimple(int x) {
        int count = 0;
        while (x != 0) {
            if ((x & 1) != 0) {
                count += 1;
            }
            x = x >>> 1; // logical right shift
        }
        return count;
    }

    public int highOrd3(int x) {
        int val = -1;
        HO3Base ho3 = new HO3Derived();
        if (x != 0)
//            val += 1;
            val = ho3.method1(x);
        return val;
    }



    public int mwwTestAndIte(boolean x, boolean y, int a) {
//        int a = 0;
        if (x && y) {
            a = a + 1;
        } else {
            a = a - 1;
        }
        return a;
    }


    public int mwwTestComplexCondition1(boolean w, boolean x, boolean y, boolean z, int a) {
        if ((w && x) || (y && z)) {
            a = a + 1;
        } else {
            a = a * 2;
        }
        return a;
    }
*/

  /*
        public int countBitsSet(int x) {
            TempClass tempClass = new TempClassDerived();
            int count = 0;
            int a = 1;
            int xOrig = x;
            //TempClass tempClass = new TempClass();
            while (x != 0) {
                if ((x & 1) != 0) {
                    // nested field access test case 1
                    //count += tempClass.tempClass2.tempInt2;
                    // nested field access test case 2
                    //TempClass2 tempClass2 = tempClass.tempClass2;
                    //tempClass2.tempInt2 += count;
                    // Test case 3: method summary test + higher order region test

                    count += tempClass.getOne(0);
                    //TempClassDerived.myInt = 1; //creates r/w interference with tempClass.getOne's method summary
                    // Test case 4: use this to test dynamic field access
                    //count += tempClass.myInt;
                    // Test case 5: testing read-after-write in a simple region
                    //count += 1;
    //                a += count;
    //                count += 2;
                    // Test case 6
                    //count += tempClass.nestedRegion(a);
                }
                x = x >>> 1; // logical right shift
            }
            assert (xOrig == 0 || TempClassDerived.tempInt == 6);
            if (x >= -15 && x < 16) assert (Bits.populationCount(xOrig) == count);
            System.out.println("TempClassDerived.tempInt = " + TempClassDerived.tempInt);
            System.out.println("TempClass.tempInt = " + TempClass.tempInt);
            return count;
        }

        public int readAfterWriteTest(int x) {
            TempClass tempClass1 = new TempClassDerived();
            TempClass tempClass2 = new TempClassDerived();
            count = 0;
            int a = 1;
            int xOrig = x;
            //TempClass tempClass = new TempClass();
            while (x != 0) {
                if ((x & 1) != 0) {
                    //tempClass1.tempInt += 1;
                    //a = tempClass2.tempInt; // should not cause a read after write
                    //tempClass1.tempInt += 1;
                    count += 1;
                }
                x = x >>> 1; // logical right shift
            }
            assert (xOrig == 0 ? count == 0 : true);
            assert (isPowerOf2(xOrig) ? count == 1 : true);
            System.out.println("a = " + a);
            return count;
        }

        public int fieldWriteTestBranch2(int x) {
            if (x != 0) count = 1;
            else count = 2;
            return count;
        }

        public int fieldWriteTestBranch1(int x) {
            if (x != 0) count = 1;
            return count;
        }
*/
/*


    public int countBitsSet1(int x) {
        int count = 1;
        int xOrig = x;
        while (x != 0) {
            if ((x & 1) != 0) { //beginning of region
                count += 1;
            } // end of region
            x = x >>> 1; // logical right shift
        }
        return count;
    }
*/

    /*
            //testing inRangeArrayLoad for symbolic & concrete index
            public int inRangeloadArrayTC(int index, int length) {
                int[] x = {300, 400};
                int temp = 1;
                try {
                    if (length <= 0) {
                        //     System.out.println("executing then branch");
                        temp = 2;
                    } else {
                        // System.out.println("executing else branch");
                        temp = x[index] + 2;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    temp = 3;
                }
                assert length <= 0 ? temp == 2 : true;
                assert length > 0 && index == 0 ? temp == 302 : true;
                assert length > 0 && index == 1 ? temp == 402 : true;
                assert length > 0 && index != 0 && index != 1 ? temp == 3 : true;
        //        Debug.printPC("vaibhav: pc = ");
        //        System.out.println("temp = " + temp);
                return temp;
            }

            //testing outRangeArrayLoad for symbolic index
            public int outRangeloadArrayTC(int index, int length) throws ArrayIndexOutOfBoundsException {
                int[] x = {300};
                int temp = 1;
                try {
                    if (length > 0) {
                        temp = x[index];
                    } else {
                        temp = 2;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("catch array out of bound, length = " + length + ", index = " + index);
                    temp = 3;
                }
                System.out.println("temp = " + temp);

                assert ((length <= 0) ? (temp == 2) : true);
                assert (length > 0) && (index == 0)? (temp == 300 ) : true;
                assert (length > 0) && (index != 0)? (temp == 3 ) : true;
        //        if (temp == 1)
        //            System.out.println("then branch");
        //        else
        //            System.out.println("else branch");
                return 0;
            }

            public int catchOutRangeloadArrayTC(int index, int length) throws ArrayIndexOutOfBoundsException {
                int[] x = {1, 2};
                int temp = 1;
                int y = 1;
                try {
                    if (length > 0) {
                        temp = x[index];
                    } else {
                        temp = 1;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("catch array out of bound");
                }
                return temp;
            }


            public int innerCatchOutRangeloadArrayTC(int index, int length) throws ArrayIndexOutOfBoundsException {
                int[] x = {300};
                int temp = 1;
                int y = 1;
                if (length > 0) {
                    try {
                        temp = x[index];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("catch array is out of bound");
                    }
                } else {
                    temp = 2;
                }
                return temp;
            }

            public int boundedOutRangeloadArrayTC(int index, int length) throws ArrayIndexOutOfBoundsException {
                int[] x = {300};
                int temp = 0;
                int y = 2;
                if (length > 0) {
                    try {
                        temp = x[index];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("catch array out of bound");
                    }
                } else {
                    temp = 2;
                }

                if (temp != 0)
                    y = 1;
                else
                    y = 0;
                return y;
            }
        */
    /*public int createObjectTC1(boolean x, boolean y) {
        int a = 3;
        if (x) {
            TempClass tempClass2 = new TempClass();
            a = 4;
        }
//        assert(x ? a==0: true);
//        assert(!x ? a==3: true);
        return a;
    }*/
/*
    public int createObjectTC2(boolean x, boolean y) {
        int a = 0;
        if (x) {
            TempClass tempClass2 = new TempClass();
        } else {
            a = 3;
        }
//        assert(x ? a==0: true);
//        assert(!x ? a==3: true);
        return a;
    }

    public int createObjectTC3(boolean x, boolean y) {
        int a = 0;
        if (x) {
            a = 3;
        } else {
            a = 2;
            TempClass tempClass2 = new TempClass();
        }
//        assert(x ? a==2: true);
//        assert(!x ? a==3: true);
        return a;
    }

    public int createObjectTC4(boolean x, boolean y) {
        int a = 0;
        if (new TempClass3(x).valid) {
            a = 3;
        } else {
            a = 2;
        }
//        assert(a==3);
        return a;
    }

*/
/*
    public int createObjectTC5(boolean x, boolean y) {
        int a = 0;
        if (x) {
            TempClass tempClass2 = new TempClass();
        } else {
            a = 2;
            TempClass tempClass2 = new TempClass();
        }
//        assert(x ? a==2: true);
//        assert(!x ? a==3: true);
        return a;
    }

*/
    /*
    public int createObjectTC6(boolean x, boolean y) {
        int a = 0;
        if (x) {
            a = 3 + a;
        } else {
            a = 2;
            TempClass tempClass2 = new TempClass();
        }
//        assert(x ? a==2: true);
//        assert(!x ? a==3: true);
        return a;
    }

    public int createObjectTC7(boolean x, boolean y) {
        int a = 3;
        if (x) {
   //         TempClass tempClass2 = new TempClass();
            a = 4;
        }
        if (y) {
            a = 4 + a;
        } else {
            a = 2 + a;
        }
        assert(x && y ? a == 8: true);
        assert(!x ? a==3: true);
        assert(x && !y ? a == 6: true);
        assert(!x && y ? a == 7: true);
        assert(!x && !y ? a == 5: true);
        return a;
    }
*/
/*

    public boolean arrayLoad1(int x[], int index0, int length) {
        int temp = 1;
        boolean bug = false;
        for (int index = 0; index < 2; index++) {
            if (index0 == 1) {
                x[index] = length;
            }
        }
        return bug;
    }


    public int createObjectTC08(boolean x, boolean y, int z) {
        count = 0;
        //if (x)
        if (y)
            count = z;
//            else
        count = z + 1;
        return count;
    }

    public int createObjectTC8(boolean x, boolean y) {
//        count = 0;
//        int a = 0;
        int[] b = {0, 1};

        if (x) {
//              a = 3 + a;
//            count = 3 + count;
            b[0] = 3 + b[0];
        } else {
            //        TempClass tempClass2 = new TempClass();
//                      a = 2 + a;
//            count = 2 + count;
            b[0] = 2 + b[0];
        }
*/

/*


        assert(x? a == 3: true);
        assert(!x? a == 2: true);

        assert(x? count == 3: true);
        assert(!x? count == 2: true);

        assert(x? b[0] == 3: true);
        assert(!x? b[0] == 2: true);
*/

/*
        if (y) {
//            a = 4 + a;
//            count = count + 4;
            b[0] = 4 + b[0];
        } else {
//            a = 2 + a;
//            count = count + 2;
            b[0] = 2 + b[0];
        }
        return b[0];
//        return a + count + b[0];
    }
*/
/*

    public int createObjectTC9(boolean x, boolean y) {
        int[] b = {0, 1};

        if (x) {
            b[0] = 3 + b[0];
        } else {
            b[0] = 2 + b[0];
        }


        if (y) {
            b[0] = 4 + b[0];
        } else {
            b[0] = 2 + b[0];
        }

        System.out.println("reached the end.");
        return b[0];
    }

*/

    /*
    public int createObjectTC9(boolean x, boolean y) {
        int a = 3;
        TempClass tempClass2 = new TempClass();
        if (x) {
            a = 4;
        }
//        assert(x ? a==0: true);
//        assert(!x ? a==3: true);
        return a;
    }


    public int createObjectTC10(boolean x, boolean y) {
        int a = 3;
        if (x) {
            a = 4;
        }
        TempClass tempClass2 = new TempClass();
        ++a;
//        assert(x ? a==0: true);
//        assert(!x ? a==3: true);
        return a;
    }

    public int createObjectComplexRegionTC1(boolean x, boolean y) {
        int a = 0;
        if (y) {
            if (x) {
                a = 3;
            } else {
                a = 2;
                TempClass tempClass2 = new TempClass();
            }
        }

//        assert((y && x) ? a==3: true);
//        assert((y && !x) ? a==2: true);
//        assert(!y ? a==0: true);

        return a;
    }
*/
/*
    public int createObjectComplexRegionTC2(boolean x, boolean y) {
        int a = 0;
        if (y) {
            if (x) {
                a = 3;
                TempClass tempClass2 = new TempClass();
            } else {
                a = 2;
            }
        } else {
            if (x) {
                a = 3;
            } else {
                TempClass tempClass2 = new TempClass();
                a = 2;
            }
        }
//        assert((y && x) ? a==3: true);
//        assert((y && !x) ? a==2: true);
//        assert(!y ? a==0: true);

        return a;
    }
    */
/*
    public int createObjectComplexRegionTC3(boolean x, boolean y) {
        int a = 0;
        if (y) {
            if (new TempClass3(x).valid) {
                a = 3;
            } else {
                a = 2;
            }
        }
        return a;
    }

    //SH:Passed April 23rd.
    public int createObjectComplexRegionTC4(boolean x, boolean y) {
        int a = 0;
        if (y) {
            if (new TempClass3(false).valid) {
                a = 3;
            } else {
                a = 2;
            }
        }
//        assert(y ? a==2: true);
//        assert(!y ? a==0: true);

        return a;
    }

    public int createObjectComplexRegionTC5(boolean x, boolean y) {
        int a = 0;
        if (y) {
            TempClass tempClass1 = new TempClass();
            a = 1;
            if (x) {
                a = 3;
            } else {
                a = 2;
            }
        }
//        assert((y && x) ? a==3: true);
//        assert((y && !x) ? a==2: true);
//        assert(!y ? a==0: true);

        return a;
    }


    public int createObjectComplexRegionTC6(boolean x, boolean y) {
        int a = 0;
        if (new TempClass3(x).valid) {
            a = 1;
            if (x) {
                a = 3;
            } else {
                a = 2;
            }
        }
        return a;
    }

    public int createObjectComplexRegionTC7(boolean x, boolean y) {
        int a = 0;
        if (new TempClass3(true).valid) {
            a = 1;
            if (x) {
                a = 3;
            } else {
                a = 2;
            }
        }
        return a;
    }
*/
/*
    public int createObjectComplexRegionTC8(boolean x, boolean y) {
        int a = 0;
        if (y) {
            if (x) {
                a = 3 + a;
            } else {
                a = 2 + a;
            }
        } else {
            if (x) {
                a = 4 + a;
                TempClass tempClass2 = new TempClass();
            } else {
                TempClass tempClass2 = new TempClass();
                a = 5 + a;
            }
        }
        /*assert((y && x) ? a==3: true);
        assert((y && !x) ? a==2: true);
        assert(!y & x ? a==4: true);
        assert(!y & !x ? a==5: true);
*/
/*
        System.out.println("reached the end.");
        return a;
    }
*/
/*
    public int arrayLoadTC1(int index, int length) {
        int[] x = {300, 400};
        int temp = 1;
        try {
            if (length <= 0) {
                //     System.out.println("executing then branch");
                temp = 2;
            } else {
                // System.out.println("executing else branch");
                temp = x[temp] + 2;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            temp = 3;
        }
        assert length <= 0 ? temp == 2 : true;
        assert length > 0 && index == 0 ? temp == 302 : true;
        assert length > 0 && index == 1 ? temp == 402 : true;
        assert length > 0 && index != 0 && index != 1 ? temp == 3 : true;
//        Debug.printPC("vaibhav: pc = ");
//        System.out.println("temp = " + temp);
        return temp;
    }
*/
/*

    public int boundedOutRangeloadArrayTC(int index, int length) throws ArrayIndexOutOfBoundsException {
        int[] x = {300, 400};
        int temp = 0;
        int y = 2;
        if (length > 0) {
            temp = x[index];
        } else {
            temp = 2;
        }

        if (temp != 0)
            y = 1;
        else
            y = 0;
        return y;
    }
*/


    public double fieldDoubleTest(int x) {
        AA aa = new AA(4.0);
        aa.myDouble = 2.0;
        double ret;

        if (x > 0) {
            aa.myDouble = 11.0;
            ret = aa.myDouble + 1.0;
        } else {
            aa.myDouble = 12.0;
            ret = aa.myDouble + 2.0;
        }
        return ret;
    }

};


class TreeNode {
    TreeNode left;
    TreeNode right;
    int data;

    public static int treeLoad(TreeNode p, int i) {
        if (i < 0 || i >= 2) {
            return 0;
        } else {
            if ((i & 2) != 0)
                p = p.right;
            else
                p = p.left;

            if ((i & 1) != 0)
                p = p.right;
            else
                p = p.left;

            return p.data;
        }
    }
}

/*
class TempClassDerived extends TempClass {

    public static int tempInt = 2; //change this to 2 to test read after write on a class field inside a Veritesting region

    public static int myInt = 1;

    public int getAnotherAnotherTempInt(int a) {
        //TempClass2 t = new TempClass2();
        //t.tempMethod();
        return 1;
    }

    public int getAnotherTempInt(int a) {
        //TempClass2 t = new TempClass2();
        //t.tempMethod();
        //return tempInt;
        return getAnotherAnotherTempInt(TempClassDerived.myInt);
    }

    public int getTempInt(int a) {
        //TempClass2 t = new TempClass2();
        //t.tempMethod();
        //return tempInt;
        return getAnotherTempInt(TempClassDerived.myInt);
    }

    public int getOne(int a) {
        //read-after-write test on tempInt field
        tempInt = a + 1; //LOCAL_INPUT,  FIELD_OUTPUT holes
        a = tempInt + 2; //LOCAL_OUTPUT, FIELD_INPUT holes
        tempInt = a + 3; //LOCAL_INPUT,  FIELD_INPUT holes
        //tempInt = 6 + a;

        //VeritestingPerf.count += 1;
        //return tempInt;
        //return nestedRegion(myInt);
        return getTempInt(tempInt);
        //return 1;
    }
    */
/*
    public int nestedRegion(int x) {
        if (x != 0) {
            if (x != 0) {
                tempInt = 3;
            } else {
                tempInt = 4;
            }
        } else {
            tempInt = 5;
        }
        return tempInt + x;
    }

}*/

class TempClass6 {

    public static int tempInt = 1;

    public static int myInt = 1;

    public TempClass6() {
        this.tempClass6 = new TempClass6();
    }

    public int getTempInt() {
        return tempInt;
    }

    public int getOne(int a) {
        System.out.println("called TempClass.getOne");
        tempInt = a;
        return tempInt;
    }

    TempClass6 tempClass6;
/*
    public int nestedRegion(int a) {
        return 0;
    }*/
}

class MyTempClass {
    public int method2(int x) {
        return 0;
    }
}

class TempClass5 extends MyTempClass {

    public int tempInt2 = 1;
    public TempClass5 innerTemp;

    public TempClass5[] tempClasses = new TempClass5[2];
    TempClass5 obj;


    //causes static analysis non termination.
    /*public TempClass5(){
        TempClass5 obj = (new TempClass5());
        obj.tempInt2 = 5;
        this.tempClasses[0] = obj;
    }*/

    public TempClass5[] getTempClasses() {

        obj.tempInt2 = 5;
        this.tempClasses[0] = obj;
        return tempClasses;
    }

    public int tempMethod() {
        return innerTemp.method2(1);
    }


    public int method2(int x) {
        int myCount = 0;
        if (x > 100) {
            myCount = 1;
        } else {
            myCount = 3;
        }
        return myCount;
    }


}

class AA {

    public int f = 1;
    public AA r;
    public double myDouble;

    public AA(double v) {
        this.myDouble = v;
    }

    public int tempMethod() {
        return 0;
    }
}


class TempClass3 {

    public boolean valid;

    public TempClass3(boolean valid) {
        this.valid = valid;
    }
}



/*
  public void collatz(int n) {
    int inter;
    while(n != 1) {
      if( (n & 1) == 1) {
        inter = 3*n + 1;
      } else {
        inter = (n >> 1);
      }
      n = inter;
    }
  }

  public void testMe4 (int[] x, int len) {
    int sum = 0; //Debug.makeSymbolicInteger("sum");
    // for(int i=0; i < len; i++) 
    //   x[i] = Debug.makeSymbolicInteger("x"+i);
    for (int i=0; i < len; i++) {
      if (x[i] < 0) sum += -1;
      else if (x[i] > 0) sum += 1;
    }
    if (sum < 0) System.out.println("neg");
    else if (sum > 0) System.out.println("pos");
    else System.out.println("bug");
  }

  public int gcd(int a, int b) {
    while( a != b ) {
      if ( a > b ) a = a - b;
      else b = b - a;
    }
    return a;
  }



  public int oneBranch(int x) {
    int sum=0;
    if(x < 0) sum += -1;
    else sum += 1;
	return sum;
  }

}*/
