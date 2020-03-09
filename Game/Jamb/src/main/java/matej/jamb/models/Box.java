package matej.jamb.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import matej.jamb.models.enums.BoxType;
import matej.jamb.network.JDBC;

@Entity
public class Box {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private int rowId;
	@Column
	private int value;
	@Column
	private BoxType boxType;
	@Column
	private boolean available;
	@Column
	private boolean written;
	@Column
	private int boxNumber;

	private static int idCounter = 1;

	public Box() {}
	
	public Box(BoxType boxType, int rowId, int boxNumber) {
		this.id = idCounter++;
		this.boxType = boxType;
		this.rowId = rowId;
		this.boxNumber = boxNumber;
		this.value = 0;
		this.available = false;
		this.written = false;
		JDBC.executeStatement("INSERT INTO box (row_id, value, box_type, available, written, box_number) VALUES (" + rowId + ", " + value + ", " + boxType.ordinal() + ", " + available + ", " + written + ", " + boxNumber + ")");
	}

	public BoxType getBoxType() {
		return boxType;
	}

	public void setBoxType(BoxType boxType) {
		this.boxType = boxType;
	}


	public int getId() {
		return id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		setAvailable(false);
		setWritten(true);
		JDBC.executeStatement("UPDATE box SET value = " + value + " WHERE id = " + id);
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
		JDBC.executeStatement("UPDATE box SET available = " + available + " WHERE id = " + id);

	}

	public boolean isWritten() {
		return written;
	}
	
	public void setWritten(boolean written) {
		this.written = written;
		JDBC.executeStatement("UPDATE box SET written = " + written + " WHERE id = " + id);
	}

	public int getBoxNumber() {
		return boxNumber;
	}

	public int getRowId() {
		return rowId;
	}
}
