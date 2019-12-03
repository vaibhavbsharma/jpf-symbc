import org.sosy_lab.sv_benchmarks.Verifier;

import static java.lang.Integer.reverse;

public class MyCRC32 {
    public static int computeCRC32(byte message[]) {
        int i, j;
        int byt, crc;

        i = 0;
        crc = 0xFFFFFFFF;
        while (i < message.length) {
            byt = message[i];            // Get next byt.
            byt = reverse(byt);         // 32-bit reversal.
            for (j = 0; j <= 7; j++) {    // Do eight times.
                if ((int)(crc ^ byt) < 0)
                    crc = (crc << 1) ^ 0x04C11DB7;
                else crc = crc << 1;
                byt = byt << 1;          // Ready next msg bit.
            }
            i = i + 1;
        }
        return reverse(~crc);
    }

    public static void main(String args[]) {
        int maxLen = Integer.parseInt(System.getenv("MAX_LENGTH"));
        byte bytes[] = new byte[maxLen];
        maxLen--;
        while (maxLen >= 0) bytes[maxLen--] = getSymChar((byte) 'a');
        for (int i = 0; i < maxLen; i++)
            bytes[i] = Verifier.nondetByte();
        int crc32Result = computeCRC32(bytes);
        for (int i = 0; i < maxLen; i++)
            bytes[i] = 'a';
        if (crc32Result == computeCRC32(bytes)) {
            System.out.println("success!!");
            assert false;
        }
    }

    private static byte getSymChar(byte a) {
        return a;
    }
}
