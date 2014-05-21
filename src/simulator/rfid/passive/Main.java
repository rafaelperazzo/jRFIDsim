package simulator.rfid.passive;

/**
 * 
 * @author Rafael Perazzo Barbosa Mota
 *
 */
public class Main {

	public static void main(String[] args) {
		
		SimulatorConstants.startHashTable();
		
		//SCHOUTE METHOD
		Thread schoute = new Thread(new Simulator(100,SimulatorConstants.SCHOUTE, 128,100,90,1000,100,true));
		schoute.start(); 
		
		//LOWER BOUND METHOD
		Thread lower = new Thread(new Simulator(100,SimulatorConstants.LOWER, 128,100,90,1000,100,true));
		lower.start();
		
		//EOM-LEE METHOD
		Thread eomlee = new Thread(new Simulator(100,SimulatorConstants.EOMLEE, 128,100,90,1000,100,true));
		eomlee.start();
		
		//C1G2 METHOD
		Thread c1g2 = new Thread(new Simulator(100,SimulatorConstants.C1G2, 4,100,90,1000,100,true));
		c1g2.start();
		
	}

}
