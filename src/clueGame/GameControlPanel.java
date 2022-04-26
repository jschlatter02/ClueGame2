package clueGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {
	// Created instance variables so that we can update the text fields later on.
	private JTextField turnTextField;
	private JTextField rollTextField;
	private JTextField guessTextField = new JTextField(30);	// initialized it so that it does not throw a null pointer exception.
	private JTextField guessResultTextField = new JTextField(30);
	private static Board board = Board.getInstance();
	private AccusationDialog showAccusation;

	public GameControlPanel() {
		setLayout(new GridLayout(2,0));
		JPanel upperPanel = createUpperPanel();
		JPanel lowerPanel = createLowerPanel();
		add(upperPanel);
		add(lowerPanel);
	}

	private JPanel createUpperPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,4));

		JPanel turnPanel = new JPanel();
		JLabel label = new JLabel("Whose turn?");
		turnTextField = new JTextField(15);
		//BorderLayout helps center the labels and textFields
		turnPanel.add(label, BorderLayout.NORTH);
		turnTextField.setEditable(false);
		turnPanel.add(turnTextField, BorderLayout.CENTER);

		panel.add(turnPanel);

		JPanel rollPanel = new JPanel();
		label = new JLabel("Roll: ");
		rollTextField = new JTextField(5);
		//puts both label and textField at the top
		rollPanel.add(label, BorderLayout.NORTH);
		rollTextField.setEditable(false);
		rollPanel.add(rollTextField, BorderLayout.NORTH);

		panel.add(rollPanel);

		JButton accusationButton = new JButton("Make Accusation");
		accusationButton.addActionListener(new AccusationListener());
		panel.add(accusationButton);

		JButton nextButton = new JButton("NEXT!");
		nextButton.addActionListener(new NextButtonListener());
		panel.add(nextButton);
		return panel;
	}

	private class NextButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			board.nextButton();
		}	
	}
	
	private class AccusationListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!board.isFinished()) {
				//created a new class so that we can easily add different combo boxes to the Dialog
				showAccusation = new AccusationDialog();
			} else {
				JOptionPane.showMessageDialog(board, "You cannot accuse when it is not your turn.");
			}
		}	
	}

	private JPanel createLowerPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));

		JPanel guessPanel = createGuessTextFields(guessTextField, "Guess"); 
		panel.add(guessPanel);

		JPanel guessResultPanel = createGuessTextFields(guessResultTextField, "Guess Result"); 
		panel.add(guessResultPanel);
		return panel;
	}


	// Added this method because adding the textfields for both panels are exactly the same.
	private JPanel createGuessTextFields(JTextField textField, String message) {
		JPanel guessPanel = new JPanel();
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(),message));
		textField.setEditable(false);
		guessPanel.add(textField, BorderLayout.WEST);

		return guessPanel;
	}

	public void setTurn(Player comPlayer, int roll) {
		turnTextField.setText(comPlayer.getName());
		rollTextField.setText(String.valueOf(roll));
	}

	public void setGuess(String message) {
		guessTextField.setText(message);
	}

	public void setGuessResult(String message) {
		guessResultTextField.setText(message);
	}

	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  
		JFrame frame = new JFrame();  
		frame.setContentPane(panel); 
		frame.setSize(750, 180);  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// test filling in the data
		panel.setTurn(new ComputerPlayer( "Col. Mustard", 0, 0, "orange"), 5);
		panel.setGuess( "I have no guess!");
		panel.setGuessResult( "So you have nothing?");
	}
}
