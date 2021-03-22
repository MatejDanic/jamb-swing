package matej.jamb.models;

import java.util.Random;

public class Dice {

	private int currNum;
	private boolean reserved;
	private Random rand;
	
	public Dice() {
		this.currNum = 1;
		this.reserved = false;
		this.rand = new Random();
	}
	
	public int getCurrNum() {
		return this.currNum;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	
	public int roll() {
		currNum = rand.nextInt(6)+1;
		return (currNum);
	}
	
	public String toString() {
		return reserved ? "|" + currNum + "|" : Integer.toString(currNum);
	}
}