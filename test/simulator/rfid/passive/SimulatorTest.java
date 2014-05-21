package simulator.rfid.passive;

import static org.junit.Assert.*;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Test;

public class SimulatorTest extends TestCase{

	
	@Test
	/**
	 * Test the Simulator object after the constructor method
	 */
	public void testSimulator() {
		
		Simulator s = new Simulator(100,SimulatorConstants.SCHOUTE, 128,200,90,5000,100,false);
		assertEquals(100, s.getNumberOfTags());
		assertEquals(1, s.getMethod());
		assertEquals(128, s.getInitialFrameSize());
		assertEquals(200, s.getIterations());
		assertEquals(90, s.getConfidenceLevel(),10);
		assertEquals(5000, s.getMaxTags());
		assertEquals(100, s.getStepTags());
		assertEquals(0, s.getStatsSef().getN());
		assertEquals(0,s.getStatsTotal().getN());
		assertEquals(0, s.getCol());
		assertEquals(0,s.getIdl());
		assertEquals(0,s.getSuc());
		assertEquals(1, s.getFrames());
		assertEquals(0,s.getTotalSlots());
		assertFalse(s.isEnd());
		assertEquals(s.getCurrentFrameSize(), s.getInitialFrameSize());
		assertEquals(0,s.getFrame().size());
		assertEquals(s.getNumberOfTags(),s.getTags().size());
		
	}
	
	@Test
	public void testSchoute() {
		
		Simulator s = new Simulator(100,SimulatorConstants.SCHOUTE, 128,200,90,5000,100,false);
		assertEquals(7,s.schoute(3));
		assertEquals(5,s.schoute(2));
		assertEquals(10,s.schoute(4));
		assertEquals(12,s.schoute(5));
	}
	
	@Test
	public void testInitCurrentFrame() {
		Simulator s = new Simulator(100,SimulatorConstants.SCHOUTE, 128,200,90,5000,100,false);
		s.initCurrentFrame();
		assertEquals(s.getInitialFrameSize(), s.getFrame().size());
	}
	
	@Test
	public void testSendQuery() {
		Simulator s = new Simulator(100,SimulatorConstants.SCHOUTE, 128,200,90,5000,100,false);
		s.initCurrentFrame();
		s.sendQuery();
		for (int i=0; i<s.getTags().size(); i++) {
			boolean isTrue = ((s.getTags().get(i).getRng16()>=0)&&(s.getTags().get(i).getRng16()<s.getCurrentFrameSize()));
			assertTrue(isTrue);
			assertNotEquals(-1, s.getTags().get(i).getRng16());
		}
	}
	
	@Test
	public void testEomLee() {
		Simulator s = new Simulator(100,SimulatorConstants.SCHOUTE, 128,200,90,5000,100,false);
		assertEquals(7, s.eomlee(3, 20, 0.001, 32));
		assertEquals(194, s.eomlee(70, 30, 0.001, 128));
	}
	
	@Test
	public void testMiniSet() {
		Simulator s = new Simulator(3,SimulatorConstants.SCHOUTE, 3,1000,90,3,1,true);
		s.startDFSA();
		File f = new File(s.statsSefFile);
		assertTrue(f.exists());
	}
	
}
