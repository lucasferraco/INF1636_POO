package Controller;

import Model.Gambler;
import Model.PlayerState;
import View.GamblerScreen;
import View.TableScreen;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;

import Model.Card;

public class GamblerController {
	private static int nextXPosition = 10;
	private static GameController gameManager = GameController.getInstance();
	
	private GamblerScreen view;
	private Gambler gambler;
	
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
		gambler.addBet(chipValue);
		
		view.updateChipsLabels(gambler.getBet(), gambler.getChips());
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
			gambler.addPoints(removedCard.number);
			gambler.cards.add(removedCard);
			
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

	public PlayerState getPlayerState() {
		return gambler.getState();
	}
	
	public int getPlayerPoints() {
		return gambler.getPoints();
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
		}
		
		view.updateChipsLabels(gambler.getBet(), gambler.getChips());
		view.addResultLabel(gambler.getState());
		view.disablePlayingButtons();
	}

	public ArrayList<Card> reset() {
		ArrayList<Card> myCards = new ArrayList<Card>();
		
		if (gambler.getState() != PlayerState.Broke) {
			myCards = gambler.reset();
			
			view.updateChipsLabels(gambler.getBet(), gambler.getChips());
			view.clear();		
		}
		else if (!gambler.cards.isEmpty()) {
			myCards = gambler.cards;
			gambler.cards.clear();
		}
		
		return myCards;
	}
}
