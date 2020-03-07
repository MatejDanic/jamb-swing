package matej.jamb.category; 
 
import java.util.ArrayList; 
import java.util.List; 
 
import matej.jamb.dice.Dice; 
 
public class CategoryChecker { 
	 
	public static int checkTrips(List<Dice> diceList) { 
		int result = 0; 
		for (Dice d1 : diceList) { 
			int num = 1; 
			int score = d1.getCurrNum(); 
			for (Dice d2 : diceList) { 
				if (d1 != d2 && d1.getCurrNum() == d2.getCurrNum()) { 
					num++; 
					if (num <= 3) score += d2.getCurrNum(); 
				} 
			} 
			if (num == 3) { 
				result = score; 
				break; 
			} 
		} 
		return result; 
	} 
 
	public static int checkStraight(List<Dice> diceList) { 
		int result = 0; 
		List<Integer> straight = new ArrayList<>(); 
		straight.add(2); 
		straight.add(3); 
		straight.add(4); 
		straight.add(5); 
		List<Integer> numbers = new ArrayList<>(); 
		for (Dice d : diceList) { 
			numbers.add(d.getCurrNum()); 
		} 
		if (numbers.containsAll(straight)){ 
			if (numbers.contains(1)) { 
				result = 35; 
			} else if (numbers.contains(6)) 
				result = 45; 
		} 
		return result; 
	} 
 
	public static int checkFull(List<Dice> diceList) { 
		int result = 0; 
		int scoreTwo = 0;  
		int scoreThree = 0; 
		for (Dice d1 : diceList) { 
			int num = 1; 
			int score = d1.getCurrNum(); 
			for (Dice d2 : diceList) { 
				if (d1 != d2 && d1.getCurrNum() == d2.getCurrNum()) { 
					num++; 
					score += d2.getCurrNum(); 
				} 
			} 
			if (num == 2) { 
				scoreTwo = score; 
			} else if (num == 3) { 
				scoreThree = score; 
			} 
			if (scoreTwo != 0 && scoreThree != 0) { 
				result = scoreTwo + scoreThree; 
				break; 
			} 
		} 
		return result; 
	} 
 
	public static int checkPoker(List<Dice> diceList) { 
		int result = 0; 
		for (Dice d1 : diceList) { 
			int num = 1; 
			int score = d1.getCurrNum(); 
			for (Dice d2 : diceList) { 
				if (d1 != d2 && d1.getCurrNum() == d2.getCurrNum()) { 
					num++; 
					if (num <= 4) score += d2.getCurrNum(); 
				} 
			} 
			if (num == 4) { 
				result = score; 
				break; 
			} 
		} 
		return result; 
	} 
 
	public static int checkJamb(List<Dice> diceList) { 
		int result = 0; 
		for (Dice d1 : diceList) { 
			int num = 1; 
			int score = d1.getCurrNum(); 
			for (Dice d2 : diceList) { 
				if (d1 != d2 && d1.getCurrNum() == d2.getCurrNum()) { 
					num++; 
					score += d2.getCurrNum(); 
				} 
			} 
			if (num == 5) { 
				result = score; 
				break; 
			} 
		} 
		return result; 
	} 
} 