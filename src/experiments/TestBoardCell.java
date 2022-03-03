package experiments;

import java.util.*;

public class TestBoardCell {
	private int row, col;
	private boolean isRoom, isOccupied;
	
	private Set<TestBoardCell> adjList;
	static TestBoard board = new TestBoard();
	
	
	public TestBoardCell(int row, int column) {
		this.row = row;
		col = column;
		adjList = new HashSet<TestBoardCell>();
	}
	
	public void addAdjacency(TestBoardCell cell) {
		adjList.add(cell);
	}
	
	public Set<TestBoardCell> getAdjList() {
		return adjList;
	}
	
	public void setRoom(boolean inRoom) {
		isRoom = inRoom;
	}

	public boolean isRoom() {
		return isRoom;
	}
	
	public void setOccupied(boolean occupiedCell) {
		isOccupied = occupiedCell;
	}
	
	public boolean getOccupied() {
		return isOccupied;
	}
	
	
	
}
