package Controller;

import Model.Gambler;
import Model.PlayerState;
import View.GamblerScreen;
import View.TableScreen;

import java.awt.Dimension;
import java.util.ArrayList;

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
		view.setVisible(true);
		
		nextXPosition += 320;
	}
	
	public void play() {
		gambler.setState(PlayerState.Playing);
		view.enableButtons();
	}
	
	public void hit() {
		Card removedCard = gameManager.popCard();
		
		if (removedCard != null) {
			gambler.addPoints(removedCard.number);
			gambler.cards.add(removedCard);
			
			view.updateLabel(gambler.getPoints());
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
	
	public void stand() {
		view.disableButtons();
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
		System.out.println("setResult " + gambler.getState());
		view.addResultLabel(gambler.getState());
		view.disableButtons();
	}

	public void reset() {
		view.clear();
		gambler.setState(PlayerState.Playing);
		view.enableButtons();
	}
}
