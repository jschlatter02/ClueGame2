package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SuggestionDialog extends JDialog {
	private static Board board = Board.getInstance();
	private JComboBox<String> playerBox;
	private JComboBox<String> weaponBox;
	JTextField roomName = new JTextField(15);
	private ArrayList<Card> playerCards = board.getPlayerCards();
	private ArrayList<Card> weaponCards = board.getWeaponsCards();
	private ArrayList<Card> roomCards = board.getRoomCards();
	private GameControlPanel gameControl = board.getGameControl();
	private static ClueGame theGame = ClueGame.getInstance();
	
	public SuggestionDialog(Room currentRoom) {
		String[] playerNames = createNameArray(playerCards);
		String[] weaponNames = createNameArray(weaponCards);
		
		playerBox = new JComboBox<String>(playerNames);
		weaponBox = new JComboBox<String>(weaponNames);
		
		//gives enough room for the labels, comboboxes, and buttons
		setLayout(new GridLayout(4,4));
		setTitle("Make a Suggestion!");
		
		JLabel person = new JLabel("Person");
		JLabel room = new JLabel("Current Room");
		JLabel weapon = new JLabel("Weapon");
		
		roomName.setText(currentRoom.getName());
		//make sure the player cannot edit the room name cause that would mess up the suggestion
		roomName.setEditable(false);
		
		add(room);
		add(roomName);		
		add(person);
		add(playerBox);
		add(weapon);
		add(weaponBox);
		
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new SubmitListener());
		add(submitButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new CancelListener());
		add(cancelButton);
		
		setSize(400, 200);
		setVisible(true);
	}
	
	private class SubmitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			String suggestedPersonName = playerBox.getSelectedItem().toString();
			String room = roomName.getText();
			String weapon = weaponBox.getSelectedItem().toString();
			
			Card playerCard = findCorrectCard(suggestedPersonName, playerCards);
			Card roomCard = findCorrectCard(room, roomCards);
			Card weaponCard = findCorrectCard(weapon, weaponCards);
			
			HumanPlayer humanPlayer = board.getHumanPlayer();
			Card guessResult = board.handleSuggestions(playerCard, roomCard, weaponCard, humanPlayer);
			gameControl.setGuess(suggestedPersonName + ", " + room + ", " + weapon);
			if (guessResult != null) { //player's card was disproven
				KnownCardsPanel knownCards = theGame.getKnownCards();
				Player cardDisprover = board.getCardDisprover();
				gameControl.setGuessResult(guessResult.getCardName() + " - disproven by " + cardDisprover.getName());
				humanPlayer.updateSeen(guessResult);
				board.setHumanPlayer(humanPlayer);
				knownCards.updatePanels();
				theGame.setVisible(true); //need to make the frame visible again so that the panel actually updates
			} else {
				gameControl.setGuessResult("No card was given to disprove your suggestion.");
			}
			//update the player who was pulled into the room
			ArrayList<Player> players = board.getPlayers();
			for (Player player : players) { //move suggested player into the room
				if (player.getName().equals(suggestedPersonName)) {
					player.setRow(humanPlayer.getRow());
					player.setCol(humanPlayer.getCol());
					player.setPulledIn(true);
					break; //break so that no other player gets moved into the room with them
				}
			}
			board.repaint(); //repaint is here because otherwise the player doesn't actually get moved for some reason
		}

		private Card findCorrectCard(String chosenCard, ArrayList<Card> cards) {
			//grab the correct selected card based on the name of the player choice
			for (Card card : cards) {
				if (card.getCardName().equals(chosenCard)) {
					return card;
				}
			}
			return null;
		}	
	}
	
	private class CancelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//close the dialog with no penalty
			setVisible(false);
		}	
	}
	
	private String[] createNameArray(ArrayList<Card> cards) {
		//turn the cards into an array of the string names
		String[] cardNames = new String[cards.size()];
		for (int i = 0; i < cards.size(); i++) {
			cardNames[i] = cards.get(i).getCardName();
		}
		
		return cardNames;
	}
}
