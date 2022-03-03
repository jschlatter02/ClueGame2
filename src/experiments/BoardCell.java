package experiments;

import java.util.*;

public class BoardCell {
	private int row, col;
	private boolean isRoom, isOccupied;
	
	private Set<BoardCell> adjList;
	static TestBoard board = new TestBoard();
	
	
	public BoardCell(int row, int column) {
		this.row = row;
		col = column;
		adjList = new HashSet<BoardCell>();
	}
	
	public void addAdjacency(BoardCell cell) {
		adjList.add(cell);
	}
	
	public Set<BoardCell> getAdjList() {
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
