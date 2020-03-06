package matej.jamb.paper;

import java.util.ArrayList;
import java.util.List;

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
		}
		return score;
	}
	
	public String toString() {
		String string = "";
		for (int i = 0; i < 13; i++) {
			for (Row row : rowList) {
				string += ("|" + row.getBoxList().get(i).getValue() + "| ");
			}
			if (i == 5 || i == 7 || i == 12) {
				string += ("\n---------------");
			}
		}
		return string;
	}
	
}
