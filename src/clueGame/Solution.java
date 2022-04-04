package clueGame;

public class Solution {
	private Card room;
	private Card person;
	private Card weapon;

	public Solution(Card room, Card person, Card weapon) {
		super();
		this.room = room;
		this.person = person;
		this.weapon = weapon;
	}

	public Solution() {
		super();
	}

	public void setRoom(Card room) {
		this.room = room;
	}

	public void setPerson(Card person) {
		this.person = person;
	}

	public void setWeapon(Card weapon) {
		this.weapon = weapon;
	}
	
	
}
