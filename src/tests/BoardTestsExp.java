package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiments.TestBoardCell;
import experiments.TestBoard;

class BoardTestsExp {
	TestBoard board;

	@BeforeEach
	public void setUp() {
		board = new TestBoard();
	}


	@Test
	public void testAdjacency1() {
		// testing for top left corner [0][0].

		TestBoardCell cell = board.getCell(0,0);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1,0)));
		assertTrue(testList.contains(board.getCell(0,1)));
		assertEquals(2, testList.size());
	}

	@Test
	public void testAdjancency2() {
		// testing for bottom corner [3][3].

		TestBoardCell cell = board.getCell(3,3);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(3,2)));
		assertTrue(testList.contains(board.getCell(2,3)));
		assertEquals(2, testList.size());
	}

	@Test
	public void testAdjancency3() {
		// testing for right edge [1][3].

		TestBoardCell cell = board.getCell(1,3);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(0,3)));
		assertTrue(testList.contains(board.getCell(1,2)));
		assertTrue(testList.contains(board.getCell(2,3)));
		assertEquals(3, testList.size());
	}

	@Test
	public void testAdjancency4() {

		// testing for middle cell [2][2].

		TestBoardCell cell = board.getCell(2,2);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1,2)));
		assertTrue(testList.contains(board.getCell(2,3)));
		assertTrue(testList.contains(board.getCell(3,2)));
		assertTrue(testList.contains(board.getCell(2,1)));
		assertEquals(4, testList.size());
	}
	
	
	@Test
	public void testTargetsRoll2() {
		//tests rolls of length 2 when the board is empty
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 2);
		Set <TestBoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(1,1)));
		assertTrue(targets.contains(board.getCell(0,2)));
		assertTrue(targets.contains(board.getCell(2,0)));
	}
	
	
	@Test
	public void testTargetsRoom() {
		//tests when there is a room at (1,1)
		board.getCell(1, 1).setRoom(true);
		TestBoardCell cell = board.getCell(2, 0);
		board.calcTargets(cell, 3);   //roll of 3 must get rid of all movement if entering a room
		Set <TestBoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(1,1)));
		assertTrue(targets.contains(board.getCell(1,2)));
		assertTrue(targets.contains(board.getCell(2,3)));
		assertTrue(targets.contains(board.getCell(3,0)));
		assertTrue(targets.contains(board.getCell(3,2)));
		assertTrue(targets.contains(board.getCell(2,1)));
		assertTrue(targets.contains(board.getCell(0,1)));
	}
	
	@Test
	public void testTargetsOccupied() {
		//tests when there is a player at (2,2).
		board.getCell(3, 1).setOccupied(true);
		TestBoardCell cell = board.getCell(2, 2);
		board.calcTargets(cell, 2);   //should not allow player to go to (3,1)
		Set <TestBoardCell> targets = board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(3,3)));
		assertTrue(targets.contains(board.getCell(1,3)));
		assertTrue(targets.contains(board.getCell(2,0)));
		assertTrue(targets.contains(board.getCell(1,1)));
		assertTrue(targets.contains(board.getCell(0,2)));

	}
	
	@Test
	public void testTargetsRoomAndOccupied() {
		// tests when there is a player at (1,1).
		board.getCell(0, 1).setRoom(true);
		board.getCell(2, 0).setOccupied(true);
		TestBoardCell cell = board.getCell(1, 1);
		board.calcTargets(cell, 2);   //should not allow player to go to (2,0) and get rid of the remaining movement if entering the room.
		Set <TestBoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(0,0)));
		assertTrue(targets.contains(board.getCell(0,1)));
		assertTrue(targets.contains(board.getCell(0,2)));
		assertTrue(targets.contains(board.getCell(1,3)));
		assertTrue(targets.contains(board.getCell(2,2)));
		assertTrue(targets.contains(board.getCell(3,1)));
	}
	
	@Test
	public void testTargetsForcedToRoom() {
		// tests when there is a player at (3,0).
		board.getCell(2, 0).setRoom(true);
		board.getCell(3, 1).setOccupied(true);
		TestBoardCell cell = board.getCell(3,0);
		board.calcTargets(cell, 1);   //should not allow player to go to (3,1) and get rid of the remaining movement if entering the room.
		Set <TestBoardCell> targets = board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(2,0)));
	}


}
