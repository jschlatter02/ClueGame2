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
				grid[i][j] = new TestBoardCell(i, j);
			}
		}
		
		for(int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
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
		
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}
	
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
	
	public void findAllTargets(TestBoardCell startCell, int pathLength) {
		for(TestBoardCell adjCell : startCell.getAdjList()) {
			if(!visited.contains(adjCell)) {
				visited.add(adjCell);
				if(adjCell.isRoom()) {
					targets.add(adjCell);
				}
				else {
					if(pathLength == 1) {
						if(!adjCell.getOccupied()) {
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






