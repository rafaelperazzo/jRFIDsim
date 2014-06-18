package simulator.rfid.passive;

import java.util.Hashtable;

public class SimulatorConstants {

	public final static int SCHOUTE = 1;
	public final static int LOWER = 2;
	public final static int EOMLEE = 3;
	public final static int NEDFSA = 4;
	public final static int C1G2 = 5;
	public final static int DBTSA = 6;
	public final static int MOTA = 7;
	private static Hashtable<Integer, String> methods = new Hashtable<Integer,String>();
	
	public static void startHashTable() {
		methods.put(1, "SCHOUTE");
		methods.put(2, "LOWER");
		methods.put(3, "EOMLEE");
		methods.put(4, "NEDFSA");
		methods.put(5, "C1G2");
		methods.put(6, "DBTSA");
		methods.put(7, "MOTA");
	}
	
	public static String getName(int m) {
		return methods.get(m).toString();
	}
	
}
