package clueGame;

public class HumanPlayer extends Player{

	
	public HumanPlayer(String name, int row, int col, String color) {
		super(name, row, col, color);
	}
	
	@Override
	public void updateHand(Card card) {
		hand.add(card);
	}
}
