package matej.jamb.paper.row;

public class Box {

	private int id;
	private BoxType boxType;
	private int value;
	private boolean available, written;
	
	public Box(BoxType boxType, int id) {
		this.boxType = boxType;
		this.id = id;
		
		this.value = 0;
		this.available = false;
		this.written = false;
	}

	public BoxType getBoxType() {
		return boxType;
	}

	public void setBoxType(BoxType boxType) {
		this.boxType = boxType;
	}


	public int getId() {
		return id;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		this.available = false;
		this.written = true;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isWritten() {
		return written;
	}
}
