/*
 * Origin of the benchmark:
 *     license: MIT (see /java/jayhorn-recursive/LICENSE)
 *     repo: https://github.com/jayhorn/cav_experiments.git
 *     branch: master
 *     root directory: benchmarks/recursive
 * The benchmark was taken from the repo: 24 January 2018
 */

import org.sosy_lab.sv_benchmarks.Verifier;

public class UnsatAddition02 {
    static int addition(int m, int n) {
        if (n == 0) {
            return m;
        } else if (n > 0) {
            return 1;//addition(m + 1, n - 1);
        } else {
            return 2;//addition(m - 1, n + 1);
        }
    }

    public static void main(String[] args) {
        int m = 1;//Verifier.nondetInt();
        int n = 2;//Verifier.nondetInt();
        int result = addition(m, n);//callIntermediate(m, n);
        if (m < 100 || n < 100 || result >= 200) {
            callIntermediate(m,n);
            return;
        } else {
           return;// assert false;
        }
    }

    public static int callIntermediate(int m, int n) {
        if ((m > 0) && (n > 0))
            return addition(m, n);
        else return 0;
    }
}
