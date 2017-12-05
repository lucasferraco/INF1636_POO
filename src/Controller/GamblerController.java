package Controller;

import Model.Gambler;
import Model.PlayerState;
import Model.Card;

import View.GamblerScreen;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Timer;
import java.awt.event.ActionListener;

public class GamblerController {
	private static int nextXPosition = 10;
	private static GameController gameManager = GameController.getInstance();
	
	private GamblerScreen view;
	private Gambler gambler;
	private int numberOfAces = 0;
	private int[] acesValues = {-1, -1, -1, -1};
	
	public GamblerController(int playerId) {
		gambler = new Gambler(playerId);
		createPlayerView();
	}
	
	public void createPlayerView() {
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		double y = screenSize.getHeight() - 320;
		
		view = new GamblerScreen(String.valueOf(gambler.id + 1), nextXPosition, y, 300, 300);
		view.setListeners(this);
		view.disableBettingButtons();
		view.disablePlayingButtons();
		view.updateChipsLabels(gambler.getBet(), gambler.getChips());
		view.setVisible(true);
		
		nextXPosition += 320;
	}
	
	public void setBettingView() {
		view.toFront();
		view.enableBettingButtons();
	}
	
	public void bet(int chipValue) {
		if (gambler.getChips() >= 0 && chipValue <= gambler.getChips()) {
			gambler.addBet(chipValue);
			
			view.updateChipsLabels(gambler.getBet(), gambler.getChips());
		}
		else if (gambler.getChips() == 0) {
			if (gambler.getBet() != 0)
				view.displayError("You are all in!");
			else
				view.displayError("Buy chips to continue playing.");
		}
		else {
			view.displayError("Your bet has to be smaller then your chips.");
		}
	}
	
	public void endBetting() {
		if (gambler.getBet() != 0) {
			view.disableBettingButtons();
			gameManager.nextPlayerToBet();
		}
		else {
			view.displayError("You have to bet at least 1 chip.");
		}
	}
	
	public void surrender() {
		PlayerState currentState = gambler.getState();
		setResult(PlayerState.Surrendered);
		
		if (currentState == PlayerState.Playing) {
			gameManager.endRound();
		}
		else {
			gameManager.nextPlayerToBet();
		}
	}
	
	public void buyChips() {
		int remainingBuys = gambler.addBuyedChips();
		
		view.updateChipsLabels(gambler.getBet(), gambler.getChips());
		
		if (remainingBuys == 0) {
			view.enableBuyButton(false);
		}
	}
	
	public void play() {
		gambler.setState(PlayerState.Playing);
		view.enablePlayingButtons();
	}
	
	public void hit() {
		Card removedCard = gameManager.popCard();
		
		if (removedCard != null) {
			gambler.cards.add(removedCard);
			
			if (removedCard.number == 1) {
				numberOfAces++;
				
				if (gambler.getPoints() + 11 > 21) {
					gambler.addPoints(removedCard.number);
					acesValues[numberOfAces - 1] = removedCard.number;
				}
				else {
					gambler.addPoints(removedCard.number + 10);
					acesValues[numberOfAces - 1] = removedCard.number + 10;
				}
			}
			else {
				gambler.addPoints(removedCard.number);
			}
			
			if (numberOfAces > 0 && gambler.getPoints() > 21) {
				for (int i = 0; i < numberOfAces; i++) {
					if (acesValues[i] == 11) {
						int currentPoints = gambler.getPoints();
						acesValues[i] = 1;
						
						gambler.setPoints(currentPoints - 10);
					}
				}
			}
			
			view.updatePointsLabel(gambler.getPoints());
			view.panel.drawCard(removedCard.imageString());
			
			if (gambler.getPoints() > 21) {
				setResult(PlayerState.Lost);
				gameManager.endRound();
			}
			else if (gambler.getPoints() == 21) {
				setResult(PlayerState.Won);
				gameManager.endRound();				
			}
		}
	}
	
	public void doublePlay() {
		bet(gambler.getBet());
		hit();
		
		if (gambler.getPoints() <= 21) {
			stand();
		}
	}
	
	public void stand() {
		view.disablePlayingButtons();
		gambler.setState(PlayerState.Waiting);
		gameManager.endRound();
	}
	
	public void setResult(PlayerState result) {
		gambler.setState(result);
		
		if (result == PlayerState.Won) {
			if (gambler.getPoints() == 21) {
				gambler.addChips((5 * gambler.getBet()) / 2);
			}
			else {
				gambler.addChips(2 * gambler.getBet());
			}
		}
		else if (result == PlayerState.Draw) {
			gambler.addChips(gambler.getBet());
		}
		
		if (gambler.getChips() == 0) {
			gambler.setState(PlayerState.Broke);
			
			if(gambler.getRemainingBuys() == 0) {
				Timer t = new Timer(1 * 1000, null);
	            t.addActionListener(new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    view.dispose();
	                }
	            });
	            t.setRepeats(false);
	            t.start();
			}
		}
		
		view.updateChipsLabels(gambler.getBet(), gambler.getChips());
		view.addResultLabel(gambler.getState());
		view.disablePlayingButtons();
	}

	public ArrayList<Card> reset() {
		ArrayList<Card> myCards = new ArrayList<Card>();
		
		if (!(gambler.getRemainingBuys() == 0 && gambler.getState() == PlayerState.Broke)) {
			myCards = gambler.reset();
			
			view.updateChipsLabels(gambler.getBet(), gambler.getChips());
			view.clear();
		}
		
		if (!gambler.cards.isEmpty()) {
			myCards = gambler.cards;
			gambler.cards.clear();
		}
		
		return myCards;
	}
	
	public void updateUI() {
		view.updatePointsLabel(gambler.getPoints());
		view.updateChipsLabels(gambler.getBet(), gambler.getChips());
		
		PlayerState gamblerState = gambler.getState();
		if (gamblerState != PlayerState.Betting) {
			for(Card card : gambler.cards) 
				view.panel.drawCard(card.imageString());
			
			if (gamblerState.stateValue <= 1)
				setResult(gamblerState);
			
			if (gamblerState == PlayerState.Playing)
				view.enablePlayingButtons();
		}
	}
	
	// Facade methods to access Gambler infos

	public PlayerState getPlayerState() {
		return gambler.getState();
	}
	
	public void setPlayerState(PlayerState state) {
		gambler.setState(state);
	}
	
	public int getPlayerPoints() {
		return gambler.getPoints();
	}
	
	public void setPlayerPoints(int numPoints) {
		gambler.addPoints(numPoints);
	}
	
	public int getPlayerChips() {
		return gambler.getChips();
	}
	
	public void setPlayerChips(int chips) {
		gambler.setChips(chips);
	}
	
	public int getPlayerBet() {
		return gambler.getBet();
	}
	
	public void setPlayerBet(int bet) {
		gambler.setBet(bet);
	}
	
	public ArrayList<Card> getPlayerCards() {
		return gambler.cards;
	}
	
	public void setPlayerCards(ArrayList<Card> cards) {
		gambler.cards = cards;
	}
	
	public int getPlayerRemaingingBuys() {
		return gambler.getRemainingBuys();
	}
	
}
