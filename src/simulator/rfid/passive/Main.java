package simulator.rfid.passive;

import java.io.File;

/**
 * 
 * @author Rafael Perazzo Barbosa Mota
 *
 */
public class Main {

	public static void main(String[] args) {
		
		/*
		 * java -jar <initialNumberOfTags> <finalNumberOfTags> <step> <confidenceLevel> <numberOfIterations> <method> <deleteStatusFile> <initialFrameSize>
		 * 
		 * method: 1 - Schoute; 2-LOWER; 3-Eom-Lee; 4-Mota; 5-C1G2
		 * deleteStatusFile: 1- Yes; 0-No
		 */
		
		SimulatorConstants.startHashTable();
		
		int begin=100,end=1000,steps=100,ci=90, iterations=100;
		int method=1, deleteStats=1, initialFrameSize=128;
		if (args.length==8) {
			begin=Integer.parseInt(args[0]);
			end=Integer.parseInt(args[1]);
			steps=Integer.parseInt(args[2]);
			ci=Integer.parseInt(args[3]);
			iterations=Integer.parseInt(args[4]);
			method=Integer.parseInt(args[5]);
			deleteStats=Integer.parseInt(args[6]);
			initialFrameSize=Integer.parseInt(args[7]);
			if (deleteStats==1) {
				File f = new File("stats.txt");
				if (f.exists()) f.delete();
			}
			Thread sim = new Thread(new Simulator(begin,method, initialFrameSize,iterations,ci,end,steps,true));
			sim.start(); 
		}
		else {
			System.out.println("Usage: ");
			System.out.println("java -jar <initialNumberOfTags> <finalNumberOfTags> <step> <confidenceLevel> <numberOfIterations> <method> <deleteStatusFile> <initialFrameSize>");
			System.out.println("method: 1 - Schoute; 2-LOWER; 3-Eom-Lee; 4-Mota; 5-C1G2");
			System.out.println("deleteStatusFile: 1- Yes; 0-No");
			System.out.println("initialFrameSize: 128,64,256(for non Q-based) or 4, 5, 6 (for Q-based)");
		}
		
	}

}
