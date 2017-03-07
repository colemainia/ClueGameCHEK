package tests;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

public class CH_EK_FileInitTests {
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 22	;
	public static final int NUM_COLUMNS = 22;
	// NOTE: I made Board static because I only want to set it up one 
	// time (using @BeforeClass), no need to do setup before each test.
	private static Board board;
	@BeforeClass
	public static void setUp() {
		board = board.getInstance();
		board.setConfigFiles("CH_EK/ClueLayout.csv", "CH_EK/legend.txt");
		board.initialize();
	}
	@Test
	public void testRoom() {
		// Get the map of initial => room 
		Map<Character, String> legend = board.getLegend();
		// Ensure we read public the correct number of rooms
		assertEquals(LEGEND_SIZE, legend.size());
		// To ensure data is correctly loaded, test retrieving a few rooms 
		// from the hash, including the first and last in the file and a few others
		assertEquals("Library", legend.get('L'));
		assertEquals("Dining", legend.get('D'));
		assertEquals("Smoking", legend.get('S'));
		assertEquals("Breakroom", legend.get('B'));
		assertEquals("Patio", legend.get('P'));
		assertEquals("Living Room", legend.get('I'));
		assertEquals("Garden", legend.get('G'));
		assertEquals("Garage", legend.get('A'));
		assertEquals("Hall", legend.get('H'));
		assertEquals("Closet", legend.get('C'));
		assertEquals("Walkway", legend.get('W'));
		
	}
	
	
	
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}

	@Test
	public void FourDoorDirections(){
		BoardCell room=board.getCellAt(14, 20);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		
		room = board.getCellAt(18, 17);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		
		room = board.getCellAt(4, 3);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		
		room = board.getCellAt(14, 5);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		// Test that room pieces that aren't doors know it
		room = board.getCellAt(21, 9);
		assertFalse(room.isDoorway());	
		// Test that walkways are not doors
		BoardCell cell = board.getCellAt(9, 14);
		assertFalse(cell.isDoorway());		

		
	}
	
	@Test
	public void testNumberOfDoorways() 
	{
		int numDoors = 0;
		for (int row=0; row<board.getNumRows(); row++)
			for (int col=0; col<board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		assertEquals(18, numDoors);
	}
	
	// Test a few room cells to ensure the room initial is correct.
		@Test
		public void testRoomInitials() {
			// Test first cell in room
			assertEquals('L', board.getCellAt(6, 2).getInitial());
			assertEquals('D', board.getCellAt(3, 10).getInitial());
			assertEquals('S', board.getCellAt(2, 15).getInitial());
			// Test last cell in room
			assertEquals('H', board.getCellAt(11, 2).getInitial());
			assertEquals('A', board.getCellAt(20, 2).getInitial());
			// Test a walkway
			assertEquals('W', board.getCellAt(8, 2).getInitial());
			// Test the closet
			assertEquals('C', board.getCellAt(10,10).getInitial());
		}
		

	
	
	
}

