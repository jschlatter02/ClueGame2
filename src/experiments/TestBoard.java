package experiments;

import java.util.*;

public class TestBoard {
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	
	final static int COLS = 4;
	final static int ROWS = 4;
	
	public TestBoard () {
		grid = new TestBoardCell[ROWS][COLS];
		targets = new HashSet<TestBoardCell>();
		visited = new HashSet<TestBoardCell>();
		
		for(int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				grid[i][j] = new TestBoardCell(i, j);  //initialize each grid piece
			}
		}
		
		for(int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
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
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		//visited and targets already initialized in the constructor
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}
	
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
	
	public void findAllTargets(TestBoardCell startCell, int pathLength) {
		Set<TestBoardCell> adjList = startCell.getAdjList();
		for(TestBoardCell adjCell : adjList) {
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
	
	
	public TestBoardCell getCell(int row, int column) {
		return grid[row][column];
	}
	
	public static void main(String[] args) {
		TestBoard testBoard = new TestBoard();
	}
}






