package matej.jamb.models;

import matej.jamb.models.enums.BoxType;

public class Box {

	private int value;
	private BoxType boxType;
	private boolean available;
	private boolean written;
	
	public Box(BoxType boxType) {
		this.boxType = boxType;
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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		setAvailable(false);
		setWritten(true);
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
	
	public void setWritten(boolean written) {
		this.written = written;
	}
}
