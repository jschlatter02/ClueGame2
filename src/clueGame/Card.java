package clueGame;

public class Card {
	private String cardName;
	private CardType cardType;
	
	
	public Card(String cardName, CardType cardType) {
		this.cardName = cardName;
		this.cardType = cardType;
	}
	
	public String getCardName() {
		return cardName;
	}
	
	public CardType getCardType() {
		return cardType;
	}

	public boolean equals(Card target) {
		return cardName.equals(target.cardName);
	}
}
