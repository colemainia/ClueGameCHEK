package experiment;

import java.util.Set;

public class BoardCell {
	private int row;
	private int column;
	public BoardCell(int row, int column) {
		this.row = row;
		this.column = column;
	}
	public int GetRow() {return row;}
	public int GetColumn() {return column;}
}
