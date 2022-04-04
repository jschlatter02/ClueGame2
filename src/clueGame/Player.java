package clueGame;

import java.awt.Color;
import java.util.*;

public abstract class Player {
	private String name;
	private Color color;
	private int row, col;
	private Set<Card> hand;
	
	public abstract void updateHand(Card card);

	public Player(String name, int row, int col, String color) {
		this.name = name;
		this.row = row;
		this.col = col;
		
		switch(color) {
			case "green":
				this.color = Color.GREEN;
				break;
			case "red":
				this.color = Color.RED;
				break;
			case "black":
				this.color = Color.BLACK;
				break;
			case "Magenta":
				this.color = Color.MAGENTA;
				break;
			case "orange":
				this.color = Color.ORANGE;
				break;
			case "yellow":
				this.color = Color.YELLOW;
				break;
		}
		
		hand = new HashSet<Card>();
	}
	
	public Set<Card> getHand() {
		return hand;
	}

	public String getName() {
		return name;
	}
	
	public Card disproveSuggestion(Card playerCard, Card roomCard, Card weaponCard) {
		return null;
	}
	
}
