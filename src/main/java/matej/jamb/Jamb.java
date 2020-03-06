package matej.jamb;

import java.util.ArrayList;
import java.util.List;

import matej.jamb.dice.Dice;
import matej.jamb.input.InputChecker;
import matej.jamb.player.Player;

public class Jamb {

	private List<Player> playerList;
	private List<Dice> diceList;

	public void start() {
		int numOfDice = InputChecker.checkInput(5, 6, "number of dice");
		diceList = new ArrayList<>();
		for (int i = 0; i < numOfDice; i++) {
			diceList.add(new Dice());
		}
		int numOfPlayers = InputChecker.checkInput(1, 3, "number of players");
		playerList = new ArrayList<>();
		for (int i = 0; i < numOfPlayers; i++) {
			String playerName = InputChecker.checkInput("player " + (i+1) + " name");
			playerList.add(new Player(playerName));
		}

		for (int i = 0; i < (Constants.NUM_OF_BOXES * Constants.NUM_OF_ROWS); i++) {
			for (Player player : playerList) {
				for (Dice dice : diceList) {
					dice.setReserved(false);
				}
				System.out.println(player.getName() + "\n" + player.getPaper());
				playTurn(player);
			}
		}
		calculateWinner();
	}
	
	public void calculateWinner() {
		int winnerIndex = 0;
		int winnerScore = 0;
		int index = 0;
		for (Player player : playerList) {
			index++;
			int score = player.getPaper().getScore();
			if (score > winnerScore) {
				winnerScore = score;
				winnerIndex = index;
			}
		}
		for (Player player : playerList) {
			if (player == playerList.get(winnerIndex)) System.out.println("Winner: " + player + "!");
			else System.out.println(player);
		}
	}

	public void playTurn(Player player) {
		for (int j = 0; j < Constants.NUM_OF_THROWS; j++) {
			int input = 0;
			if (j == 0) {
				input = InputChecker.checkInput(1, 1, "option number (1 - throw dice)");
			} else if (j == 1) {
				input = InputChecker.checkInput(1, 4, "option number (1 - throw dice, 2 - keep some dice, 3 - writeDown, 4 - announce)");
			} else {
				input = InputChecker.checkInput(1, 2, "option number (1 - throw dice, 2 - keep some dice, 3 - writeDown");
			}
			switch (input) {
			case 1:
				for (Dice dice : diceList) {
					if (!dice.isReserved()) System.out.println("|" + dice.throwDice() + "| ");
					else  System.out.println(dice.getCurrNum() + " ");
				}
				continue;
			case 2: 
				continue;
			case 3:
				continue;
			default:
				continue;
			}
		}
	}
}
