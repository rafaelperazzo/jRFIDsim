package simulator.rfid.passive;

import java.io.File;

/**
 * 
 * @author Rafael Perazzo Barbosa Mota
 *
 */
public class Main {

	public static void main(String[] args) {
		SimulatorConstants.startHashTable();
		File f = new File("sef.schoute.128.5000.txt.stats");
		if (f.exists()) f.delete();
		f = new File("total.schoute.128.5000.txt.stats");
		if (f.exists()) f.delete();
		f = new File("sef.lower.128.5000.txt.stats");
		if (f.exists()) f.delete();
		f = new File("total.lower.128.5000.txt.stats");
		if (f.exists()) f.delete();
		f = new File("sef.c1g2.4.5000.txt.stats");
		if (f.exists()) f.delete();
		f = new File("total.c1g2.4.5000.txt.stats");
		if (f.exists()) f.delete();
		File runStats = new File("stats.txt");
		if (runStats.exists()) runStats.delete();
		f = new File("instructions.stats");
		if (f.exists()) f.delete();
		Thread schoute = new Thread(new Simulator(100,SimulatorConstants.SCHOUTE, "sef.schoute.128.5000.txt", "total.schoute.128.5000.txt",128,100,90,5000,100));
		schoute.start();
		
		Thread lower = new Thread(new Simulator(100,SimulatorConstants.LOWER, "sef.lower.128.5000.txt", "total.lower.128.5000.txt",128,100,90,5000,100));
		lower.start();
		
		Thread eomlee = new Thread(new Simulator(100,SimulatorConstants.EOMLEE, "sef.eom.128.5000.txt", "total.eom.128.5000.txt",128,100,90,5000,100));
		eomlee.start();
		
		Thread c1g2 = new Thread(new Simulator(100,SimulatorConstants.C1G2, "sef.c1g2.4.5000.txt", "total.c1g2.4.5000.txt",4,100,90,5000,100));
		c1g2.start();
		
	}

}
