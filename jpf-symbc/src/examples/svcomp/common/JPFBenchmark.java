package svcomp.common;// This file is part of the SV-Benchmarks collection of verification tasks:
// https://gitlab.com/sosy-lab/benchmarking/sv-benchmarks
//
// SPDX-FileCopyrightText: 2024 The SV-Benchmarks Community
//
// SPDX-License-Identifier: MIT
/*
 * This benchmark task is a modification of the following original Benchmark:
 * Origin of the benchmark:
 * 	   license: MIT (see /java/float-nonlinear-calculation/LICENSE)
 *     repo: https://github.com/osl/concolic-walk
 *     branch: master
 *     root directory: experiments/src/programs/coral/
 * The benchmark was taken from the repo: 8 October 2024
 *
 * Following the original license model, modifications are as well licensed under the
 * MIT license.
 */

public class JPFBenchmark {

  // sin(x) + cos(y) > 1.0
  public static void benchmark01(double x, double y) {
    if (Math.sin(x) + Math.cos(y) > 1) {
      assert false;
    }
  }

  // sin(x) - cos(y) < 0.0000000001
  public static void benchmark02(double x, double y) {
    if (Math.sin(x) - Math.cos(y) < 0.0000000001) {
      assert false;
    }
  }

  // sin(x) - cos(y) == 0.0
  public static void benchmark03(double x, double y) {
    if (Math.sin(x) - Math.cos(y) == 0) {
      assert false;
    }
  }

  // exp(x) > 0.0
  public static void benchmark04(double x) {
    if (Math.exp(x) > 0) {
      assert false;
    }
  }

  // sin A + sin B + sin C = 4 * cos A * cos B * cos C
  public static void benchmark05(double x, double y, double z) {
    if (Math.sin(x) + Math.sin(y) + Math.sin(z) == 4 * Math.cos(x) * Math.cos(y) * Math.cos(z)) {
      System.out.println("sucess");
    }
  }

  // cos A + cos B + cos C > 4 sin A/2 sin B/2 sin C/2
  public static void benchmark06(double x, double y, double z) {
    if (Math.cos(x) + Math.cos(y) + Math.cos(z)
        > 4 * Math.sin(x / 2) * Math.sin(y / 2) * Math.sin(z / 2)) {
      assert false;
    }
  }

  // Math.sin(Math.cos(x*y)) < Math.cos(Math.sin(x*y))
  public static void benchmark09(double x, double y) {
    if (Math.sin(Math.cos(x * y)) < Math.cos(Math.sin(x * y))) {
      assert false;
    }
  }

  // Math.sin(x*Math.cos(y*Math.sin(z))) > Math.cos(x*Math.sin(y*Math.cos(z)))
  public static void benchmark10(double x, double y, double z) {
    if (Math.sin(x * Math.cos(y * Math.sin(z))) > Math.cos(x * Math.sin(y * Math.cos(z)))) {
      assert false;
    }
  }

  // asin(x) < cos(y)*cos(z) - Math.atan(w)
  public static void benchmark11(double x, double y, double z, double w) {
    if (Math.asin(x) < Math.cos(y) * Math.cos(z) - Math.atan(w)) {
      assert false;
    }
  }

  // (asin(x) * Math.asin(y))-1 < Math.atan(z) * Math.atan(w)
  public static void benchmark12(double x, double y, double z, double w) {
    if ((Math.asin(x) * Math.asin(y)) - 1 < Math.atan(z) * Math.atan(w)) {
      assert false;
    }
  }

  // sin(y) * Math.asin(x) < cos(y)*cos(z) - Math.atan(w)
  public static void benchmark13(double x, double y, double z, double w) {
    if (Math.sin(y) * Math.asin(x) < Math.cos(y) * Math.cos(z) - Math.atan(w)) {
      assert false;
    }
  }

  // sin(y) * Math.asin(x) - 300 < cos(y)*cos(z) - Math.atan(w)
  public static void benchmark14(double x, double y, double z, double w) {
    if (Math.sin(y) * Math.asin(x) - 300 < Math.cos(y) * Math.cos(z) - Math.atan(w)) {
      assert false;
    }
  }

  // ((Math.asin(1) * Math.asin(cos(9*57)))-1) < (Math.atan(0) * Math.atan(0))
  public static void benchmark15(double x, double y, double z, double w) {
    if (((Math.asin(x) * Math.asin(Math.cos(y))) - x) < (Math.atan(z) * Math.atan(w))) {
      assert false;
    }
  }

