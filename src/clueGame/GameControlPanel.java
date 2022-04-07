package clueGame;

import java.awt.*;

import javax.swing.*;

public class GameControlPanel extends JPanel{
	
	public GameControlPanel() {
		setLayout(new GridLayout(2,0));
		JPanel panel = createUpperPanel();
		add(panel);
	}
	
	private JPanel createUpperPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,4));
		
		JPanel turnPanel = new JPanel();
		JLabel label = new JLabel("Whose turn?");
		JTextField textField = new JTextField(15);
		//BorderLayout helps center the labels and textFields
		turnPanel.add(label, BorderLayout.NORTH);
		turnPanel.add(textField, BorderLayout.CENTER);
		
		panel.add(turnPanel);
		
		JPanel rollPanel = new JPanel();
		label = new JLabel("Roll: ");
		textField = new JTextField(5);
		//puts both label and textField at the top
		rollPanel.add(label, BorderLayout.NORTH);
		rollPanel.add(textField, BorderLayout.NORTH);
		
		panel.add(rollPanel);
		
		JButton accusationButton = new JButton("Make Accusation");
		panel.add(accusationButton);
		
		JButton nextButton = new JButton("NEXT!");
		panel.add(nextButton);
		return panel;
	}

	
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  
		JFrame frame = new JFrame();  
		frame.setContentPane(panel); 
		frame.setSize(750, 180);  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
}
