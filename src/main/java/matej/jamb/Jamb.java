package matej.jamb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import matej.jamb.constants.Constants;
import matej.jamb.dice.Dice;
import matej.jamb.input.InputChecker;
import matej.jamb.paper.row.BoxType;
import matej.jamb.paper.row.RowType;
import matej.jamb.player.Player;

public class Jamb {

	private List<Player> playerList;
	private List<Dice> diceList;
	private InputChecker ic;
	private int numOfDice;

	public void start() {
		ic = new InputChecker(new Scanner(System.in));
		numOfDice = ic.checkInput(5, 6, "number of dice");
		diceList = new ArrayList<>();
		for (int i = 0; i < numOfDice; i++) {
			diceList.add(new Dice());
		}
		int numOfPlayers = ic.checkInput(1, 3, "number of players");

		playerList = new ArrayList<>();
		for (int i = 0; i < numOfPlayers; i++) {
			String playerName = ic.checkInput("player " + (i+1) + " name");
			playerList.add(new Player(playerName));
		}

		for (int i = 0; i < (Constants.NUM_OF_BOXES * Constants.NUM_OF_ROWS); i++) {
			for (Player player : playerList) {
				for (Dice dice : diceList) {
					dice.setReserved(false);
				}
				System.out.println("\n" + player + "\n\n" + player.getPaper());
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
			int score = player.getPaper().getScore();
			System.out.println(player + ": " + score);
			if (score > winnerScore) {
				winnerScore = score;
				winnerIndex = index;
			}
			index++;
		}
		for (Player player : playerList) {
			if (player == playerList.get(winnerIndex)) System.out.println("Winner: " + player + "!");
			else System.out.println(player);
		}
	}

	public void playTurn(Player player) {
		int diceThrows = 0;
		String announcement = "";
		Map<Integer, String> availBoxMap;
		RowType rowType;
		BoxType boxType;
		while(diceThrows <= Constants.NUM_OF_THROWS) {
			int input = 0;
			if (diceThrows == 0) {
//				input = ic.checkInput(1, 1, "option number (1 - throw dice)");
				input = 1;
			} else if (diceThrows == 1 && announcement.isEmpty()) {
//				input = ic.checkInput(1, 4, "option number (1 - throw dice, 2 - keep some dice, 3 - write down, 4 - announce)");
				input = 3;
			} else if (diceThrows == Constants.NUM_OF_THROWS) {
				input = ic.checkInput(3, 3, "option number (3 - write down)");
			} else {
				input = ic.checkInput(1, 3, "option number (1 - throw dice, 2 - keep some dice, 3 - write down");
			}
			switch (input) {
			case 1:
				throwDice(diceList);
				diceThrows++;
				break;
			case 2:
				System.out.println(diceList);
				List<Integer> diceForKeep = ic.checkInput(numOfDice, "dice that you want to keep (select already kept dice to unkeep)");
				for (int diceIndex : diceForKeep) {
					diceList.get(diceIndex-1).setReserved(!diceList.get(diceIndex-1).getReserved());
				}
				System.out.println(diceList);
				break;
			case 3:
				System.out.println("\nAvailable boxes are:\n");
				availBoxMap = player.getPaper().getAvailBoxMap(diceThrows, announcement);
				for (Integer boxIndex : availBoxMap.keySet()) {
					System.out.println(boxIndex + ". " + availBoxMap.get(boxIndex).toString());
				}
//				input = ic.checkInput(1, availBoxMap.size(), "index of box");
				input = 1;
				rowType = RowType.valueOf(availBoxMap.get(input).split(" ")[0]);
				boxType = BoxType.valueOf(availBoxMap.get(input).split(" ")[1]);
				player.getPaper().getRow(rowType).writeDown(diceList, player.getPaper().getRow(rowType).getBox(boxType).getId());
				System.out.println(player.getPaper());
				return;
			case 4:
				System.out.println("\nAvailable announcements are:\n");
				announcement = "ANNOUNCE";
				availBoxMap = player.getPaper().getAvailBoxMap(diceThrows, announcement);
				for (Integer boxIndex : availBoxMap.keySet()) {
					System.out.println(boxIndex + ". " + availBoxMap.get(boxIndex).toString());
				}
				System.out.println(diceList);
				input = ic.checkInput(1, availBoxMap.size(), "index of announcement");
				announcement += " " + BoxType.valueOf(availBoxMap.get(input).split(" ")[1]);
				break;
			}
		}

		System.out.println("\n");
	}

	private void throwDice(List<Dice> diceList2) {
		System.out.println("\nDICE RESULTS:");
		for (Dice dice : diceList) {
			if (!dice.isReserved()) dice.throwDice();
		}
		System.out.println(diceList);
	}
}
