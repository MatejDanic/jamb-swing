package matej.jamb.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputChecker {

	private Scanner sc;

	public InputChecker(Scanner sc) {
		this.sc = new Scanner(System.in);
	}

	public int checkInput(int min, int max, String string) {
		int input = 0;
		System.out.println("Enter " + string + ":\n[" + min + "-" + max + "]\n");
		while (sc.hasNextLine()) {
			try {
				input = Integer.parseInt(sc.nextLine());
				if (input >= min && input <= max) break;
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		return input;
	}

	public String checkInput(String string) {
		String input = "";
		System.out.println("Enter " + string + ":\n");
		while(sc.hasNextLine()) {
			input = sc.nextLine();
			break;
		}
		return input;
	}

	public List<Integer> checkInput(int numOfDice, String string) {
		String[] input;
		List<Integer> diceIndexList = new ArrayList<>();
		System.out.println("Enter " + string + ":\n");
		while(sc.hasNextLine()) {
			try {
				input = sc.nextLine().split(",");
				for (String index : input) {
					diceIndexList.add(Integer.parseInt(index));
				}
				if (diceIndexList.size() != 0) break;
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		return diceIndexList;
	}
}
