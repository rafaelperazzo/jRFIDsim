package simulator.rfid.passive;

public class MainTest {
	
	public static void main(String[] args) {
		Simulator s;
		s = new Simulator(20,SimulatorConstants.DBTSA, 4,50,90,100,100,false,1);
		s.startDBTSA();
	}
	
}
