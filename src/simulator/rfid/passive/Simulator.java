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
	
	protected int colSlots;
	
	protected int idlSlots;
	
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
	 * Statistics for Collision Slots 
	 */
	protected DescriptiveStatistics statsCol = new DescriptiveStatistics();
	
	/**
	 * Stats Collisions File
	 */
	protected String statsColFile;
	
	/**
	 * Statistics for Idle Slots 
	 */
	protected DescriptiveStatistics statsIdl = new DescriptiveStatistics();
	
	/**
	 * Stats Idle File
	 */
	protected String statsIdlFile;
	
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
	
	/**
	 * Tells if it is necessary to generate a log
	 */
	protected boolean debug;
	
	/**
	 * Anti-collision methods
	 */
	protected Hashtable<Integer, String> methods = new Hashtable<Integer,String>();
	
	/**
	 * Local System efficiency statistics
	 */
	protected Hashtable<Integer, PerformanceData> statsDataSef = new Hashtable<Integer,PerformanceData>();
	
	/**
	 * Local Total slots statistics
	 */
	protected Hashtable<Integer, PerformanceData> statsDataTotal = new Hashtable<Integer,PerformanceData>();
	
	/**
	 * Local Frame statistics
	 */
	protected Hashtable<Integer, PerformanceData> statsDataFrames = new Hashtable<Integer,PerformanceData>();
	
	/**
	 * Not defined
	 */
	protected int nedfsaSlotsInCollision = 0;
	
	
	protected double estimationAdjust = 1;
	
	protected boolean collisionsLog = false;
	
	protected int DBTSA_sc = 0;
	
	
	/**
	 * Default Constructor
	 */
	public Simulator() {
		this.col = 0;
		this.suc = 0;
		this.idl = 0;
		this.colSlots = 0;
		this.idlSlots = 0;
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
		methods.put(4, "NEDFSA");
		methods.put(5, "C1G2");
		methods.put(6, "DBTSA");
		methods.put(7, "MOTA");
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
	 * @param cc Confidence interval (90%, 95%, 99% , ...) 
	 * @param maxTags Final number of tags
	 * @param stepTags Steps to the number of tags
	 */
	public Simulator(int numTags, int method, int initialFrameSize, int iterations, double cc, int maxTags, int stepTags, boolean debug, double adjust) {
		this.method = method;
		this.debug = debug;
		this.statsSefFile = this.traceSefFilename + ".stats";
		this.statsTotalFile = this.traceTotalSlotsFilename + ".stats";
		this.confidenceLevel = cc;
		this.maxTags = maxTags;
		this.stepTags = stepTags;
		this.minTags = numTags;
		this.estimationAdjust = adjust;
		this.colSlots = 0;
		this.idlSlots = 0;
		methods.put(1, "SCHOUTE");
		methods.put(2, "LOWER");
		methods.put(3, "EOMLEE");
		methods.put(4, "NEDFSA");
		methods.put(5, "C1G2");
		methods.put(6, "DBTSA");
		methods.put(7, "MOTA");
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
		this.statsSefFile = "01_SEF." + methods.get(this.method) + "." + this.initialFrameSize + "." + this.maxTags + "." + this.estimationAdjust + ".txt";
		this.statsTotalFile = "02_TOTAL." + methods.get(this.method) + "." + this.initialFrameSize + "." + this.maxTags  + "." + this.estimationAdjust + ".txt";
		this.statsInstFile = "03_FRAMES." + methods.get(this.method) + "." + this.initialFrameSize + "." + this.maxTags + "." + this.estimationAdjust +".txt";
		this.statsColFile = "04_COL." + methods.get(this.method) + "." + this.initialFrameSize + "." + this.maxTags + "." + this.estimationAdjust  + ".txt";
		this.statsIdlFile = "05_IDL." + methods.get(this.method) + "." + this.initialFrameSize + "." + this.maxTags + "." + this.estimationAdjust + ".txt";
		File f = new File(this.statsInstFile);
		if (f.exists()) f.delete();
		f = new File(this.statsSefFile);
		if (f.exists()) f.delete();
		f = new File(this.statsTotalFile);
		if (f.exists()) f.delete();
		f = new File(this.statsIdlFile);
		if (f.exists()) f.delete();
		f = new File(this.statsColFile);
		if (f.exists()) f.delete();
		statsTotal.clear();
		statsSef.clear();
		statsInst.clear();
		statsCol.clear();
		statsIdl.clear();
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


	public boolean isCollisionsLog() {
		return collisionsLog;
	}


	public void setCollisionsLog(boolean collisionsLog) {
		this.collisionsLog = collisionsLog;
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
		this.colSlots = 0;
		this.idlSlots = 0;
		this.frames = 1;
		this.totalSlots = 0;
		this.iCounter = 0;
		this.end = false;
		this.qValue = this.initialFrameSize;
		this.qfp = this.initialFrameSize;
		this.c = 0.3;
		if (this.method==5) { //C1G2
			this.currentFrameSize = (int)Math.round(Math.pow(2,this.qValue));
		}
		else if (this.method==4) { //NEDFSA
			this.currentFrameSize = (int)Math.round(Math.pow(2,this.qValue));
			//this.currentFrameSize = (int)(this.currentFrameSize*0.66);
		}
		else if (this.method==7) { //MOTA
			this.currentFrameSize = (int)Math.round(Math.pow(2,this.qValue));
			//this.currentFrameSize = (int)(this.currentFrameSize*this.estimationAdjust);
		}
		else if (this.method==6) { //DBTSA 
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
				if (this.collisionsLog)
					this.writeToFile(String.valueOf(frame.get(i).getSlotSize()), "collisions.col");
				if ((this.method==SimulatorConstants.NEDFSA)||(this.method==SimulatorConstants.MOTA)) {
					if (!this.collisionsLog) {
						for (int j=0; j<frame.get(i).getTags().size(); j++) {
							this.removeTag(frame.get(i).getTags().get(j).getCode());
						}
						//System.out.println(frame.get(i).getTags().size());
						this.identifyTagsInCollision(frame.get(i).getTags());
					}
					this.colSlots++;
				}
				else {
					this.col++;
					this.colSlots++;
				}
			}
			else if (frame.get(i).getSlotSize()==1) { //SUCCESS SLOT
				this.suc++;
				this.removeTag(frame.get(i).getTags().get(0).getCode());
			}
			else if (frame.get(i).getSlotSize()==0) { //IDLE SLOT
				this.idl++;
				this.idlSlots++;
			}
		}
	}
	
	/**
	 * Identify collision tags
	 * @param tagsInCollision Tags in collision
	 */
	protected void identifyTagsInCollision(ArrayList<Tag> tagsInCollision) {
		Simulator colHandler = null;
		if (this.method==SimulatorConstants.NEDFSA) {
			colHandler = new Simulator(tagsInCollision.size(),SimulatorConstants.SCHOUTE, 3,1,90,3,1,false,1);
		}
		else if (this.method==SimulatorConstants.MOTA) {
			colHandler = new Simulator(tagsInCollision.size(),SimulatorConstants.LOWER, 2,1,90,3,1,false,1);
		}
		else if (this.method==SimulatorConstants.DBTSA) {
			colHandler = new Simulator(tagsInCollision.size(),SimulatorConstants.C1G2, 2,1,90,3,1,false,1);
			colHandler.setC(1);
		}
		colHandler.setTags(tagsInCollision);
		if (colHandler.method==SimulatorConstants.C1G2) {
			colHandler.startQ();
		}
		else {
			colHandler.standardDfsa();
		}
		this.totalSlots = this.totalSlots + colHandler.totalSlots;
		this.idlSlots = this.idlSlots + colHandler.idlSlots;
		this.colSlots = this.colSlots + colHandler.colSlots;
		if (colHandler.tags.size()>0) { 
			System.out.println("ERRO!!(" + colHandler.tags.size() + ")");
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
				else if (this.method==SimulatorConstants.NEDFSA) {
					this.startEstimation();
					this.setC(0.3);
					this.currentFrameSize = (int)(this.currentFrameSize * 0.66);
					this.standardDfsa();
				}
				else if (this.method==SimulatorConstants.MOTA) {
					this.startEstimation();
					this.setC(0.3);
					this.currentFrameSize = (int)(this.currentFrameSize*this.estimationAdjust);
					this.standardDfsa();
				}
				else if (this.method==SimulatorConstants.DBTSA) {
					this.setC(1);
					this.DBTSA_sc = 0;
					this.startDBTSA();
				}
				
				statsTotal.addValue(this.totalSlots);
				statsSef.addValue((this.numberOfTags/(float)this.totalSlots));
				statsInst.addValue(this.iCounter);
				statsCol.addValue(this.colSlots);
				statsIdl.addValue(this.idlSlots);
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
			String msg = "[" + methods.get(this.method) + "] " + "Execution time: " + secondsToHours(timer.getTime());
	 		this.writeToFile(msg, "stats.txt");
			//System.out.println("Tempo de execução: " + timer.getTime()/1000 + " segundos");
			Runtime runtime = Runtime.getRuntime();
		    long memory = runtime.totalMemory() - runtime.freeMemory();
		    this.writeToFile("Used memory :" + bytesToMegabytes(memory) + " Mbytes","stats.txt");
		    this.writeToFile("Number of iterations: " + this.iterations, "stats.txt");
		    this.writeToFile("Initial number of tags: " + this.minTags, "stats.txt");
		    this.writeToFile("Final number of tags: " + this.maxTags, "stats.txt");
		    this.writeToFile("Steps: " + this.stepTags, "stats.txt");
		    this.writeToFile("Initial frame size: " + this.initialFrameSize, "stats.txt");
		    this.writeToFile("--------------------------", "stats.txt");
		}
	}
	
	/**
	 * Convert Seconds to Hours, minutes and seconds
	 * @param timer Time in Ms
	 * @return A string in hours, minutes and seconds
	 */
	protected String secondsToHours(long timer) {
		int time = (int)timer/1000;
		String executionTime = String.valueOf(time) + " seconds";
		int hours = (int) time / 3600;
	    int remainder = (int) time - hours * 3600;
	    int mins = remainder / 60;
	    remainder = remainder - mins * 60;
	    int secs = remainder;
	    executionTime = String.valueOf(hours) + " hour(s) and " + String.valueOf(mins) + " minute(s) and " + String.valueOf(secs) + " second(s)";
		return executionTime;
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
		File collisions = new File(this.statsColFile);
		File idle = new File(this.statsIdlFile);
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
		if (!collisions.exists()) {
			try {
				collisions.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!idle.exists()) {
			try {
				idle.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fwriter = null;
		FileWriter fwriterTotal = null;
		FileWriter fwriterCounter = null;
		FileWriter fwriterCol = null;
		FileWriter fwriterIdl = null;
		try {
			fwriter = new FileWriter(sef,true);
			fwriterTotal = new FileWriter(total,true);
			fwriterCounter = new FileWriter(counter,true);
			fwriterCol = new FileWriter(collisions,true);
			fwriterIdl = new FileWriter(idle,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter out = new PrintWriter(fwriter,true);
		PrintWriter outTotal = new PrintWriter(fwriterTotal,true);
		PrintWriter outCounter = new PrintWriter(fwriterCounter,true);
		PrintWriter outCol = new PrintWriter(fwriterCol,true);
		PrintWriter outIdl = new PrintWriter(fwriterIdl,true);
		double[] ciSef = this.confidenceInterval(statsSef.getMean(), statsSef.getStandardDeviation(), this.getP(this.confidenceLevel));
		double[] ciTotal = this.confidenceInterval(statsTotal.getMean(), statsTotal.getStandardDeviation(), this.getP(this.confidenceLevel));
		double[] ciCounter = this.confidenceInterval(statsInst.getMean(), statsInst.getStandardDeviation(), this.getP(this.confidenceLevel));
		double[] ciCol = this.confidenceInterval(statsCol.getMean(), statsCol.getStandardDeviation(), this.getP(this.confidenceLevel));
		double[] ciIdl = this.confidenceInterval(statsIdl.getMean(), statsIdl.getStandardDeviation(), this.getP(this.confidenceLevel));
		this.statsDataSef.put(this.numberOfTags, new PerformanceData(this.statsSef.getMean(),ciSef[0],ciSef[1]));
		out.printf(Locale.US,"%d %.4f %.4f %.4f\n", this.numberOfTags,this.statsSef.getMean(), ciSef[0], ciSef[1]);
		outTotal.printf(Locale.US,"%d %.0f %.0f %.0f\n", this.numberOfTags,this.statsTotal.getMean(), ciTotal[0], ciTotal[1]);
		outCounter.printf(Locale.US,"%d %.2f %.2f %.2f\n", this.numberOfTags,this.statsInst.getMean(), ciCounter[0], ciCounter[1]);
		outCol.printf(Locale.US,"%d %.4f %.4f %.4f\n", this.numberOfTags,this.statsCol.getMean(), ciCol[0], ciCol[1]);
		outIdl.printf(Locale.US,"%d %.4f %.4f %.4f\n", this.numberOfTags,this.statsIdl.getMean(), ciIdl[0], ciIdl[1]);
		out.close();
		outTotal.close();
		outCounter.close();
		outCol.close();
		outIdl.close();
		try {
			fwriter.close();
			fwriterTotal.close();
			fwriterCounter.close();
			fwriterCol.close();
			fwriterIdl.close();
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
		File runStats = new File(filename);
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
		if (this.qfp<=0) {
			this.end = true;
			this.frame.clear();
		}
		else {
			if (this.col==1) {
				this.qfp = this.qfp + this.c; 
				this.qfp = Math.min(15,this.qfp);
			}
			else if (this.idl==1) {
				this.qfp = this.qfp - this.c;
				this.qfp = Math.max(0, this.qfp);
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
	
	/**
	 * Check if the Slot has a good size (Cui Method)
	 * @return Yes or No
	 */
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
	
	/**
	 * Check the collision state: col, idl or suc
	 * @return
	 */
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

	public void testEstimation() {
		String estimationFile = "04_EST." + this.initialFrameSize + "." + this.maxTags + ".txt";
		File f = new File(estimationFile);
		if (f.exists()) {
			f.delete();
		}
		DescriptiveStatistics est = new DescriptiveStatistics();
		DescriptiveStatistics differenceMean = new DescriptiveStatistics();
		double diference=0;
		for (this.numberOfTags=this.minTags; this.numberOfTags<=this.maxTags; this.numberOfTags = this.numberOfTags+ this.stepTags) {
			est.clear();
			for (int i=1; i<=this.iterations; i++) {
				this.initData();
				this.startEstimation();
				est.addValue(this.qValue);
			}
			double[] ciEst = this.confidenceInterval(est.getMean(), est.getStandardDeviation(), this.getP(this.confidenceLevel));
			String msg = String.valueOf(this.numberOfTags) + " " + String.valueOf(est.getMean());
			msg = msg + " " + String.valueOf(ciEst[0]) + " " + String.valueOf(ciEst[1]) + " ";
			msg = msg + String.valueOf((int)Math.pow(2, Math.round(est.getMean())));
			diference = (this.numberOfTags-Math.pow(2, Math.round(est.getMean())))/this.numberOfTags;
			diference = 100*diference;
			differenceMean.addValue(diference);
			msg = msg + " " + String.valueOf(diference);
			this.writeToFile(msg, estimationFile);
		}
		this.writeToFile(String.valueOf(differenceMean.getMean()),"dif.txt");
	}
	

	/**
	 * Start Dynamic BTSA
	 */
	protected void startDBTSA() {
		//TODO IMPLEMENTAR DBTSA
		do {
			this.initCurrentFrame();
			this.sendQuery();
			this.iCounter++;
			this.prepareSlots();
			this.identifyTagsDBTSA();
			this.finalizeFrameDBTSA();
		} while (end==false);
	}
	
	/**
	 * Identify tags in DBTSA Algorithm
	 */
	protected void identifyTagsDBTSA() {
			if (frame.get(0).getSlotSize()>1) { 
				this.col++;
				this.identifyTagsInCollision(frame.get(0).getTags());
			}
			else if (frame.get(0).getSlotSize()==1) {
				if (this.DBTSA_sc!=0) {
					this.suc++;
					this.removeTag(frame.get(0).getTags().get(0).getCode());
				}
				this.DBTSA_sc++;
			}
			else if (frame.get(0).getSlotSize()==0) {
				this.idl++;
			}
	}
	
	/**
	 * Ends frame for DBTSA Algorithm
	 */
	protected void finalizeFrameDBTSA() {
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
	
	@Override
	public void run() {
			this.startDFSA();
	}
	
}
