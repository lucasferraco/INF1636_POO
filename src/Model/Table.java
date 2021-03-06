package Model;

import java.util.ArrayList;

public class Table {
	public ArrayList<Card> cards;
	private int points;
	
	public Table() {
		cards = new ArrayList<Card>();
		points = 0;
	}

	public int getPoints() {
		return points;
	}
	
	public void addPoints(int numPoints) {
		points += numPoints;
	}
	
	public ArrayList<Card> reset() {
		ArrayList<Card> myCards = cards;
		
		cards.clear();
		points = 0;
		
		return myCards;
	}
}
