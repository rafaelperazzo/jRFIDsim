package simulator.rfid.passive;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		
		File f = new File("sef.schoute.128.5000.txt.stats");
		if (f.exists()) f.delete();
		f = new File("total.schoute.128.5000.txt.stats");
		if (f.exists()) f.delete();
		File runStats = new File("stats.txt");
		if (runStats.exists()) runStats.delete();
		Thread s = new Thread(new Simulator(100,SimulatorConstants.SCHOUTE, "sef.schoute.128.5000.txt", "total.schoute.128.5000.txt",128,200,90,5000,100));
		s.start();
		
		Thread p = new Thread(new Simulator(100,SimulatorConstants.LOWER, "sef.lower.128.5000.txt", "total.lower.128.5000.txt",128,200,90,5000,100));
		p.start();
		
	}

}
