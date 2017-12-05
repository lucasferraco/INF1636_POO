package Controller;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import Model.Card;
import Model.PlayerState;
import Model.Suit;
import Model.Table;
import SupportingFiles.Observer;
import View.NewGameScreen;
import View.TableScreen;

public final class GameController implements Observer {
	static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	
	private ArrayList<Card> deck;
	private TableScreen tableView;
	private Table table;
	
	private int numberOfPlayers;
	public ArrayList<GamblerController> gamblersControllers;
	public int currentPlayer;
		
	// Sligleton Definition
	private static final GameController theOnlyInstance = new GameController();
	private BufferedReader bufferedReader;
	
	public static GameController getInstance() {
		return theOnlyInstance;
	}
	
	private GameController() {
		deck = new ArrayList<Card>();
		
		NewGameScreen home = new NewGameScreen(this);
		home.setVisible(true);
	}
	
	public void initializeGame(int numberOfPlayers) {
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
			
			while (table.getPoints() < 17)
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
			if (	gamblersControllers.get(i).getPlayerState() == PlayerState.Broke && gamblersControllers.get(i).getPlayerRemaingingBuys() == 0) {
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

	public void save(String filePath) {		
		try {
			PrintWriter writer = new PrintWriter(filePath + ".txt", "UTF-8");
			
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
			
			writer.printf("Gamblers " + numberOfPlayers);
			writer.println();
			
			for (int i = 0; i < numberOfPlayers; i++) {				
				writer.printf("Chips " + gamblersControllers.get(i).getPlayerChips());
				writer.println();
				
				writer.printf("Bet " + gamblersControllers.get(i).getPlayerBet());
				writer.println();
				
				writer.printf("Points " + gamblersControllers.get(i).getPlayerPoints());
				writer.println();
				
				writer.printf("State " + gamblersControllers.get(i).getPlayerState().stateValue);
				writer.println();
				
				writer.printf("Cards");
				for(Card card : gamblersControllers.get(i).getPlayerCards())
					writer.printf(" " + card.number + " " + card.suit);
				
				writer.println();
				writer.println();
			}
			
			writer.printf("End");
			writer.close();
		} 
		catch (IOException e) {
			System.out.println("GameController : save : error = " + e.getMessage());
			System.exit(1);
		}
		
		end();
	}
	
	public void retrieveSavedGame(String filePath) {
		try {
			bufferedReader = new BufferedReader(new FileReader(filePath));
		    String currentLine = bufferedReader.readLine();
		    String[] currentComponents = currentLine.split(" ");
		    
		    while (currentLine != null) {
		    		switch (currentComponents[0]) {
		    		case "Table":
		    			table = new Table();

		    			tableView = new TableScreen(screenSize.getWidth()/2, 200);
		    			tableView.setListeners(this);
		    			tableView.register(this);
		    			tableView.setVisible(true);
		    			initializeDeck();
		    			
		    			currentLine = bufferedReader.readLine();
				    currentComponents = currentLine.split(" ");
				    
				    if (currentComponents[0].compareTo("Points") == 0)
				    		table.addPoints(Integer.parseInt(currentComponents[1]));
				    else
				    		break;
				    
				    currentLine = bufferedReader.readLine();
				    currentComponents = currentLine.split(" ");
				    
				    if (currentComponents[0].compareTo("Cards") == 0) {
				    		for(int i = 1; i < currentComponents.length; i += 2) {
				    			Card newCard = new Card(Suit.getSuitWith(currentComponents[i + 1]), Integer.parseInt(currentComponents[i]));
				    			table.cards.add(newCard);
				    			
				    			deck.remove(newCard);
				    		}
				    }
				    
		    			break;
		    			
		    		case "Gamblers":
		    			numberOfPlayers = Integer.parseInt(currentComponents[1]);
		    		    currentPlayer = -1;
		    			gamblersControllers = new ArrayList<GamblerController>();
		    			
		    			for (int i = 0; i < numberOfPlayers; i++) {
		    				gamblersControllers.add(new GamblerController(i));
		    				GamblerController currentGambler = gamblersControllers.get(i);
		    				
		    				currentLine = bufferedReader.readLine();
						currentComponents = currentLine.split(" ");
						
						for (int j = 0; j < 5; j++) {
							switch (currentComponents[0]) {
							case "Chips":
								currentGambler.setPlayerChips(Integer.parseInt(currentComponents[1]));;
								break;
								
							case "Bet":
								currentGambler.setPlayerBet(Integer.parseInt(currentComponents[1]));
								break;
								
							case "Points":
								currentGambler.setPlayerPoints(Integer.parseInt(currentComponents[1]));
								break;
								
							case "State":
								currentGambler.setPlayerState(PlayerState.getStateWith(Integer.parseInt(currentComponents[1])));
								break;
								
							case "Cards":
								ArrayList<Card> cards = new ArrayList<Card>();
								
								for(int k = 1; k < currentComponents.length; k += 2) {
									Card newCard = new Card(Suit.getSuitWith(currentComponents[k + 1]), Integer.parseInt(currentComponents[k]));
									cards.add(newCard);
									
					    				deck.remove(newCard);
								}
								
								currentGambler.setPlayerCards(cards);
								break;
								
							default:
				    				System.out.println("GameController : retrieveSavedGame : invalid content");
				    				System.exit(1);
							}
							
							currentLine = bufferedReader.readLine();
							currentComponents = currentLine.split(" ");
						}
		    			}
		    			
		    			break;
		    			
		    		default:
		    			System.out.println("GameController : retrieveSavedGame : invalid content");
		    			System.exit(1);
		    		}
		        
		    		bufferedReader.readLine();
		        if (( currentLine = bufferedReader.readLine()) != null)
		        		currentComponents = currentLine.split(" ");
		    }
		}
		catch (IOException e) {
			System.out.println("GameController : retrieveSavedGame : error = " + e.getMessage());
			System.exit(1);			
		}
		
		updateUI();		
		
		boolean isEndOfRound = true;
		for (int i = 0; i < numberOfPlayers; i++) {
			gamblersControllers.get(i).updateUI();
			
			PlayerState playerState = gamblersControllers.get(i).getPlayerState();
			isEndOfRound = playerState != PlayerState.Playing || playerState != PlayerState.Waiting;
		}
		
		if (gamblersControllers.get(0).getPlayerState() == PlayerState.Betting)
			nextPlayerToBet();
		
		if (isEndOfRound)
			tableView.showResetOption();
	}
	
	private void updateUI() {
		tableView.updateLabel(table.getPoints());
		
		if (table.cards.isEmpty())
			tableView.showResetOption();
		else {
			if (table.cards.size() == 1)
				tableView.drawUpsideDownCard();
			
			for(Card card : table.cards) 
				tableView.panel.drawCard(card.imageString());
		}
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
