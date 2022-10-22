public class SheepAndGoat {


    public int reverseBits(int n) {
        int rev = 0;

        // traversing bits of 'n' from the right
        while (n > 0) {
            // bitwise left shift 'rev' by 1
            rev <<= 1;

            // if current bit is '1'
            if ((int) (n & 1) == 1)
                rev = Integer.sum(rev, 1);

            // bitwise right shift 'n' by 1
            n >>= 1;
        }
        return rev;
    }

    public int maxLocal(int a1, int a2, int a3) {

        int max;

        int maxa1a2 = maxPair(a1, a2);
        int maxa2a3 = maxPair(a2, a3);
        int maxa1a3 = maxPair(a1, a3);


        if ((a1 == maxa1a2) && (a1 == maxa1a3))
            max = a1;
        else if ((a2 == maxa1a2) && (a2 == maxa2a3))
            max = a2;
        else
            max = a3;

        return max;
    }


    public int maxPair(int a1, int a2) {

        if(a1 < 0)
            a1 = Math.abs(a1);

        if(a2 < 0)
            a2 = Math.abs(a2);

        if (a1 > a2)
            return a1;
        else
            return a2;
    }


    public int sheepAndGoatLeft(int i) {
        int j = 0;

        System.out.println(Integer.toBinaryString(i));

        while (i != 0) {
            int zeroCount = Integer.numberOfTrailingZeros(i);
            if (zeroCount != 0) {
                i = i >> zeroCount;
            } else {
                j = j >>> 1;
                j = j ^ (Integer.reverse(1));
                i = i >> 1;
            }
        }

        System.out.println(Integer.toBinaryString(j));
        return j;

    }

    public int sheepAndGoatRight(int i) {
        int j = 0;
        System.out.println(Integer.toBinaryString(i));

        while (i != 0) {
            int zeroCount = Integer.numberOfTrailingZeros(i);
            if (zeroCount != 0) {
                i = i >> zeroCount;
            } else {
                j = j << 1;
                j = j ^ 1;
                i = i >> 1;
            }
        }

        System.out.println(Integer.toBinaryString(j));
        return j;
    }


    public int sheepAndGoatLeftNoTrail(int i) {
        int j = 0;

        System.out.println(Integer.toBinaryString(i));

        while (i != 0) {
            if ((i & 0x1) == 0) {
                i = i >> 1;
            } else {
                j = j >>> 1;
                j = j ^ (Integer.reverse(1));
                i = i >> 1;
            }
            System.out.println(Integer.toBinaryString(j));
        }

        System.out.println(Integer.toBinaryString(j));
        return j;

    }


}
