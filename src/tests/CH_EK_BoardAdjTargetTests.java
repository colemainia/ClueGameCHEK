
package tests;

/*
 * This program tests that adjacencies and targets are calculated correctly.
 */

import java.util.Set;

//Doing a static import allows me to write assertEquals rather than
//assertEquals
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class  CH_EK_BoardAdjTargetTests {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance and initialize it		
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("CH_EK/ClueLayout.csv", "CH_EK/Legend.txt");		
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesInsideRooms()
	{
		Set<BoardCell> adjacencies = board.getAdjList(5, 20);
		assertEquals(adjacencies.size(),0);
	}

	// Ensure that the adjacency list from a doorway is only the
	// walkway.
	// These tests are PINK on the planning spreadsheet
	@Test
	public void testAdjacencyRoomExit()
	{
		Set<BoardCell> adjacencies = board.getAdjList(5, 3);
		assertEquals(1, adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(5, 4)));
		
		adjacencies=board.getAdjList(4, 14);
		assertEquals(1,adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(4, 13)));
		
	}
	// Test adjacencies beside rooms, not doorways. Marked DARK BLUE
	@Test 
	public void testAdjecencyBesideRoom() {
		Set<BoardCell> adjacencies= board.getAdjList(8, 15);
		assertEquals(3,adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(7, 15)));
		assertTrue(adjacencies.contains(board.getCellAt(8, 14)));
		assertTrue(adjacencies.contains(board.getCellAt(9, 15)));
		
		//second test
		adjacencies=board.getAdjList(6,7);
		assertEquals(2,adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(6, 8)));
		assertTrue(adjacencies.contains(board.getCellAt(7, 7)));
		
		
		
	}
	// Test adjacency at entrance to rooms
	// These tests are DARK GREY in planning spreadsheet
	@Test
	public void testAdjacencyDoorways()
	{
		// right door
		Set<BoardCell> adjacencies = board.getAdjList(4, 4);
		assertEquals(4, adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(3, 4)));
		assertTrue(adjacencies.contains(board.getCellAt(5, 4)));
		assertTrue(adjacencies.contains(board.getCellAt(4, 3)));
		assertTrue(adjacencies.contains(board.getCellAt(4, 5)));
		//up door
		adjacencies=board.getAdjList(19,6);
		assertEquals(3,adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(18, 6)));
		assertTrue(adjacencies.contains(board.getCellAt(19, 7)));
		assertTrue(adjacencies.contains(board.getCellAt(20, 6)));
		//down door
		adjacencies=board.getAdjList(6, 11);
		assertEquals(3, adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(6, 10)));
		assertTrue(adjacencies.contains(board.getCellAt(6, 12)));
		assertTrue(adjacencies.contains(board.getCellAt(5, 11)));
		// left door
		adjacencies=board.getAdjList(4, 17);
		assertEquals(3,adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(5, 17)));
		assertTrue(adjacencies.contains(board.getCellAt(3, 17)));
		assertTrue(adjacencies.contains(board.getCellAt(4,18)));
		
		
		
	}

	// Test a variety of walkway scenarios
	// These tests are WHITE on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test adjacencies, walkways only
		
		Set<BoardCell> adjacencies = board.getAdjList(11, 20);
		assertEquals(4, adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(10, 20)));
		assertTrue(adjacencies.contains(board.getCellAt(12, 20)));
		assertTrue(adjacencies.contains(board.getCellAt(11, 19)));
		assertTrue(adjacencies.contains(board.getCellAt(11, 21)));
		
	}
	// Test adjacencies on board edges. On the map, these tests are BROWN.
	@Test
	public void testAdjacencyBoardEdge() {
		//Right edge
		Set<BoardCell> adjacencies= board.getAdjList(3,21);
		assertEquals(2,adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(3, 20)));
		assertTrue(adjacencies.contains(board.getCellAt(2, 21)));
		//Bottom edge
		adjacencies = board.getAdjList(21, 13);
		assertEquals(3, adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(21, 12)));
		assertTrue(adjacencies.contains(board.getCellAt(20, 13)));
		assertTrue(adjacencies.contains(board.getCellAt(21, 14)));
		
		//Top edge
		adjacencies=board.getAdjList(0, 12);
		assertEquals(2,adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(0, 13)));
		assertTrue(adjacencies.contains(board.getCellAt(1, 12)));
		
		//left edge
		adjacencies=board.getAdjList(7, 0);
		assertEquals(2,adjacencies.size());
		assertTrue(adjacencies.contains(board.getCellAt(7, 1)));
		assertTrue(adjacencies.contains(board.getCellAt(8, 0)));
		

		
		
	}
	// TEST TARGETS (n)Step are marked LIGHT BLUE on the sheet. They only go to walkways.
	@Test
	public void testTargetsOneStep() {
		board.calcTargets(10, 17, 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(11, 17)));
		assertTrue(targets.contains(board.getCellAt(9, 17)));
		assertTrue(targets.contains(board.getCellAt(10, 18)));
		assertTrue(targets.contains(board.getCellAt(10, 16)));
	}
	
	// Tests of just walkways, 2 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsTwoSteps() {
		board.calcTargets(6, 13, 2);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(6, 11)));
		assertTrue(targets.contains(board.getCellAt(6, 15)));
		assertTrue(targets.contains(board.getCellAt(5, 12)));
		assertTrue(targets.contains(board.getCellAt(7, 14)));
		assertTrue(targets.contains(board.getCellAt(5, 14)));
		assertTrue(targets.contains(board.getCellAt(4, 13)));
	}
	
	// Tests of just walkways, 4 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsFourSteps() {
		board.calcTargets(14, 12, 4);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCellAt(18, 12)));
		assertTrue(targets.contains(board.getCellAt(16, 12)));
		assertTrue(targets.contains(board.getCellAt(12, 14)));
		assertTrue(targets.contains(board.getCellAt(15, 9)));
		assertTrue(targets.contains(board.getCellAt(15, 13)));
		assertTrue(targets.contains(board.getCellAt(15, 11)));
		assertTrue(targets.contains(board.getCellAt(14, 10)));
		assertTrue(targets.contains(board.getCellAt(14, 8)));
		assertTrue(targets.contains(board.getCellAt(17, 13)));
		assertTrue(targets.contains(board.getCellAt(16, 14)));
		assertTrue(targets.contains(board.getCellAt(14, 14)));
	}	
	
	// Tests of just walkways, 6 steps
	// These are LIGHT BLUE on the planning spreadsheet

	@Test
	public void testTargetsSixSteps() {
		board.calcTargets(16, 0, 6);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 2)));
		assertTrue(targets.contains(board.getCellAt(15, 5)));
		assertTrue(targets.contains(board.getCellAt(16, 6)));
		assertTrue(targets.contains(board.getCellAt(14, 4)));
		assertTrue(targets.contains(board.getCellAt(16, 4)));
		assertTrue(targets.contains(board.getCellAt(15, 1)));
		assertTrue(targets.contains(board.getCellAt(15, 3)));
	}	
	
	// Test getting into a room
	// These are PURPLE on the planning spreadsheet
	@Test 
	public void testTargetsIntoRoom()
	{
		board.calcTargets(19, 14, 2);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(5,targets.size());
		assertTrue(targets.contains(board.getCellAt(21, 14)));
		assertTrue(targets.contains(board.getCellAt(20, 13)));
		assertTrue(targets.contains(board.getCellAt(19, 12)));
		assertTrue(targets.contains(board.getCellAt(18, 13)));
		assertTrue(targets.contains(board.getCellAt(17, 14)));
		
		board.calcTargets(13, 20, 2);
		targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCellAt(11, 20)));
		assertTrue(targets.contains(board.getCellAt(12, 19)));
		assertTrue(targets.contains(board.getCellAt(13, 18)));
		assertTrue(targets.contains(board.getCellAt(14, 20)));
		assertTrue(targets.contains(board.getCellAt(14, 21)));
		assertTrue(targets.contains(board.getCellAt(14, 19)));
		assertTrue(targets.contains(board.getCellAt(12, 21)));
		
		
	}
	

	// Test getting out of a room
	// These are RED on the planning spreadsheet
	@Test
	public void testRoomExit()
	{
		board.calcTargets(18, 17, 4);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCellAt(17, 18)));
		assertTrue(targets.contains(board.getCellAt(16, 17)));
		assertTrue(targets.contains(board.getCellAt(18, 19)));
		assertTrue(targets.contains(board.getCellAt(15, 18)));
		assertTrue(targets.contains(board.getCellAt(19, 18)));
		assertTrue(targets.contains(board.getCellAt(21, 18)));
		assertTrue(targets.contains(board.getCellAt(19, 17)));
		assertTrue(targets.contains(board.getCellAt(16, 19)));
		assertTrue(targets.contains(board.getCellAt(20, 19)));
		
		board.calcTargets(12, 17, 3);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(10, 18)));
		assertTrue(targets.contains(board.getCellAt(12, 18)));
		assertTrue(targets.contains(board.getCellAt(11, 19)));
		assertTrue(targets.contains(board.getCellAt(10, 16)));
		assertTrue(targets.contains(board.getCellAt(9, 17)));
		assertTrue(targets.contains(board.getCellAt(11, 15)));
		
	}

}
