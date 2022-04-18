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
	
	public boolean equals(Solution o) {
		//check each individual card and if they are equal then set their respective boolean to true
		boolean bRoom = false;
		boolean bWeapon = false;
		boolean bPerson = false;
		o = (Solution) o;
		
		if (room.equals(o.room)) {
			bRoom = true;
		}
		
		if (weapon.equals(o.weapon)) {
			bWeapon = true;
		}
		
		if (person.equals(o.person)) {
			bPerson = true;
		}
		
		return (bRoom && bWeapon && bPerson);
	}

	public Solution() {
		super();
	}

	public Card getRoom() {
		return room;
	}

	public Card getPerson() {
		return person;
	}

	public Card getWeapon() {
		return weapon;
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
