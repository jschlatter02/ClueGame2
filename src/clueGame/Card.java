package clueGame;

public class Card {
	private String cardName;
	private CardType cardType;
	
	
	public Card(String cardName, CardType cardType) {
		this.cardName = cardName;
		this.cardType = cardType;
	}
	
	
	
	public CardType getCardType() {
		return cardType;
	}

	public boolean equals(Card target) {
		return true;
	}
}
