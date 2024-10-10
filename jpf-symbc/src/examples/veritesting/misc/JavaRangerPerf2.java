package veritesting.misc;

import java.util.Arrays;
import java.util.List;

public class JavaRangerPerf2 {
    public static void main(String args[]) {
        int wordCount = getWordCount(0, 0, 0, 0, 0, 0);
//        return wordCount;
    }

    private static int getWordCount(int in0, int in1, int in2, int in3, int in4, int in5) {
        Integer arr[] = new Integer[]{in0, in1, in2, in3, in4, in5};
        List<Integer> list = Arrays.asList(arr);
        //put 200 symbolic int into list
        int wordCount = 0;
        boolean inWord;
        if (list.size() > 0) {
            int firstElement = list.get(0);
            if (firstElement == 0)
                inWord = false;
            else inWord = true;
            for (int i = 0;
                 i < list.size();
                 i++) {
                if (inWord) {
                    //list.get(i) returns sym. int
                    if (list.get(i) == 0) {
                        ++wordCount;
                        inWord = false;
                    }
                } else {
                    if (list.get(i) != 0)
                        inWord = true;
                }
            }
        }
        return wordCount;
    }
}
