package Controller;

import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.event.ActionListener;

public class NumberOfPlayerButtonListener implements ActionListener {
	private int numberOfPlayers;
	private JFrame frame;
	
	public NumberOfPlayerButtonListener(int numberOfPlayers, JFrame frame) {
		this.numberOfPlayers = numberOfPlayers;
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.setVisible(false);
		
		GameController game = GameController.getInstance();
		game.initializeGame(numberOfPlayers);
	}
}
