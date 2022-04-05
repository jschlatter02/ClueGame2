package clueGame;

import java.util.Set;

public class ComputerPlayer extends Player{

	public ComputerPlayer(String name, int row, int col, String color) {
		super(name, row, col, color);
	}

	@Override
	public void updateHand(Card card) {
		super.getHand().add(card);
	}
	
	public Solution createSuggestion() {
		return null;
	}

}
