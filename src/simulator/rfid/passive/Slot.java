package simulator.rfid.passive;

import java.util.ArrayList;

/**
 * 
 * @author Rafael Perazzo Barbosa Mota
 *
 */
public class Slot {

	private ArrayList<Tag> tags = new ArrayList<Tag>();
	
	public Slot() {
		tags.clear();
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}
	
	public void addTag(Tag code) {
		tags.add(code);
	}
	
	public int getSlotSize() {
		return(this.tags.size());
	}
}
