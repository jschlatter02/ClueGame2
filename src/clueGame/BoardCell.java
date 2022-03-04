package clueGame;

import java.util.*;

import experiments.TestBoardCell;

public class BoardCell {
	private int row;
	private int col;
	private char initial;
	private boolean isRoom, isOccupied;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private Set<BoardCell> adjList;
	
	public BoardCell(int row, int column) {
		this.row = row;
		col = column;
		adjList = new HashSet<BoardCell>();
	}
	
	public void addAdjacency(BoardCell cell) {
		adjList.add(cell);
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public boolean isLabel() {
		return roomLabel;
	}
	
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	public boolean isDoorway() {
		return true;
	}
	
	public char getSecretPassage() {
		return secretPassage;
	}

	public Set<BoardCell> getAdjList() {
		return adjList;
	}

	public boolean isRoom() {
		return isRoom;
	}

	public boolean getOccupied() {
		return isOccupied;
	}

	
}
