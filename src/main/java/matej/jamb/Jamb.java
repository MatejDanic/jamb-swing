package matej.jamb;

import java.util.List;
import java.util.Scanner;

import matej.jamb.player.Player;

public class Jamb {

	private List<Player> playerList;
	
	public void start() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the number of players:");
		sc.close();
	}

}
