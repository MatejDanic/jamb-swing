package matej.jamb.dice;

import java.util.Random;

public class Dice {
	
	private boolean keep;
	private Random rand;
	private int currNum;
	
	public Dice() {
		this.keep = false;
		this.rand = new Random();
	}

	public boolean iskeep() {
		return keep;
	}

	public void setkeep(boolean keep) {
		this.keep = keep;
	}

	public int getCurrNum() {
		return this.currNum;
	}
	
	public int throwDice() {
		currNum = rand.nextInt(6)+1;
		return (currNum);
	}
}