package matej.jamb.paper.row;

import java.util.ArrayList;
import java.util.List;

import matej.jamb.constants.Constants;
import matej.jamb.dice.Dice;

public class Row {

	private List<Box> boxList;
	private boolean done;
	private RowType rowType;
	private int UPWARDperScore, middleScore, lowerScore;

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
		this.UPWARDperScore = 0;
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
				break;
			case 1:
				boxType = BoxType.TWOS;
				break;
			case 2:
				boxType = BoxType.THREES;
				break;
			case 3:
				boxType = BoxType.FOURS;
				break;
			case 4:
				boxType = BoxType.FIVES;
				break;
			case 5:
				boxType = BoxType.SIXES;
				break;
			case 6:
				boxType = BoxType.MAX;
				break;
			case 7:
				boxType = BoxType.MIN;
				break;
			case 8:
				boxType = BoxType.TRIPS;
				break;
			case 9:
				boxType = BoxType.STRAIGHT;
				break;
			case 10:
				boxType = BoxType.FULL;
				break;
			case 11:
				boxType = BoxType.POKER;
				break;
			case 12:
				boxType = BoxType.JAMB;
				if (rowType == RowType.UPWARD) available = true;
				break;
			default:
				boxType = BoxType.ONES;
				available = true;
				break;
			}
			if (rowType == RowType.ANYDIR || rowType == RowType.ANNOUNCE) available = true;
			Box box = new Box(boxType, i);
			box.setAvailable(available);
			boxList.add(box);
		}
	}

	public int getUPWARDperScore() {
		int UPWARDperScore = 0;
		for (int i = 0; i < 6; i++) {
			UPWARDperScore += boxList.get(i).getValue();
		}
		return UPWARDperScore >= 60 ? UPWARDperScore : UPWARDperScore + 30;
	}

	public int getMiddleScore() {
		int middleScore;
		if (boxList.get(6).isAvailable() || boxList.get(7).isAvailable() || boxList.get(0).isAvailable()) middleScore = 0;
		else middleScore = (boxList.get(6).getValue() - boxList.get(7).getValue()) * boxList.get(0).getValue();
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
		UPWARDperScore = getUPWARDperScore();
		middleScore = getMiddleScore();
		lowerScore = getLowerScore();
		return (UPWARDperScore + middleScore + lowerScore);
	}

	public void writeDown(List<Dice> diceList, int boxNum) {
//		System.out.println(boxNum);
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
//		System.out.println("score: " + score);
//		boxList.get(boxNum).setAvailable(false);

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
			categoryScore = checkTrips(diceList) == 0 ? 0 : checkTrips(diceList) + Constants.BONUS_TRIP;
			break;
		case 9:
			categoryScore = checkStraight(diceList);
			break;
		case 10:
			categoryScore = checkFull(diceList) == 0 ? 0 : checkFull(diceList) + Constants.BONUS_FULL;
			break;
		case 11:
			categoryScore = checkPoker(diceList) == 0 ? 0 : checkPoker(diceList) + Constants.BONUS_POKER;
			break;
		case 12:
			categoryScore = checkJamb(diceList) == 0 ? 0 : checkJamb(diceList) + Constants.BONUS_JAMB;
			break;
		default:
			categoryScore = 0;
			break;
		}
		return categoryScore;
	}

	public int checkTrips(List<Dice> diceList) {
		int tripScore = 0;
		for (Dice d1 : diceList) {
			int num = 1;
			int score = d1.getCurrNum();
			for (Dice d2 : diceList) {
				if (d1 != d2 && d1.getCurrNum() == d2.getCurrNum()) {
					num++;
					if (num <= 3) score += d2.getCurrNum();
				}
			}
//			System.out.println(score + " " + num);
			if (num == 3) {
				tripScore = score;
				break;
			}
		}
		return tripScore;
	}

	public int checkStraight(List<Dice> diceList) {
		int straightScore = 0;
		List<Integer> straight = new ArrayList<>();
		straight.add(2);
		straight.add(3);
		straight.add(4);
		straight.add(5);
		List<Integer> numbers = new ArrayList<>();
		for (Dice k : diceList) {
			numbers.add(k.getCurrNum());
		}
		if (numbers.containsAll(straight)){
			if (numbers.contains(1)) {
				straightScore = 35;
			} else if (numbers.contains(6))
				straightScore = 45;
		}
		return straightScore;
	}

	public int checkFull(List<Dice> diceList) {
		int fullScore = 0;
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
				fullScore = scoreTwo + scoreThree;
				break;
			}
		}
		return fullScore;
	}

	public int checkPoker(List<Dice> diceList) {
		int pokerScore = 0;
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
				pokerScore = score;
				break;
			}
		}
		return pokerScore;
	}

	public int checkJamb(List<Dice> diceList) {
		int jambScore = 0;
		for (Dice d1 : diceList) {
			int num = 0;
			int score = d1.getCurrNum();
			for (Dice d2 : diceList) {
				if (d1 != d2 && d1.getCurrNum() == d2.getCurrNum()) {
					num++;
					score += d2.getCurrNum();
				}
			}
			if (num == 5) {
				jambScore = score;
				break;
			}
		}
		return jambScore;
	}

	public Box getBox(BoxType boxType) {
		for (Box box : boxList) {
			if (box.getBoxType() == boxType) {
				return box;
			}
		}
		return null;
	}
}
