//package com.vonhessling.peaktraffic;

package veritesting.benchmarksElena;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    
    
    /**
     * Generates the list of all t-combinations of indices of items from 0...n-1. 
     * Implementation description: http://www-cs-faculty.stanford.edu/~knuth/fasc3a.ps.gz, page 9: Algorithm T (faster than Algorithm L)
     * @param t The value for t: the number of desired items in the subset(s)
     * @param n The value for n: the set's size
     * @return Returns all possible (ordered) t-combinations of n 
     */
    public static List<List<Integer>> getTCombinations(int t, int n) {
        if ((t > n) || ((n < 1) || (t < 1))) {
            throw new IllegalArgumentException("Error: need to supply n and t values both > 1, where n >= t. You supplied n: " + n + ", t: " + t);
        }
        List<List<Integer>> combinations = new ArrayList<List<Integer>>();
        
        if (t == n) { // special case, only one combination with all indices
            List<Integer> combination = new ArrayList<Integer>();
            for (int i = 1; i <= t; i++) {
                combination.add(i - 1);
            }
            combinations.add(combination);
            return combinations;
        }
        
        // Step T1: Initialize
        int c[] = new int[t + 3];
        for (int j = 1; j <= t; j++) {
            c[j] =  j - 1;
        }
        c[t + 1] = n;
        c[t + 2] = 0;
        int j = t; 
        
        while (true) {
            // Step T2: Visit
            // j is now the smallest index such that c[j + 1] > j 
            // harvest c[1] through c[t]
            List<Integer> combination = new ArrayList<Integer>();
            for (int i = 1; i <= t; i++) {
                combination.add(c[i]);
            }
            combinations.add(combination);
            
            int x;
            if (j > 0) {
                x = j; 
                // GOTO Step 6
            } else {
                // Step T3: Easy case
                if (c[1] + 1 < c[2]) { 
                    c[1]++;
                    continue; // GOTO Step 2
                } else {
                    j = 2;
                }
                
                boolean repeat;                
                // Step 4: Find j
                do {
                    c[j - 1] = j - 2;
                    x = c[j] + 1;
                    repeat = false;                    
                    if (x == c[j + 1]) {
                        j++;
                        repeat = true; // I'm too proud to write a GOTO here...                        
                    }
                } while (repeat);
                
                // Step 5: Done?
                if (j > t) {   
                    break;
                }
            } // end GOTO Step 6 from Step 2
            
            // Step 6: Increase c[j]
            c[j] = x;
            j--;
        }
        return combinations;
    }

    
}
