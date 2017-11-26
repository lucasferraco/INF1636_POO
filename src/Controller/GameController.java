package Controller;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;

import View.*;
import Model.*;

public final class GameController {
	private ArrayList<Card> deck;
	private TableScreen tableView;
	private Table table;
	
	private int numberOfPlayers;
	public ArrayList<GamblerController> gamblersControllers;
	public int currentPlayer;
	
	// Sligleton Definition
	private static final GameController theOnlyInstance = new GameController();
	
	private GameController() {
		deck = new ArrayList<Card>();
		
		HomeScreen home = new HomeScreen(this);
		home.setVisible(true);
	}
	
	public static GameController getInstance() {
		return theOnlyInstance;
	}
	
	public void initializeGame(int numberOfPlayers) {
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		
		table = new Table();
		tableView = new TableScreen(screenSize.getWidth()/2, 200);
		tableView.setListeners(this);
		tableView.setVisible(true);
		initializeDeck();
		
		this.numberOfPlayers = numberOfPlayers;
		currentPlayer = -1;
		gamblersControllers = new ArrayList<GamblerController>();
		
		for(int i = 0; i < numberOfPlayers; i++) 
			gamblersControllers.add(new GamblerController(i));
		
		nextPlayerToBet();
	}
	
	public void initializeDeck() {
		for(int i = 0; i < 13; i++) {
			deck.add(new Card(Suit.Clubs   , i+1));
			deck.add(new Card(Suit.Diamonds, i+1));
			deck.add(new Card(Suit.Hearts  , i+1));
			deck.add(new Card(Suit.Spades  , i+1));
		}
		
		shuffleDeck();
	}
	
	public void shuffleDeck() {
		Collections.shuffle(deck);
	}
	
	public void nextPlayerToBet() {
		currentPlayer++;
		
		if (currentPlayer == numberOfPlayers) {
			currentPlayer = 0;
			endBets();
		}
		else {
			gamblersControllers.get(currentPlayer).setBettingView();
		}
	}
	
	public void endBets() {
		// Prepare playing scenario
		for(int i = 0; i < numberOfPlayers; i++)
			gamblersControllers.get(i).play();
			
		for(int i = 0; i < numberOfPlayers; i++) {
			gamblersControllers.get(i).hit();
			gamblersControllers.get(i).hit();
		}
		
		tableView.drawUpsideDownCard();
		getNewCard();
	}
	
	public Card popCard() {	
		if (deck.isEmpty()) {
			return null;
		}
		
		return deck.remove(0);
	}
	
	private void getNewCard() {
		Card tableCard = deck.remove(0);
		
		table.cards.add(tableCard);
		table.addPoints(tableCard.number);
		
		tableView.updateLabel(table.getPoints());
		tableView.panel.drawCard(tableCard.imageString());
	}
	
	public void endRound() {
		boolean everyPlayerHasEnded = true;
		
		for(int i = 0; i < numberOfPlayers; i++) {
			PlayerState playerCurrentState = gamblersControllers.get(i).getPlayerState();
			
			if (playerCurrentState == PlayerState.Playing) {
				everyPlayerHasEnded = false;
				break;
			}
		}
		
		if (everyPlayerHasEnded) {
			tableView.removeUpsideDownCard();
			getNewCard();
			
			while (table.getPoints() < 17 )
				getNewCard();
			
			if (table.getPoints() > 21) {
				for(int i = 0; i < numberOfPlayers; i++) {
					if (gamblersControllers.get(i).getPlayerState() == PlayerState.Waiting) {
						gamblersControllers.get(i).setResult(PlayerState.Won);
						System.out.println("player " + (i+1) + " chips = " + gamblersControllers.get(i).getPlayerState());
					}
				}
			}
			else {
				int tablePoints = table.getPoints();
				
				for(int i = 0; i < numberOfPlayers; i++) {
					int playerPoints = gamblersControllers.get(i).getPlayerPoints();
					PlayerState playerState = gamblersControllers.get(i).getPlayerState();
					
					if (playerState == PlayerState.Waiting) {
						int result = playerPoints - tablePoints;
						if (result != 0)
							result /= Math.abs(result);
						
						PlayerState resultState = PlayerState.getStateWith(result);
						gamblersControllers.get(i).setResult(resultState);
					}
				}
			}
			
			tableView.showResetOption();
		}
	}
	
	public void reset() {
		for(int i = 0; i < numberOfPlayers; i++)
			gamblersControllers.get(i).reset();
		
		table.reset();
		tableView.clear();

		currentPlayer = -1;
		nextPlayerToBet();
	}
}
