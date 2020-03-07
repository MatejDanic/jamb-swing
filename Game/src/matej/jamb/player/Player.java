package matej.jamb.player;

import matej.jamb.paper.Paper;

public class Player {

	private Paper paper;
	private String name;
	
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
