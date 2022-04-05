package clueGame;

import java.util.*;

public class ComputerPlayer extends Player{

	public ComputerPlayer(String name, int row, int col, String color) {
		super(name, row, col, color);
	}

	@Override
	public void updateHand(Card card) {
		super.getHand().add(card);
	}
	
	public Solution createSuggestion(Room currentRoom, ArrayList<Card> playerCards, ArrayList<Card> weaponCards) {
		Set<Card> hand = super.getHand();
		Set<Card> seenCards = super.getSeenCards();
		ArrayList<Card> unseenPlayers = new ArrayList<Card>(); //want a separate arraylist to keep track of the 
		ArrayList<Card> unseenWeapons = new ArrayList<Card>(); //cards that have not been seen
		Random randInt = new Random();
		
		//System.out.println(currentRoom.getName());
		
		Card roomCard = new Card(currentRoom.getName(), CardType.ROOM);
		
		for (Card playerCard : playerCards) {
			if(!hand.contains(playerCard) && !seenCards.contains(playerCard)) {
				unseenPlayers.add(playerCard);
			}
		}
		
		for (Card weaponCard : weaponCards) {
			if(!hand.contains(weaponCard) && !seenCards.contains(weaponCard)) {
				unseenWeapons.add(weaponCard); //if not seen then add it to the list
			}
		}
		
		//grab a random card from the list of unseen cards
		int randomIdx = randInt.nextInt(unseenPlayers.size());
		Card playerCard = unseenPlayers.get(randomIdx);
		
		randomIdx = randInt.nextInt(unseenWeapons.size());
		Card weaponCard = unseenWeapons.get(randomIdx);
		
		Solution computerSuggestion = new Solution(roomCard, playerCard, weaponCard);
		
		return computerSuggestion;
	}
	
	public BoardCell selectTarget(Set<BoardCell> targets, Map<Character, Room> roomMap) {
		Set<Card> hand = super.getHand();
		Set<Card> seenCards = super.getSeenCards();
		ArrayList<Card> allRooms = new ArrayList<Card>();
		ArrayList<Card> unseenRooms = new ArrayList<Card>();
		
		for (BoardCell target : targets) {
			if (target.isRoomCenter()) { //add all the rooms to a separate array
				Room adjRoom = roomMap.get(target.getInitial());
				Card targetCard = new Card(adjRoom.getName(), CardType.ROOM);
				allRooms.add(targetCard);
			}
		}
		
		for(Card roomCard : allRooms) { //.contains() would not work for some reason so we had to implement our own algorithm
			int handValue = 0; //we do have a .equals() for Card
			for (Card handCard : hand) {
				if (handCard.equals(roomCard)) {
					handValue++;
				}
			}
			int seenValue = 0;
			for (Card seenCard : seenCards) {
				if (seenCard.equals(roomCard)) {
					seenValue++;
				}
			}
			
			if (seenValue + handValue == 0) { //make sure the Card is not in hand or seenCards
				unseenRooms.add(roomCard);
			}
		}
		
		Random randInt = new Random();
		if (unseenRooms.size() == 0) { //choose a random location from the targets
			BoardCell[] targetsArray = targets.toArray(new BoardCell[targets.size()]);
			int randIdx = randInt.nextInt(targetsArray.length);
			return targetsArray[randIdx];
		} else { //choose a random room from the rooms that have not been seen
			Card randomTarget = unseenRooms.get(randInt.nextInt(unseenRooms.size()));
			for (Room room : roomMap.values()) {
				if (room.getName().equals(randomTarget.getCardName())) {
					BoardCell roomCell = room.getCenterCell();
					return roomCell;
				}
			}
		}
		return null;
	}

}
