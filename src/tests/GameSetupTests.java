package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.ClassOrderer.OrderAnnotation;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;

class GameSetupTests {
	
	private static Board board;
	
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}
	
	@Test
	void LoadPlayersTest() {
		
		ArrayList<Player> player =  board.getPlayers();
		assertEquals(player.get(0).getName(), "Aristotle");
		assertEquals(player.get(5).getName(), "Brian Kernighan");
		assertTrue(player.size() == 6);
	}
	
	
	@Test
	public void testDealAndDeck() {
		ArrayList<Player> player =  board.getPlayers();
		ArrayList<Card> deck = board.getDeck();
		
		assertTrue(deck.size() == 21);
		
		int isRoom = 0, isPlayer = 0, isWeapon = 0;
		
		//checks to make sure the correct amount of card types are in the deck
		for (Card card : deck) {
			if (card.getCardType() == CardType.ROOM) {
				isRoom++;
			} else if (card.getCardType() == CardType.PERSON) {
				isPlayer++;
			} else if (card.getCardType() == CardType.WEAPON) {
				isWeapon++;
			}
		}
		
		assertEquals(isRoom, 9);
		assertEquals(isPlayer, 6);
		assertEquals(isWeapon, 6);
		
		board.deal();
		// tests if all the players get equal amount of cards.
		for(int i = 0; i < player.size(); i++) {
			assertEquals(player.get(i).getHand().size(), 3);
		}
		
		// checks if all the cards are dealt.
		assertEquals(deck.size(), 0);
		
		//check if a player's card is in anyone else's hand
		for(Card handCard : player.get(0).getHand()) {
			for (int i = 1; i < player.size(); i++) {
				assertFalse(player.get(i).getHand().contains(handCard));
			}
		}
	}

}
