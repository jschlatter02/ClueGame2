package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;


public class BoardCell {
	private int row;
	private int col;
	private char initial;
	
	private boolean isRoom, isOccupied, hasSecretPassage;
	private DoorDirection doorDirection;
	private boolean roomLabel, doorway;
	private boolean roomCenter;
	private char secretPassage;
	private Set<BoardCell> adjList;
	
	public BoardCell(int row, int column) {
		this.row = row;
		col = column;
		doorDirection = DoorDirection.NONE;
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
			switch(roomSymbol.charAt(1)) { //check second character
			case '#': //label cell
				roomLabel = true;
				roomMap.get(roomSymbol.charAt(0)).setLabelCell(this);
				break;
			case '*': //center cell
				roomCenter = true;
				roomMap.get(roomSymbol.charAt(0)).setCenterCell(this);
				isRoom = true;
				break;
			case '^': //door directions
				doorDirection = DoorDirection.UP;
				doorway = true;
				break;
			case '<':
				doorDirection = DoorDirection.LEFT;
				doorway = true;
				break;
			case '>':
				doorDirection = DoorDirection.RIGHT;
				doorway = true;
				break;
			case 'v':
				doorDirection = DoorDirection.DOWN;
				doorway = true;
				break;
			default: //secret passage
				secretPassage = roomSymbol.charAt(1);
				hasSecretPassage = true;
				break;
			}
		}
	}

	public void drawCell(Graphics graphics, int width, int height, int horizontalOffset, int topOffset, Map<Character, Room> roomMap) {
		if (initial == 'W') {
			graphics.setColor(Color.YELLOW);
			graphics.fillRect(horizontalOffset, topOffset, width, height);
			graphics.setColor(Color.BLACK);
			graphics.drawRect(horizontalOffset, topOffset, width, height);
			switch(doorDirection) { //draw the blue door line in the cell that has the doorway
			case UP:
				graphics.setColor(Color.WHITE);
				graphics.drawLine(0, 0, width, 0);
				break;
			case LEFT:
				graphics.setColor(Color.WHITE);
				graphics.drawLine(0, 0, 0, height);
				break;
			case RIGHT:
				graphics.setColor(Color.WHITE);
				graphics.drawLine(width, 0, width, height);
				break;
			case DOWN:
				graphics.setColor(Color.WHITE);
				graphics.drawLine(0, height, width, height);
				break;
			case NONE:
				break;
			}
		} else if (initial == 'X') {
			graphics.setColor(Color.BLACK);
			graphics.fillRect(horizontalOffset, topOffset, width, height);
		} else {
			graphics.setColor(Color.GRAY);
			graphics.fillRect(horizontalOffset, topOffset, width, height);
			if (roomLabel) { 
				//pass in the roomMap map so that we can easily get the room name
				String name = roomMap.get(initial).getName();
				graphics.setColor(Color.BLUE);
				topOffset += height;
				graphics.drawString(name, horizontalOffset, topOffset);
			} else if (hasSecretPassage) {
				graphics.setColor(Color.YELLOW);
				graphics.fillRect(horizontalOffset, topOffset, width, height);
				//these adjustments put the "S" in the correct spot inside the square
				horizontalOffset += (width / 3);
				topOffset += (height / 1.5);
				graphics.setColor(Color.BLUE);
				graphics.drawString("S", horizontalOffset, topOffset);
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

	public void setOccupied(boolean occupied) {
		if (!roomCenter) {
			isOccupied = occupied;
		}
	}

	public boolean isSecretPassage() {
		return hasSecretPassage;
	}

	
	

	
}
