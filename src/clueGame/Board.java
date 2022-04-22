package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.function.BooleanSupplier;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements MouseListener {
	private BoardCell[][] grid;
	private Set<BoardCell> targets, visited;
	private int numRows, numColumns;
	private String layoutConfigFile, setupConfigFile;
	private static Board theInstance = new Board();
	private Map<Character, Room> roomMap;

	// These are defined globally since two methods uses these instance variables.
	private FileReader reader = null;
	private Scanner scanner = null;

	//card instance variables
	private HumanPlayer humanPlayer;
	private Player currentPlayer; //keeps track of who's currently moving on the board
	private int playerValue; //keeps track of the player arraylist value
	private ArrayList<Player> players;
	private ArrayList<Card> deck;
	private boolean finished = true; //check if the human player is done yet
	//used so that when we delete from deck, we don't lose the list of cards
	//helpful for the computer suggestions
	private ArrayList<Card> weaponsCards, playerCards, roomCards;
	
	private Solution theAnswer;
	private static final int ROLL_SIZE = 6;


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
				if((row - 1) >= 0) { //upper cell
					BoardCell adjCell = grid[row - 1][col];
					thisCellChoice(thisCell, adjCell, DoorDirection.UP);
				}
				if((row + 1) <= numRows - 1) { //lower cell
					BoardCell adjCell = grid[row + 1][col];
					thisCellChoice(thisCell, adjCell, DoorDirection.DOWN);
				}
				if((col - 1) >= 0) { //left cell
					BoardCell adjCell = grid[row][col - 1];
					thisCellChoice(thisCell, adjCell, DoorDirection.LEFT);
				}
				if((col + 1) <= numColumns - 1) { //right cell
					BoardCell adjCell = grid[row][col + 1];
					thisCellChoice(thisCell, adjCell, DoorDirection.RIGHT);
				}
			}
		}
	}

	private void thisCellChoice(BoardCell thisCell, BoardCell adjCell, DoorDirection doorDirection) {
		if (thisCell.isDoorway()) {
			//put door direction in parameters so that we can have one method that can use any direction
			doorwayAdjacenceList(thisCell, adjCell, doorDirection);
		} else if (thisCell.getInitial() == 'W' && adjCell.getInitial() == 'W') {
			thisCell.addAdjacency(adjCell);
		} else if(thisCell.isSecretPassage()) {
			addSecretPassage(thisCell);
		}
	}

	private void doorwayAdjacenceList(BoardCell thisCell, BoardCell adjCell, DoorDirection doorDirection) {
		char initial = adjCell.getInitial();
		if (initial != 'W' && initial != 'X' && thisCell.getDoorDirection() == doorDirection) {
			//only want adjacent doors that actually enter the room
			BoardCell centerCell = roomMap.get(initial).getCenterCell();
			thisCell.addAdjacency(centerCell);
			//add to room adjList because it is easier this way
			//rooms only want their adjacent doors
			centerCell.addAdjacency(thisCell);
		} else if (initial == 'W') {
			thisCell.addAdjacency(adjCell);
		}
	}

	private void addSecretPassage(BoardCell thisCell) {
		//adds room centers to each secret passage room center cell
		char roomInitial = thisCell.getInitial();
		char secretPassageInitial = thisCell.getSecretPassage();

		BoardCell centerCell = roomMap.get(roomInitial).getCenterCell();
		BoardCell passageCenterCell = roomMap.get(secretPassageInitial).getCenterCell();

		centerCell.addAdjacency(passageCenterCell);
	}


	public void calcTargets(BoardCell startCell, int pathLength) {
		//clear targets and visited because we only have one Board instance
		visited.clear();
		visited.add(startCell);
		targets.clear();
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
				//only add if not occupied
				else  if (!adjCell.getOccupied()){  //don't want lower part of code to run if the adjCell is a room
					if(pathLength == 1) {   	
						targets.add(adjCell);
						adjCell.setInTarget(true);
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
		players = new ArrayList<Player>();
		deck = new ArrayList<Card>();
		weaponsCards = new ArrayList<Card>();
		playerCards = new ArrayList<Card>();
		roomCards = new ArrayList<Card>();

		try {
			reader = new FileReader(setupConfigFile);
			scanner = new Scanner(reader);
		} catch (FileNotFoundException e) {
			System.out.println("The file does not exist in the directory. Retry with a new file.");
		}

		while (scanner.hasNextLine()) {
			String input = scanner.nextLine();
			if (input.contains(",")) {
				String [] setupArray = input.split(", "); //holds room, name, and label
				//space included since file has a space before name and label
				if (setupArray[0].equals("Room") || setupArray[0].equals("Space")) {
					String name = setupArray[1];
					char character = setupArray[2].charAt(0);
					roomMap.put(character, new Room(name));
					if (setupArray[0].equals("Room")) {
						deck.add(new Card(setupArray[1], CardType.ROOM));
						roomCards.add(new Card(setupArray[1], CardType.ROOM));
					}

				} else if (setupArray[0].equals("Player")) {  
					if (setupArray[1].equals("Human")) {
						humanPlayer = new HumanPlayer(setupArray[2], Integer.parseInt(setupArray[3]), Integer.parseInt(setupArray[4]), setupArray[5]);
						players.add(humanPlayer);
						currentPlayer = humanPlayer; //first player will always be the human player
						playerValue = -1; //when added in nextButton, it will cause it to be increased to 0
						//this starts the turn with the human player first
					} else {
						Player computerPlayer = new ComputerPlayer(setupArray[2], Integer.parseInt(setupArray[3]), Integer.parseInt(setupArray[4]), setupArray[5]);
						players.add(computerPlayer);
					}
					deck.add(new Card(setupArray[2], CardType.PERSON));
					playerCards.add(new Card(setupArray[2], CardType.PERSON));
				} else if (setupArray[0].equals("Weapon")) {
					deck.add(new Card(setupArray[1], CardType.WEAPON));
					weaponsCards.add(new Card(setupArray[1], CardType.WEAPON));
				}
				else {
					throw new BadConfigFormatException("The specified line does not have the right card format. Retry with a new file.");
				}
			}
		}
	}

	public void loadLayoutConfig() throws BadConfigFormatException {
		ArrayList<String[]> symbolList = new ArrayList<String[]>();
		layoutConfigFile = "data/" + layoutConfigFile;
		try {
			reader = new FileReader(layoutConfigFile);
			scanner = new Scanner(reader);
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
		String input = scanner.nextLine();
		String[] symbols = input.split(",");
		//each value in the ArrayList holds one full row
		symbolList.add(symbols);
		numColumns = symbols.length; //can use initial array length to check every other array
		//they must all be consistent to have a valid board

		while (scanner.hasNextLine()) {
			input = scanner.nextLine();
			symbols = input.split(",");
			symbolList.add(symbols);

			if(symbols.length != numColumns) { //check for bad columns
				throw new BadConfigFormatException("One of the rows does not have the right amount of columns.");
			}
		}
		numRows = symbolList.size();  //ArrayList size determines amount of rows
	}

	public void deal() {
		Random random = new Random();
		int deckIndex;

		Card roomCard = createSolution(random, CardType.ROOM);
		Card playerCard = createSolution(random, CardType.PERSON);
		Card weaponCard = createSolution(random, CardType.WEAPON);

		theAnswer = new Solution(roomCard, playerCard, weaponCard);

		int currentPlayer = 0;
		do {
			deckIndex = random.nextInt(deck.size());
			Player player = players.get(currentPlayer);
			player.updateHand(deck.get(deckIndex));
			deck.remove(deckIndex);
			if (player.getHand().size() == 3) { //way to iterate through every player
				currentPlayer++;
			}
		} while (deck.size() > 0); //want deck to be empty at the end
	}

	private Card createSolution(Random random, CardType cardType) {
		Card card;
		int deckIndex;
		do { //grab random card until it is the correct card type
			deckIndex = random.nextInt(deck.size());
			card = deck.get(deckIndex);
		} while (card.getCardType() != cardType);

		deck.remove(deckIndex);
		//card removed from deck so we know that it cannot be drawn by someone else
		return card; 
	}

	public void setAnswer(Card playerCard, Card roomCard, Card weaponCard) {
		theAnswer.setPerson(playerCard);
		theAnswer.setRoom(roomCard);
		theAnswer.setWeapon(weaponCard);
	}

	public Boolean checkAccusation(Solution accusation) {
		return theAnswer.equals(accusation);
	}

	public Card handleSuggestions(Card playerCard, Card roomCard, Card weaponCard, Player accusingPlayer) {
		for (Player player : players) {
			//check if the player is not accusing because only the non-accusing players can disprove a suggestion
			if (!player.equals(accusingPlayer)) {
				Card disprovenCard = player.disproveSuggestion(playerCard, roomCard, weaponCard);
				if (disprovenCard != null) {
					//means that a player was able to disprove the suggestion
					return disprovenCard;
				}
			}
		}
		return null; //no valid cards to prove the suggestion
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		ArrayList<String []> nameLocations = new ArrayList<String []>();
		//start at 0 to get the top left corner
		int horizontalOffset = 0;
		int topOffset = 0;
		int initialHorizontalOffset = horizontalOffset;
		int width = (getWidth() - (2*horizontalOffset)) / numColumns; //width of an individual board cell
		int height = (getHeight() - (2*topOffset)) / numRows;  //height of an individual board cell
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				String[] locations = grid[row][col].drawCell(graphics, width, height, horizontalOffset, topOffset, roomMap);
				if (locations != null) { //means there is a room name at this location
					nameLocations.add(locations);
				}
				horizontalOffset += width;
				if(col == numColumns - 1) {
					//reset so that the cells don't keep going past the window
					//if there is no reset, then you would only be able to see the first row
					horizontalOffset = initialHorizontalOffset;
				}

			}
			topOffset += height;
		}
		//targets are drawn before the players so that they don't overwrite the player drawing
		if (!finished) {
			//draw the targets in a new color
			for (BoardCell target : targets) {
				target.drawTargets(graphics, width, height);
			}
		}
		
		Set<Player> sameLocation = new HashSet<Player>();
		for (int i = 0; i < players.size(); i++) {
			for (int j = i + 1; j < players.size(); j++) {
				if (players.get(i).getRow() == players.get(j).getRow() && players.get(i).getCol() == players.get(j).getCol()) {
					sameLocation.add(players.get(i));
					sameLocation.add(players.get(j));
				}
			}
		}

		//reset these values so that we can calculate the position in the player method
		horizontalOffset = 0;
		topOffset = 0;
		int sameLocationOffset = width / 3;
		for (Player player : players) {
			if (sameLocation.contains(player)) {
				player.drawPlayer(graphics, width, height, horizontalOffset + sameLocationOffset, topOffset);
				sameLocationOffset += sameLocationOffset;
			} else {
			player.drawPlayer(graphics, width, height, horizontalOffset, topOffset);
			}
		}
		

		//write the room names so they don't get written over
		for (String[] locations : nameLocations) {
			String name = locations[0];
			horizontalOffset = Integer.parseInt(locations[1]);
			topOffset = Integer.parseInt(locations[2]);

			//font size is determined by width so that it scales with the player stretching
			graphics.setFont(new Font("Cambria", Font.BOLD, width/2));
			graphics.setColor(Color.BLUE);
			graphics.drawString(name, horizontalOffset, topOffset);
		}
	}

	public void nextButton(GameControlPanel gameControl) {
		if(finished) {
			playerValue = (playerValue + 1) % players.size(); //better way of getting the iterator back to 0
			currentPlayer = players.get(playerValue);
			Random random = new Random();
			int roll = random.nextInt(ROLL_SIZE);
			//add 1 to the roll since the range of random is 0-5 instead of 1-6
			calcTargets(getCell(currentPlayer.getRow(), currentPlayer.getCol()), roll + 1);
			gameControl.setTurn(currentPlayer, roll + 1);
			if(currentPlayer.equals(humanPlayer)) {
				finished = false; //signifies that the player is not done and that you should draw the targets

				//for loop allows every other cell in the room to be colored
				for(int i = 0; i < numRows; i++) {
					for(int j = 0; j < numColumns; j++) {
						char initial = grid[i][j].getInitial();
						if(targets.contains(roomMap.get(initial).getCenterCell())){
							targets.add(grid[i][j]);
						}
					}
				}
				//our current algorithm goes through the targets and colors them a different color
				//by adding the other room cells to the targets, it means they are colored correctly
				//targets is cleared after the move anyways so it does no harm

				repaint();
			} else {
				boolean cannotDisprove = currentPlayer.isCannotDisprove();
				if (cannotDisprove) { //computer accusation
					Room currentRoom = null;
					Solution computerAccusation = currentPlayer.createSuggestion(currentRoom, cannotDisprove);
					boolean accusationResult = checkAccusation(computerAccusation);
					if (accusationResult) {
						Card accusedPlayer = computerAccusation.getPerson();
						Card accusedRoom = computerAccusation.getRoom();
						Card accusedWeapon = computerAccusation.getWeapon();
						JOptionPane.showMessageDialog(this, currentPlayer.getName() + " wins!\nThe answer was " + accusedPlayer.getCardName() + ", " + accusedRoom.getCardName() + ", " + accusedWeapon.getCardName());
						System.exit(0);
					}
					currentPlayer.setCannotDisprove(false);
				}
				
				int row = currentPlayer.getRow();
				int col = currentPlayer.getCol();
				grid[row][col].setOccupied(false);

				BoardCell chosenTarget = currentPlayer.selectTarget();
				//want to update the computer player's row and column
				currentPlayer.setRow(chosenTarget.getRow());
				currentPlayer.setCol(chosenTarget.getCol());
				row = currentPlayer.getRow();
				col = currentPlayer.getCol();
				grid[row][col].setOccupied(true); //computer player cell can not be chosen by any other player

				//suggestion code
				if (grid[row][col].isRoomCenter()) {
					char initial = grid[row][col].getInitial();
					Room currentRoom = roomMap.get(initial);
					Solution computerSuggestion = currentPlayer.createSuggestion(currentRoom, cannotDisprove);
					//update the guess panel
					Card suggestedPlayer = computerSuggestion.getPerson();
					Card suggestedRoom = computerSuggestion.getRoom();
					Card suggestedWeapon = computerSuggestion.getWeapon();
					
					for (Player player : players) { //move suggested player into the room
						if (player.getName().equals(suggestedPlayer.getCardName())) {
							player.setRow(row);
							player.setCol(col);
							break; //break so that no other player gets moved into the room with them
						}
					}
					
					gameControl.setGuess(suggestedPlayer.getCardName() + ", " + suggestedRoom.getCardName() + ", " + suggestedWeapon.getCardName());
					Card disprovenCard = handleSuggestions(suggestedPlayer, suggestedRoom, suggestedWeapon, currentPlayer);
					if (disprovenCard != null) {
						//add to player's seen list so that they cannot suggest that card again
						currentPlayer.updateSeen(disprovenCard);
						gameControl.setGuessResult("Suggestion disproven!");
					} else {
						//next computer player can make an accusation
						cannotDisprove = true;
						gameControl.setGuessResult("No card could disprove that accusation.");
					}
				}
				
				repaint();
			}
		} else { //player has not done their move
			JOptionPane.showMessageDialog(this, "Please finish your turn!");
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(currentPlayer.equals(humanPlayer)) {
			BoardCell clickedCell = null;
			int width = getWidth() / numColumns; //width of an individual board cell
			int height = getHeight() / numRows;  //height of an individual board cell

			for(BoardCell target : targets) { //only need to loop through the targets since they are the only valid cells
				if(target.containsClicked(e.getX(), e.getY(), width, height)) {
					clickedCell = target;
					break;
				}
			}

			if(clickedCell == null) {
				JOptionPane.showMessageDialog(this, "Not a target Cell.");
			} else { //player has clicked on a valid target cell
				//make sure that computer players can move to the previous cell
				int row = currentPlayer.getRow();
				int col = currentPlayer.getCol();
				grid[row][col].setOccupied(false); //set previous cell's occupied value to false
				if(clickedCell.getInitial() != 'W' && !clickedCell.isRoomCenter()) { //random room cell
					//need to move the player to the center of the room so we find the center cell
					BoardCell boardCenterCell = roomMap.get(clickedCell.getInitial()).getCenterCell();
					//update player location so that it gets redrawn in the correct location
					currentPlayer.setRow(boardCenterCell.getRow());
					currentPlayer.setCol(boardCenterCell.getCol());
				} else { //already in the room center or on target
					currentPlayer.setRow(clickedCell.getRow());
					currentPlayer.setCol(clickedCell.getCol());
				}
				row = currentPlayer.getRow();
				col = currentPlayer.getCol();
				grid[row][col].setOccupied(true); //set the current cell's occupied to true

				finished = true;//player is finished with their turn
				repaint();
			}
		} 
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

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public Set<BoardCell> getAdjList(int row, int col) {
		//no adjList in Board so we have to call each BoardCell's getter for adj
		return grid[row][col].getAdjList();
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public ArrayList<Card> getWeaponsCards() {
		return weaponsCards;
	}

	public ArrayList<Card> getPlayerCards() {
		return playerCards;
	}

	public Map<Character, Room> getRoomMap() {
		return roomMap;
	}

	public HumanPlayer getHumanPlayer() {
		return humanPlayer;
	}

	public void setHumanPlayer(HumanPlayer humanPlayer) {
		this.humanPlayer = humanPlayer;
	}

	public ArrayList<Card> getRoomCards() {
		return roomCards;
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