  // ((((tan_(($V4-$V1))*cos_(sin_(($V4/$V5))))-atan_((($V2+20.0)+$V3)))+asin_(($V2-15.0))) <
  // ((sin_(($V4*$V4))*cos_((($V1*$V4)*$V5)))-tan_((cos_((($V1*$V4)*$V1))+sin_($V4)))))
  public static void benchmark16(double x, double y, double z, double w, double v) {
    if (Math.tan(w - x) * Math.cos(Math.sin(w / v)) - Math.atan(y + 20 + z) + Math.asin(y - 15)
        < Math.sin(w * w) * Math.cos(x * w * v) - Math.tan(Math.cos(x * w * x)) + Math.sin(w)) {
      assert false;
    }
  }

  // Math.asin(x) * Math.acos(x) < Math.atan(x)
  public static void benchmark17(double x) {
    if (Math.asin(x) * Math.acos(x) < Math.atan(x)) {
      assert false;
    }
  }

  // (1+Math.acos(x)) < Math.asin(x)
  public static void benchmark18(double x) {
    if ((1 + Math.acos(x)) < Math.asin(x)) {
      assert false;
    }
  }

  // 3*Math.acos(x) < Math.atan(y) + Math.asin(z)
  public static void benchmark19(double x, double y, double z) {
    if (3 * Math.acos(x) < Math.atan(y) + Math.asin(z)) {
      assert false;
    }
  }

  // sin(sin((x*y)) < 0 && cos(2x) > 0.25
  public static void benchmark20(double x, double y) {
    if (Math.sin(Math.sin(x * y)) < 0 && Math.cos(2 * x) > 0.25) {
      assert false;
    }
  }

  // cos(x*y) < 0 && sin(2x) > 0.25
  public static void benchmark21(double x, double y) {
    if (Math.cos(x * y) < 0 && Math.sin(2 * x) > 0.25) {
      assert false;
    }
  }

  // (sin_(cos_(($V1*$V2))) < cos_(sin_(($V2*$V3)))) &
  // ((sin_((($V4*2.0)-$V2))/(cos_((($V6*2.0)+$V7))+1.0)) ==
  // (cos_((($V3*2.0)+$V1))/(sin_((($V4*2.0)+$V5))+1.0))))
  public static void benchmark22(
      double x, double y, double z, double w, double v, double t, double q) {
    if ((Math.sin(Math.cos(x * y)) < Math.cos(Math.sin(y * z)))
        && Math.sin(w * 2.0 - y) / (Math.cos(t * 2.0 + q) + 1.0)
            == (Math.cos(z * 2.0 + x) / (Math.sin(w * 2.0 + v) + 1.0))) {
      assert false;
    }
  }

  // (sin(2x - y)/(cos(2y + x) + 1) = cos(2z + x)/(sin(2w + y) - 1) &
  // sin(x*y*z*w) > 0 &
  // cos(x*y*z*w) < 0

  public static void benchmark23(double x, double y, double z, double w) {
    if (Math.sin(2 * x - y) / (Math.cos(2 * y + x) + 1)
            == Math.cos(2 * z + x) / (Math.sin(2 * w + y) - 1)
        && Math.sin(x * y * z * w) > 0
        && Math.cos(x * y * z * w) < 0) {
      assert false;
    }
  }

  // sin(cos(x*y)) < cos(sin(x*z))
  // (sin(2w - y)/(cos(2y + v) + 1) = cos(2z + x)/(sin(2w + v) - 1)
  // sin(x*y*z*w) > 0 && cos(x*y*z*w) < 0
  public static void benchmark26(double x, double y, double z, double w, double v) {
    if (Math.sin(Math.cos(x * y)) < Math.cos(Math.sin(x * z))
        && (Math.sin(2 * w - y) / (Math.cos(2 * y + v) + 1)
            == Math.cos(2 * z + x) / (Math.sin(2 * w + v) - 1))
        && Math.sin(x * y * z * w) > 0
        && Math.cos(x * y * z * w) < 0) {
      assert false;
    }
  }

  // sin(x*cos(y*sin(z))) > cos(x*sin(y*cos(z))) && sin(cos(x*y)) < cos(sin(x*y))
  public static void benchmark27(double x, double y, double z) {
    if (Math.sin(x * Math.cos(y * Math.sin(z))) > Math.cos(x * Math.sin(y * Math.cos(z)))
        && Math.sin(Math.cos(x * y)) < Math.cos(Math.sin(x * y))) {
      assert false;
    }
  }

