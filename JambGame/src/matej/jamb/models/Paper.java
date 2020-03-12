package matej.jamb.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import matej.jamb.models.enums.BoxType;
import matej.jamb.models.enums.RowType;

public class Paper {


	private List<Row> rowList;
	private List<String> rowStringList;
	
	public Paper() {
		this.rowList = new ArrayList<>();
		this.rowStringList = new ArrayList<>();
		rowList.add(new Row(RowType.DOWNWARD));
		rowList.add(new Row(RowType.UPWARD));
		rowList.add(new Row(RowType.ANYDIR));
		rowList.add(new Row(RowType.ANNOUNCE));
		
		rowStringList.add(new String("Σ<br/>1-6"));
		rowStringList.add(new String("MAX"));
		rowStringList.add(new String("MIN"));
		rowStringList.add(new String("DIFFx1"));
		rowStringList.add(new String("TRIS"));
		rowStringList.add(new String("SKALA"));
		rowStringList.add(new String("FULL"));
		rowStringList.add(new String("POKER"));
		rowStringList.add(new String("JAMB"));
		rowStringList.add(new String("Σ<br/>11-15"));
		
	}

	public List<Row> getRowList() {
		return rowList;
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
		}
		return score;
	}

	public String toString() {
		String dashes = "\n-------------------";
		String string = dashes + 
						"\n |    A    |A    a" + 
						"\n V    |    V|    n"+
						dashes;
		int value;
		for (int i = 0; i < 13; i++) {
			string += "\n";
			for (Row row : rowList) {
				if (!row.getBoxList().get(i).isWritten()) string += "|--| ";
				else {
					value = row.getBoxList().get(i).getValue();
					string += (value >= 0 && value <= 9 ? "| " : "|") + value + "| ";
				}
			}

			if (i == 5 || i == 7 || i == 12) {
				string += dashes + "\n";
				value = 0;
				for (Row row : rowList) {
					if (i == 5) {
						value = row.getUpperScore();
					}  else if (i == 7) {
						value = row.getMiddleScore();
					} else if (i == 12) {
						value = row.getLowerScore();
					}
					string += (value >= 0 && value <= 9 ? "| " : "|") + value + "| ";
				}
				string += dashes;
			}


		}
		return string + "\nScore: " + getScore() + "\n";
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

	public List<String> getRowStringList() {
		return rowStringList;
	}
}
