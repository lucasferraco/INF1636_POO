package Controller;

import java.awt.Dimension;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import View.*;
import Model.*;
import SupportingFiles.Observer;
import SupportingFiles.Subject;

public final class GameController implements Observer {
	private ArrayList<Card> deck;
	private TableScreen tableView;
	private Table table;
	
	private int numberOfPlayers;
	public ArrayList<GamblerController> gamblersControllers;
	public int currentPlayer;
		
	// Sligleton Definition
	private static final GameController theOnlyInstance = new GameController();
	
	public static GameController getInstance() {
		return theOnlyInstance;
	}
	
	private GameController() {
		deck = new ArrayList<Card>();
		
		HomeScreen home = new HomeScreen(this);
		home.setVisible(true);
	}
	
	public void initializeGame(int numberOfPlayers) {
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		
		table = new Table();
		tableView = new TableScreen(screenSize.getWidth()/2, 200);
		tableView.setListeners(this);
		tableView.register(this);
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
			
			int totalSurrendered = 0;
			for(int i = 0; i < numberOfPlayers; i++) {
				if (gamblersControllers.get(i).getPlayerState() == PlayerState.Surrendered) {
					totalSurrendered++;
				}
			}
			
			if (totalSurrendered == numberOfPlayers)
				tableView.showResetOption();
			else
				endBets();
		}
		else {
			gamblersControllers.get(currentPlayer).setBettingView();
		}
	}
	
	public void endBets() {
		// Prepare playing scenario
		for(int i = 0; i < numberOfPlayers; i++) 
			if(gamblersControllers.get(i).getPlayerState() != PlayerState.Surrendered) 
				gamblersControllers.get(i).play();
		
		for(int i = 0; i < numberOfPlayers; i++) {
			if(gamblersControllers.get(i).getPlayerState() != PlayerState.Surrendered) {
				gamblersControllers.get(i).hit();
				gamblersControllers.get(i).hit();
			}
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
				for(int i = 0; i < numberOfPlayers; i++)
					if (gamblersControllers.get(i).getPlayerState() == PlayerState.Waiting)
						gamblersControllers.get(i).setResult(PlayerState.Won);
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
	
	// Game Flow Methods
	
	public void reset() {
		ArrayList<Card> distributedCards = new ArrayList<Card>();
		
		for(int i = 0; i < numberOfPlayers; i++)
			distributedCards.addAll(gamblersControllers.get(i).reset());
		
		for(int i = 0; i < numberOfPlayers; i++) {
			if (	gamblersControllers.get(i).getPlayerState() == PlayerState.Broke) {
				gamblersControllers.remove(i);
				numberOfPlayers--;
				i--;
			}
		}
		
		distributedCards.addAll(table.reset());
		tableView.clear();

		currentPlayer = -1;
		deck.addAll(distributedCards);
		shuffleDeck();
		
		nextPlayerToBet();
	}

	public void save(String path) {
		System.out.println("GameController : save() : " + path );
		
		try {
			PrintWriter writer = new PrintWriter(path + ".txt", "UTF-8");
			
			// Store table
			
			writer.printf("Table");
			writer.println();
			
			writer.printf("Points " + table.getPoints());
			writer.println();
			
			writer.printf("Cards");
			for(Card card : table.cards)
				writer.printf(" " + card.number + " " + card.suit);
			
			writer.println();
			writer.println();
			
			// Store players
			
			for (int i = 0; i < numberOfPlayers; i++) {
				writer.printf("Gambler " + i); // Player id
				writer.println();
				
				writer.printf("Chips " + gamblersControllers.get(i).getPlayerChips());
				writer.println();
				
				writer.printf("Bet " + gamblersControllers.get(i).getPlayerBet());
				writer.println();
				
				writer.printf("Points " + gamblersControllers.get(i).getPlayerPoints());
				writer.println();
				
				writer.printf("State " + gamblersControllers.get(i).getPlayerState());
				writer.println();
				
				writer.printf("Cards");
				for(Card card : gamblersControllers.get(i).getPlayerCards())
					writer.printf(" " + card.number + " " + card.suit);
				
				writer.println();
				writer.println();
			}
			
			writer.close();
		} 
		catch (IOException e) {
			System.out.println("GameController : save() : error = " + e.getMessage());
			System.exit(1);
		}
		
		end();
	}
	
	public void retrieveSavedGame() {
		
	}
	
	public void end() {
		System.exit(0);
	}
	
	// Observer Methods
	
	@Override
	public void update(int value) {
		gamblersControllers.get(currentPlayer).bet(value);
	}
	
}
