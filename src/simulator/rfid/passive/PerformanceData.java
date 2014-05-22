package simulator.rfid.passive;

public class PerformanceData {

	private double mean;
	private double lower;
	private double upper;
	
	public PerformanceData(double mean, double lower, double upper) {
		this.mean = mean;
		this.lower = lower;
		this.upper = upper;
	}

	/**
	 * @return the mean
	 */
	public double getMean() {
		return mean;
	}

	/**
	 * @return the lower
	 */
	public double getLower() {
		return lower;
	}

	/**
	 * @return the upper
	 */
	public double getUpper() {
		return upper;
	}
	
	
	
}
