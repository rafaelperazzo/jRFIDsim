package simulator.rfid.passive;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

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
	 * Statistics for Instructions counter 
	 */
	protected DescriptiveStatistics statsInst = new DescriptiveStatistics();
	
	/**
	 * Stats System Efficiency File
	 */
	protected String statsInstFile;
	
	/**
	 * Confidence Level. Ex: 90%, 99%, 95%, ...
	 */
	protected double confidenceLevel;
	
	/**
	 * Max number of tags to be evaluated
	 */
	protected int maxTags;
	
	/**
	 * The number of tags to be added to current number of tags
	 */
	protected int stepTags;
	
	/**
	 * Min number of tags to be evaluated
	 */
	protected int minTags;
	
	/**
	 * Q Value of Q Algorithm
	 */
	protected int qValue;
	
	protected int initialQValue;
	
	/**
	 * Float value of qValue
	 */
	protected double qfp;
	
	/**
	 * Constant c
	 */
	protected double c;
	
	/**
	 * SendQuery Counter
	 */
	protected int iCounter;
	
	protected boolean debug;
	
	protected Hashtable<Integer, String> methods = new Hashtable<Integer,String>();
	
	protected Hashtable<Integer, PerformanceData> statsDataSef = new Hashtable<Integer,PerformanceData>();
	protected Hashtable<Integer, PerformanceData> statsDataTotal = new Hashtable<Integer,PerformanceData>();
	protected Hashtable<Integer, PerformanceData> statsDataFrames = new Hashtable<Integer,PerformanceData>();
	
	protected int nedfsaSlotsInCollision = 0;
	
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
		this.iCounter = 0;
		methods.put(1, "SCHOUTE");
		methods.put(2, "LOWER");
		methods.put(3, "EOMLEE");
		methods.put(4, "MOTA");
		methods.put(5, "C1G2");
		statsTotal.clear();
		statsSef.clear();
		statsInst.clear();
		this.initTagList();
	}
	
	
	/**
	 * Constructor
	 * @param numTags Initial number of tags
	 * @param method DFSA method
	 * @param initialFrameSize Initial frame size
	 * @param iterations Number of repetitions
	 * @param cc Confidence interval (90%, 95%, 99% , ...
	 * @param maxTags Final number of tags
	 * @param stepTags Steps to the number of tags
	 */
	public Simulator(int numTags, int method, int initialFrameSize, int iterations, double cc, int maxTags, int stepTags, boolean debug) {
		this.method = method;
		this.debug = debug;
		this.statsSefFile = this.traceSefFilename + ".stats";
		this.statsTotalFile = this.traceTotalSlotsFilename + ".stats";
		this.confidenceLevel = cc;
		this.maxTags = maxTags;
		this.stepTags = stepTags;
		this.minTags = numTags;
		methods.put(1, "SCHOUTE");
		methods.put(2, "LOWER");
		methods.put(3, "EOMLEE");
		methods.put(4, "MOTA");
		methods.put(5, "C1G2");
		//this.end = false;
		this.numberOfTags = numTags;
		this.initialFrameSize = initialFrameSize;
		this.iterations = iterations;
		this.qValue = this.initialFrameSize;
		this.qfp = this.initialFrameSize;
		this.c = 0.3;
		if ((this.method==5)||(this.method==4)) {
			this.currentFrameSize = (int)Math.round(Math.pow(2,this.qValue));
		} 
		this.iCounter = 0;
		this.statsSefFile = "01_SEF." + methods.get(this.method) + "." + this.initialFrameSize + "." + this.maxTags + ".txt";
		this.statsTotalFile = "02_TOTAL." + methods.get(this.method) + "." + this.initialFrameSize + "." + this.maxTags + ".txt";
		this.statsInstFile = "03_FRAMES." + methods.get(this.method) + "." + this.initialFrameSize + "." + this.maxTags + ".txt";
		File f = new File(this.statsInstFile);
		if (f.exists()) f.delete();
		f = new File(this.statsSefFile);
		if (f.exists()) f.delete();
		f = new File(this.statsTotalFile);
		if (f.exists()) f.delete();
		f = new File(this.statsInstFile);
		if (f.exists()) f.delete();
		statsTotal.clear();
		statsSef.clear();
		statsInst.clear();
		this.initData();
		
	}
	
	/**
	 * @return the qValue
	 */
	public int getqValue() {
		return qValue;
	}


	/**
	 * @param qValue the qValue to set
	 */
	public void setqValue(int qValue) {
		this.qValue = qValue;
	}


	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}


	/**
	 * @return the c
	 */
	public double getC() {
		return c;
	}


	/**
	 * @param c the c to set
	 */
	public void setC(double c) {
		this.c = c;
	}


	/**
	 * @return the tags
	 */
	public ArrayList<Tag> getTags() {
		return tags;
	}

	/**
	 * @return the frame
	 */
	public ArrayList<Slot> getFrame() {
		return frame;
	}

	/**
	 * @return the col
	 */
	public int getCol() {
		return col;
	}


	/**
	 * @return the suc
	 */
	public int getSuc() {
		return suc;
	}


	/**
	 * @return the idl
	 */
	public int getIdl() {
		return idl;
	}


	/**
	 * @return the numberOfTags
	 */
	public int getNumberOfTags() {
		return numberOfTags;
	}


	/**
	 * @return the frames
	 */
	public int getFrames() {
		return frames;
	}


	/**
	 * @return the initialFrameSize
	 */
	public int getInitialFrameSize() {
		return initialFrameSize;
	}


	/**
	 * @return the currentFrameSize
	 */
	public int getCurrentFrameSize() {
		return currentFrameSize;
	}


	/**
	 * @return the totalSlots
	 */
	public int getTotalSlots() {
		return totalSlots;
	}


	/**
	 * @return the end
	 */
	public boolean isEnd() {
		return end;
	}


	/**
	 * @return the method
	 */
	public int getMethod() {
		return method;
	}


	/**
	 * @return the traceSefFilename
	 */
	public String getTraceSefFilename() {
		return traceSefFilename;
	}


	/**
	 * @return the traceTotalSlotsFilename
	 */
	public String getTraceTotalSlotsFilename() {
		return traceTotalSlotsFilename;
	}


	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return iterations;
	}


	/**
	 * @return the statsTotal
	 */
	public DescriptiveStatistics getStatsTotal() {
		return statsTotal;
	}


	/**
	 * @return the statsTotalFile
	 */
	public String getStatsTotalFile() {
		return statsTotalFile;
	}


	/**
	 * @return the statsSef
	 */
	public DescriptiveStatistics getStatsSef() {
		return statsSef;
	}


	/**
	 * @return the statsSefFile
	 */
	public String getStatsSefFile() {
		return statsSefFile;
	}


	/**
	 * @return the confidenceLevel
	 */
	public double getConfidenceLevel() {
		return confidenceLevel;
	}


	/**
	 * @return the maxTags
	 */
	public int getMaxTags() {
		return maxTags;
	}


	/**
	 * @return the stepTags
	 */
	public int getStepTags() {
		return stepTags;
	}


	/**
	 * @return the minTags
	 */
	public int getMinTags() {
		return minTags;
	}


	/**
	 * Reset the simulation parameters
	 */
	protected void initData() {
		this.col = 0;
		this.suc = 0;
		this.idl = 0;
		this.frames = 1;
		this.totalSlots = 0;
		this.iCounter = 0;
		this.end = false;
		this.qValue = this.initialFrameSize;
		this.qfp = this.initialFrameSize;
		this.c = 0.3;
		if ((this.method==5)||(this.method==4)) {
			this.currentFrameSize = (int)Math.round(Math.pow(2,this.qValue));
		}
		else {
			this.currentFrameSize = this.initialFrameSize;
		}
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
	protected int schoute(int col) {
		double c = 2.39;
		double frameSize = c * col;
		return ((int)Math.round(frameSize));
	}
	
	/**
	 * Calculate the next frame size based on eom-lee iterative algorithm
	 * @param collisions Number of collisions slots
	 * @param success Number of success slots
	 * @param e Error
	 * @param L QValue
	 * @return Best Frame Size
	 */
	protected int eomlee(int collisions, int success, double e, int L) {
		
		double bprox;
		double yprox;
		double temp;
		double backlog;
		double y1 = 2;
		
		do {
	        bprox = L/((y1*(double)collisions)+(double)success);
	        yprox = (1.0-Math.exp((-1.0)/bprox))/(bprox*(1.0-(1.0+ 1.0/bprox)*Math.exp((-1.0)/bprox)));
	        backlog = yprox*collisions;
	        temp = y1;
	        y1 = yprox;
	    } while (Math.abs(y1-temp)>e);
		
		return ((int)Math.round(backlog));
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
			if (frame.get(i).getSlotSize()>1) { //COLLISION SLOT
				if (this.method==SimulatorConstants.MOTA) {
					this.identifyTagsInCollision(frame.get(i).getTags());
				}
				else {
					this.col++;
				}
			}
			else if (frame.get(i).getSlotSize()==1) { //SUCCESS SLOT
				this.suc++;
				this.removeTag(frame.get(i).getTags().get(0).getCode());
			}
			else if (frame.get(i).getSlotSize()==0) { //IDLE SLOT
				this.idl++;
			}
		}
	}
	
	protected void identifyTagsInCollision(ArrayList<Tag> tagsInCollision) {
		Simulator colHandler = new Simulator(tagsInCollision.size(),SimulatorConstants.LOWER, 2,1,90,3,1,false);
		colHandler.setTags(tagsInCollision);
		colHandler.standardDfsa();
		this.totalSlots = this.totalSlots + colHandler.totalSlots;
		if (colHandler.tags.size()>0) System.out.println("ERRO!!");
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
				this.currentFrameSize = this.schoute(this.col);
			}
			else if (this.method==SimulatorConstants.LOWER) {
				this.currentFrameSize = this.col*2;
			}
			else if (this.method==SimulatorConstants.EOMLEE) {
				this.currentFrameSize = this.eomlee(this.col, this.suc, 0.001, this.currentFrameSize);
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
			this.iCounter++;
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
	public void startDFSA() {
		StopWatch timer = new StopWatch();
		timer.start();
		//*********** BEGIN DFSA********************
		for (this.numberOfTags=this.minTags; this.numberOfTags<=this.maxTags; this.numberOfTags = this.numberOfTags+ this.stepTags) {
			for (int i=1; i<=this.iterations; i++) {
				this.initData();
				if (this.method<4) {
					this.standardDfsa();
				} 
				else if (this.method==SimulatorConstants.C1G2) {
					this.startQ();
				}
				else if (this.method==SimulatorConstants.MOTA) {
					this.startEstimation();
					this.setC(0.3);
					this.standardDfsa();
					//System.out.println(this.nedfsaSlotsInCollision);
				}
				statsTotal.addValue(this.totalSlots);
				statsSef.addValue((this.numberOfTags/(float)this.totalSlots));
				statsInst.addValue(this.iCounter);
				//this.writeOutputToFile();
			}
			this.setupStatsHashTable();
			if (this.debug) {
				this.writeStatsToFile();
			}
		}
		//****************** END DFSA ****************
		timer.stop();
		if (this.debug) {
			String msg = "[" + methods.get(this.method) + "] " + "Execution time: " + String.valueOf(timer.getTime()/1000) + " seconds";
	 		this.writeToFile(msg, "stats.txt");
			//System.out.println("Tempo de execução: " + timer.getTime()/1000 + " segundos");
			Runtime runtime = Runtime.getRuntime();
		    long memory = runtime.totalMemory() - runtime.freeMemory();
		    this.writeToFile("Used memory :" + bytesToMegabytes(memory) + " Mbytes","stats.txt");
		    //this.writeToFile("Identified tags :" + (this.numberOfTags-this.stepTags),"stats.txt");
		    //this.writeToFile("Non identified tags :" + this.tags.size(),"stats.txt");
		    this.writeToFile("Number of iterations: " + this.iterations, "stats.txt");
		    this.writeToFile("Initial number of tags: " + this.minTags, "stats.txt");
		    this.writeToFile("Final number of tags: " + this.maxTags, "stats.txt");
		    this.writeToFile("Steps: " + this.stepTags, "stats.txt");
		    this.writeToFile("Initial frame size: " + this.initialFrameSize, "stats.txt");
		    this.writeToFile("--------------------------", "stats.txt");
		}
		
	}
	
	/**
	 * Write Sef, total used slots and instructions counter Values to file
	 */
	
	protected void writeOutputToFile() {
		this.traceSefFilename = "trace.Sef." + this.methods.get(this.method) + "." + this.numberOfTags + ".txt";
		this.traceTotalSlotsFilename = "trace.Total." + this.methods.get(this.method) + "." + this.numberOfTags + ".txt";
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
		File counter = new File(this.statsInstFile);
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
		if (!counter.exists()) {
			try {
				counter.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fwriter = null;
		FileWriter fwriterTotal = null;
		FileWriter fwriterCounter = null;
		try {
			fwriter = new FileWriter(sef,true);
			fwriterTotal = new FileWriter(total,true);
			fwriterCounter = new FileWriter(counter,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter out = new PrintWriter(fwriter,true);
		PrintWriter outTotal = new PrintWriter(fwriterTotal,true);
		PrintWriter outCounter = new PrintWriter(fwriterCounter,true);
		double[] ciSef = this.confidenceInterval(statsSef.getMean(), statsSef.getStandardDeviation(), this.getP(this.confidenceLevel));
		double[] ciTotal = this.confidenceInterval(statsTotal.getMean(), statsTotal.getStandardDeviation(), this.getP(this.confidenceLevel));
		double[] ciCounter = this.confidenceInterval(statsInst.getMean(), statsInst.getStandardDeviation(), this.getP(this.confidenceLevel));
		this.statsDataSef.put(this.numberOfTags, new PerformanceData(this.statsSef.getMean(),ciSef[0],ciSef[1]));
		//String linhaSef = String.valueOf(this.numberOfTags + " " + String.valueOf(this.statsSef.getMean()) + " " + ciSef[0] + " " + ciSef[1]);
		//String linhaTotal = String.valueOf(this.numberOfTags + " " + String.valueOf(this.statsTotal.getMean()) + " " + ciTotal[0] + " " + ciTotal[1]);
		//String linhaCounter = String.valueOf(this.numberOfTags + " " + String.valueOf(this.statsInst.getMean()) + " " + ciCounter[0] + " " + ciCounter[1]);
		out.printf(Locale.US,"%d %.4f %.4f %.4f\n", this.numberOfTags,this.statsSef.getMean(), ciSef[0], ciSef[1]);
		outTotal.printf(Locale.US,"%d %.0f %.0f %.0f\n", this.numberOfTags,this.statsTotal.getMean(), ciTotal[0], ciTotal[1]);
		outCounter.printf(Locale.US,"%d %.2f %.2f %.2f\n", this.numberOfTags,this.statsInst.getMean(), ciCounter[0], ciCounter[1]);
		out.close();
		outTotal.close();
		outCounter.close();
		try {
			fwriter.close();
			fwriterTotal.close();
			fwriterCounter.close();
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
	public double getP(double confidenceLevel) { 
		NormalDistribution n = new NormalDistribution();
		double value = 100-confidenceLevel;
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
	
	protected static final long MEGABYTE = 1024L * 1024L;
	
	/**
	 * Convert bytes to megabytes
	 * @param bytes Number of bytes to be converted
	 * @return Value converted to Megabytes
	 */
	protected static long bytesToMegabytes(long bytes) {
	    return bytes / MEGABYTE;
	}
	
	/**
	 * Start Q Algorithm (C1G2)
	 */
	protected void startQ() {
		do {
			this.initCurrentFrame();
			this.sendQuery();
			this.iCounter++;
			this.prepareSlots();
			this.identifyTagsQ();
			this.finalizeFrameQ();
		} while (end==false);
	}
	
	/**
	 * Identify tags in Q Algorithm
	 */
	protected void identifyTagsQ() {
			if (frame.get(0).getSlotSize()>1) {
				this.col++;
			}
			else if (frame.get(0).getSlotSize()==1) {
				this.suc++;
				this.removeTag(frame.get(0).getTags().get(0).getCode());
			}
			else if (frame.get(0).getSlotSize()==0) {
				this.idl++;
			}
	}
	
	/**
	 * Ends frame for Q Algorithm
	 */
	protected void finalizeFrameQ() {
		this.totalSlots++;
		if (this.qfp<1) {
			this.end = true;
			this.frame.clear();
		}
		else {
			if (this.col==1) {
				this.qfp = this.qfp + this.c;
			}
			else if (this.idl==1) {
				this.qfp = this.qfp - this.c;
			}
			this.qValue = (int)Math.round(this.qfp);
			this.currentFrameSize = (int)Math.round(Math.pow(2,this.qValue));
			this.col = 0;
			this.idl = 0;
			this.suc = 0;
			this.frames++;
			this.frame.clear();
		}
	}
	
	/**
	 * Add statistics data to hashtable
	 */
	private void setupStatsHashTable() {
		double[] ciSef = this.confidenceInterval(statsSef.getMean(), statsSef.getStandardDeviation(), this.getP(this.confidenceLevel));
		double[] ciTotal = this.confidenceInterval(statsTotal.getMean(), statsTotal.getStandardDeviation(), this.getP(this.confidenceLevel));
		double[] ciCounter = this.confidenceInterval(statsInst.getMean(), statsInst.getStandardDeviation(), this.getP(this.confidenceLevel));
		this.statsDataSef.put(this.numberOfTags, new PerformanceData(this.statsSef.getMean(),ciSef[0],ciSef[1]));
		this.statsDataTotal.put(this.numberOfTags, new PerformanceData(this.statsTotal.getMean(),ciTotal[0],ciTotal[1]));
		this.statsDataFrames.put(this.numberOfTags, new PerformanceData(this.statsInst.getMean(),ciCounter[0],ciCounter[1]));
	}
	
	/**
	 * @return the statsData
	 */
	public Hashtable<Integer, PerformanceData> getStatsDataSef() {
		return statsDataSef;
	}
	
	/**
	 * @return the statsDataTotal
	 */
	public Hashtable<Integer, PerformanceData> getStatsDataTotal() {
		return statsDataTotal;
	}


	/**
	 * @return the statsDataFrames
	 */
	public Hashtable<Integer, PerformanceData> getStatsDataFrames() {
		return statsDataFrames;
	}
	
	/**
	 * Start Estimation Algorithm (CUI) based on power of 2
	 */
	protected int startEstimation() {
		this.c = 1;
		boolean t1=false,t2=false;
		do {	
			t1 = this.isSlotSizeGood();
			if (t1) { //Yes, Good frame size.
				t2 = this.isSlotSizeGood();//Confirm (2nd time)	
			}	
		} while (!(t1&&t2));
		return (this.qValue);
	}
	
	protected boolean isSlotSizeGood() {
		int colSlot = 0;
		int idlSlot = 0;
		
		for (int i=0; i<3; i++) {
			this.initCurrentFrame();
			this.sendQuery();
			this.iCounter++;
			this.prepareSlots();
			//Check the type of slot (collision, success or idle)
			if (this.checkSlot()==0) {
				colSlot++;
			}
			else if (this.checkSlot()==1) {
			}
			else {
				idlSlot++;
			}
			this.goToNextEstimationFrame();
		}
		if ((colSlot==3)||(idlSlot==3)) {
			if (colSlot==3) {
				this.qfp = this.qfp + this.c;
			}
			if (idlSlot==3) {
				this.qfp = this.qfp - this.c;
			}
			this.qValue = (int)Math.round(this.qfp);
			this.currentFrameSize = (int)Math.round(Math.pow(2, this.qValue));
			return false;
		}
		else {
			return (true);
		}
	}
	
	protected int checkSlot() {
		if (frame.get(0).getSlotSize()>1) { //COLLISION SLOTS
			return 0;
		}
		else if (frame.get(0).getSlotSize()==1) { //SUCCESS SLOT
			return 1;
		}
		else { //IDLE SLOT
			return 2;
		}
	}
	
	protected void goToNextEstimationFrame() {
		this.totalSlots++;
		this.frames++;
		this.frame.clear();
	}
	
	//TODO Implementar NEDFSA
	
	protected void startNEDFSA() {
		do {
			this.initCurrentFrame();
			this.sendQuery();
			this.iCounter++;
			this.prepareSlots();
			this.identifyTags();
			this.finalizeFrame();
		} while (end==false);
	}
	
	
	
	@Override
	public void run() {
			this.startDFSA();
	}
	
}
