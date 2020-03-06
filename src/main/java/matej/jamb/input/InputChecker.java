package matej.jamb.input;

import java.util.Scanner;

public class InputChecker {
	
	public static int checkInput(int min, int max, String string) {
		Scanner sc = new Scanner(System.in);
		int input = 0;
		while(true) {
			System.out.println("Enter " + string + ".\n[" + min + "-" + max + "]\n");
			try {
				input = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
//				e.printStackTrace();
				continue;
			}
			if (input >= min && input <= max) break;
		}
		sc.close();
		return input;
	}
	
	public static String checkInput(String string) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter " + string + ":\n");
		String input = sc.nextLine();
		sc.close();
		return input;
	}
}
