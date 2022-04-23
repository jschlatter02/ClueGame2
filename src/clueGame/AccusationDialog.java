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

public class AccusationDialog extends JDialog {
	private static Board board = Board.getInstance();
	private JComboBox<String> playerBox;
	private JComboBox<String> roomBox;
	private JComboBox<String> weaponBox;
	ArrayList<Card> playerCards = board.getPlayerCards();
	ArrayList<Card> roomCards = board.getRoomCards();
	ArrayList<Card> weaponCards = board.getWeaponsCards();
	
	public AccusationDialog() {
		String[] playerNames = createNameArray(playerCards);
		String[] roomNames = createNameArray(roomCards);
		String[] weaponNames = createNameArray(weaponCards);
		
		playerBox = new JComboBox<String>(playerNames);
		roomBox = new JComboBox<String>(roomNames);
		weaponBox = new JComboBox<String>(weaponNames);
		
		//gives enough room for the labels, comboboxes, and buttons
		setLayout(new GridLayout(4,4));
		setTitle("Make an Accusation!");
		
		JLabel person = new JLabel("Person");
		JLabel room = new JLabel("Room");
		JLabel weapon = new JLabel("Weapon");
		
		add(person);
		add(playerBox);
		add(room);
		add(roomBox);
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

	private class CancelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//close the dialog with no penalty
			setVisible(false);
		}	
	}
	
	private class SubmitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			String person = playerBox.getSelectedItem().toString();
			String room = roomBox.getSelectedItem().toString();
			String weapon = weaponBox.getSelectedItem().toString();
			
			Card playerCard = findCorrectCard(person, playerCards);
			Card roomCard = findCorrectCard(room, roomCards);
			Card weaponCard = findCorrectCard(weapon, weaponCards);
			
			Solution playerGuess = new Solution(roomCard, playerCard, weaponCard);
			boolean guessResult = board.checkAccusation(playerGuess);
			if (guessResult) {
				JOptionPane.showMessageDialog(null, "You are correct! You win!");
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(null, "You are wrong. You lose!");
				System.exit(0);
			}
		}

		private Card findCorrectCard(String person, ArrayList<Card> cards) {
			for (Card playerCard : cards) {
				if (playerCard.getCardName().equals(person)) {
					return playerCard;
				}
			}
			return null;
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
