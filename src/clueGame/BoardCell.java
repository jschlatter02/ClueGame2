package clueGame;

import java.util.*;


public class BoardCell {
	private int row;
	private int col;
	private char initial;
	private boolean isRoom, isOccupied;
	private DoorDirection doorDirection;
	private boolean roomLabel, doorway;
	private boolean roomCenter;
	private char secretPassage;
	private Set<BoardCell> adjList;
	
	public BoardCell(int row, int column) {
		this.row = row;
		col = column;
		adjList = new HashSet<BoardCell>();
	}
	
	public void setInitial(char initial) {
		this.initial = initial;
	}
	
	public void addAdjacency(BoardCell cell) {
		adjList.add(cell);
	}
	
	public void setBoardCells(String roomSymbol, Map<Character,Room> roomMap) {
		initial = roomSymbol.charAt(0);
		if (roomSymbol.length() == 2) {
			switch(roomSymbol.charAt(1)) {
			case '#':
				roomLabel = true;
				roomMap.get(roomSymbol.charAt(0)).setLabelCell(this);
				break;
			case '*':
				roomCenter = true;
				roomMap.get(roomSymbol.charAt(0)).setCenterCell(this);
				break;
			case '^':
				doorDirection = DoorDirection.UP;
				isRoom = true;
				doorway = true;
				break;
			case '<':
				doorDirection = DoorDirection.LEFT;
				isRoom = true;
				doorway = true;
				break;
			case '>':
				doorDirection = DoorDirection.RIGHT;
				isRoom = true;
				doorway = true;
				break;
			case 'v':
				doorDirection = DoorDirection.DOWN;
				isRoom = true;
				doorway = true;
				break;
			default:
				secretPassage = roomSymbol.charAt(1);
				break;
			}
		}
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
	
	public char getInitial() {
		return initial;
	}

	public boolean isDoorway() {
		return doorway;
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
