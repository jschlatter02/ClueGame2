package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ClueGame extends JFrame{
	private static Board board;
	
	public ClueGame() {
		board = board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.deal();
		
		GameControlPanel gameControl = new GameControlPanel();
		add(gameControl, BorderLayout.SOUTH);
		
		KnownCardsPanel knownCards = new KnownCardsPanel(board);
		add(knownCards, BorderLayout.EAST);
		
		add(board, BorderLayout.CENTER);
		
		setSize(780, 780);
		setTitle("Clue Game - CSCI 306");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[] args) {
		ClueGame theGame = new ClueGame();
	}
	
}
