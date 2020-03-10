package matej.jamb.models;

public class Player {
	
	private String name;
	private Paper paper;
	
	public Player(String name) {
		this.name = name;
		this.paper = new Paper();
	}

	public Paper getPaper() {
		return paper;
	}
	
	public String toString() {
		return name;
	}
}
