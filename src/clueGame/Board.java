package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import experiments.TestBoardCell;

public class Board {
	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private static Board theInstance = new Board();
	private Map<Character, Room> roomMap;
	private FileReader reader = null;
	private Scanner setupScanner = null;


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
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();

		try {
			loadSetupConfig();
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}

		try {
			loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}

		createAdjacencyList();
	}

	
	// creates the adjacency list.
	private void createAdjacencyList() {
		for(int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell thisCell = grid[row][col];
				if((row - 1) >= 0) {
					addAdjList(thisCell, grid[row - 1][col]);
				}
				if((row + 1) <= numRows - 1) {
					addAdjList(thisCell, grid[row + 1][col]);
				}
				if((col - 1) >= 0) {
					addAdjList(thisCell, grid[row][col - 1]);
				}
				if((col + 1) <= numColumns - 1) {
					addAdjList(thisCell, grid[row][col + 1]);
				}
			}
		}
	}

	private void addAdjList(BoardCell thisCell, BoardCell adjCell) {
		if (thisCell.isDoorway()) {
			char initial = adjCell.getInitial();
			if (initial != 'W' && initial != 'X') {
				BoardCell centerCell = roomMap.get(initial).getCenterCell();
				thisCell.addAdjacency(centerCell);
				centerCell.addAdjacency(thisCell);
			} else if (initial == 'W') {
				thisCell.addAdjacency(adjCell);
			}
		} else if (thisCell.getInitial() == 'W') {
			
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

	public void loadSetupConfig() throws BadConfigFormatException{
		roomMap = new HashMap<Character, Room>();
		setupConfigFile = "data/" + setupConfigFile;

		try {
			reader = new FileReader(setupConfigFile);
			setupScanner = new Scanner(reader);
		} catch (FileNotFoundException e) {
			System.out.println("The file does not exist in the directory. Retry with a new file.");
		}

		String[] setupArray = new String[3]; //will always have 3 so ok to have this random value
		while (setupScanner.hasNextLine()) {
			String input = setupScanner.nextLine();
			if (input.contains(",")) {
				setupArray = input.split(", "); //holds room, name, and label
				//space included since file has a space before name and label
				if (setupArray[0].equals("Room") || setupArray[0].equals("Space")) {
					String name = setupArray[1];
					char character = setupArray[2].charAt(0);
					roomMap.put(character, new Room(name));
				} else {
					throw new BadConfigFormatException("The specified line does not have the right room format. Retry with a new file.");
				}
			}
		}
	}

	public void loadLayoutConfig() throws BadConfigFormatException {
		ArrayList<String[]> symbolList = new ArrayList<String[]>();
		layoutConfigFile = "data/" + layoutConfigFile;
		try {
			reader = new FileReader(layoutConfigFile);
			setupScanner = new Scanner(reader);
		} catch (FileNotFoundException e) {
			System.out.println("The file does not exist in the directory. Retry with a new file.");
		}

		readLayoutConfig(symbolList);
		grid = new BoardCell[numRows][numColumns];

		for(int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				grid[i][j] = new BoardCell(i, j);  //initialize each grid piece
			}
		}
		
		//sets initial, roomCenter/Label, etc for each board piece
		for(int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				String roomSymbol = symbolList.get(i)[j]; //variable names for readability
				BoardCell thisCell = grid[i][j];
				if (roomMap.containsKey(roomSymbol.charAt(0))) {
					//made a new method so we didn't have to use a ton of setters
					thisCell.setBoardCells(roomSymbol, roomMap);
				} else {
					throw new BadConfigFormatException("This csv file has an invalid room label. Retry with a new file");
				}
			}
		}
	}

	private void readLayoutConfig(ArrayList<String[]> symbolList) throws BadConfigFormatException {
		String input = setupScanner.nextLine();
		String[] symbols = input.split(",");
		//each value in the ArrayList holds one full row
		symbolList.add(symbols);
		numColumns = symbols.length; //can use initial array length to check every other array
		//they must all be consistent to have a valid board

		while (setupScanner.hasNextLine()) {
			input = setupScanner.nextLine();
			symbols = input.split(",");
			symbolList.add(symbols);

			if(symbols.length != numColumns) { //check for bad columns
				throw new BadConfigFormatException("One of the rows does not have the right amount of columns.");
			}
		}

		numRows = symbolList.size();  //ArrayList size determines amount of rows
	}

	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = layoutConfigFile;
		this.setupConfigFile = setupConfigFile;
	}


	public Room getRoom(char c) {
		return roomMap.get(c);
	}

	public Room getRoom(BoardCell cell) {
		char label = cell.getInitial();
		return roomMap.get(label);
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public Set<BoardCell> getAdjList(int row, int col) {
		return grid[row][col].getAdjList();
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

}