  // log($V1) == 2.0
  public static void benchmark28(double x) {
    if (Math.log(x) == 2) {
      assert false;
    }
  }

  public static void benchmark29(double x) {
    if (Math.exp(x) > 5) {
      assert false;
    }
  }

  // log_10($v1) == 2.0
  public static void benchmark30(double x) {
    if (Math.log10(x) == 2) {
      assert false;
    }
  }

  public static void benchmark31(double x) {
    if (Math.round(x) > 5) {
      assert false;
    }
  }

  public static void benchmark32(double x) {
    if (Math.sqrt(x) > 5) {
      assert false;
    }
  }

  // sqrt(sin($V1)) > sqrt(cos($V1))
  public static void benchmark33(double x) {
    if (Math.sqrt(Math.sin(x)) > Math.sqrt(Math.cos(x))) {
      assert false;
    }
  }

  // sqrt(sin($V1)) < sqrt(cos($V1))
  public static void benchmark34(double x) {
    if (Math.sqrt(Math.sin(x)) < Math.sqrt(Math.cos(x))) {
      assert false;
    }
  }

  //  1.0/sqrt(sin($V1)) > sqrt(cos(exp($V2)))
  public static void benchmark35(double x, double y) {
    if (1.0 / Math.sqrt(Math.sin(x)) > Math.sqrt(Math.cos(Math.exp(y)))) {
      assert false;
    }
  }

  // ((log10($V3)*(1.0/sqrt(sin_($V1)))) == sqrt(cos_(exp($V2))))
  public static void benchmark36(double x, double y, double z) {
    if (Math.log10(z) * (1.0 / Math.sqrt(Math.sin(x))) == Math.sqrt(Math.cos(Math.exp(y)))) {
      assert false;
    }
  }

  // (atan2_($V1,$V2) == 1.0)
  public static void benchmark38(double x, double y) {
    if (Math.atan2(x, y) == 1.0) {
      assert false;
    }
  }

  // (pow_($V1,$V2) == 1.0)
  public static void benchmark39(double x, double y) {
    if (Math.pow(x, y) == 1.0) {
      assert false;
    }
  }

  // pow(x,2) == x + y
  public static void benchmark40(double x, double y) {
    if (Math.pow(x, 2) == x + y) {
      assert false;
    }
  }

  // pow(x,2) == x + y & x >= -1 & y <=  2
  public static void benchmark41(double x, double y) {
    if (Math.pow(x, 2) == x + y && x >= -1 && y <= 2) {
      assert false;
    }
  }

  // Math.pow(x,y) > Math.pow(y,x) & x > 1 & y <= 10
  public static void benchmark42(double x, double y) {
    if (Math.pow(x, y) > Math.pow(y, x) && x > 1 && y <= 10) {
      assert false;
    }
  }

  // Math.pow(x,y) > Math.pow(y,x) && Math.exp(x,y) > Math.exp(y,x) && y < x ^ 2
  public static void benchmark43(double x, double y) {
    if (Math.pow(x, y) > Math.pow(y, x) && Math.exp(y) > Math.exp(x) && y < Math.pow(x, 2)) {
      assert false;
    }
  }

  // Math.pow(x,y) > Math.pow(y,x) && Math.exp(x,y) < Math.exp(y,x)
  public static void benchmark44(double x, double y) {
    if (Math.pow(x, y) > Math.pow(y, x) && Math.exp(y) < Math.exp(x)) {
      assert false;
    }
  }

  // sqrt(Math.exp(x+y)) < Math.pow(z,x) && x > 0 && y > 1 && z > 1 && y <= x + 2
  public static void benchmark45(double x, double y, double z) {
    if (Math.sqrt(Math.exp(x + y)) < Math.pow(z, x) && x > 0 && y > 1 && z > 1 && y <= x + 2) {
      assert false;
    }
  }

  // Math.sqrt(e^(x + z)) < z^x && x > 0 && y > 1 && z > 1 && y < 1 && y < x + 2 && w = x + 2

  public static void benchmark46(double x, double y, double z, double w) {
    if (Math.sqrt(Math.pow(Math.E, (x + z))) < Math.pow(z, x)
        && x > 0
        && y > 1
        && z > 1
        && y < 1
        && y < x + 2
        && w == x + 2) {
      System.out.println("sucess");
    }
  }

