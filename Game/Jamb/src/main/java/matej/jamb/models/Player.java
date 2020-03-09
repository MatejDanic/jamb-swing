package matej.jamb.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import matej.jamb.network.JDBC;

@Entity
public class Player {
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	@Column
	private String name;
	@Column
    private int gameId;
	@Transient
	private Paper paper;

	private static int idCounter = 1;
	
	public Player() {}
	
	public Player(int gameId, String name) {
		this.id = idCounter++;
		this.name = name;
		this.paper = new Paper(id);
		JDBC.executeStatement("INSERT INTO player (game_id, name) VALUES (" + gameId + ", '" + name + "')");
	}
	
	public int getId() {
		return id;
	}

	public Paper getPaper() {
		return paper;
	}
	
	public String toString() {
		return name;
	}
}
