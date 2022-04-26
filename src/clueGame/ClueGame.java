package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	private static Board board;
	private static ClueGame theGame;
	private KnownCardsPanel knownCards;

	private ClueGame() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.deal();

		Player humanPlayer = board.getHumanPlayer();
		
		
		knownCards = new KnownCardsPanel();
		add(knownCards, BorderLayout.EAST);
		board.setKnownCards(knownCards); //allow board to change the known cards panel from mousePressed
		GameControlPanel gameControl = new GameControlPanel();
		add(gameControl, BorderLayout.SOUTH);
		board.setGameControl(gameControl); //do the same for the game control panel

		add(board, BorderLayout.CENTER);

		board.addMouseListener(board); //so we can actually click on the board

		setSize(750, 780);
		setTitle("Clue Game - CSCI 306");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JOptionPane.showMessageDialog(this, "You are " + humanPlayer.getName() + ".\nCan you find the solution\nbefore the Computer players?");
		board.nextButton();
	}
	
	public static ClueGame getInstance() {
		return theGame;
	}

	public KnownCardsPanel getKnownCards() {
		return knownCards;
	}

	public static void main(String[] args) {
		theGame = new ClueGame();
	}

}
