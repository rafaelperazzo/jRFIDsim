package simulator.rfid.passive;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.lang3.time.StopWatch;
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
	protected ArrayList<Tag> tags = new ArrayList<Tag>();
	
	/**
	 * Each frame is a List of Slots
	 */
	protected ArrayList<Slot> frame = new ArrayList<Slot>();	
	/**
	 * Collision Slots Counter
	 */
	protected int col; 
	
	/**
	 * Success Slots Counter
	 */
	protected int suc;
	
	/**
	 * Idle Slots COunter
	 */
	protected int idl;
	
	/**
	 * Number of Tags
	 */
	protected int numberOfTags;
	
	/**
	 * Number of Frames
	 */
	
	protected int frames;
	
	/**
	 * System Efficiency
	 */
	protected float sef;
	
	/**
	 * Initial Frame Size
	 */
	protected int initialFrameSize;
	
	/**
	 * Current Frame Size
	 */
	protected int currentFrameSize;
	
	/**
	 * Total number of used slots
	 */
	protected int totalSlots;
	
	/**
	 * End of DFSA ?
	 */
	protected boolean end;
	
	/**
	 * DFSA method (Schoute, Eom-Lee, ...)
	 */
	protected int method;
	
	/**
	 * Trace filename to store Sef Values
	 */
	protected String traceSefFilename;
	
	/**
	 * Trace filename to store Total Used Slots Values
	 */
	protected String traceTotalSlotsFilename;
	
	/**
	 * Number of repetitions
	 */
	protected int iterations;
	
	/**
	 * Statistcs for total number of used slots
	 */
	protected DescriptiveStatistics statsTotal = new DescriptiveStatistics();
	
	/**
	 * Stats total File
	 */
	protected String statsTotalFile;
	
	/**
	 * Statistics for System Efficiency 
	 */
	protected DescriptiveStatistics statsSef = new DescriptiveStatistics();
	
	/**
	 * Stats System Efficiency File
	 */
	protected String statsSefFile;
	
	/**
	 * Confidence Level. Ex: 90%, 99%, 95%, ...
	 */
	protected double confidenceLevel;
	
	protected int maxTags;
	
	protected int stepTags;
	
	protected int minTags;
	
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
	public Simulator(int numTags, int method, String sefFile, String totalFile, int initialFrameSize, int iterations, double cc, int maxTags, int stepTags) {
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
		this.maxTags = maxTags;
		this.stepTags = stepTags;
		this.minTags = numTags;
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
	
	protected void initData() {
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
	protected void initTagList() {
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
	protected int schoute() {
		double c = 2.59;
		double frameSize = c * this.col;
		return ((int)Math.round(frameSize));
	}
	
	/**
	 * Calculate the next frame size
	 * @return Next frame size based on EomLee formula
	 */
	protected int eomlee() {
		//TODO Implementar Eom-Lee
		return (this.col*2);
	}
	
	/**
	 * Add slots to current frame
	 */
	protected void initCurrentFrame() {
		for (int i=0; i<this.currentFrameSize; i++) {
			frame.add(new Slot());
		}
	}
	
	/**
	 * Tags should generate a random Number
	 */
	protected void sendQuery() {
		for (int i=0; i<this.tags.size(); i++) {
			tags.get(i).setRng16(this.currentFrameSize);
		}
	}
	
	/**
	 * Set up each slot
	 */
	protected void prepareSlots() {
		for (int i=0; i<this.tags.size(); i++) {
			int rng16 = tags.get(i).getRng16();
			frame.get(rng16).addTag(tags.get(i));
		}
	}
	
	/**
	 * Show tags in slots
	 */
	
	protected void showFrame() {
		for (int i=0; i<this.currentFrameSize; i++) {
			for (int j=0; j<this.frame.get(i).getSlotSize(); j++) {
				System.out.println("Slot[" + i + "," + j + "]: " + frame.get(i).getTags().get(j).getCode());
			}
		}
	}
	
	/**
	 * Identify tags in current frame
	 */
	protected void identifyTags() {
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
	protected void finalizeFrame() {
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
	protected void standardDfsa() {
		do {
			this.initCurrentFrame();
			this.sendQuery();
			this.prepareSlots();
			this.identifyTags();
			this.finalizeFrame();
		} while (end==false);
		//this.showResults();
	}
	
	
	
	protected void showResults() {
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
	protected void removeTag(int code) {
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
		for (this.numberOfTags=this.minTags; this.numberOfTags<=this.maxTags; this.numberOfTags = this.numberOfTags+ this.stepTags) {
			for (int i=1; i<=this.iterations; i++) {
				this.initData();
				this.standardDfsa();
				statsTotal.addValue(this.totalSlots);
				statsSef.addValue((this.numberOfTags/(float)this.totalSlots)); 
				//this.writeOutputToFile();
			}
			this.writeStatsToFile();
		}
		
	}
	
	/**
	 * Write Sef Values to file
	 */
	
	protected void writeOutputToFile() {
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
	protected void writeStatsToFile() {
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
	protected double[] confidenceInterval(double mean, double standardDev, double confidence) {
		
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
	
	/**
	 * Write a String to a specified text file
	 * @param msg Message
	 * @param filename File name
	 */
	protected void writeToFile(String msg, String filename) {
		File runStats = new File("stats.txt");
		if (!runStats.exists()) {
			try {
				runStats.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(runStats,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter out = new PrintWriter(writer,true);
		out.println(msg);
		out.close();
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		StopWatch timer = new StopWatch();
		timer.start();
		this.startStandardDFSA();
		timer.stop();
		String msg = "[" + String.valueOf(this.method) + "] " + "Execution time: " + String.valueOf(timer.getTime()/1000) + " seconds";
 		this.writeToFile(msg, "stats.txt");
		//System.out.println("Tempo de execução: " + timer.getTime()/1000 + " segundos");
		Runtime runtime = Runtime.getRuntime();
	    long memory = runtime.totalMemory() - runtime.freeMemory();
	    this.writeToFile("Used memory :" + bytesToMegabytes(memory) + " Mbytes","stats.txt");
	    this.writeToFile("--------------------------", "stats.txt");
	}
	
	protected static final long MEGABYTE = 1024L * 1024L;
	
	/**
	 * Convert bytes to megabytes
	 * @param bytes Number of bytes to be converted
	 * @return Value converted to Megabytes
	 */
	protected static long bytesToMegabytes(long bytes) {
	    return bytes / MEGABYTE;
	}
	
}