  // x + y != z
  public static void benchmark48(double x, double y, double z) {
    if (x + y != z) {
      assert false;
    }
  }

  // x^2 + 3*Math.sqrt(y) < x*y && x < y ^ 2 && x + y < 50 //556 possible integer solutions
  public static void benchmark49(double x, double y) {
    if (Math.pow(x, 2) + 3 * Math.sqrt(y) < x * y && x < Math.pow(y, 2) && x + y < 50) {
      assert false;
    }
  }

  // x^2 + 3*Math.sqrt(y) < x*y && x < y ^ 2 && x + y < 50 && x = -13 + y //18 possible integer
  // solutions
  public static void benchmark50(double x, double y) {
    if (Math.pow(x, 2) + 3 * Math.sqrt(y) < x * y
        && x < Math.pow(y, 2)
        && x + y < 50
        && x == -13 + y) {
      assert false;
    }
  }

  // x^2 + 3*Math.sqrt(y) < x*y && x < y ^ 2 && x + y < 50 && x = -13 + y && x ^ x < Math.log10(y)
  // //one integer solution
  public static void benchmark51(double x, double y) {
    if (Math.pow(x, 2) + 3 * Math.sqrt(y) < x * y
        && x < Math.pow(y, 2)
        && x + y < 50
        && Math.pow(x, x) < Math.log10(y)) {
      assert false;
    }
  }

  // x ^ tan(y) + z < x * Math.atan(z) && sin(y) + cos(y) + tan(y) >= x - z
  public static void benchmark52(double x, double y, double z) {
    if (Math.pow(x, Math.tan(y)) + z < x * Math.atan(z)
        && Math.sin(y) + Math.cos(y) + Math.tan(y) >= x - z) {
      assert false;
    }
  }

  // x ^ Math.tan(y) + z < x * Math.atan(z) && Math.sin(y) + cos(y) + Math.tan(y) >= x - z &&
  // Math.atan(x) + Math.atan(y) > y
  public static void benchmark53(double x, double y, double z) {
    if (Math.pow(x, Math.tan(y)) + z < x * Math.atan(z)
        && Math.sin(y) + Math.cos(y) + Math.tan(y) >= x - z
        && Math.atan(x) + Math.atan(y) > y) {
      assert false;
    }
  }

  // x ^ Math.tan(y) + z < x * Math.atan(z) && Math.sin(y) + Math.cos(y) + Math.tan(y) >= x - z &&
  // Math.atan(x) + Math.atan(y) > y && Math.log(x^Math.tan(y)) < Math.log(z)
  public static void benchmark54(double x, double y, double z) {
    if (Math.pow(x, Math.tan(y)) + z < x * Math.atan(z)
        && Math.sin(y) + Math.cos(y) + Math.tan(y) >= x - z
        && Math.atan(x) + Math.atan(y) > y
        && Math.log(Math.pow(x, Math.tan(y))) < Math.log(z)) {
      assert false;
    }
  }

  // x ^ Math.tan(y) + z < x * Math.atan(z) &&  Math.sin(y) + Math.cos(y) + Math.tan(y) >= x - z &&
  // Math.atan(x) + Math.atan(y) > y &&  Math.log(x^Math.tan(y)) < Math.log(z) &&  Math.sqrt(y+z) >
  // Math.sqrt(x^(x-y))
  public static void benchmark55(double x, double y, double z) {
    if (Math.pow(x, Math.tan(y)) + z < x * Math.atan(z)
        && Math.sin(y) + Math.cos(y) + Math.tan(y) >= x - z
        && Math.atan(x) + Math.atan(y) > y
        && Math.log(Math.pow(x, Math.tan(y))) < Math.log(z)
        && Math.sqrt(y + z) > Math.sqrt(Math.pow(x, (x - y)))) {
      assert false;
    }
  }

  // x * y + Math.atan(z) * Math.sin(w*t) > x/y + z + Math.tan(w+t)
  public static void benchmark56(double x, double y, double z, double w, double t) {
    if (x * y + Math.atan(z) * Math.sin(w * t) > x / y + z + Math.tan(w + t)) {
      assert false;
    }
  }

  // x * y + Math.atan(z) * Math.sin(w*t) > x/y + z + Math.tan(w+t) &&
  // Math.pow(Math.log10(x),Math.log10(y)) <= Math.pow(Math.log10(z+w+t),Math.tan(w*t))
  public static void benchmark57(double x, double y, double z, double w, double t) {
    if (x * y + Math.atan(z) * Math.sin(w * t) > x / y + z + Math.tan(w + t)
        && Math.pow(Math.log10(x), Math.log10(y))
            <= Math.pow(Math.log10(z + w + t), Math.tan(w * t))) {
      assert false;
    }
  }

