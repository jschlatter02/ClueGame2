package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class KnownCardsPanel extends JPanel {
	//instance variables so that we can update the panels when we add cards to the seen
	private JPanel playerPanel, roomPanel, weaponPanel;
	private static HumanPlayer humanPlayer;
	private static Board board;
	
	public KnownCardsPanel(Board board) {
		this.board = board;
		setLayout(new GridLayout(3,0));
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
			
		playerPanel = new JPanel();
		playerPanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		updatePanel(playerPanel, CardType.PERSON);
		add(playerPanel);
		
		roomPanel = new JPanel();
		roomPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		updatePanel(roomPanel, CardType.ROOM);
		add(roomPanel);
		
		weaponPanel = new JPanel();
		weaponPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		updatePanel(weaponPanel, CardType.WEAPON);
		add(weaponPanel);
	}
	
	private void updatePanel(JPanel panel, CardType cardType) {
		humanPlayer = board.getHumanPlayer();
		Set<Card> inHand = humanPlayer.getHand();
		ArrayList<Card> correctHandCards = addToCardList(panel, cardType, inHand);
		
		Set<Card> seenCards = humanPlayer.getSeenCards();
		ArrayList<Card> correctSeenCards = addToCardList(panel, cardType, seenCards);	
		
		//2 here so that we can account for the two labels
		int gridSize = correctHandCards.size() + correctSeenCards.size() + 2;
		if (correctHandCards.size() == 0) { //so we can add "None" as a Text Field
			gridSize++; //None + 2 labels + seenCards
		}
		
		if (correctSeenCards.size() == 0) {
			gridSize++;
		}
		
		panel.setLayout(new GridLayout(gridSize,0)); //gives us a good outline in each panel
		
		JLabel handLabel = new JLabel("In Hand:");
		panel.add(handLabel, BorderLayout.SOUTH);

		addTextFields(panel, correctHandCards);
		
		JLabel seenLabel = new JLabel("Seen:");
		panel.add(seenLabel, BorderLayout.SOUTH);
		
		addTextFields(panel, correctSeenCards);
	}

	private void addTextFields(JPanel panel, ArrayList<Card> playerCards) {
		//add the correct amount of text fields to each panel
		if (playerCards.size() == 0) { //add only none
			JTextField textField = new JTextField(15);
			textField.setText("None");
			textField.setEditable(false);			// makes the text fields un-editable so that user can't change it.
			panel.add(textField);
		} else {
			for (Card card : playerCards) { //add all the cards
				JTextField textField = new JTextField(15);
				textField.setText(card.getCardName());
				textField.setEditable(false);
				panel.add(textField);
			}
		}
	}

	private ArrayList<Card> addToCardList(JPanel panel, CardType cardType, Set<Card> playerCards) {
		//check if the card is the correct type and then add it to a separate array list
		ArrayList<Card> correctCardType = new ArrayList<Card>();
		for(Card card : playerCards) {
			if(card.getCardType() == cardType) {
				correctCardType.add(card);
			}
		}
		return correctCardType; //holds only the player, room, or weapon cards
	}
	
	public void updatePanels() {
		//remove all to reset the panels and start again
		playerPanel.removeAll();
		roomPanel.removeAll();
		weaponPanel.removeAll();
		
		updatePanel(playerPanel, CardType.PERSON);
		updatePanel(roomPanel, CardType.ROOM);
		updatePanel(weaponPanel, CardType.WEAPON);	
	}

	public static void main(String[] args) {
		board = board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.deal();
		
		KnownCardsPanel panel = new KnownCardsPanel(board);  
		JFrame frame = new JFrame();  
		//adding many seen cards to each type so that we can test our outline
		humanPlayer.updateSeen(new Card("Will Smith", CardType.PERSON));
		humanPlayer.updateSeen(new Card("Aristotle", CardType.PERSON));
		humanPlayer.updateSeen(new Card("Alan Turing", CardType.PERSON));
		humanPlayer.updateSeen(new Card("Foyer", CardType.ROOM));
		humanPlayer.updateSeen(new Card("Bathroom", CardType.ROOM));
		humanPlayer.updateSeen(new Card("Laundry Room", CardType.ROOM));
		humanPlayer.updateSeen(new Card("Kitchen", CardType.ROOM));
		humanPlayer.updateSeen(new Card("Office", CardType.ROOM));
		humanPlayer.updateSeen(new Card("Knife", CardType.WEAPON));
		humanPlayer.updateSeen(new Card("Sickel", CardType.WEAPON));
		humanPlayer.updateSeen(new Card("Spear", CardType.WEAPON));
		
		board.setHumanPlayer(humanPlayer); //need setter so that we can have the correct humanPlayer data in updatePanel
		panel.updatePanels();

		frame.setContentPane(panel); 
		frame.setSize(180, 700);  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
