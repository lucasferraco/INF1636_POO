package Model;

import java.util.ArrayList;

import Model.Card;
import Model.PlayerState;

public class Gambler {
	public int id;
	
	private PlayerState state;
	private int points;
	public ArrayList<Card> cards;
	
	private int[] chips;
	private int totalBet;
	private int avaiableMoney;

	public Gambler(int id) {
		cards = new ArrayList<Card>();
		this.id = id;
		state = PlayerState.Playing;
		points = 0;
		totalBet = 0;
	}
	
	public void setState(PlayerState newState) {
		state = newState;
		
		if (newState == PlayerState.Playing) {
			reset();
		}
	}
	
	public PlayerState getState() {
		return state;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void addPoints(int numPoints) {
		points += numPoints;
		
		if (points > 21) {
			state = PlayerState.Lost;
		}
	}
	
	public void reset() {
		cards.clear();
		points = 0;
	}
}
