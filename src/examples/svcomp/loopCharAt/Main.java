package svcomp.loopCharAt;
/*
 * Origin of the benchmark:
 *     inspired by the motivating example on page 1085 in Thanassis Avgerinos,
 *     Alexandre Rebert, Sang Kil Cha, and David Brumley. 2014.
 *     Enhancing symbolic execution with veritesting.
 *     license: MIT (see /java/java-ranger/LICENSE-2.0)
 *     repo: https://github.com/vaibhavbsharma/java-ranger
 *     branch: svcomp2021
 *     root directory: examples/svcomp
 * The benchmark was taken from the repo: 18 October 2022
 */

import org.sosy_lab.sv_benchmarks.Verifier;

public class Main {

  public static void main(String[] args) {
    String arg = Verifier.nondetString();
    int counter = 0;
    for (int i = 0; i < arg.length(); i++) {
      char myChar = arg.charAt(i);
      if (myChar == 'B')
        counter++;
      }
    assert (counter != 121);
    assert true;
  }
}
