package View;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JPanel;

import Controller.GameController;
import Controller.NumberOfPlayerButtonListener;

public class HomeScreen extends JFrame {	
	public HomeScreen(GameController game) {
		JPanel p = new JPanel();
		p.setBackground(new Color(44, 128, 65));
		
		p.add(new JLabel("Selecione a quantidade de Jogadores:"));
		
		for(int i = 1; i < 5; i++) {
			JButton newBtn = new JButton(String.valueOf(i));
			
			newBtn.addActionListener(new NumberOfPlayerButtonListener(i, this));
			p.add(newBtn);
		}
		
		add(p);
		
		
		setSize(300, 150);
		setTitle("Blackjack");
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2 - getSize().width/2, dim.height/2 - getSize().height/2);
	}
}
