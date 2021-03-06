package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;

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
	private JLabel chipsControlLabel = new JLabel("Bet: 0");
	private JLabel totalChipsLabel = new JLabel("Chips: 0");
	private JLabel resultLabel = new JLabel();
	
	// Action Buttons
	private JButton betButton = new JButton("bet");
	private JButton buyChipsButton = new JButton("buy chips");
	private JButton surrenderButton = new JButton("surrender");
	private JButton hitButton = new JButton("hit");
	private JButton doubleButton = new JButton("double");
	private JButton standButton = new JButton("stand");
	
	public GamblerScreen(String gamblerId, double posX, double posY, double width, double height) {
		panel = new CardPanel();
		panel.setLayout(new FlowLayout());
		panel.setBackground(new Color(44, 128, 65));
		
		// Add actions buttons to panel
		panel.add(betButton);
		panel.add(buyChipsButton);
		panel.add(surrenderButton);
		panel.add(hitButton);
		panel.add(doubleButton);
		panel.add(standButton);
		
		// Add info labels to panel
		pointsLabel.setForeground(Color.white);
		panel.add(pointsLabel);
		panel.add(chipsControlLabel);
		panel.add(totalChipsLabel);
		
		disablePlayingButtons();
		disableBuyButtons(0);
		add(panel);
		
		panel.repaint();
		
		setSize((int) width,(int) height);
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
		
		doubleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.doublePlay();
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
		
		buyChipsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {		
				controller.endBuying();
			}
		});
		
		surrenderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {		
				controller.surrender();
			}
		});
	}
	
	public void updatePointsLabel(int points) {
		pointsLabel.setText("Points: " + String.valueOf(points));
	}
	
	public void updateChipsLabels(int bet, int totalChips) {
		resultLabel.setVisible(false);
		
		chipsControlLabel.setText("Bet: " + String.valueOf(bet));
		totalChipsLabel.setText("Chips: " + String.valueOf(totalChips));
	}
	
	public void updateBuyLabels(int buy, int totalChips) {
		resultLabel.setVisible(false);
		
		chipsControlLabel.setText("Buying chips: " + String.valueOf(buy));
		totalChipsLabel.setText("Chips: " + String.valueOf(totalChips));
	}
	
	public void enableBettingButtons() {
		betButton.setEnabled(true);
		surrenderButton.setEnabled(true);
	}
	
	public void disableBettingButtons() {
		panel.remove(resultLabel);
		panel.repaint();
		
		betButton.setEnabled(false);
	}
	
	public void enableBuyButton() {
		buyChipsButton.setEnabled(true);
		
		chipsControlLabel.setText("Buying chips: 0");
	}
	
	public void disableBuyButtons(int newTotalChips) {
		chipsControlLabel.setText("Bet: 0");
		totalChipsLabel.setText("Chips: " + String.valueOf(newTotalChips));
		
		buyChipsButton.setEnabled(false);
		panel.remove(resultLabel);
		panel.repaint();
		
	}
	
	public void enablePlayingButtons() {
		hitButton.setEnabled(true);
		doubleButton.setEnabled(true);
		standButton.setEnabled(true);
	}
	
	public void disablePlayingButtons() {
		hitButton.setEnabled(false);
		doubleButton.setEnabled(false);
		standButton.setEnabled(false);
	}
	
	public void displayError(String errorDesc) {
		resultLabel.setForeground(new Color(128, 44, 49));
		resultLabel.setText(errorDesc);
		resultLabel.setVisible(true);
		
		panel.add(resultLabel);
		panel.validate();
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
		else if (state == PlayerState.Draw) {
			resultLabel.setForeground(Color.white);
			resultLabel.setText("DRAW. You receive your bet back.");
		}
		else if (state == PlayerState.Surrendered) {
			resultLabel.setForeground(Color.darkGray);
			resultLabel.setText("SURRENDERED. Wait for the next round.");
			
			disableBettingButtons();
		}
		else { // if (state == PlayerState.Broke)
			resultLabel.setForeground(new Color(128, 44, 49));
			resultLabel.setText("BROKE. Better luck next time!");
		}
		
		surrenderButton.setEnabled(false);
		resultLabel.setVisible(true);
		panel.add(resultLabel);
		panel.repaint();
	}
	
	public void clear() {
		updatePointsLabel(0);
		disablePlayingButtons();
		
		resultLabel.setVisible(false);
		panel.remove(resultLabel);
		panel.cardsImages.clear();
		panel.repaint();		
	}
}
