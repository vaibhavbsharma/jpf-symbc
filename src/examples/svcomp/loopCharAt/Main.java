package svcomp.loopCharAt;
/*
 * JR Regression:
 * Origin of the benchmark:
 *     license: MIT (see /java/jayhorn-recursive/LICENSE)
 *     repo: https://github.com/jayhorn/cav_experiments.git
 *     branch: master
 *     root directory: benchmarks/recursive
 * The benchmark was taken from the repo: 24 January 2018

This example is inspired by the motivating example on page 1085 in
Thanassis Avgerinos, Alexandre Rebert, Sang Kil Cha, and David Brumley. 2014.
Enhancing symbolic execution with veritesting. In Proceedings of the 36th International Conference on Software Engineering (ICSE 2014). Association for Computing Machinery, New York, NY, USA, 1083–1094. https://doi.org/10.1145/2568225.2568293
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
