package matej.jamb.paper;

import java.util.ArrayList;
import java.util.List;

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
			if (row.getType() == type) {
				return row;
			}
		}
		return null;
	}
	
	public boolean isPaperFinished() {
		for (Row row : rowList) {
			if (!row.isRowFinished()) return false;
		}
		return true;
	}

	public int izracunajRezultat() {
		int score = 0;
		for (Row row : rowList) {
			score += row.getScore();
		}
		return score;
	}
	
	public void ispisi() {
		for (int i = 0; i < 13; i++) {
			System.out.println("|" + downward.zapis[i] + "| |" + upward.zapis[i] + "| |" + downwardUpward.zapis[i] + "| |" + announce.zapis[i] + "|");
			if (i == 5 || i == 7 || i == 12) {
				System.out.println("---------------");
			}
		}
	}
	
}
