package simulator.rfid.passive;

import java.util.ArrayList;

/**
 * Simulator Class to allow performance evaluation of passive RFID anti-collision algorithms
 * @author Rafael Perazzo Barbosa Mota
 * @email perazzo@ime.usp.br
 *
 */

public class Simulator {
	
	/**
	 * Set of Tags (List of tags)
	 */
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	
	/**
	 * Each frame is a List of Slots
	 */
	private ArrayList<Slot> frame = new ArrayList<Slot>();	
	/**
	 * Collision Slots Counter
	 */
	private int col; 
	
	/**
	 * Success Slots Counter
	 */
	private int suc;
	
	/**
	 * Idle Slots COunter
	 */
	private int idl;
	
	/**
	 * Number of Tags
	 */
	private int numberOfTags;
	
	/**
	 * Number of Frames
	 */
	
	private int frames;
	
	/**
	 * System Efficiency
	 */
	private float sef;
	
	/**
	 * Initial Frame Size
	 */
	private int initialFrameSize;
	
	/**
	 * Current Frame Size
	 */
	private int currentFrameSize;
	
	/**
	 * Total number of used slots
	 */
	private int totalSlots;
	
	/**
	 * End of DFSA ?
	 */
	private boolean end;
	
	/**
	 * DFSA method (Schoute, Eom-Lee, ...)
	 */
	private int method;
	
	/**
	 * Default Constructor
	 */
	public Simulator() {
		this.col = 0;
		this.suc = 0;
		this.idl = 0;
		this.frames = 1;
		this.totalSlots = 0;
		this.end = false;
		this.initialFrameSize = 128;
		this.currentFrameSize = this.initialFrameSize;
		this.numberOfTags = 1;
		this.initTagList();
	}
	
	
	/**
	 * Constructor - Must tell the number of tags
	 * @param numTags Number of Tags
	 */
	public Simulator(int numTags, int method) {
		this.col = 0;
		this.suc = 0;
		this.idl = 0;
		this.frames = 1;
		this.totalSlots = 0;
		this.method = method;
		this.end = false;
		this.numberOfTags = numTags;
		this.initialFrameSize = 128;
		this.currentFrameSize = this.initialFrameSize;
		this.initTagList();
		
	}
		
	public void setInitialFrameSize(int initialFrameSize) {
		this.initialFrameSize = initialFrameSize;
	}


	/**
	 * Create all tags with their EPC Codes
	 */
	private void initTagList() {
		for (int i=0; i<this.numberOfTags;i++) {
			tags.add(new Tag(i));
		}
	}
	
	/**
	 * System Efficiency Getter
	 * @return System Efficiency
	 */
	public float getSef() {
		return sef;
	}
	
	/**
	 * Calculate the next frame size
	 * @return Next frame size based on Schoute formula
	 */
	private int schoute() {
		double c = 2.59;
		double frameSize = c * this.col;
		return ((int)Math.round(frameSize));
	}
	
	/**
	 * Calculate the next frame size
	 * @return Next frame size based on EomLee formula
	 */
	private int eomlee() {
		return (this.col*2);
	}
	
	/**
	 * Add slots to current frame
	 */
	private void initCurrentFrame() {
		for (int i=0; i<this.currentFrameSize; i++) {
			frame.add(new Slot());
		}
	}
	
	/**
	 * Tags should generate a random Number
	 */
	private void sendQuery() {
		for (int i=0; i<this.tags.size(); i++) {
			tags.get(i).setRng16(this.currentFrameSize);
		}
	}
	
	/**
	 * Set up each slot
	 */
	private void prepareSlots() {
		for (int i=0; i<this.tags.size(); i++) {
			int rng16 = tags.get(i).getRng16();
			frame.get(rng16).addTag(tags.get(i));
		}
	}
	
	/**
	 * Show tags in slots
	 */
	@SuppressWarnings("unused")
	private void showFrame() {
		for (int i=0; i<this.currentFrameSize; i++) {
			for (int j=0; j<this.frame.get(i).getSlotSize(); j++) {
				System.out.println("Slot[" + i + "," + j + "]: " + frame.get(i).getTags().get(j).getCode());
			}
		}
	}
	
	/**
	 * Identify tags in current frame
	 */
	private void identifyTags() {
		for (int i=0; i<this.currentFrameSize; i++) {
			if (frame.get(i).getSlotSize()>1) {
				this.col++;
			}
			else if (frame.get(i).getSlotSize()==1) {
				this.suc++;
				this.removeTag(frame.get(i).getTags().get(0).getCode());
			}
			else if (frame.get(i).getSlotSize()==0) {
				this.idl++;
			}
		}
	}
	
	/**
	 * Prepare next frame size
	 */
	private void finalizeFrame() {
		this.totalSlots = this.totalSlots + this.currentFrameSize;
		if (this.col==0) {
			this.end = true;
		}
		else {
			if (this.method==SimulatorConstants.SCHOUTE) {
				this.currentFrameSize = this.schoute();
			}
			else if (this.method==SimulatorConstants.LOWER) {
				this.currentFrameSize = this.col*2;
			}
			else if (this.method==SimulatorConstants.MOTA) {
				this.currentFrameSize = (int)(Math.round(this.col*2.65));
			}
			else if (this.method==SimulatorConstants.EOMLEE) {
				this.currentFrameSize = this.eomlee();
			}
			this.col = 0;
			this.idl = 0;
			this.suc = 0;
			this.frames++;
			this.frame.clear();
		}
		
	}
	
	/**
	 * Start the Standard DFSA procedure
	 */
	private void standardDfsa() {
		do {
			this.initCurrentFrame();
			this.sendQuery();
			this.prepareSlots();
			this.identifyTags();
			this.finalizeFrame();
		} while (end==false);
		//this.showResults();
	}
	
	
	@SuppressWarnings("unused")
	private void showResults() {
		System.out.println("---------------------------------------------");
		System.out.println("Fim do Frame " + (this.frames));
		System.out.println("Proximo Quadro: " + this.currentFrameSize);
		System.out.println("Total de slots: " + this.totalSlots);
		System.out.println("Total de colisoes: " + this.col);
		System.out.println("Total de vazios: " + this.idl);
		System.out.println("Total de sucessos: " + this.suc);
		System.out.println("Numero de etiquetas restantes: " + this.tags.size());
		System.out.println("---------------------------------------------");
	}
	
	/**
	 * Remove identified tag
	 * @param code EPC Code from identified tag
	 */
	private void removeTag(int code) {
		for (int i=0; i<tags.size(); i++) {
			if (tags.get(i).getCode()==code) {
				tags.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Shows System Efficiency
	 */
	public void showSef() {
		System.out.println((this.numberOfTags/(float)this.totalSlots));
	}
	
	/**
	 * Shows total number of used slots
	 */
	public void showTotal() {
		System.out.println(this.totalSlots);
	}
	
	/**
	 * Start the DFSA procedure
	 */
	public void start() {
		this.standardDfsa();
	}
	
}
