package matej.jamb.paper.row;

public class Box {
	
	private int value;
	private BoxType type;
	private boolean available;
	
	public Box(BoxType type) {
		this.value = 0;
		this.available = false;
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public BoxType getType() {
		return type;
	}

	public void setType(BoxType type) {
		this.type = type;
	}

	public boolean isavailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}	
}
