package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
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
	void LoadWeaponsTest() {
		
		
		
		
	}

}
