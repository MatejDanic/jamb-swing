package matej.jamb.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import matej.jamb.paper.row.Box;
import matej.jamb.paper.row.BoxType;
import matej.jamb.paper.row.Row;
import matej.jamb.paper.row.RowType;

public class Paper {

	private List<Row> rowList;

	public Paper() {
		this.rowList = new ArrayList<>();
		rowList.add(new Row(RowType.DOWNWARD));
		rowList.add(new Row(RowType.UPWARD));
		rowList.add(new Row(RowType.ANYDIR));
		rowList.add(new Row(RowType.ANNOUNCE));
	}

	public Row getRow(RowType type) {
		for (Row row : rowList) {
			if (row.getRowType() == type) {
				return row;
			}
		}
		return null;
	}

	public boolean isDone() {
		for (Row row : rowList) {
			if (!row.isDone()) return false;
		}
		return true;
	}

	public int getScore() {
		int score = 0;
		for (Row row : rowList) {
			score += row.getScore();
//			System.out.println("row score: " + row.getScore());
		}
		return score;
	}

	public String toString() {
		String string = "\n------------------" + 
						"\n |    A    |A   a" + 
						"\n V    |    V|   n"+
						"\n------------------";
		for (int i = 0; i < 13; i++) {
			string += "\n";
			for (Row row : rowList) {
				if (!row.getBoxList().get(i).isWritten()) string += "|--| ";
				else string += ("|" + row.getBoxList().get(i).getValue() + "| ");
			}
			
			if (i == 5 || i == 7 || i == 12) {
				string += "\n------------------\n";
				for (Row row : rowList) {
					if (i == 5) {
						//					
						string += "|" + row.getUpperScore() + "|  ";
						//					string += "\n---------------";
					}  else if (i == 7) {
						string += "|" + row.getMiddleScore() + "|  ";
					} else if (i == 12) {
						string += "|" + row.getLowerScore() + "|  ";
					}
				}
				string += "\n------------------";
			}
			
			
		}
		return string + "\nScore: " + getScore() + "\n";
	}

	public List<Row> getRowList() {
		return rowList;
	}

	public Map<Integer, String> getAvailBoxMap(int throwNumber, String announcement) {
		Map<Integer, String> availBoxMap = new HashMap<>();
		int index = 1;
		if (announcement.isEmpty()) {
			for (Row row : rowList) {
				if (throwNumber > 1) {
					if (row.getRowType() == RowType.ANNOUNCE) continue;
				}
				for (Box box : row.getBoxList()) {
					if (box.isAvailable()) {
						availBoxMap.put(index, row.getRowType() + " " + box.getBoxType());
						index++;
					}
				}
			}
		} else if (announcement.equals("ANNOUNCE")) {
			for (Box box : getRow(RowType.ANNOUNCE).getBoxList()) {
				if (box.isAvailable()) {
					availBoxMap.put(index, RowType.ANNOUNCE + " " + box.getBoxType());
					index++;
				}
			}
		} else {
			availBoxMap.put(1, RowType.ANNOUNCE + " " + getRow(RowType.ANNOUNCE).getBox(BoxType.valueOf(announcement.split(" ")[1])).getBoxType());
		}
		return availBoxMap;

	}

}
