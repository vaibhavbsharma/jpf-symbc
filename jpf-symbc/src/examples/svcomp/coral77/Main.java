package svcomp.coral77;// This file is part of the SV-Benchmarks collection of verification tasks:
// https://gitlab.com/sosy-lab/benchmarking/sv-benchmarks
//
// SPDX-FileCopyrightText: 2024 The SV-Benchmarks Community
//
// SPDX-License-Identifier: Apache-2.0

import org.sosy_lab.sv_benchmarks.Verifier;
import svcomp.common.JPFBenchmark;

public class Main {
  public static void main(String[] main) {
    double d1 = Verifier.nondetDouble();
    double d2 = Verifier.nondetDouble();
    double d3 = Verifier.nondetDouble();
    double d4 = Verifier.nondetDouble();
    double d5 = Verifier.nondetDouble();
    double d6 = Verifier.nondetDouble();
    double d7 = Verifier.nondetDouble();
    double d8 = Verifier.nondetDouble();
    double d9 = Verifier.nondetDouble();
    double d10 = Verifier.nondetDouble();
    JPFBenchmark.benchmark77(d1, d2, d3, d4, d5, d6, d7, d8, d9, d10);
  }
}
