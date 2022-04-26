package clueGame;

public class HumanPlayer extends Player{

	
	public HumanPlayer(String name, int row, int col, String color) {
		super(name, row, col, color);
	}
	
	@Override
	public void updateHand(Card card) {
		super.getHand().add(card);
	}

	@Override
	public void updateSeen(Card card) {
		super.getSeenCards().add(card);
	}
	
	@Override
	public BoardCell selectTarget() {
		return null;
	}

	@Override
	public Solution createSuggestion(Room currentRoom, boolean cannotDisprove) {
		return null;
	}
}
