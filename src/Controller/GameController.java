package Controller;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;

import View.*;
import Controller.GamblerController;
import Model.Gambler;
import Model.Card;
import Model.Suit;

public final class GameController {
	private ArrayList<Card> deck;
	private int numberOfPlayers;
	private ArrayList<GamblerScreen> gamblersScreens;
	private ArrayList<GamblerController> gamblersControllers = new ArrayList<GamblerController>();
	private int currentPlayer = 1;
	
	private static final GameController theOnlyInstance = new GameController();
	
	private GameController() {
		deck = new ArrayList<Card>();
		gamblersScreens = new ArrayList<GamblerScreen>();
		
		HomeScreen home = new HomeScreen(this);
		home.setVisible(true);
	}
	
	public static GameController getInstance() {
		return theOnlyInstance;
	}
	
	public void initializeDeck() {
		for(int i = 0; i < 13; i++) {
			deck.add(new Card(Suit.Clubs   , i+1));
			deck.add(new Card(Suit.Diamonds, i+1));
			deck.add(new Card(Suit.Hearts  , i+1));
			deck.add(new Card(Suit.Spades  , i+1));
		}		
	}
	
	public void shuffleDeck() {
		Collections.shuffle(deck);
	}
	
	public void createPlayersViews(int numberOfPlayers) {
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		
		this.numberOfPlayers = numberOfPlayers;
		double width = screenSize.getWidth()/(numberOfPlayers + 1);
		double height = screenSize.getHeight() / 3;
		
		double totalWidth = 100;
		double totalHeight = screenSize.getHeight()/2 + 50;
		
		TableScreen table = new TableScreen(screenSize.getWidth()/2, 200);
		table.setVisible(true);
		
		for(int i = 0; i < numberOfPlayers; i++) {
			GamblerScreen frame = new GamblerScreen(String.valueOf(i+1), width, height, totalWidth, totalHeight);
			GamblerController gamblerController = new GamblerController(new Gambler(i+1), frame);
			
			gamblersControllers.add(gamblerController);
			gamblersScreens.add(frame);
			frame.setVisible(true);
			totalWidth += width + 10;
		}
			
	}
	
	public Card popCard() {	
		if (deck.isEmpty()) {
			return null;
		}
		
		return deck.remove(0);
	}
}
