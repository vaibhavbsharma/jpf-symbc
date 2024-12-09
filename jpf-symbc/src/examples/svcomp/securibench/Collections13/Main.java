package svcomp.securibench.Collections13;// SPDX-FileCopyrightText: 2021 Falk Howar falk.howar@tu-dortmund.de
// SPDX-License-Identifier: Apache-2.0

// This file is part of the SV-Benchmarks collection of verification tasks:
// https://gitlab.com/sosy-lab/benchmarking/sv-benchmarks

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import svcomp.securibench.micro.mockx.servlet.http.HttpServletRequest;
import svcomp.securibench.micro.mockx.servlet.http.HttpServletResponse;
import svcomp.securibench.micro.securibench.micro.collections.Collections13;
import org.sosy_lab.sv_benchmarks.Verifier;


public class Main {

  public static void main(String[] args) {
    String s1 = Verifier.nondetString();
//      List c4 = java.util.Arrays.asList(new String[] {new String(s1)});
//      String s= (String) c4.get(0);
//      if (s != null) {
//        System.out.println("inside s!=null");
//        if(s.contains("<bad/>")) {
//          System.out.println("inside bad");
//          assert false;
//        }
//      }

    HttpServletRequest req = new HttpServletRequest();
    HttpServletResponse res = new HttpServletResponse();
    req.setTaintedValue(s1);

    Collections13 sut = new Collections13();
    try {
      sut.doGet(req, res);
    } catch (IOException e) {

    }
  }

}
