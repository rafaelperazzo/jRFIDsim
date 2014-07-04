package simulator.rfid.passive;

import java.util.Random;

import org.apache.commons.math3.random.RandomDataGenerator;

/**
 * 
 * @author Rafael Perazzo Barbosa Mota
 *
 */
public class Tag {

	private int code;
	private int rng16;
	private Random generator;
	RandomDataGenerator randomData; 
	private int state;
	
	
	public Tag() {
		generator = new Random();
		randomData =  new RandomDataGenerator();
		this.code = 0;
		this.state = TagConstants.READY;
		this.rng16 = -1;
		this.randomData.reSeedSecure();
		
	}
	
	public Tag(int code) {
		generator = new Random();
		randomData =  new RandomDataGenerator();
		this.code = code;
		this.state = TagConstants.READY;
		this.rng16 = -1;
		this.randomData.reSeedSecure();
		
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int codigo) {
		this.code = codigo;
	}
	
	public void setRng16(int up) { 
		if (up!=0) {
			this.rng16 = generator.nextInt(up);
			//this.rng16 = randomData.nextSecureInt(0, up);
		}
		else this.rng16 = 0;
	}
	
	public void updateRng16(int value) {
		this.rng16 = this.rng16 + value;
		if (this.rng16<0) System.out.println("Erro!");
	}
	
	public void updateRng16() {
		this.rng16 = this.rng16 + generator.nextInt(2);
	}
	
	public int getRng16() {
		return rng16;
	}
	
	
	
}