  // x * y + Math.atan(z) * Math.sin(w*t) > x/y + z + Math.tan(w+t) &&
  // Math.pow(Math.log10(x),Math.log10(y)) <= Math.pow(Math.log10(z+w+t),Math.tan(w*t)) &&
  // Math.tan(w*(x+y)) + Math.sin(t*(y+z)) > Math.asin(x+y+z) + acos(x+y+z) + Math.atan(x+y+z)
  public static void benchmark58(double x, double y, double z, double w, double t) {
    if (x * y + Math.atan(z) * Math.sin(w * t) > x / y + z + Math.tan(w + t)
        && Math.pow(Math.log10(x), Math.log10(y))
            <= Math.pow(Math.log10(z + w + t), Math.tan(w * t))
        && Math.tan(w * (x + y)) + Math.sin(t * (y + z))
            > Math.asin(x + y + z) + Math.acos(x + y + z) + Math.atan(x + y + z)) {
      assert false;
    }
  }

  // x * y + Math.atan(z) * Math.sin(w*t) > x/y + z + Math.tan(w+t) &&
  // Math.pow(Math.log10(x),Math.log10(y)) <= Math.pow(Math.log10(z+w+t),Math.tan(w*t))	&&
  // Math.tan(w*(x+y)) + Math.sin(t*(y+z)) > Math.asin(x+y+z) + Math.acos(x+y+z) + Math.atan(x+y+z)
  // && w = t * 3 / 4
  public static void benchmark59(double x, double y, double z, double w, double t) {
    if (x * y + Math.atan(z) * Math.sin(w * t) > x / y + z + Math.tan(w + t)
        && Math.pow(Math.log10(x), Math.log10(y))
            <= Math.pow(Math.log10(z + w + t), Math.tan(w * t))
        && Math.tan(w * (x + y)) + Math.sin(t * (y + z))
            > Math.asin(x + y + z) + Math.acos(x + y + z) + Math.atan(x + y + z)
        && w == t * 3 / 4) {
      assert false;
    }
  }

  // x * y + Math.atan(z) * Math.sin(w*t) > x/y + z + Math.tan(w+t) &&
  // Math.pow(Math.log10(x),Math.log10(y)) <= Math.pow(Math.log10(z+w+t),Math.tan(w*t))	&&
  // Math.tan(w*(x+y)) + Math.sin(t*(y+z)) > Math.asin(x+y+z) + Math.acos(x+y+z) + Math.atan(x+y+z)
  // && w = t * 3 / 4 && x < 2y - 3z
  public static void benchmark60(double x, double y, double z, double w, double t) {
    if (x * y + Math.atan(z) * Math.sin(w * t) > x / y + z + Math.tan(w + t)
        && Math.pow(Math.log10(x), Math.log10(y))
            <= Math.pow(Math.log10(z + w + t), Math.tan(w * t))
        && Math.tan(w * (x + y)) + Math.sin(t * (y + z))
            > Math.asin(x + y + z) + Math.acos(x + y + z) + Math.atan(x + y + z)
        && w == t * 3 / 4
        && x < 2 * y - 3 * z) {
      assert false;
    }
  }

  // x + y > z / w && Math.sqrt(x) > z / y && z*2 + w*3 + x*7 < Math.pow(y,6) && z + w > x + y && w
  // < x/y
  public static void benchmark61(double x, double y, double z, double w) {
    if (x + y > z / w
        && Math.sqrt(x) > z / y
        && z * 2 + w * 3 + x * 7 < Math.pow(y, 6)
        && z + w > x + y
        && w < x / y) {
      assert false;
    }
  }

  // x + y > z / w && Math.sqrt(x) > z / y && z*2 + w*3 + x*7 < Math.pow(y,6) && z + w > x + y && w
  // < x/y && x > (w+y-z)
  public static void benchmark62(double x, double y, double z, double w) {
    if (x + y > z / w
        && Math.sqrt(x) > z / y
        && z * 2 + w * 3 + x * 7 < Math.pow(y, 6)
        && z + w > x + y
        && w < x / y
        && x > (w + y - z)) {
      assert false;
    }
  }

