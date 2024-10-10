package veritesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ISSTAExample {

    public static void main(String[] args) {

        ArrayList<Integer> numberList = new ArrayList<Integer>();

        (new ISSTAExample()).paperExampleNum(1,1, 1,1,1,1);


    }


    public int paperExampleNum(int x1, int x2, int x3, int x4, int x5, int x6) {
        List<Integer> list = new ArrayList<>(Arrays.asList(x1, x2, x3, x4, x5, x6));
//        ArrayList<Integer> list = new ArrayList<>(200);
        int wordCount = 0;
        boolean inWord;
        if (list.size() > 0) {
            int firstElement = list.get(0);
            if (firstElement == 0)
                inWord = false;
            else inWord = true;
            for (int i = 0; i < list.size(); i++) {
                if (inWord) {
                    if (list.get(i) == 0) { //0 is a delimiter
                        ++wordCount;
                        inWord = false;
                    }
                } else {
                    if (list.get(i) != 0) {
                        inWord = true;
                    }
                }
            }
        }
        return wordCount;
    }


    public int paperExampleChar(List<Character> textList){
        boolean inWord = false;
        int wordCount = 0;

        for(int i = 0; i < textList.size(); i++){
            if(inWord){
                if(Character.isWhitespace(textList.get(i))){
                    ++wordCount;
                    inWord = false;
                }
            }
            else{
                if(!Character.isWhitespace(textList.get(i))){
                    inWord = true;
                }
            }
        }

        System.out.println("Number of words is:" + wordCount);

        return wordCount;
    }

}
