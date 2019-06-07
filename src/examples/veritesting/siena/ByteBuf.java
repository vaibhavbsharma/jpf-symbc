import java.util.ArrayList;

public class ByteBuf {
    private ArrayList buf;
    
    public ByteBuf(int x, int y) {
    	buf = new ArrayList();
    }

    public ByteBuf() {
    	buf = new ArrayList();
    }

    public void appendBit(byte b) {
    	Byte bb = new Byte(b);
    	buf.add(bb);
    }

    public void appendInt(int x) {
    	byte x_b = (byte)x;
    	Byte xx = new Byte(x_b);
    	buf.add(xx);
    }

    public void appendByte(byte[] bytes) {
    	for(int i = 0; i < bytes.length;) {
    		byte byte_P = bytes[i];
    		Byte byte_PP = new Byte(byte_P);
    		buf.add(byte_PP);
    		i = i + 1;
    	}
    }

    public void appendString(String s) {
    	byte[] s_P = s.getBytes();
    	appendByte(s_P);
    }

    public void reset() {
    	buf = new ArrayList();
    }

    public byte[] bytes() {
    	int pos = buf.size();
    	byte[] res = new byte[pos];
    	for(int i = 0; i < pos;){
    		Byte byte_PP = (Byte)buf.get(i);
    		byte byte_P = byte_PP.byteValue();
    		res[i] = byte_P;
    		i = i + 1;
    	}
    	return res;
    }
}
