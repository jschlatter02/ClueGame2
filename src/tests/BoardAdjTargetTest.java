package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class BoardAdjTargetTest {
	
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout306.csv", "ClueSetup306.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	@Test
	public void testAdjacencyWalkways() {
		//surrounded by walkways
		Set<BoardCell> testList = board.getAdjList(16, 16);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(16, 15)));
		assertTrue(testList.contains(board.getCell(16, 17)));
		assertTrue(testList.contains(board.getCell(15, 16)));
		assertTrue(testList.contains(board.getCell(17, 16)));
		
		//walkway at edge
		testList = board.getAdjList(15, 24);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(16, 24)));
		assertTrue(testList.contains(board.getCell(15, 23)));
		
		//walkway by Bathroom
		testList = board.getAdjList(22, 2);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(22, 1)));
		assertTrue(testList.contains(board.getCell(22, 3)));
		assertTrue(testList.contains(board.getCell(21, 2)));
		
		//top of board, one adjacent piece
		testList = board.getAdjList(0, 7);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(1, 7)));
	}
	
	@Test
	public void testAdjacencyRooms() {
		//room S with secret passage to G
		Set<BoardCell> testList = board.getAdjList(12, 20);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(12, 16)));
		assertTrue(testList.contains(board.getCell(3, 3)));
		
		//test Foyer - 3 doors right next to each other
		testList = board.getAdjList(21, 9);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(16, 8)));
		assertTrue(testList.contains(board.getCell(16, 9)));
		assertTrue(testList.contains(board.getCell(16, 10)));
		
		//random cell in the Kitchen
		testList = board.getAdjList(18, 20);
		assertEquals(0, testList.size());
	}
	
	@Test
	public void testAdjacencyDoors() {
		//testing one of the Foyer doors since there are 3 next to each other
		Set<BoardCell> testList = board.getAdjList(16, 9);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(16, 8)));
		assertTrue(testList.contains(board.getCell(16, 10)));
		assertTrue(testList.contains(board.getCell(15, 9)));
		assertTrue(testList.contains(board.getCell(21, 9)));
	}

}
