package clueGame;

import java.awt.Color;

public abstract class Player {
	private String name;
	private String color;
	private int row, col;
	
	public abstract void updateHand(Card card);
}
