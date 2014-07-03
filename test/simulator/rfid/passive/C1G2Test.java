package simulator.rfid.passive;

import junit.framework.TestCase;

import org.junit.Test;

public class C1G2Test extends TestCase{

	@Test
	public void testNEDFSAMethod() {
		Simulator s;
		
		for (int i=0; i<=1; i++) {
			s = new Simulator(500,SimulatorConstants.NEDFSA, 4,100,90,500,100,false,1);
			assertEquals(500,s.tags.size());
			s.startDFSA();
			assertEquals(0,s.tags.size());
			assertEquals(0.4, s.getStatsDataSef().get(500).getMean(),0.025);
		}
	}
	
	@Test
	public void testC1G2Method() {
		Simulator s;
		
		for (int i=0; i<=1; i++) {
			s = new Simulator(100,SimulatorConstants.C1G2, 4,50,90,100,100,false,1);
			assertEquals(100,s.tags.size());
			s.startDFSA();
			//System.out.println(s.tags.size());
			assertEquals(0,s.tags.size());
			assertEquals(0.33, s.getStatsDataSef().get(100).getMean(),0.025);
		}
	}
	/*
	@Test
	public void testMOTAMethod() {
		Simulator s;
		
		for (int i=0; i<=100; i++) {
			s = new Simulator(100,SimulatorConstants.MOTA, 4,50,90,100,100,false,1);
			assertEquals(100,s.tags.size());
			s.startDFSA();
			assertEquals(0,s.tags.size());
		}
	}
	
	@Test
	public void testLowerMethod() {
		Simulator s;
		
		for (int i=0; i<=100; i++) {
			s = new Simulator(100,SimulatorConstants.LOWER, 128,50,90,100,100,false,1);
			assertEquals(100,s.tags.size());
			s.startDFSA();
			assertEquals(0,s.tags.size());
		}
	}
	
	@Test
	public void testSchouteMethod() {
		Simulator s;
		
		for (int i=0; i<=100; i++) {
			s = new Simulator(100,SimulatorConstants.SCHOUTE, 128,50,90,100,100,false,1);
			assertEquals(100,s.tags.size());
			s.startDFSA();
			assertEquals(0,s.tags.size());
		}
	}
	
	@Test
	public void testEomleeMethod() {
		Simulator s;
		
		for (int i=0; i<=100; i++) {
			s = new Simulator(100,SimulatorConstants.EOMLEE, 128,50,90,100,100,false,1);
			assertEquals(100,s.tags.size());
			s.startDFSA();
			assertEquals(0,s.tags.size());
		}
	}
*/
}
