package Model;

import java.util.ArrayList;

import Model.Card;
import Model.PlayerState;

public class Gambler {
	public int id;
	
	private PlayerState state;
	private int points;
	public ArrayList<Card> cards;
	
	private int totalBet;
	private int totalChips;

	public Gambler(int id) {
		this.id = id;
		
		state = PlayerState.Betting;
		points = 0;
		cards = new ArrayList<Card>();
		
		totalBet = 0;
		totalChips = 1000;
	}
	
	public PlayerState getState() {
		return state;
	}
	
	public void setState(PlayerState newState) {
		state = newState;
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
	
	public int getBet() {
		return totalBet;
	}

	public void addBet(int newBet) {
		totalBet += newBet;
		totalChips -= newBet;
	}
	public int getChips() {
		return totalChips;
	}

	public void addChips(int newChips) {
		totalChips += newChips;
	}

	public void reset() {
		cards.clear();
		points = 0;
		totalBet = 0;
	}
}
