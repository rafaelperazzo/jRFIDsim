package simulator.rfid.passive;

import junit.framework.TestCase;

import org.junit.Test;

public class C1G2Test extends TestCase{
	
	@Test
	public void testDBTSA() {
		Simulator s;
		for (int i =0; i<=0; i++) {
			s = new Simulator(20,SimulatorConstants.DBTSA, 4,50,90,100,100,false,1);
			s.startDBTSA();
		}
		
		//System.out.println(s.tags.size());
	}
	/*
	@Test
	public void testC1G2Method() {
		Simulator s;
		
		for (int i=0; i<=10; i++) {
			s = new Simulator(100,SimulatorConstants.C1G2, 4,50,90,100,100,false,1);
			assertEquals(100,s.tags.size());
			s.startDFSA();
			assertEquals(0,s.tags.size());
			assertEquals(0.33, s.getStatsDataSef().get(100).getMean(),0.025);
		}
	} /*
	
	@Test
	public void testDBTSAMethod() {
		Simulator s;
		
		for (int i=0; i<=1; i++) {
			s = new Simulator(500,SimulatorConstants.DBTSA, 4,100,90,500,100,false,1);
			assertEquals(500,s.tags.size());
			s.startDFSA();
			assertEquals(0,s.tags.size());
			assertEquals(0.39, s.getStatsDataSef().get(500).getMean(),0.025);
		}
	}
	
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
	public void testMiniC1G2Method() {
		Simulator s;
		
		for (int i=0; i<=1; i++) {
			s = new Simulator(3,SimulatorConstants.C1G2, 2,50,90,3,100,false,1);
			assertEquals(3,s.tags.size());
			s.startDFSA();
			//System.out.println(s.tags.size());
			assertEquals(0,s.tags.size());
			//assertEquals(0.33, s.getStatsDataSef().get(3).getMean(),0.025);
			//System.out.println("Mini C1G2: " + s.getStatsDataSef().get(3).getMean());
		}
	}
	
	@Test
	public void testMOTAMethod() {
		Simulator s;
		
		for (int i=0; i<=1; i++) {
			s = new Simulator(100,SimulatorConstants.MOTA, 4,50,90,100,100,false,1);
			assertEquals(100,s.tags.size());
			s.startDFSA();
			assertEquals(0,s.tags.size());
			assertEquals(0.4, s.getStatsDataSef().get(100).getMean(),0.025);
		}
	}
	
	@Test
	public void testLowerMethod() {
		Simulator s;
		
		for (int i=0; i<=1; i++) {
			s = new Simulator(1000,SimulatorConstants.LOWER, 128,50,90,1000,100,false,1);
			assertEquals(1000,s.tags.size());
			s.startDFSA();
			assertEquals(0,s.tags.size());
			assertEquals(0.32, s.getStatsDataSef().get(1000).getMean(),0.025);
		}
	}
	
	@Test
	public void testSchouteMethod() {
		Simulator s;
		
		for (int i=0; i<=1000; i++) {
			s = new Simulator(1000,SimulatorConstants.SCHOUTE, 128,50,90,1000,100,false,1);
			assertEquals(1000,s.tags.size());
			s.standardDfsa();
			assertEquals(0,s.tags.size());
		}
	}
	
	@Test
	public void testEomleeMethod() {
		Simulator s;
		
		for (int i=0; i<=1; i++) {
			s = new Simulator(1000,SimulatorConstants.EOMLEE, 128,50,90,1000,100,false,1);
			assertEquals(1000,s.tags.size());
			s.startDFSA();
			assertEquals(0,s.tags.size());
			assertEquals(0.34, s.getStatsDataSef().get(1000).getMean(),0.025);
		}
	}
	
	*/
}
