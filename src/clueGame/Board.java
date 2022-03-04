package clueGame;

import java.io.FileNotFoundException;
import java.util.*;

import experiments.TestBoardCell;



public class Board {
	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	
	private int numRows = 26;
	private int numColumns = 26;
	
	private String layoutConfigFile;
	private String setupConfigFile;
	
	private static Board theInstance = new Board();
	
	private Map<Character, Room> roomMap;

	// constructor is private to ensure only one can be created
	private Board() {
		super() ;
	}
	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}
	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize(){
		grid = new BoardCell[numRows][numColumns];
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		
		
		for(int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				grid[i][j] = new BoardCell(i, j);  //initialize each grid piece
			}
		}
		
		for(int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				//check to see if the four blocks around the current cell are valid
				//if so, add them to their respective adjacency list
				if((row - 1) >= 0) {  
					grid[row][col].addAdjacency(grid[row - 1][col]);
				}
				if((row + 1) <= 3) {
					grid[row][col].addAdjacency(grid[row + 1][col]);
				}
				if((col - 1) >= 0) {
					grid[row][col].addAdjacency(grid[row][col - 1]);
				}
				if((col + 1) <= 3) {
					grid[row][col].addAdjacency(grid[row][col + 1]);
				}
			}
		}
	}
	
	public void calcTargets(BoardCell startCell, int pathLength) {
		//visited and targets already initialized in the constructor
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}
	
	public void findAllTargets(BoardCell startCell, int pathLength) {
		Set<BoardCell> adjList = startCell.getAdjList();
		for(BoardCell adjCell : adjList) {
			if(!visited.contains(adjCell)) {   //only want unused adjCells
				visited.add(adjCell);
				if(adjCell.isRoom()) {      //get rid of all movement - hence the if-else on this
					targets.add(adjCell);
				}
				else {  //don't want lower part of code to run if the adjCell is a room
					if(pathLength == 1) {
						if(!adjCell.getOccupied()) {  //only add if not occupied
							targets.add(adjCell);
						}
					} 
					else {
						findAllTargets(adjCell, pathLength - 1);
					}
				}
				visited.remove(adjCell); 
			}
			
		}
	}
	
	public BoardCell getCell(int row, int column) {
		return grid[row][column];
	}
	
	public void loadSetupConfig() {}
	
	public void loadLayoutConfig() {}
	
	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = layoutConfigFile;
		this.setupConfigFile = setupConfigFile;
	}
	

	public Room getRoom(char c) {
		return new Room();
	}
	
	public Room getRoom(BoardCell cell) {
		return new Room();
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	
}