  // x + y > z / w && Math.sqrt(x) > z / y && Math.log(x*y) > Math.log(t+w+z) && z*2 + w*3 + x*7 <
  // Math.pow(y,6) && z + w > x + y && w < x/y && x > (w+y-z) && Math.log10(t*x) < Math.sqrt(w*y*z)
  public static void benchmark63(double x, double y, double z, double w, double t) {
    if (x + y > z / w
        && Math.sqrt(x) > z / y
        && Math.log(x * y) > Math.log(t + w + z)
        && z * 2 + w * 3 + x * 7 < Math.pow(y, 6)
        && z + w > x + y
        && w < x / y
        && x > (w + y - z)
        && Math.log10(t * x) < Math.sqrt(w * y * z)) {
      assert false;
    }
  }

  // x + y > z / (w + t) && Math.sqrt(x) > z / y && Math.log(x*y) > Math.log(t+w+z) && z*2 + w*3 +
  // x*7 < Math.pow(y,t) && z + w > x + y && w < x/y && x > (w+y-z) && Math.log10(t*x) <
  // Math.sqrt(w*y*z) &&	x * (t + y) > Math.log(w*z*3)
  public static void benchmark64(double x, double y, double z, double w, double t) {
    if (x + y > z / (w + t)
        && Math.sqrt(x) > z / y
        && Math.log(x * y) > Math.log(t + w + z)
        && z * 2 + w * 3 + x * 7 < Math.pow(y, t)
        && z + w > x + y
        && w < x / y
        && x > (w + y - z)
        && Math.log10(t * x) < Math.sqrt(w * y * z)
        && x * (t + y) > Math.log(w * z * 3)) {
      assert false;
    }
  }

  // x + y > z / (w + t) && Math.sqrt(x) > z / y && Math.log(x*y) > Math.log(t+w+z) && z*2 + w*3 +
  // x*7 < Math.pow(y,t) && z + w > x + y && w < x/y && x > (w+y-z) && Math.log10(t*x) <
  // Math.sqrt(w*y*z) && x * Math.cos(t + y) > Math.log(w*z*3)
  public static void benchmark65(double x, double y, double z, double w, double t) {
    if (x + y > z / (w + t)
        && Math.sqrt(x) > z / y
        && Math.log(x * y) > Math.log(t + w + z)
        && z * 2 + w * 3 + x * 7 < Math.pow(y, t)
        && z + w > x + y
        && w < x / y
        && x > (w + y - z)
        && Math.log10(t * x) < Math.sqrt(w * y * z)
        && x * Math.cos(t + y) > Math.log(w * z * 3)) {
      assert false;
    }
  }

  // x + y > (z+ t) / (w + t) && Math.sqrt(x) > z / y && Math.log(x*y) > Math.log(t+w+z) && z*2 +
  // w*3 + x*7 < Math.pow(y,t) && z + w > x + y && w < x/y && x > (w+y-z) && Math.log10(t*x) <
  // Math.sqrt(w*y*z) && x * Math.cos(t + y) > Math.log(w*z*3) && Math.cos(t) > Math.cos(y)
  public static void benchmark66(double x, double y, double z, double w, double t) {
    if (x + y > (z + t) / (w + t)
        && Math.sqrt(x) > z / y
        && Math.log(x * y) > Math.log(t + w + z)
        && z * 2 + w * 3 + x * 7 < Math.pow(y, t)
        && z + w > x + y
        && w < x / y
        && x > (w + y - z)
        && Math.log10(t * x) < Math.sqrt(w * y * z)
        && x * Math.cos(t + y) > Math.log(w * z * 3)
        && Math.cos(t) > Math.cos(y)) {
      assert false;
    }
  }

  // x - y + Math.tan(v)> (z+ t) / (w + t) && Math.sqrt(x-t) > z / y && Math.log(x*y) >
  // Math.log(t+w+z) && z*2 + w*3 + x*7 < Math.pow(y,t)*Math.cos(v) && z + w > x + y && w < x/y && x
  // > (w+y-z) && Math.log10(t*x) < Math.sqrt(w*y*z) && x * Math.cos(t + y) > Math.log(w*z*3) &&
  // Math.cos(t) * Math.sin(v) > Math.cos(y)
  public static void benchmark67(double x, double y, double z, double w, double t, double v) {
    if (x - y + Math.tan(v) > (z + t) / (w + t)
        && Math.sqrt(x - t) > z / y
        && Math.log(x * y) > Math.log(t + w + z)
        && z * 2 + w * 3 + x * 7 < Math.pow(y, t) * Math.cos(v)
        && z + w > x + y
        && w < x / y
        && x > (w + y - z)
        && Math.log10(t * x) < Math.sqrt(w * y * z)
        && x * Math.cos(t + y) > Math.log(w * z * 3)
        && Math.cos(t) * Math.sin(v) > Math.cos(y)) {
      assert false;
    }
  }

