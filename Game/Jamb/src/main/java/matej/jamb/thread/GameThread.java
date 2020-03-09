package matej.jamb.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import matej.jamb.checkers.InputChecker;
import matej.jamb.constants.Constants;
import matej.jamb.dice.Dice;
import matej.jamb.models.Player;
import matej.jamb.models.enums.BoxType;
import matej.jamb.models.enums.RowType;

public class GameThread extends Thread {
	
	private int id;

	public GameThread(int id) {
		this.id = id;
	}

	public void run() {
		try {
			List<Dice> diceList = new ArrayList<>();
			for (int i = 0; i < Constants.NUM_OF_DICE; i++) {
				diceList.add(new Dice());
			}
			int numOfPlayers = 1;

			List<Player> playerList = new ArrayList<>();
			for (int i = 0; i < numOfPlayers; i++) {
				String playerName = "Matej";
				playerList.add(new Player(id, playerName));
			}

			for (int i = 0; i < (Constants.NUM_OF_BOXES * Constants.NUM_OF_ROWS); i++) {
				for (Player player : playerList) {
					for (Dice dice : diceList) {
						dice.setReserved(false);
					}
					System.out.println("\n" + player + "\n\n" + player.getPaper());
					playTurn(player, diceList);
				}
			}
			calculateWinner(playerList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playTurn(Player player, List<Dice> diceList) {
		int diceThrows = 0;
		String announcement = "";
		Map<Integer, String> availBoxMap;
		RowType rowType;
		BoxType boxType;
		while(diceThrows <= Constants.NUM_OF_THROWS) {
			int input = 0;
			int paperId = player.getPaper().getId();
			if (diceThrows == 0) {
				input = InputChecker.checkInput(paperId, player.getPaper().getmoveNum(), 1, 1, "option number (1 - throw dice)");
				//				input = 1;
			} else if (diceThrows == 1 && announcement.isEmpty()) {
				input = InputChecker.checkInput(paperId, player.getPaper().getmoveNum(), 1, 4, "option number (1 - throw dice, 2 - keep some dice, 3 - write down, 4 - announce)");
				//				input = 3;
			} else if (diceThrows == Constants.NUM_OF_THROWS) {
				input = InputChecker.checkInput(paperId, player.getPaper().getmoveNum(), 3, 3, "option number (3 - write down)");
			} else {
				input = InputChecker.checkInput(paperId, player.getPaper().getmoveNum(), 1, 3, "option number (1 - throw dice, 2 - keep some dice, 3 - write down");
			}
			switch (input) {
			case 1:
				throwDice(diceList);
				diceThrows++;
				break;
			case 2:
				System.out.println(diceList);
				String diceForKeep = Integer.toBinaryString(InputChecker.checkInput(paperId, player.getPaper().getmoveNum(), 1, 31, "dice that you want to keep (select already kept dice to unkeep)"));
				for (int i = 0; i < diceForKeep.length(); i++) {
					if (diceForKeep.charAt(i) == '1') {
						diceList.get(i).setReserved(!diceList.get(i).isReserved());
					}
				}
				System.out.println(diceList);
				break;
			case 3:
				System.out.println("\nAvailable boxes are:\n");
				availBoxMap = player.getPaper().getAvailBoxMap(diceThrows, announcement);
				for (Integer boxIndex : availBoxMap.keySet()) {
					System.out.println(boxIndex + ". " + availBoxMap.get(boxIndex).toString());
				}
				input = InputChecker.checkInput(player.getPaper().getId(), player.getPaper().getmoveNum(), 1, availBoxMap.size(), "index of box");
				rowType = RowType.valueOf(availBoxMap.get(input).split(" ")[0]);
				boxType = BoxType.valueOf(availBoxMap.get(input).split(" ")[1]);
				player.getPaper().getRow(rowType).writeDown(diceList, player.getPaper().getRow(rowType).getBox(boxType).getBoxNumber());
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
				input = InputChecker.checkInput(player.getPaper().getId(), player.getPaper().getmoveNum(), 1, availBoxMap.size(), "index of announcement");
				announcement += " " + BoxType.valueOf(availBoxMap.get(input).split(" ")[1]);
				break;
			}
		}

		System.out.println("\n");
	}

	private void throwDice(List<Dice> diceList) {
		System.out.println("\nDICE RESULTS:");
		for (Dice dice : diceList) {
			if (!dice.isReserved()) dice.throwDice();
		}
		System.out.println(diceList);
	}

	private void calculateWinner(List<Player> playerList) {
		int winnerIndex = 0;
		int winnerScore = 0;
		int index = 0;
		System.out.println("");
		for (Player player : playerList) {
			int score = player.getPaper().getScore();
			System.out.println(player + ": " + score);
			if (score > winnerScore) {
				winnerScore = score;
				winnerIndex = index;
			}
			index++;
		}
		System.out.println("Winner: " + playerList.get(winnerIndex));
	}

}
