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
		Thread schoute = new Thread(new Simulator(100,SimulatorConstants.SCHOUTE, "sef.schoute.128.5000.txt", "total.schoute.128.5000.txt",128,100,90,5000,100));
		schoute.start(); 
		
		//LOWER BOUND METHOD
		Thread lower = new Thread(new Simulator(100,SimulatorConstants.LOWER, "sef.lower.128.5000.txt", "total.lower.128.5000.txt",128,100,90,5000,100));
		lower.start();
		
		//EOM-LEE METHOD
		Thread eomlee = new Thread(new Simulator(100,SimulatorConstants.EOMLEE, "sef.eom.128.5000.txt", "total.eom.128.5000.txt",128,100,90,5000,100));
		eomlee.start();
		
		//C1G2 METHOD
		Thread c1g2 = new Thread(new Simulator(100,SimulatorConstants.C1G2, "sef.c1g2.4.5000.txt", "total.c1g2.4.5000.txt",4,100,90,5000,100));
		c1g2.start();
		
	}

}
