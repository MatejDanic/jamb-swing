package matej.jamb.models;

import java.util.ArrayList;
import java.util.List;

import matej.jamb.checkers.CategoryChecker;
import matej.jamb.constants.JambConstants;
import matej.jamb.models.enums.BoxType;
import matej.jamb.models.enums.RowType;

public class Row {

	private RowType	rowType;
	private List<Box> boxList;
	
	public Row(RowType rowType) {
		this.rowType = rowType;
		this.boxList = new ArrayList<>();
		initializeBoxes();
	}

	public List<Box> getBoxList() {
		return boxList;
	}

	public RowType getRowType() {
		return rowType;
	}

	protected int getUpperScore() {
		int upperScore = 0;
		for (int i = 0; i < 6; i++) {
			upperScore += boxList.get(i).getValue();
		}
		upperScore = upperScore < 60 ? upperScore : upperScore + 30;
		return upperScore;
	}

	protected int getMiddleScore() {
		int middleScore;
		if (boxList.get(6).isAvailable() || boxList.get(7).isAvailable() || boxList.get(0).isAvailable()) middleScore = 0;
		else middleScore = (boxList.get(6).getValue() - boxList.get(7).getValue()) * boxList.get(0).getValue();
		return middleScore;
	}
	protected int getLowerScore() {
		int lowerScore = 0;
		for (int i = 8; i < 13; i++) {
			lowerScore += boxList.get(i).getValue();
		}
		return lowerScore;
	}

	protected int getScore() {
		return (getUpperScore() + getMiddleScore() + getLowerScore());
	}

	protected boolean isDone() {
		for(Box box : boxList) {
			if (box.isAvailable()) return false;
		}
		return true;
	}

	private void initializeBoxes() {
		for (int i = 0; i < JambConstants.NUM_OF_BOXES; i++) {
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
			Box box = new Box(boxType);
			box.setAvailable(available);
			boxList.add(box);
		}
	}

	public void writeDown(List<Dice> diceList, int boxNum) {
		//		System.out.println(boxNum);
		System.out.println(diceList);
		System.out.println(boxNum);
		int score = 0;
		if (boxNum >= 0 && boxNum <= 5) {
			for (Dice k : diceList) {
				if (k.getCurrNum() == boxNum + 1) {
					score += k.getCurrNum();
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


		if (boxNum < 12 && rowType == RowType.DOWNWARD) boxList.get(boxNum + 1).setAvailable(true);
		if (boxNum > 0 && rowType == RowType.UPWARD) boxList.get(boxNum - 1).setAvailable(true);
	}

	public int checkCategory(int boxNum, List<Dice> diceList) {
		int categoryScore = 0;
		int bonus;
		switch (boxNum) {
		case 8:
			categoryScore = CategoryChecker.checkTrips(diceList);
			bonus = JambConstants.BONUS_TRIPS;
			break;
		case 9:
			categoryScore = CategoryChecker.checkStraight(diceList);
			bonus = 0;
			break;
		case 10:
			categoryScore = CategoryChecker.checkFull(diceList);
			bonus = JambConstants.BONUS_FULL;
			break;
		case 11:
			categoryScore = CategoryChecker.checkPoker(diceList);
			bonus = JambConstants.BONUS_POKER;
			break;
		case 12:
			categoryScore = CategoryChecker.checkJamb(diceList);
			bonus = JambConstants.BONUS_JAMB;
			break;
		default:
			categoryScore = 0;
			bonus = 0;
			break;
		}
		return categoryScore == 0 ? categoryScore : categoryScore + bonus;
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
