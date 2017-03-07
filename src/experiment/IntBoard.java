package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IntBoard { 
	public IntBoard(BoardCell[][] grid) { 
		this.grid = grid;
		calcAdjacencies();
		targets = new HashSet<BoardCell>();
	}
	private void calcAdjacencies() {  // calculated adjacent cells for each cell
		adjMtx = new HashMap<BoardCell, Set<BoardCell>>();
		for(int i=0; i<grid.length; i++){
			for (int j=0; j<grid[i].length; j++){
				Set<BoardCell> adjacencies = new HashSet<BoardCell>();
				// Test each "neighbor" to see if it is valid before adding
				if (i + 1 < grid.length) adjacencies.add(grid[i + 1][j]);
				if (!((i-1) < 0)) adjacencies.add(grid[i - 1][j]);
				if (j+1 <grid[i].length) adjacencies.add(grid[i][j+1]);
				if (!((j-1)<0)) adjacencies.add(grid[i][j-1]);
				adjMtx.put(grid[i][j], adjacencies);
			}
		}
	}
	private void calcTargets(BoardCell cell, int distance) {
		// Initialize sets Targets and Visited
		targets.clear();
		Set<BoardCell> visited = new HashSet<BoardCell>();
		// Call the recursive helper
		calcTargetsHelper(cell, distance, visited);
	}
	private void calcTargetsHelper(BoardCell cell, int distance, Set<BoardCell> visited) {
		// base case
		if (distance == 0) {
			targets.add(cell);
			return;
		}
		// mark current location as visited
		visited.add(cell);
		for (BoardCell neighbor : getAdjList(cell)) {
			// for every neighbor that has not been visited
			if (!visited.contains(neighbor)) {
				// clone visited
				Set<BoardCell> nextVisited = new HashSet<BoardCell>(visited);
				// make new key with less distance
				// recursive call
				calcTargetsHelper(neighbor, distance - 1, nextVisited);
			}
		}
	}
	public Set<BoardCell> getAdjList(BoardCell start) {return adjMtx.get(start);}
	public Set<BoardCell> getTargets(BoardCell cell, int distance) {
		calcTargets(cell, distance);
		return targets;
	}
	public BoardCell getCell(int row, int col) {return grid[row][col];}
	private Map<BoardCell, Set<BoardCell>> adjMtx;
	private Set<BoardCell> targets;
	private BoardCell[][] grid;
}
