package matej.jamb.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import matej.jamb.models.enums.BoxType;
import matej.jamb.models.enums.RowType;
import matej.jamb.network.JDBC;

@Entity
public class Paper {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	@Column
    private int playerId;
	@Transient
	private List<Row> rowList;
	
	@Column
	private int moveNum;

	private static int idCounter = 1;
	
	public Paper() {}
	
	public Paper(int playerId) {
		this.id = idCounter++;
		this.rowList = new ArrayList<>();
		this.playerId = playerId;
		this.moveNum = 0;
		rowList.add(new Row(RowType.DOWNWARD, id));
		rowList.add(new Row(RowType.UPWARD, id));
		rowList.add(new Row(RowType.ANYDIR, id));
		rowList.add(new Row(RowType.ANNOUNCE, id));
		JDBC.executeStatement("INSERT INTO paper (player_id, move_num) VALUES (" + playerId + ", 0)");
		JDBC.executeStatement("INSERT INTO option (paper_id, option, move_num) VALUES (" + id + ", 0, 0)");
	}
	
	public int getId() {
		return id;
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

	public int getmoveNum() {
		JDBC.executeStatement("UPDATE paper SET move_num = move_num + 1 WHERE id = " + id);
		moveNum = moveNum + 1;
		return moveNum;
	}

}
