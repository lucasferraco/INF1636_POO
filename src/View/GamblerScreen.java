package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;

import View.CardPanel;
import Controller.GamblerController;
import Model.PlayerState;

public class GamblerScreen extends JFrame {	
	public CardPanel panel;
	private JLabel pointsLabel = new JLabel("Points: 0");
	private JLabel resultLabel = new JLabel();
	private JButton hitButton = new JButton("hit");
	private JButton standButton = new JButton("stand");
	
	public GamblerScreen(String gamblerId, double posX, double posY, double width, double height) {
		panel = new CardPanel();		
		panel.setBackground(new Color(44, 128, 65));
		panel.repaint();
		panel.add(hitButton);
		panel.add(standButton);
		panel.add(pointsLabel);
		add(panel);
		
		setSize((int) width,(int) height);
		setLocation(new Point((int) posX,(int) posY));
		
		setTitle("Gambler " + gamblerId);
	}
	
	public void updateLabel(int points) {
		pointsLabel.setText("Points: " + String.valueOf(points));
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
				controller.stand();
			}
		});
	}
	
	public void enableButtons() {
		hitButton.setEnabled(true);;
		standButton.setEnabled(true);
	}
	
	public void disableButtons() {
		hitButton.setEnabled(false);
		standButton.setEnabled(false);
	}
	
	public void addResultLabel(PlayerState state) {
		if (state == PlayerState.Lost) {
			resultLabel.setForeground(new Color(128, 44, 49));
			resultLabel.setText("You LOST. Better luck next time!");
		}
		else if (state == PlayerState.Won) {
			resultLabel.setForeground(Color.yellow);
			resultLabel.setText("You WON. Congratulations!");
		}
		else {
			resultLabel.setForeground(Color.white);
			resultLabel.setText("DRAW. You'll receive your bet back.");
		}
		System.out.println("addResultLabel " + resultLabel.getText());
		panel.add(resultLabel);
	}
	
	public void clear() {
		panel.remove(resultLabel);
		updateLabel(0);
		panel.cardsImages.clear();
		panel.repaint();		
	}
}
