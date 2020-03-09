package matej.jamb.checkers;

import java.util.concurrent.TimeUnit;

import matej.jamb.network.JDBC;

public class InputChecker {

//	private static Scanner sc = new Scanner(System.in);

	public static int checkInput(int paperId, int moveNum, int min, int max, String string) {
		System.out.println("Enter " + string + ":\n[" + min + "-" + max + "]\n");
		int input = 0;
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(1);
//				System.out.println("trazim movNum " + moveNum);
				input = JDBC.executeStatement("SELECT option FROM option WHERE paper_id = " + paperId + " AND move_num = " + moveNum);
//				System.out.println("dobio sam " + input);
				if (input >= min && input <= max) break;
			} catch (Exception e) {
				System.out.println("no value");
			}

		}
		return input;


		//		int input = 0;
		//		System.out.println("Enter " + string + ":\n[" + min + "-" + max + "]\n");
		//		while (sc.hasNextLine()) {
		//			try {
		//				input = Integer.parseInt(sc.nextLine());
		//				if (input >= min && input <= max) break;
		//			} catch (Exception e) {
		////				e.printStackTrace();
		//			}
		//		}
		//		return input;
	}

	//	public static String checkInput(String string) {
	//		String input = "";
	//		System.out.println("Enter " + string + ":\n");
	//		while(sc.hasNextLine()) {
	//			input = sc.nextLine();
	//			break;
	//		}
	//		return input;
	//	}
	//
	//	public static List<Integer> checkInput(int numOfDice, String string) {
	//		String[] input;
	//		List<Integer> diceIndexList = new ArrayList<>();
	//		System.out.println("Enter " + string + ":\n");
	//		while(sc.hasNextLine()) {
	//			try {
	//				input = sc.nextLine().split(",");
	//				for (String index : input) {
	//					if (Integer.parseInt(index) >= 1 && Integer.parseInt(index) <= numOfDice) diceIndexList.add(Integer.parseInt(index));
	//				}
	//				if (diceIndexList.size() != 0) break;
	//			} catch (Exception e) {
	//				//				e.printStackTrace();
	//			}
	//		}
	//		return diceIndexList;
	//	}
}
