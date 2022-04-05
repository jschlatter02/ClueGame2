package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class GameSolutionTest {
	private static Board board;	
	private static Card smithCard, aristotleCard, turingCard, wozniakCard, mccarthyCard, kernighanCard;
	private static Card foyerCard, bathroomCard, laundryCard, kitchenCard, officeCard, bedroomCard, studyCard, livingCard, gameCard;
	private static Card knifeCard, bowCard, katanaCard, musketCard, sickleCard, spearCard;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
		//players
		smithCard = new Card("Will Smith", CardType.PERSON);
		aristotleCard = new Card("Aristotle", CardType.PERSON);
		turingCard = new Card("Alan Turing", CardType.PERSON);
		wozniakCard = new Card("Scott Wozniak", CardType.PERSON);
		mccarthyCard = new Card("John McCarthy", CardType.PERSON);
		kernighanCard = new Card("Brian Kernighan", CardType.PERSON);
		//rooms
		foyerCard = new Card("Foyer", CardType.ROOM);
		bathroomCard = new Card("Bathroom", CardType.ROOM);
		laundryCard = new Card("Laundry Room", CardType.ROOM);
		kitchenCard = new Card("Kitchen", CardType.ROOM);
		officeCard = new Card("Office", CardType.ROOM);
		bedroomCard = new Card("Master Bedroom", CardType.ROOM);
		studyCard = new Card("Study Room", CardType.ROOM);
		livingCard = new Card("Living Room", CardType.ROOM);
		gameCard = new Card("Game Room", CardType.ROOM);
		//weapons
		knifeCard = new Card("Knife", CardType.WEAPON);
		bowCard = new Card("Bow and Arrow", CardType.WEAPON);
		katanaCard = new Card("Katana", CardType.WEAPON);
		musketCard = new Card("Musket", CardType.WEAPON);
		sickleCard = new Card("Sickle", CardType.WEAPON);
		spearCard = new Card("Spear", CardType.WEAPON);
	}
	
	@Test
	public void testAccusations() {
		board.deal(); //deals cards so that theAnswer is initialized in board
		board.setAnswer(smithCard, bathroomCard, katanaCard);
		assertTrue(board.checkAccusation(new Solution(bathroomCard, smithCard, katanaCard)));
		//must be false
		assertFalse(board.checkAccusation(new Solution(bathroomCard, mccarthyCard, katanaCard))); //player wrong
		assertFalse(board.checkAccusation(new Solution(foyerCard, smithCard, katanaCard))); //room wrong
		assertFalse(board.checkAccusation(new Solution(bathroomCard, smithCard, sickleCard))); //weapon wrong
	}
	
	@Test
	public void testDisproveSuggestion() {
		HumanPlayer testPlayer = new HumanPlayer("Will Smith", 0, 0, "green");
		testPlayer.updateHand(aristotleCard);
		testPlayer.updateHand(kernighanCard);
		testPlayer.updateHand(studyCard);
		
		//no matching cards
		Card emptyCard = testPlayer.disproveSuggestion(turingCard, officeCard, spearCard);
		assertEquals(emptyCard, null);
		
		//one matching card
		Card oneCard = testPlayer.disproveSuggestion(aristotleCard, bedroomCard, bowCard);
		assertEquals(oneCard, aristotleCard);
		
		//multiple matching cards
		int aristotleCardValue = 0;
		int studyCardValue = 0;
		for (int i = 0; i < 100; i++) { //loops 100 times to ensure that each card is chosen at least once
			Card multipleValuesCard = testPlayer.disproveSuggestion(aristotleCard, studyCard, musketCard);
			if (multipleValuesCard.equals(aristotleCard)) {
				aristotleCardValue++;
			} else if (multipleValuesCard.equals(studyCard)) {
				studyCardValue++;
			}
		}
		
		assertTrue(aristotleCardValue > 0); //aristotle was picked at least once
		assertTrue(studyCardValue > 0); // study room was picked at least once
	}
	
	@Test
	public void testHandleSuggestion() {
		ArrayList<Player> players = new ArrayList<Player>();
		HumanPlayer humanPlayer = new HumanPlayer("Will Smith", 0, 0, "green");
		humanPlayer.updateHand(wozniakCard);
		humanPlayer.updateHand(laundryCard);
		ComputerPlayer compPlayer1 = new ComputerPlayer("Will Smith", 0, 0, "green");
		//using the parameters since they don't matter for this test
		compPlayer1.updateHand(livingCard);
		ComputerPlayer compPlayer2 = new ComputerPlayer("Will Smith", 0, 0, "green");
		compPlayer2.updateHand(knifeCard);
		ComputerPlayer compPlayer3 = new ComputerPlayer("Will Smith", 0, 0, "green");
		compPlayer3.updateHand(kitchenCard);
		
		players.add(humanPlayer);
		players.add(compPlayer1);
		players.add(compPlayer2);
		players.add(compPlayer3);
		
		board.setPlayers(players); //make sure that our player list is used for the tests
		
		Card notDisproven = board.handleSuggestions(aristotleCard, bathroomCard, sickleCard, 0);
		//last integer is which player is making a suggestion
		assertEquals(notDisproven, null);
		
		Card accuserDisproven = board.handleSuggestions(wozniakCard, laundryCard, bowCard, 0);
		assertEquals(accuserDisproven, null);
		
		//test if other players are accuser that their card is not chosen
		Card player1Disproven = board.handleSuggestions(smithCard, livingCard, bowCard, 1);
		assertEquals(player1Disproven, null);
	
		//player 1's card must disprove the statement and not player 2's card
		Card earliestDisproven = board.handleSuggestions(aristotleCard, livingCard, knifeCard, 0);
		assertEquals(earliestDisproven, livingCard);
	}

}
