package clueGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	private boolean inTarget;
	
	final static int ROOM_LOCATION_SIZE = 3;
	
	public BoardCell(int row, int column) {
		setInTarget(false);
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

	public String[] drawCell(Graphics graphics, int width, int height, int horizontalOffset, int topOffset, Map<Character, Room> roomMap) {
		if (initial == 'W') {
			graphics.setColor(Color.YELLOW);
			graphics.fillRect(horizontalOffset, topOffset, width, height);
			graphics.setColor(Color.BLACK);
			graphics.drawRect(horizontalOffset, topOffset, width, height);
			
			Graphics2D graphics2D = (Graphics2D) graphics;
			
			switch(doorDirection) { //draw the blue door line in the cell that has the doorway
			case UP:
				//use graphics2d to make the stroke bigger above the cell
				graphics2D.setColor(Color.BLUE);
				graphics2D.setStroke(new BasicStroke(height / 8));
				graphics2D.drawLine(horizontalOffset, topOffset, horizontalOffset + width, topOffset);
				//need to reset the stroke size so that all the other lines aren't bold as well
				graphics2D.setStroke(new BasicStroke(1));
				break;
			case LEFT:
				graphics2D.setColor(Color.BLUE);
				//stroke size based on width or height to make sure it doesn't take up the whole cell
				//also makes sure that it scales well with the player moving the window
				graphics2D.setStroke(new BasicStroke(width / 7));
				graphics2D.drawLine(horizontalOffset, topOffset, horizontalOffset, topOffset + height);
				graphics2D.setStroke(new BasicStroke(1));
				break;
			case RIGHT:
				graphics2D.setColor(Color.BLUE);
				graphics2D.setStroke(new BasicStroke(width / 5));
				graphics2D.drawLine(horizontalOffset + width, topOffset, horizontalOffset + width, topOffset + height);
				graphics2D.setStroke(new BasicStroke(1));
				break;
			case DOWN:
				graphics2D.setColor(Color.BLUE);
				graphics2D.setStroke(new BasicStroke(height / 5));
				graphics2D.drawLine(horizontalOffset, topOffset + height, horizontalOffset + width, topOffset + height);
				graphics2D.setStroke(new BasicStroke(1));
				break;
			case NONE:
				break;
			}
			return null;
		} else if (initial == 'X') {
			//need a black place to tell players to not go there
			graphics.setColor(Color.BLACK);
			graphics.fillRect(horizontalOffset, topOffset, width, height);
			return null;
		} else {
			graphics.setColor(Color.GRAY);
			graphics.fillRect(horizontalOffset, topOffset, width, height);
			
			if (roomLabel) { 
				String[] roomLabels = new String[ROOM_LOCATION_SIZE];
				//pass in the roomMap map so that we can easily get the room name
				String name = roomMap.get(initial).getName();
				topOffset += height;
				//have an array because otherwise the room name gets written over when drawing the next cell
				roomLabels[0] = name;
				roomLabels[1] = String.valueOf(horizontalOffset);
				roomLabels[2] = String.valueOf(topOffset);
				return roomLabels;
				
			} else if (hasSecretPassage) {
				graphics.setColor(Color.YELLOW);
				graphics.fillRect(horizontalOffset, topOffset, width, height);
				//these adjustments put the "S" in the correct spot inside the square
				horizontalOffset += (width / 3);
				topOffset += (height / 1.5);
				graphics.setFont(new Font("Cambria", Font.BOLD, width/2));
				graphics.setColor(Color.BLUE);
				graphics.drawString("S", horizontalOffset, topOffset);
				return null;
			}
		}
		return null;
	}
	
	public void drawTargets(Graphics graphics, int width, int height) {
		Color cellColor = new Color(51, 153, 255);
		int horizontalOffset = width * col;
		int topOffset = height * row;
		if (initial == 'W') {
			graphics.setColor(cellColor);
			graphics.fillRect(horizontalOffset, topOffset, width, height);
			graphics.setColor(Color.black);
			graphics.drawRect(horizontalOffset, topOffset, width, height);
		} else if (roomCenter) {
			graphics.setColor(cellColor);
			graphics.fillRect(horizontalOffset, topOffset, width, height);
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

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setInTarget(boolean inTarget) {
		this.inTarget = inTarget;
	}


	
	

	
}
