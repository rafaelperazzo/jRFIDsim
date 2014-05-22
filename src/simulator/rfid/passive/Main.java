package simulator.rfid.passive;

/**
 * 
 * @author Rafael Perazzo Barbosa Mota
 *
 */
public class Main {

	public static void main(String[] args) {
		
		SimulatorConstants.startHashTable();
		
		Simulator s = new Simulator(3000,SimulatorConstants.C1G2, 4,200,90,5000,100,false);
		
		/*boolean t1,t2;
		s.startEstimation();
		System.out.println(s.c);
		System.out.println(s.qValue);
		System.out.println(s.currentFrameSize);
		System.out.println(s.totalSlots);*/
		
		//SCHOUTE METHOD
		//Thread schoute = new Thread(new Simulator(100,SimulatorConstants.SCHOUTE, 128,100,90,1000,100,true));
		//schoute.start(); 
		
		//LOWER BOUND METHOD
		//Thread lower = new Thread(new Simulator(100,SimulatorConstants.LOWER, 128,100,90,1000,100,true));
		//lower.start();
		
		//EOM-LEE METHOD
		//Thread eomlee = new Thread(new Simulator(100,SimulatorConstants.EOMLEE, 128,100,90,1000,100,true));
		//eomlee.start();
		
		//C1G2 METHOD
		Thread c1g2 = new Thread(new Simulator(100,SimulatorConstants.C1G2, 4,100,90,1000,100,true));
		c1g2.start();
		
	}

}
