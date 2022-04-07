package clueGame;

import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class KnownCardsPanel extends JPanel {
	//local variables so that we can update the panels when we add cards to the seen
	private JPanel playerPanel, roomPanel, weaponPanel;
	private static Board board;
	
	public KnownCardsPanel() {
		setLayout(new GridLayout(3,0));
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		
		playerPanel = new JPanel();
		updatePanel(playerPanel, CardType.PERSON);
		add(playerPanel);
		
		roomPanel = new JPanel();
		updatePanel(roomPanel, CardType.ROOM);
		add(roomPanel);
		
		weaponPanel = new JPanel();
		updatePanel(weaponPanel, CardType.WEAPON);
		add(weaponPanel);
	}
	
	private void updatePanel(JPanel panel, CardType cardType) {
		HumanPlayer humanPlayer = board.getHumanPlayer();
		JLabel handLabel = new JLabel("In Hand:");
		
		
	}

	public static void main(String[] args) {
		board = board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.deal();
		
		KnownCardsPanel panel = new KnownCardsPanel();  
		JFrame frame = new JFrame();  
		frame.setContentPane(panel); 
		frame.setSize(180, 700);  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
