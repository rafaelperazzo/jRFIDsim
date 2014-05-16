package simulator.rfid.passive;

public class Main {

	public static void main(String[] args) {
		Simulator s = new Simulator(1000,SimulatorConstants.SCHOUTE, "sef.1000.txt", "total.1000.txt",128,100,90);
		s.startStandardDFSA();
		//System.out.println(s.getP());
	}

}
