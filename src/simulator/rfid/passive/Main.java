package simulator.rfid.passive;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Simulator s = new Simulator(20000,SimulatorConstants.SCHOUTE);
		s.start();
		s.showSef();
		s.showTotal();
	}

}
