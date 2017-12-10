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
	private int totalBuy;
	private int totalChips;
	
	private int defaultValue = 1000;
	private int remainingBuys = 2;

	public Gambler(int id) {
		this.id = id;
		
		state = PlayerState.Betting;
		points = 0;
		cards = new ArrayList<Card>();
		
		totalBet = 0;
		totalBuy = 0;
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
	
	public void setPoints(int points) {
		this.points = points;
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

	public void setBet(int bet) {
		totalBet = bet;
	}

	public void addBet(int newBet) {
		totalBet += newBet;
		totalChips -= newBet;
	}
	
	public int getChips() {
		return totalChips;
	}
	
	public void setChips(int chips) {
		totalChips = chips;
	}

	public int getBuy() {
		return totalBuy;
	}
	
	public boolean addChips(int newChips) {
		if (totalBuy + newChips > defaultValue/2)
			return false;
		
		totalBuy += newChips;
		return true;
	}
	
	public void addWonChips(int prize) {
		totalChips += prize;
		totalBet = 0;
	}
	
	public int addBuyedChips() {
		if (remainingBuys == 0)
			return 0;
		
		totalChips += totalBuy;
		totalBuy = 0;
		remainingBuys--;
		
		return remainingBuys;
	}
	
	public int getRemainingBuys() {
		return remainingBuys;
	}
	
	public ArrayList<Card> reset() {
		ArrayList<Card> myCards = cards;
		
		if (state != PlayerState.Broke)
			state = PlayerState.Betting;			
		
		points = 0;
		cards.clear();
		
		totalBet = 0;
		totalBuy = 0;
		return myCards;
	}
	
}
