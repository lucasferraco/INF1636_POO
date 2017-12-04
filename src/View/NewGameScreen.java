package View;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Controller.GameController;
import Controller.NumberOfPlayerButtonListener;

public class NewGameScreen extends JFrame {	
	public NewGameScreen(GameController game) {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(44, 128, 65));
		
		panel.add(new JLabel("Selecione a quantidade de Jogadores:"));
		
		for(int i = 1; i < 5; i++) {
			JButton newBtn = new JButton(String.valueOf(i));
			
			newBtn.addActionListener(new NumberOfPlayerButtonListener(i, this));
			panel.add(newBtn);
		}
		
		JButton openGameButton = new JButton("open saved game");
		openGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileScreen fileScreen = new FileScreen();
				fileScreen.showChooseFileScreen();
				setVisible(false);
			}
		});
		panel.add(openGameButton);
		
		add(panel);
		
		setSize(300, 150);
		setTitle("Blackjack");
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2 - getSize().width/2, dim.height/2 - getSize().height/2);
	}
	
	
}
