package simulator.rfid.passive;

import java.io.File;

/**
 * 
 * @author Rafael Perazzo Barbosa Mota
 *
 */
public class Main {

	private static void printHelp() {
		System.out.println("Usage: ");
		System.out.println("java -jar <initialNumberOfTags> <finalNumberOfTags> <step> <confidenceLevel> <numberOfIterations> <method> <deleteStatusFile> <initialFrameSize> <kind> <adjust>");
		System.out.println("method: 1 - Schoute; 2-LOWER; 3-Eom-Lee; 4-Mota; 5-C1G2");
		System.out.println("deleteStatusFile: 1- Yes; 0-No");
		System.out.println("initialFrameSize: 128,64,256(for non Q-based) or 4, 5, 6 (for Q-based)");
		System.out.println("kind: estimation: Test estimation. all: All methods; no: Just the selected method");
		System.out.println("adjust: Estimation frame adjust: 0.66, 1.3, ...");
	}
	
	public static void main(String[] args) {
		
		/*
		 * java -jar <initialNumberOfTags> <finalNumberOfTags> <step> <confidenceLevel> <numberOfIterations> <method> <deleteStatusFile> <initialFrameSize>
		 * 
		 * method: 1 - Schoute; 2-LOWER; 3-Eom-Lee; 4-Mota; 5-C1G2
		 * deleteStatusFile: 1- Yes; 0-No
		 */
		SimulatorConstants.startHashTable();
		Util.writePlotFile(100, 15000, 1000, 200, 90, 33000);
		int begin=100,end=1000,steps=100,ci=90, iterations=100;
		int method=1, deleteStats=1, initialFrameSize=128;
		double adjust = 1;
		String all = "no";
		if (args.length==10) {
			begin=Integer.parseInt(args[0]);
			end=Integer.parseInt(args[1]);
			steps=Integer.parseInt(args[2]);
			ci=Integer.parseInt(args[3]);
			iterations=Integer.parseInt(args[4]);
			method=Integer.parseInt(args[5]);
			deleteStats=Integer.parseInt(args[6]);
			initialFrameSize=Integer.parseInt(args[7]);
			all = args[8];
			adjust = Double.parseDouble(args[9]);
			if (deleteStats==1) {
				File f = new File("stats.txt");
				if (f.exists()) f.delete();
			}
			if (all.equals("no")) {
				Thread sim = new Thread(new Simulator(begin,method, initialFrameSize,iterations,ci,end,steps,true,adjust));
				sim.start();
			}
			else if (all.equals("test")) {
				Simulator sim = new Simulator(begin,4, 4,iterations,ci,end,steps,true,adjust);
				sim.testEstimation();
			}
			else {
				Simulator schoute = new Simulator(begin,1, 128,iterations,ci,end,steps,true,adjust);
				schoute.startDFSA();
				System.gc();
				Simulator lower = new Simulator(begin,2, 128,iterations,ci,end,steps,true,adjust);
				lower.startDFSA();
				Util.writePlotFile(begin, end, iterations, steps, ci, lower.getStatsDataTotal().get(end).getMean());
				System.gc();
				Simulator eomlee = new Simulator(begin,3, 128,iterations,ci,end,steps,true,adjust);
				eomlee.startDFSA();
				System.gc();
				Simulator mota = new Simulator(begin,4, 4,iterations,ci,end,steps,true,adjust);
				mota.startDFSA();
				System.gc();
				Simulator c1g2 = new Simulator(begin,5, 4,iterations,ci,end,steps,true,adjust);
				c1g2.startDFSA();
			}
			 
		}
		
		else if (args.length==1) {
			if (args[0].equals("gnuplot")) {
				Util.writePlotFile(100, 15000, 1000, 200, 90, 32000);
				System.out.println("Gnuplot file created ...");
			}
			else {
				printHelp();
			}
		}
		
		else {
			printHelp();
		}
		
	}

}
