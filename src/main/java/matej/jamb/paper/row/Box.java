package matej.jamb.paper.row;

public class Box {
	
	private int value;
	private BoxType boxType;
	private boolean available, written;
	private int id;
	
	public Box(BoxType boxType, int id) {
		this.id = id;
		this.value = 0;
		this.available = false;
		this.written = false;
		this.boxType = boxType;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		this.available = false;
		this.written = true;
	}

	public BoxType getBoxType() {
		return boxType;
	}

	public void setBoxType(BoxType boxType) {
		this.boxType = boxType;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getId() {
		return id;
	}

	public boolean isWritten() {
		return written;
	}
	
	public void setWritten(boolean written) {
		this.written = written;
	}
}
