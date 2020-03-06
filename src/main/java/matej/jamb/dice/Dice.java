package matej.jamb.dice;

import java.util.Random;

public class Dice {
	
	private boolean reserved;
	private Random rand;
	private int currNum;
	
	public Dice() {
		this.reserved = false;
		this.rand = new Random();
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public int getCurrNum() {
		return this.currNum;
	}
	
	public int throwDice() {
		currNum = rand.nextInt(6)+1;
		return (currNum);
	}
}