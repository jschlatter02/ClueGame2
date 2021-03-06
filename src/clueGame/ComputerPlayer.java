package clueGame;

import java.util.*;

public class ComputerPlayer extends Player{
	private static Board board;

	public ComputerPlayer(String name, int row, int col, String color) {
		super(name, row, col, color);
		board = Board.getInstance();
	}

	@Override
	public void updateHand(Card card) {
		super.getHand().add(card);
	}
	
	public Solution createSuggestion(Room currentRoom, boolean cannotDisprove) {
		Set<Card> hand = super.getHand();
		Set<Card> seenCards = super.getSeenCards();
		Random randInt = new Random();
		ArrayList<Card> playerCards = board.getPlayerCards();
		ArrayList<Card> weaponCards = board.getWeaponsCards();
		ArrayList<Card> roomCards = board.getRoomCards();
		Card roomCard;
		
		if (cannotDisprove) {
			ArrayList<Card> unseenRooms = addToUnseen(roomCards, hand, seenCards); 
			int randomIdx = randInt.nextInt(unseenRooms.size());
			roomCard = unseenRooms.get(randomIdx);
		} else {
			roomCard = new Card(currentRoom.getName(), CardType.ROOM); //room suggestion based on the current room
		}
		
		ArrayList<Card> unseenPlayers = addToUnseen(playerCards, hand, seenCards); //list of players/weapons that have
		                      													   //not been seen
		ArrayList<Card> unseenWeapons = addToUnseen(weaponCards, hand, seenCards);
		
		//grab a random card from the list of unseen cards
		int randomIdx = randInt.nextInt(unseenPlayers.size());
		Card playerCard = unseenPlayers.get(randomIdx);
		
		randomIdx = randInt.nextInt(unseenWeapons.size());
		Card weaponCard = unseenWeapons.get(randomIdx);
		
		return new Solution(roomCard, playerCard, weaponCard);
	}

	private ArrayList<Card> addToUnseen(ArrayList<Card> listOfCards, Set<Card> hand, Set<Card> seenCards) {
		//want a separate arraylist to keep track of the unseen players/weapons to 
		ArrayList<Card> unseenCards = new ArrayList<Card>();
		for (Card card : listOfCards) {
			if(!hand.contains(card) && !seenCards.contains(card)) {
				unseenCards.add(card); //if not seen then add it to the list
			}
		}
		
		return unseenCards;
	}
	
	public BoardCell selectTarget() {
		Set<Card> hand = super.getHand();
		Set<Card> seenCards = super.getSeenCards();
		Set<BoardCell> targets = board.getTargets();
		Map<Character, Room> roomMap = board.getRoomMap();
		ArrayList<Card> allRooms = new ArrayList<Card>();
		ArrayList<Card> unseenRooms = new ArrayList<Card>();
		
		for (BoardCell target : targets) {
			if (target.isRoomCenter()) { //add all the rooms to a separate array
				//we do this so that we can eventually see if there are any unseen 
				Room adjRoom = roomMap.get(target.getInitial());
				Card targetCard = new Card(adjRoom.getName(), CardType.ROOM);
				allRooms.add(targetCard);
			}
		}
		
		for(Card roomCard : allRooms) { 
			//if the room is unseen, then add it to a separate array so that we can shuffle it and get a random room
			if (!hand.contains(roomCard) && !seenCards.contains(roomCard)) {
				unseenRooms.add(roomCard);
			}
		}
		
		Random randInt = new Random();
		if (unseenRooms.size() == 0) { //choose a random location from the targets
			BoardCell[] targetsArray = targets.toArray(new BoardCell[targets.size()]);
			int randIdx = randInt.nextInt(targetsArray.length);
			return targetsArray[randIdx];
		} else { //choose a random room from the rooms that have not been seen
			Card randomTarget = unseenRooms.get(randInt.nextInt(unseenRooms.size())); //random room
			for (Room room : roomMap.values()) {
				if (room.getName().equals(randomTarget.getCardName())) {
					//checks the name between the room variable and the card because they should be the same
					//do this to change a card to an actual board cell
					BoardCell roomCell = room.getCenterCell();
					return roomCell;
				}
			}
		}
		return null;
	}

	
	
}
