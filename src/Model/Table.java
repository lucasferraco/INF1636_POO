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
}
