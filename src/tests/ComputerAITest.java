package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class ComputerAITest {
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
	public void testCompSuggestions() {
		ComputerPlayer compPlayer = new ComputerPlayer("Will Smith", 14, 3, "green"); //puts the player in the center of a room
		//set up hand and seenCards lists
		compPlayer.updateHand(bedroomCard);
		compPlayer.updateHand(katanaCard);
		compPlayer.updateHand(kernighanCard);
		
		compPlayer.updateSeen(sickleCard);
		compPlayer.updateSeen(aristotleCard);
		
		Set<Card> hand = compPlayer.getHand();
		Set<Card> seenCards = compPlayer.getSeenCards();
		//created so that the list of cards
		ArrayList<Card> weaponsCards = board.getWeaponsCards();
		ArrayList<Card> playerCards = board.getPlayerCards();
		
		BoardCell roomCell = board.getCell(14, 3);
		Solution compSuggestion = compPlayer.createSuggestion(board.getRoom(roomCell), playerCards, weaponsCards); //want the player in the living room
		//make sure the room is the one that the player is in
		Card roomSuggestion = compSuggestion.getRoom();
		assertTrue(roomSuggestion.equals(livingCard)); //used .equals() because AssertEquals was not passing
		                                                         //even though the .equals() condition was true
		
		//make sure the player that was returned has not been seen at all
		Card playerSuggestion = compSuggestion.getPerson();
		assertFalse(hand.contains(playerSuggestion));
		assertFalse(seenCards.contains(playerSuggestion));
		
		//make sure the weapon has not been seen at all
		Card weaponSuggestion = compSuggestion.getWeapon();
		assertFalse(hand.contains(weaponSuggestion));
		assertFalse(seenCards.contains(weaponSuggestion));
	}
	
	@Test
	public void testComputerTargets() {
		ComputerPlayer compPlayer = new ComputerPlayer("Will Smith", 18, 14, "green");
		
		board.calcTargets(board.getCell(18, 14), 3);
		Set<BoardCell> targets = board.getTargets();
		
		BoardCell selectedCell = compPlayer.selectTarget(targets);
		assertEquals(selectedCell, board.getCell(22, 19));
		
		compPlayer.updateSeen(kitchenCard);
		int inSeenRoom = 0;
		for (int i = 0; i < 100; i++) {
			BoardCell chosenCell = compPlayer.selectTarget(targets);
			if (chosenCell == board.getCell(22, 19)) {
				inSeenRoom++;
			}
		}
		//if this is not 100, then other cells are being selected
		assertTrue(inSeenRoom != 100); //make sure that if the room is seen, it is not always chosen (still could be though)
		
		board.calcTargets(board.getCell(9, 4), 3); //next to two rooms that are unseen
		targets = board.getTargets();
		
		int inLiving = 0;
		int inGameRoom = 0;
		for (int i = 0; i < 100; i++) {
			BoardCell chosenCell = compPlayer.selectTarget(targets);
			if (chosenCell == board.getCell(14, 3)) {
				inLiving++;
			} else if (chosenCell == board.getCell(3, 3)) {
				inGameRoom++;
			}
		}
		
		assertTrue(inLiving > 0);
		assertTrue(inGameRoom > 0);
		assertTrue(inLiving + inGameRoom == 100); //only the rooms are chosen if they are unseen
		
	}
	
	
}
