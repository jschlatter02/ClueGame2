package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

public abstract class Player {
	private String name;
	private Color color;
	private int row, col;
	private Set<Card> hand;
	private Set<Card> seenCards;
	
	public abstract void updateHand(Card card);
	public abstract BoardCell selectTarget();

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
			case "magenta":
				this.color = Color.MAGENTA;
				break;
			case "orange":
				this.color = Color.ORANGE;
				break;
			case "white":
				this.color = Color.WHITE;
				break;
		}
		
		hand = new HashSet<Card>();
		seenCards = new HashSet<Card>();
	}
	
	public Card disproveSuggestion(Card playerCard, Card roomCard, Card weaponCard) {
		ArrayList<Card> disprovenCards = new ArrayList<Card>();
		//check if the player has any cards that 
		addDisproven(playerCard, disprovenCards);
		addDisproven(roomCard, disprovenCards);
		addDisproven(weaponCard, disprovenCards);
		
		if (disprovenCards.size() >= 1) { //one if statement for >= 1 because it helps cut down code
			//if size is 1 then it'll just randomly select 1
			Random rand = new Random();
			int setIndex = rand.nextInt(disprovenCards.size());
			return disprovenCards.get(setIndex);
		} else {
			return null;
		}
	}

	private void addDisproven(Card card, ArrayList<Card> disprovenCards) {
		if (hand.contains(card)) {
			disprovenCards.add(card);
		}
	}
	
	public void drawPlayer(Graphics graphics, int width, int height, int horizontalOffset, int topOffset) {
		//find position here
		horizontalOffset += width * col;
		topOffset += height * row;
		graphics.setColor(color);
		graphics.fillOval(horizontalOffset, topOffset, width, height);
		graphics.setColor(Color.BLACK); //have black outline so the player is easier to see
		graphics.drawOval(horizontalOffset, topOffset, width, height);
	}
	
	public void updateSeen(Card seenCard) {
		seenCards.add(seenCard);
	}
	
	public Set<Card> getHand() {
		return hand;
	}

	public String getName() {
		return name;
	}

	public Set<Card> getSeenCards() {
		return seenCards;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	
}
