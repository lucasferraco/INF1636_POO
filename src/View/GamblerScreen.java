package View;

import javax.swing.JFrame;
//import Controller.GamblerController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JButton;

import View.GamblerScreenPanel;
import Controller.GamblerController;

public class GamblerScreen extends JFrame {	
	public GamblerScreenPanel panel;
	private JButton hitButton = new JButton("hit");
	private JButton standButton = new JButton("stand");
	
	public GamblerScreen(String gamblerId, double x, double y,double posX, double posY) {
		panel = new GamblerScreenPanel();		
		panel.setBackground(new Color(44, 128, 65));
		panel.add(hitButton);
		panel.add(standButton);
		add(panel);
		
		setSize((int) x,(int) y);
		setLocation(new Point((int) posX,(int) posY));
		
		setTitle("Gambler " + gamblerId);
	}
	
	public void setListeners(GamblerController controller) {
		hitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.hit();
			}
			
		});
		
		standButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {		
				
			}
		});
	}
	
}
