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
	
	@Override
	public boolean equals(Object o) {
		Card target = (Card) o;
		return cardName.equals(target.cardName);
	}
	
	@Override
	public int hashCode() {
		return cardName.hashCode();
	}
}
