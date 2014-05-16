package simulator.rfid.passive;

import java.util.Random;

/**
 * 
 * @author Rafael Perazzo Barbosa Mota
 *
 */
public class Tag {

	private int code;
	private int rng16;
	private Random generator;
	private int state;
	
	public Tag() {
		generator = new Random();
		this.code = 0;
		this.state = TagConstants.READY;
	}
	
	public Tag(int code) {
		generator = new Random();
		this.code = code;
		this.state = TagConstants.READY;
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
		this.rng16 = generator.nextInt(up);
		//this.rng16 = generator.nextInt(up);
	}

	public int getRng16() {
		return rng16;
	}
	
	
	
}
