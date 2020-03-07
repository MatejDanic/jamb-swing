package matej.jamb.dice;

import java.util.Random;

public class Dice {

	private int currNum;
	private boolean reserved;
	private Random rand;
	
	public Dice() {
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
	
	public int throwDice() {
		currNum = rand.nextInt(6)+1;
		return (currNum);
	}
	
	public String toString() {
		if (reserved) return "|" + currNum + "|";
		else return "" + currNum;
	}
}