  // x - y + Math.tan(v)> (z+ t) / (w + t) && Math.sqrt(x-t) > z / y && Math.log(x*y) >
  // Math.log(t+w+z) && z*2 + w*3 + x*7 < Math.pow(y,t)*Math.cos(v) && z + w > x + y && w < x/y && x
  // > (w+y-z) && Math.log10(t*x) < Math.sqrt(w*y*z) && x * Math.cos(t + y) > Math.log(w*z*3) &&
  // Math.cos(t) * Math.sin(v) > Math.cos(y)
  public static void benchmark68(double x, double y, double z, double w, double t, double v) {
    if (x - y + Math.tan(v) > (z + t) / (w + t)
        && Math.sqrt(x - t) > z / y
        && Math.log(x * y) > Math.log(t + w + z)
        && z * 2 + w * 3 + x * 7 < Math.pow(y, t) * Math.cos(v)
        && z + w > x + y
        && w < x / y
        && x > (w + y - z)
        && Math.log10(t * x) < Math.sqrt(w * y * z)
        && x * Math.cos(t + y) > Math.log(w * z * 3)
        && Math.cos(t) * Math.sin(v) > Math.cos(y)) {
      assert false;
    }
  }

  // x - y + Math.tan(v)> (z+ t) / (w + t) && Math.sqrt(x-t) > z / y && Math.log(x*y) >
  // Math.log(t+w+z) && z*2 + w*3 + x*7 < Math.pow(y,t)*Math.cos(v) && z + w > x + y && w < x/y && x
  // > (w+y-z) && Math.log10(t*x) < Math.sqrt(w*y*z) && x * Math.cos(t + y) > Math.log(w*z*3) &&
  // Math.cos(t) * Math.sin(v) > Math.cos(y) && Math.sin(x*y) + Math.sin(z*w) + Math.sin(t*v) <
  // Math.cos(x*y) + Math.cos(z*w) + Math.cos(t*v)
  public static void benchmark69(double x, double y, double z, double w, double t, double v) {
    if (x - y + Math.tan(v) > (z + t) / (w + t)
        && Math.sqrt(x - t) > z / y
        && Math.log(x * y) > Math.log(t + w + z)
        && z * 2 + w * 3 + x * 7 < Math.pow(y, t) * Math.cos(v)
        && z + w > x + y
        && w < x / y
        && x > (w + y - z)
        && Math.log10(t * x) < Math.sqrt(w * y * z)
        && x * Math.cos(t + y) > Math.log(w * z * 3)
        && Math.cos(t) * Math.sin(v) > Math.cos(y)
        && Math.sin(x * y) + Math.sin(z * w) + Math.sin(t * v)
            < Math.cos(x * y) + Math.cos(z * w) + Math.cos(t * v)) {
      assert false;
    }
  }

  public static void benchmark71(
      double a,
      double b,
      double c,
      double d,
      double e,
      double f,
      double g,
      double h,
      double i,
      double j,
      double k,
      double l) {
    if (Math.sin(a) > Math.sin(b)
        && Math.sin(b) > Math.sin(c)
        && Math.sin(c) > Math.sin(d)
        && Math.sin(d) > Math.sin(e)
        && Math.sin(e) > Math.sin(f)
        && Math.sin(f) > Math.sin(g)
        && Math.sin(g) > Math.sin(h)
        && Math.sin(h) > Math.sin(i)
        && Math.sin(i) > Math.sin(j)
        && Math.sin(j) > Math.sin(k)
        && Math.sin(k) > Math.sin(l)) {
      assert false;
    }
  }

  public static void benchmark72(
      double a, double b, double c, double d, double e, double f, double g) {
    if (Math.sin(a) > Math.sin(b)
        && Math.sin(b) > Math.sin(c)
        && Math.sin(c) > Math.sin(d)
        && Math.sin(d) > Math.sin(e)
        && Math.sin(e) > Math.sin(f)
        && Math.sin(f) > Math.sin(g)) {
      assert false;
    }
  }

