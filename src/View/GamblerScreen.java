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
	
	// Info labels
	private JLabel pointsLabel = new JLabel("Points: 0");
	private JLabel bettingLabel = new JLabel("Bet: 0");
	private JLabel totalChipsLabel = new JLabel("Chips: 0");
	private JLabel resultLabel = new JLabel();
	
	// Action Buttons
	private JButton betButton = new JButton("bet");
	private JButton hitButton = new JButton("hit");
	private JButton standButton = new JButton("stand");
	private JButton surrenderButton = new JButton("surrender");
	
	public GamblerScreen(String gamblerId, double posX, double posY, double width, double height) {
		panel = new CardPanel();		
		panel.setBackground(new Color(44, 128, 65));
		
		// Add actions buttons to panel
		panel.add(betButton);
		panel.add(hitButton);
		panel.add(standButton);
		panel.add(surrenderButton);
		
		// Add info labels to panel
		panel.add(pointsLabel);
		panel.add(bettingLabel);
		panel.add(totalChipsLabel);
		
		disablePlayingButtons();
		add(panel);
		
		panel.repaint();
		
		setSize((int) width,(int) height);
		setLocation(new Point((int) posX,(int) posY));
		
		setTitle("Gambler " + gamblerId);
	}
	
	public void updatePointsLabel(int points) {
		pointsLabel.setText("Points: " + String.valueOf(points));
	}
	
	public void updateChipsLabels(int bet, int totalChips) {
		bettingLabel.setText("Bet: " + String.valueOf(bet));
		totalChipsLabel.setText("Chips: " + String.valueOf(totalChips));
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
		
		betButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {		
				controller.endBetting();
			}
		});
	}
	
	public void enableBettingButtons() {
		betButton.setEnabled(true);
	}
	
	public void disableBettingButtons() {
		betButton.setEnabled(false);
	}
	
	public void enablePlayingButtons() {
		hitButton.setEnabled(true);
		standButton.setEnabled(true);
	}
	
	public void disablePlayingButtons() {
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
		updatePointsLabel(0);
		panel.cardsImages.clear();
		panel.repaint();		
	}
}
