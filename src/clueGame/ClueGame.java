package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	private static Board board;

	public ClueGame() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.deal();

		Player humanPlayer = board.getHumanPlayer();

		GameControlPanel gameControl = new GameControlPanel();
		add(gameControl, BorderLayout.SOUTH);
		//put board in the constructor because otherwise it throws a null pointer for board in KnownCards
		KnownCardsPanel knownCards = new KnownCardsPanel(board);
		add(knownCards, BorderLayout.EAST);

		add(board, BorderLayout.CENTER);

		board.addMouseListener(board); //so we can actually click on the board

		setSize(750, 780);
		setTitle("Clue Game - CSCI 306");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JOptionPane.showMessageDialog(this, "You are " + humanPlayer.getName() + ".\nCan you find the solution\nbefore the Computer players?");
		board.nextButton(gameControl);
	}


	public static void main(String[] args) {
		ClueGame theGame = new ClueGame();
	}

}
