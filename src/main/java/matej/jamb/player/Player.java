package matej.jamb.player;

import matej.jamb.paper.Paper;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
