package Model;

import java.util.ArrayList;

import Model.Card;

public class Gambler {
	public ArrayList<Card> cards;
	public int id;
	private int[] chips;
	private int totalPoints;
	private int totalBet;
	private int avaiableMoney;

	public Gambler(int id) {
		cards = new ArrayList<Card>();
		this.id = id;
		totalPoints = 0;
		totalBet = 0;
	}
	
	public void addPoint(int numPoints) {
		this.totalPoints += numPoints;
	}
}
