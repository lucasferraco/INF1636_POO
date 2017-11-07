package Controller;

import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.event.ActionListener;

public class NumberOfPlayerButtonListener implements ActionListener {
	int numberOfPlayers;
	JFrame frame;
	
	public NumberOfPlayerButtonListener(int numberOfPlayers, JFrame frame) {
		this.numberOfPlayers = numberOfPlayers;
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GameController game = GameController.getInstance();
		game.createPlayersViews(numberOfPlayers);
		game.initializeDeck();
		game.shuffleDeck();
		
		frame.setVisible(false);
	}
}
