package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	private int numRows;
	private int numColumns;
	private static final Set<String> ACCEPTABLE_SUFFIXES = new HashSet<String>(Arrays.asList("Card", "Other"));
	private static Board theInstance = new Board();
	public static final int MAX_BOARD_SIZE = 100;
	private BoardCell [][] board;
	private BoardCell startingCell;
	private Map<Character,String> legend;
	private Map<BoardCell, Set<BoardCell>> adjMatrix;
	private Set<BoardCell> targets;
	private String boardConfigFile;
	private String roomConfigFile;
	
	public static Board getInstance(){
		return theInstance;
	}
	
	public void initialize() {
		try {
			loadRoomConfig();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			loadBoardConfig();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calcAdjacencies();
		targets = new HashSet<BoardCell>();
	}
	public void setConfigFiles(String boardConfigFile, String roomConfigFile) {
		this.boardConfigFile = "src/data/" + boardConfigFile;
		this.roomConfigFile = "src/data/" + roomConfigFile;
	}
	
	public void loadRoomConfig() throws BadConfigFormatException {
		legend = new HashMap<Character, String>();
		FileReader reader = null;
		try {
			reader = new FileReader(roomConfigFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner in = new Scanner(reader);
		while (in.hasNextLine()) {
			String legendLine = in.nextLine();
			Character letter = new Character(legendLine.charAt(0));
			if (legendLine.substring(3).lastIndexOf(',') == -1) {
				in.close();
				throw new BadConfigFormatException(roomConfigFile);
			}
			if (!ACCEPTABLE_SUFFIXES.contains(legendLine.substring(legendLine.lastIndexOf(',') + 2))) {
				in.close();
				throw new BadConfigFormatException(roomConfigFile);
			}
			String roomName = legendLine.substring(3, legendLine.lastIndexOf(','));
			legend.put(letter, roomName);
		}
		in.close();
	}
	
	public void loadBoardConfig() throws BadConfigFormatException{
		FileReader getDimensions = null;
		try {
			getDimensions = new FileReader(boardConfigFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner in = new Scanner(getDimensions);
		String[] row1 = in.nextLine().split(",");
		numColumns = row1.length;
		numRows = 1;
		while(in.hasNextLine()) {
			in.nextLine();
			numRows++;
		}
		in.close();
		board = new BoardCell[numRows][numColumns];
		FileReader reader = null;
		try {
			reader = new FileReader(boardConfigFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in = new Scanner(reader);
		for (int i = 0; i < numRows; i++) {
			String[] row = in.nextLine().split(",");
			if (row.length != numColumns) {
				in.close();
				throw new BadConfigFormatException(boardConfigFile);
			}
			for (int j = 0; j < numColumns; j++) {
				String cell = row[j];
				char initial = cell.charAt(0);
				DoorDirection doorDirec = DoorDirection.NONE;
				if (cell.length() == 2) {
					switch(cell.charAt(1)) {
					case 'U':
						doorDirec = DoorDirection.UP;
						break;
					case 'D':
						doorDirec = DoorDirection.DOWN;
						break;
					case 'L':
						doorDirec = DoorDirection.LEFT;
						break;
					case 'R':
						doorDirec = DoorDirection.RIGHT;
						break;
					}
				}
				if (!legend.containsKey(initial)) {
					in.close();
					throw new BadConfigFormatException(boardConfigFile);
				}
				board[i][j] = new BoardCell(i, j, initial, doorDirec);
			}
		}
		in.close();
	}
	
	public Map<Character, String> getLegend() {
		return legend;
	}
	public int getNumColumns() {
		return numColumns;
	}
	
	public int getNumRows(){
		return numRows;
	}
	
	public BoardCell getCellAt(int row, int col){
		return board[row][col];
	}
	public void calcAdjacencies(){
		adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				BoardCell cell = board[i][j];
				Set<BoardCell> neighbors = new HashSet<BoardCell>();
				switch(cell.getDoorDirection()) {
				case NONE:
					if (cell.getInitial() != 'W') break;
					if ((i - 1) >= 0) {
						if(board[i - 1][j].getInitial() == 'W' || board[i - 1][j].getDoorDirection() == DoorDirection.DOWN) 
							neighbors.add(board[i-1][j]);
					}
					if ((i + 1) < numRows) {
						if(board[i + 1][j].getInitial() == 'W' || board[i + 1][j].getDoorDirection() == DoorDirection.UP) 
							neighbors.add(board[i+1][j]);
					}
					if ((j - 1) >= 0) {
						if(board[i][j-1].getInitial() == 'W' || board[i][j - 1].getDoorDirection() == DoorDirection.RIGHT) 
							neighbors.add(board[i][j - 1]);
					}
					if ((j + 1) <  numColumns ) {
						if(board[i][j+1].getInitial() == 'W' || board[i][j + 1].getDoorDirection() == DoorDirection.LEFT) 
							neighbors.add(board[i][j + 1]);
					}
					break;
				case UP:
					neighbors.add(board[i-1][j]);
					break;
				case DOWN:
					neighbors.add(board[i+1][j]);
					break;
				case LEFT:
					neighbors.add(board[i][j-1]);
					break;
				case RIGHT:
					neighbors.add(board[i][j+1]);
					break;
				}
				adjMatrix.put(cell, neighbors);
			}
		}
	}
	public Set<BoardCell> getAdjList(int i, int j) {
		return adjMatrix.get(board[i][j]);
	}
	public Set<BoardCell> getTargets() {
		return targets;
	}

	public void calcTargets(int row, int column, int steps) {
		targets.clear();
		Set<BoardCell> visited = new HashSet<BoardCell>();
		startingCell = board[row][column];
		calcTargetsHelper(board[row][column], steps, visited);
	}

	private void calcTargetsHelper(BoardCell cell, int steps, Set<BoardCell> visited) {
		if (steps == 0 || (cell.getDoorDirection() != DoorDirection.NONE && cell != startingCell)) {
			targets.add(cell);
			return;
		}
		visited.add(cell);
		for(BoardCell neighbor : getAdjList(cell.GetRow(), cell.GetColumn())) {
			if (!visited.contains(neighbor)) calcTargetsHelper(neighbor, steps - 1, new HashSet<BoardCell>(visited));
		}
	}
	
}