  public static void benchmark73(
      double a,
      double b,
      double c,
      double d,
      double e,
      double f,
      double g,
      double h,
      double i,
      double j) {
    if (Math.sin(a) > Math.sin(b)
        && Math.sin(b) > Math.sin(c)
        && Math.sin(c) > Math.sin(d)
        && Math.sin(d) > Math.sin(e)
        && Math.sin(e) > Math.sin(f)
        && Math.sin(f) > Math.sin(g)
        && Math.sin(g) > Math.sin(h)
        && Math.sin(h) > Math.sin(i)
        && Math.sin(i) > Math.sin(j)) {
      assert false;
    }
  }

  public static void benchmark74(
      double a, double b, double c, double d, double e, double f, double g, double h) {
    if (Math.sin(a) > Math.sin(b)
        && Math.sin(b) > Math.sin(c)
        && Math.sin(c) > Math.sin(d)
        && Math.sin(d) > Math.sin(e)
        && Math.sin(e) > Math.sin(f)
        && Math.sin(f) > Math.sin(g)
        && Math.sin(g) > Math.sin(h)) {
      assert false;
    }
  }

  public static void benchmark75(
      double a, double b, double c, double d, double e, double f, double g, double h, double i) {
    if (Math.sin(a) > Math.sin(b)
        && Math.sin(b) > Math.sin(c)
        && Math.sin(c) > Math.sin(d)
        && Math.sin(d) > Math.sin(e)
        && Math.sin(e) > Math.sin(f)
        && Math.sin(f) > Math.sin(g)
        && Math.sin(g) > Math.sin(h)
        && Math.sin(h) > Math.sin(i)) {
      assert false;
    }
  }

  public static void benchmark76(
      double a, double b, double c, double d, double e, double f, double g, double h, double i) {
    if (a > b && b > c && c > d && d > e && e > f && f > g && g > h && h > i) {
      assert false;
    }
  }

  public static void benchmark77(
      double a,
      double b,
      double c,
      double d,
      double e,
      double f,
      double g,
      double h,
      double i,
      double j) {
    if (a > b && b > c && c > d && d > e && e > f && f > g && g > h && h > i && i > j) {
      assert false;
    }
  }

  // AND((0.0 ==
  // (pow_((($V84*sin_(((0.017453292519943295*$V85)-(0.017453292519943295*$V86))))-(0.0*$V87)),2.0)+pow_(($V84*cos_((((0.017453292519943295*$V85)-(0.017453292519943295*$V86))+0.0))),2.0))),($V82 != 0))

  public static void benchmark79(double a, double b, double c, double d, int e) {
    if ((0.0
            == (Math.pow(
                    ((a * Math.sin((0.017453292519943295 * b) - (0.017453292519943295 * c)))
                        - (0.0 * d)),
                    2.0)
                + Math.pow(
                    (a * Math.cos(((0.017453292519943295 * b) - (0.017453292519943295 * c)) + 0.0)),
                    2.0)))
        && e != 0) {
      assert false;
    }
  }

  // (1.5 - x1 * (1 - x2)) == 0
  public static void benchmark80(double a, double b) {
    if ((1.5 - a * (1 - b)) == 0) {
      assert false;
    }
  }

  // (-13 + x1 + ((5 - x2) * x2 - 2) * x2) + (-29 + x1 + ((x2 + 1) * x2 - 14) * x2) == 0
  public static void benchmark81(double a, double b) {
    if ((-13 + a + ((5 - b) * b - 2) * b) + (-29 + a + ((b + 1) * b - 14) * b) == 0) {
      assert false;
    }
  }

  // (10 * (x2 - x1 * x1)) == 0 && (1 - x1) == 0 && (Math.sqrt(90) * (x4 - x3 * x3)) == 0 && (1 -
  // x3) == 0 && (Math.sqrt(10) * (x2 + x4 - 2)) == 0 && (Math.Pow(10, -0.5) * (x2 - x4)) == 0
  public static void benchmark84(double a, double b, double c, double d) {
    if ((10 * (b - a * a)) == 0
        && (1 - a) == 0
        && (Math.sqrt(90) * (d - c * c)) == 0
        && (1 - c) == 0
        && (Math.sqrt(10) * (b + d - 2)) == 0
        && (Math.pow(10, -0.5) * (b - d)) == 0) {
      assert false;
    }
  }

  public static void benchmark91(double x, double y) {
    if (Math.sin(x) == -Math.sin(y) && Math.sin(x) > 0) assert false;
  }
}
