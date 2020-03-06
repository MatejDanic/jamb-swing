package matej.jamb.paper.row;

import java.util.ArrayList;
import java.util.List;

import matej.jamb.Constants;
import matej.jamb.dice.Dice;

public class Row {

	private List<Box> boxList;
	private boolean done;
	private RowType rowType;
	private int upperScore, middleScore, lowerScore;

	public Boolean isDone() {
		return done;
	}

	public RowType getRowType() {
		return rowType;
	}

	public List<Box> getBoxList() {
		return boxList;
	}
	
	public Row(RowType rowType) {
		this.boxList = new ArrayList<>();
		this.rowType = rowType;
		this.upperScore = 0;
		this.middleScore = 0;
		this.lowerScore = 0;
		initializeBoxes();
	}

	private void initializeBoxes() {
		for (int i = 0; i < Constants.NUM_OF_BOXES; i++) {
			boolean available = false;
			BoxType boxType;
			switch (i) {
			case 0:
				boxType = BoxType.ONES;
				if (rowType == RowType.DOWNWARD) available = true;
				continue;
			case 1:
				boxType = BoxType.TWOS;
				continue;
			case 2:
				boxType = BoxType.THREES;
				continue;
			case 3:
				boxType = BoxType.FOURS;
				continue;
			case 4:
				boxType = BoxType.FIVES;
				continue;
			case 5:
				boxType = BoxType.SIXES;
				continue;
			case 6:
				boxType = BoxType.MAX;
				continue;
			case 7:
				boxType = BoxType.MIN;
				continue;
			case 8:
				boxType = BoxType.TRIPS;
				continue;
			case 9:
				boxType = BoxType.STRAIGHT;
				continue;
			case 10:
				boxType = BoxType.FULL;
				continue;
			case 11:
				boxType = BoxType.POKER;
				continue;
			case 12:
				boxType = BoxType.JAMB;
				if (rowType == RowType.UPWARD) available = true;
				continue;
			default:
				boxType = BoxType.ONES;
				available = false;
			}
			if (rowType == RowType.ANYDIR || rowType == RowType.ANNOUNCE) available = true;
			Box box = new Box(boxType);
			box.setAvailable(available);
			boxList.add(box);
		}
	}

	public int getUpperScore() {
		int upperScore = 0;
		for (int i = 0; i < 6; i++) {
			upperScore += boxList.get(i).getValue();
		}
		return upperScore >= 60 ? upperScore : upperScore + 30;
	}

	public int getMiddleScore() {
		int middleScore;
		if (boxList.get(6).isavailable() || boxList.get(7).isavailable() || boxList.get(0).isavailable()) middleScore = 0;
		else {
			middleScore = (boxList.get(6).getValue() - boxList.get(7).getValue()) * boxList.get(0).getValue();
		}
		return middleScore;
	}
	public int getLowerScore() {
		int lowerScore = 0;
		for (int i = 8; i < 13; i++) {
			lowerScore += boxList.get(i).getValue();
		}
		return lowerScore;
	}

	public int getScore() {
		upperScore = getUpperScore();
		middleScore = getMiddleScore();
		lowerScore = getLowerScore();
		return (upperScore + middleScore + lowerScore);
	}

	public void writeDown(List<Dice> diceList, int boxNum) {
		int score = 0;
		if (boxNum >= 0 && boxNum <= 5) {
			for (Dice k : diceList) {
				int currNum = k.getCurrNum();
				if (currNum == boxNum + 1) {
					score += currNum;
				}
			}
		} else if (boxNum >= 6 && boxNum <= 7) {
			for (Dice k : diceList) {
				score += k.getCurrNum();
			}
		} else {
			score = checkCategory(boxNum, diceList);
		}
		boxList.get(boxNum).setValue(score);
		boxList.get(boxNum).setAvailable(false);

		if (boxNum > 0 && boxNum < 12) {
			if (rowType == RowType.DOWNWARD) {
				boxList.get(boxNum + 1).setAvailable(true);
			} else if(rowType == RowType.UPWARD) {
				boxList.get(boxNum - 1).setAvailable(true);
			}
		}
	}
	
	public int checkCategory(int boxNum, List<Dice> diceList) {
		int categoryScore = 0;
		switch (boxNum) {
		case 8:
			categoryScore = checkTrips(diceList);
		case 9:
			categoryScore = checkStraight(diceList);
		case 10:
			categoryScore = checkFull(diceList);
		case 11:
			categoryScore = checkPoker(diceList);
		case 12:
			categoryScore = checkJamb(diceList);
		}
		return categoryScore;
	}

	public int checkTrips(List<Dice> diceList) {
		int rezultat = 0;
		for (Dice k1 : diceList) {
			int broj = 0;
			int score = k1.getCurrNum();
			for (Dice k2 : diceList) {
				if (k1 != k2 && k1.getCurrNum() == k2.getCurrNum()) {
					broj++;
					score += k2.getCurrNum();
				}
			}
			if (broj == 3) {
				rezultat = score;
				break;
			}
		}
		return rezultat;
	}

	public int checkStraight(List<Dice> diceList) {
		int rezultat = 0;
		List<Integer> skala = new ArrayList<>();
		skala.add(2);
		skala.add(3);
		skala.add(4);
		skala.add(5);
		List<Integer> brojevi = new ArrayList<>();
		for (Dice k : diceList) {
			brojevi.add(k.getCurrNum());
		}
		if (brojevi.containsAll(skala)){
			if (brojevi.contains(1)) {
				rezultat = 35;
			} else if (brojevi.contains(6))
				rezultat = 45;
		}
		return rezultat;
	}

	public int checkFull(List<Dice> diceList) {
		int rezultat = 0;
		int score2 = 0; 
		int score3 = 0;
		for (Dice k1 : diceList) {
			int broj = 0;
			int score = k1.getCurrNum();
			for (Dice k2 : diceList) {
				if (k1 != k2 && k1.getCurrNum() == k2.getCurrNum()) {
					broj++;
					score += k2.getCurrNum();
				}
			}
			if (broj == 2) {
				score2 = score;
			} else if (broj == 3) {
				score3 = score;
			}
			if (score2 != 0 && score3 != 0) {
				rezultat = score2 + score3;
				break;
			}
		}
		return rezultat;
	}

	public int checkPoker(List<Dice> diceList) {
		int rezultat = 0;
		for (Dice k1 : diceList) {
			int broj = 0;
			int score = k1.getCurrNum();
			for (Dice k2 : diceList) {
				if (k1 != k2 && k1.getCurrNum() == k2.getCurrNum()) {
					broj++;
					score += k2.getCurrNum();
				}
			}
			if (broj == 4) {
				rezultat = score;
				break;
			}
		}
		return rezultat;
	}

	public int checkJamb(List<Dice> diceList) {
		int rezultat = 0;
		for (Dice k1 : diceList) {
			int broj = 0;
			int score = k1.getCurrNum();
			for (Dice k2 : diceList) {
				if (k1 != k2 && k1.getCurrNum() == k2.getCurrNum()) {
					broj++;
					score += k2.getCurrNum();
				}
			}
			if (broj == 5) {
				rezultat = score;
				break;
			}
		}
		return rezultat;
	}
}
