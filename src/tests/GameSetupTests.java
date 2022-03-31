package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
	void checkDeck() {
		ArrayList<Card> deck = board.getDeck();
		assertTrue(deck.size() == 21);
		
		int isRoom = 0, isPlayer = 0, isWeapon = 0;
		
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
		
	}

}
