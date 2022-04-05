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

}
