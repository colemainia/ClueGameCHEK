package clueGame;

import java.util.Set;

public class BoardCell {
	private DoorDirection direction;
	private int row;
	private int column;
	private char initial;
	public BoardCell(int row, int column, char initial, DoorDirection direction) {
		this.row = row;
		this.column = column;
		this.direction = direction;
		this.initial = initial;
	}
	public char getInitial(){
		return initial;
	}
	public boolean isWalkway() {return true;}
	public boolean isRoom() {return true;}
	public boolean isDoorway() {return (!(direction == DoorDirection.NONE));}
	public DoorDirection getDoorDirection() {return direction;}
	public int GetRow() {return row;}
	public int GetColumn() {return column;}
}
