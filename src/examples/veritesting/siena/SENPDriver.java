public class SENPDriver {

//	public static byte[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//	public static SENPPacket packet = new SENPPacket();
	public static void main(String[] args) throws Exception {
		mainProcess(1, 2, 3, 4, 5, 5, 7, 8, 9);
	}
	
	public static void mainProcess(int to1, int to2, int to3, int id1, int id2, int id3, int handler1, int handler2, int handler3) {
		SENPPacket packet = new SENPPacket();

		byte _to1 = (byte)to1;
		byte _to2 = (byte)to2;
		byte _to3 = (byte)to3;
		packet.initTo(_to1, _to2, _to3);
		byte _id1 = (byte)id1;
		byte _id2 = (byte)id2;
		byte _id3 = (byte)id3;
		packet.initId(_id1, _id2, _id3);
		byte _handler1 = (byte)handler1;
		byte _handler2 = (byte)handler2;
		byte _handler3 = (byte)handler3;
		packet.initHandler(_handler1, _handler2, _handler3);
		
		SENP.encodePacket(packet);
//		byte[] result = SENP.encodePacket(packet);
//		for (int i = 0; i < result.length; i++) {
//			System.out.print(result[i] + " ");
//		}

	}
	

}
