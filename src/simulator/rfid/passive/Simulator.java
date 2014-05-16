package simulator.rfid.passive;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Simulator Class to allow performance evaluation of passive RFID anti-collision algorithms
 * @author Rafael Perazzo Barbosa Mota
 * @email perazzo@ime.usp.br
 *
 */

public class Simulator implements Runnable{
	
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
	 * Trace filename to store Sef Values
	 */
	private String traceSefFilename;
	
	/**
	 * Trace filename to store Total Used Slots Values
	 */
	private String traceTotalSlotsFilename;
	
	/**
	 * Number of repetitions
	 */
	private int iterations;
	
	/**
	 * Statistcs for total number of used slots
	 */
	private DescriptiveStatistics statsTotal = new DescriptiveStatistics();
	
	/**
	 * Stats total File
	 */
	private String statsTotalFile;
	
	/**
	 * Statistics for System Efficiency 
	 */
	private DescriptiveStatistics statsSef = new DescriptiveStatistics();
	
	/**
	 * Stats System Efficiency File
	 */
	private String statsSefFile;
	
	/**
	 * Confidence Level. Ex: 90%, 99%, 95%, ...
	 */
	private double confidenceLevel;
	
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
		statsTotal.clear();
		statsSef.clear();
		this.initTagList();
	}
	
	
	/**
	 * Constructor - Must tell the number of tags
	 * @param numTags Number of Tags
	 */
	public Simulator(int numTags, int method, String sefFile, String totalFile, int initialFrameSize, int iterations, double cc) {
		/*this.col = 0;
		this.suc = 0;
		this.idl = 0;
		this.frames = 1;
		this.totalSlots = 0;*/
		this.method = method;
		this.traceSefFilename = sefFile;
		this.traceTotalSlotsFilename = totalFile;
		this.statsSefFile = this.traceSefFilename + ".stats";
		this.statsTotalFile = this.traceTotalSlotsFilename + ".stats";
		this.confidenceLevel = cc;
		//this.end = false;
		this.numberOfTags = numTags;
		this.initialFrameSize = initialFrameSize;
		this.iterations = iterations;
		statsTotal.clear();
		statsSef.clear();
		//this.initialFrameSize = 128;
		//this.currentFrameSize = this.initialFrameSize;
		//this.initTagList();
		this.initData();
		
	}
	
	private void initData() {
		this.col = 0;
		this.suc = 0;
		this.idl = 0;
		this.frames = 1;
		this.totalSlots = 0;
		this.end = false;
		this.currentFrameSize = this.initialFrameSize;
		this.tags.clear();
		this.frame.clear();
		this.initTagList();
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
		//TODO Implementar Eom-Lee
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
			else if (this.method==SimulatorConstants.EOMLEE) {
				this.currentFrameSize = this.eomlee();
			}
			else {
				this.currentFrameSize = this.col*2;
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
	public void startStandardDFSA() {
		//TODO Variação do número de etiquetas e intervalo de variação
		for (int i=1; i<=this.iterations; i++) {
			this.initData();
			this.standardDfsa();
			statsTotal.addValue(this.totalSlots);
			statsSef.addValue((this.numberOfTags/(float)this.totalSlots)); 
			//this.writeOutputToFile();
		}
		this.writeStatsToFile();
	}
	
	/**
	 * Write Sef Values to file
	 */
	@SuppressWarnings("unused")
	private void writeOutputToFile() {
		File file = new File(this.traceSefFilename);
		File total = new File(this.traceTotalSlotsFilename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!total.exists()) {
			try {
				total.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fwriter = null;
		FileWriter fwriterTotal = null;
		try {
			fwriter = new FileWriter(file,true);
			fwriterTotal = new FileWriter(total,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter out = new PrintWriter(fwriter,true);
		PrintWriter outTotal = new PrintWriter(fwriterTotal,true);
		out.println((this.numberOfTags/(float)this.totalSlots));
		outTotal.println(this.totalSlots );
		out.close();
		outTotal.close();
		try {
			fwriter.close();
			fwriterTotal.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write stats to files
	 */
	private void writeStatsToFile() {
		File sef = new File(this.statsSefFile);
		File total = new File(this.statsTotalFile);
		if (!sef.exists()) {
			try {
				sef.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!total.exists()) {
			try {
				total.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fwriter = null;
		FileWriter fwriterTotal = null;
		try {
			fwriter = new FileWriter(sef,true);
			fwriterTotal = new FileWriter(total,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter out = new PrintWriter(fwriter,true);
		PrintWriter outTotal = new PrintWriter(fwriterTotal,true);
		double[] ciSef = this.confidenceInterval(statsSef.getMean(), statsSef.getStandardDeviation(), this.getP());
		double[] ciTotal = this.confidenceInterval(statsTotal.getMean(), statsTotal.getStandardDeviation(), this.getP());
		String linhaSef = String.valueOf(this.numberOfTags + " " + String.valueOf(this.statsSef.getMean()) + " " + ciSef[0] + " " + ciSef[1]);
		String linhaTotal = String.valueOf(this.numberOfTags + " " + String.valueOf(this.statsTotal.getMean()) + " " + ciTotal[0] + " " + ciTotal[1]);
		out.println(linhaSef);
		outTotal.println(linhaTotal);
		
		out.close();
		outTotal.close();
		try {
			fwriter.close();
			fwriterTotal.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Calculates de confidence interval
	 * @param mean Sample mean
	 * @param standardDev Sample Standard Deviation
	 * @param confidence Confidence Level constant
	 * @return An array with lower and upper confidence interval
	 */
	private double[] confidenceInterval(double mean, double standardDev, double confidence) {
		
		double[] resultado = new double[2];
		
		resultado[0] = mean - (confidence*standardDev)/Math.sqrt(this.numberOfTags);
		resultado[1] = mean + (confidence*standardDev)/Math.sqrt(this.numberOfTags);
		
		return resultado;
	}
	
	/**
	 * Calculates the Confidence Level constant
	 * @return Confidence Level constant
	 */
	public double getP() { 
		NormalDistribution n = new NormalDistribution();
		double value = 100-this.confidenceLevel;
		return Math.abs((n.inverseCumulativeProbability(value/200)));
	}
	
	@Override
	public void run() {
		// TODO Implementar o método run da Thread
		
	}
	
}
