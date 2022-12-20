package tcgbenchmarks.testcaseperf;

import static java.lang.Integer.numberOfTrailingZeros;

public class TCG_PaperExample {

  public static void main(String[] args) {
    getSetBits(1);
  }


  public static int getSetBits(int i) {
    int numOfSetBits = 0;
    while (i != 0) {
      if ((i & 15) != 0)
        numOfSetBits += count4_setBits(i);
      i = (i >>> 4);
    }
    return numOfSetBits;
  }

  public static int count4_setBits(int i) {
    int count = 0;
    if ((i & 1) == 1)
      count++;
    if ((i & 2) == 2)
      count++;
    if ((i & 4) == 4)
      count++;
    if ((i & 8) == 8)
      count++;

    return count;
  }


  public static int getTrailingZeros1(int i) {
    i = i << 28;
    int numOfZeros = 3;
    int temp = (i << 2);
    if (temp != 0) {
      numOfZeros = 1;
      i = temp;
    }
    temp = (i << 1);
    if (temp != 0)
      numOfZeros = (numOfZeros - 1);

    return numOfZeros;
  }


  public static int separateBits_2(int i) {
    int j = 0;
    if (i >= -2 && i <= 3) {
//        if (i >= -2 && i <=2) {
      while (i != 0) {
        int trailHasZero = (i & 1);
        if (trailHasZero == 0) {
          int numberOfTrailingZeros = numberOfTrailingZeros_4(i);
          i = (i >> numberOfTrailingZeros);
        } else {
          j = (j >>> 1);
          j = (j ^ 2);
          i = (i >>> 1);
        }
      }
//            System.out.println(Integer.toBinaryString(j));
//            System.out.println(j);
    }
    return j;
  }

  public static int numberOfTrailingZeros_2(int i) {
    // HD, Figure 5-14
    i = i << 30;
    if (i == 0)
      return 2;

    if (i << 1 != 0) {
      return 0;
    }
    return 1;
//        return (n - ((((i << 1)) >>> 3)));
  }

  public static int separateBits_4(int i) {
    int j = 0;
    if (i >= -8 && i <= 7) {
//        if (i >= -2 && i <=2) {
      while (i != 0) {
        int trailHasZero = (i & 1);
        if (trailHasZero == 0) {
          int numberOfTrailingZeros = numberOfTrailingZeros_4(i);
          i = (i >> numberOfTrailingZeros);
        } else {
          j = (j >>> 1);
          j = (j ^ 8);
          i = (i >>> 1);
        }
      }
//            System.out.println(Integer.toBinaryString(j));
//            System.out.println(j);
    }
    return j;
  }

  public static int numberOfTrailingZeros_4(int i) {
    // HD, Figure 5-14
    int y;
    i = i << 28;
    if (i == 0)
      return 4;
    int n = 3;

    y = (i << 2);
    if (y != 0) {
      n = (n - 2);
      i = y;
    }
    y = (i << 1);
    if (y != 0) {
      n = (n - 1);
    }

    return n;
//        return (n - ((((i << 1)) >>> 3)));
  }

  public static int separateBits_8(int i) {
    int j = 0;
    if (i >= -128 && i <= 127) {
      while (i != 0) {// the loop
        int trailHasZero = (i & 1);
        if (trailHasZero == 0) {
          int numberOfTrailingZeros = numberOfTrailingZeros_8(i);
          i = (i >> numberOfTrailingZeros);
        } else {
          j = (j >>> 1);
          j = (j ^ 128);
          i = (i >>> 1);
        }
      }
//            System.out.println(Integer.toBinaryString(j));
//            System.out.println(j);
    }
    return j;
  }

  public static int numberOfTrailingZeros_8(int i) {
    // HD, Figure 5-14
    int y;
    i = i << 24;
//        if (i == 0) return 8;
    int n = 7;

    y = (i << 4);
    if (y != 0) {
      n = (n - 4);
      i = y;
    }
    y = (i << 2);
    if (y != 0) {
      n = (n - 2);
      i = y;
    }
    y = (i << 1);
    if (y != 0) {
      n = (n - 1);
    }

    return n;
//        return (n - ((((i << 1)) >>> 7)));
  }

  public static int separateBits_16(int i) {
    int j = 0;
    if (i >= -32768 && i <= 32767) {
      while (i != 0) {
        int trailHasZero = (i & 1);
        if (trailHasZero == 0) {
          int numberOfTrailingZeros = numberOfTrailingZeros_16(i);
          i = (i >> numberOfTrailingZeros);
        } else {
          j = (j >>> 1);
          j = (j ^ 32768);
          i = (i >>> 1);
        }
      }
//            System.out.println(Integer.toBinaryString(j));
//            System.out.println(j);
    }
    return j;
  }

  public static int numberOfTrailingZeros_16(int i) {
    // HD, Figure 5-14
    int y;
    i = i << 16;
//        if (i == 0) return 16;
    int n = 15;

    y = (i << 8);
    if (y != 0) {
      n = (n - 8);
      i = y;
    }
    y = (i << 4);
    if (y != 0) {
      n = (n - 4);
      i = y;
    }
    y = (i << 2);
    if (y != 0) {
      n = (n - 2);
      i = y;
    }
    y = (i << 1);
    if (y != 0) {
      n = (n - 1);
    }

    return n;
//        return (n - ((((i << 1)) >>> 15)));
  }

  public static int separateBits_32(int i) {
    int j = 0;
//        if (i >= -2147483648 && i <= 2147483647) {
//        if (i >= -1 && i < 20) {
    if (i >= -3 && i < 5) {
      while (i != 0) {
        int trailHasZero = (i & 1);
        if (trailHasZero == 0) {
          int numberOfTrailingZeros = numberOfTrailingZeros(i);
          i = (i >> numberOfTrailingZeros);
        } else {
          j = (j >>> 1);
          j = j ^ (Integer.reverse(1));
          i = (i >>> 1);
        }
      }
//            System.out.println(Integer.toBinaryString(j));
//            System.out.println(j);
    }
    return j;
  }

  public static int numberOfTrailingZeros(int i) {
    // HD, Figure 5-14
    int y;
    if (i == 0)
      return 32;
    int n = 31;
    y = i << 16;
    if (y != 0) {
      n = n - 16;
      i = y;
    }
    y = i << 8;
    if (y != 0) {
      n = n - 8;
      i = y;
    }
    y = i << 4;
    if (y != 0) {
      n = n - 4;
      i = y;
    }
    y = i << 2;
    if (y != 0) {
      n = n - 2;
      i = y;
    }
    y = i << 1;
    if (y != 0) {
      n = n - 1;
    }

    return n;

//        return n - ((i << 1) >>> 31);
  }
}
