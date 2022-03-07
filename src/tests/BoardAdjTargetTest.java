package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
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
	
	
	@Test
	public void testTargetWalkways() {
		// testing target for walkways when roll 1.
		board.calcTargets(board.getCell(1, 12), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(2, 12)));
		
		// testing target when roll is 3.
		board.calcTargets(board.getCell(1, 12), 3);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(3, 13)));
		assertTrue(targets.contains(board.getCell(4, 12)));
		assertTrue(targets.contains(board.getCell(3, 11)));		
	}
	
	@Test 
	public void testTargetIntoRoom() {
		// testing target for the walkways that goes into the room with roll of 3.
		board.calcTargets(board.getCell(13, 6), 3);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(14, 3)));
		assertTrue(targets.contains(board.getCell(16, 6)));
		assertTrue(targets.contains(board.getCell(14, 6)));
		assertTrue(targets.contains(board.getCell(12, 8)));
		assertTrue(targets.contains(board.getCell(12, 6)));	
	}

	
	@Test
	public void testTargetSecretEntrance() {
		// tests for going out from kitchen to living room through secret passage at roll of 1.
		board.calcTargets(board.getCell(22, 19), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(20, 14)));
		assertTrue(targets.contains(board.getCell(17, 18)));
		assertTrue(targets.contains(board.getCell(14, 3)));
		
		// at roll of 3.
		board.calcTargets(board.getCell(22, 19), 3);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(21, 13)));
		assertTrue(targets.contains(board.getCell(15, 18)));
		assertTrue(targets.contains(board.getCell(14, 3)));
		
	}
	
	@Test
	public void testTargetOutofRoom() {
		// tests out of the MasterBedroom center at roll of 2.
		board.calcTargets(board.getCell(3, 20), 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 16)));
		assertTrue(targets.contains(board.getCell(4, 16)));
		assertTrue(targets.contains(board.getCell(4, 15)));
		
		// tests out of the MasterBedroom center at roll of 4.
		board.calcTargets(board.getCell(3, 20), 4);
		targets= board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(1, 14)));
		assertTrue(targets.contains(board.getCell(0, 16)));
		assertTrue(targets.contains(board.getCell(5, 15)));
	}
	
	@Test
	public void testTargetOccupiedCells() {
		// test a roll of 1 which contains occupied cells.
		board.getCell(13, 7).setOccupied(true);
		board.calcTargets(board.getCell(12, 7), 1);
		board.getCell(13, 7).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(12, 6)));
		assertTrue(targets.contains(board.getCell(12, 8)));
		assertTrue(targets.contains(board.getCell(11, 7)));	
		assertFalse( targets.contains( board.getCell(13, 7)));
		
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(3, 3).setOccupied(true);
		board.calcTargets(board.getCell(8, 3), 1);
		board.getCell(3, 3).setOccupied(false);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(8, 4)));	
		assertTrue(targets.contains(board.getCell(9, 3)));	
		assertTrue(targets.contains(board.getCell(8, 2)));	
		assertTrue(targets.contains(board.getCell(3, 3)));	
	}
}
