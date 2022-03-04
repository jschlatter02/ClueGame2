package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

class FileInitTest {
	
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 26;
	public static final int NUM_COLUMNS = 25;

	private static Board board;
	
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(15, numDoors);
	}
	
	@Test
	public void testNumberOfRooms() {
		int numCenter = 0;
        for (int row = 0; row < board.getNumRows(); row++)
            for (int col = 0; col < board.getNumColumns(); col++) {
                BoardCell cell = board.getCell(row, col);
                if (cell.isRoomCenter())
                    numCenter++;
            }
        Assert.assertEquals(9, numCenter); 
	}
	
	@Test
	public void testNumberOfLabels() {
		int numLabels = 0;
        for (int row = 0; row < board.getNumRows(); row++)
            for (int col = 0; col < board.getNumColumns(); col++) {
                BoardCell cell = board.getCell(row, col);
                if (cell.isLabel())
                    numLabels++;
            }
        Assert.assertEquals(9, numLabels); 
	}
	
	@Test
	public void testSetupFile() {
		// if this is true then the layout and setup files are loaded.
		BoardCell cell = board.getCell(13, 3);
		Room room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Living Room" ) ;	
		
		cell = board.getCell(24, 1);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Bathroom" ) ;
		assertTrue( room.getCenterCell() == cell );  // tests for center cell of Bathroom.
		assertFalse( cell.isRoomCenter());
		
		cell = board.getCell(0, 13);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Office Room" ) ;	
		assertTrue( cell.isLabel() );			// tests for label of Office Room.
		
		cell = board.getCell(11, 24);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Study Room" ) ;	
		assertTrue( cell.getSecretPassage() == 'K' );	
	}
	
	@Test
	public void testLabels() {
		// Checks few other room names and checks if all the cells are in the grid.
		assertEquals("Unused", board.getRoom('X').getName() );
		assertEquals("Foyer", board.getRoom('F').getName() );
		assertEquals("Walkway", board.getRoom('W').getName() );
		
		BoardCell cell = board.getCell(0, 0);
		Room room = board.getRoom( cell ) ;
		assertEquals( room.getName(), "Unused" );
		
		cell = board.getCell(25, 24);
		room = board.getRoom( cell ) ;
		assertEquals( room.getName(), "Unused" );
		assertFalse( cell.isDoorway() );
	}
	
	// Tests for at least one doorway in each direction.
	@Test
	public void FourDoorDirections() {
		BoardCell cell = board.getCell(3, 10);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		
		cell = board.getCell(3, 16);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		
		cell = board.getCell(14, 6);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		
		cell = board.getCell(16, 8);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
	}

}
