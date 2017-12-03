package View;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Controller.ChipButtonListener;
import Controller.GamblerController;
import Controller.GameController;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import View.TableScreenPanel;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TableScreen extends JFrame {
	public TableScreenPanel panel;
	private JLabel pointsLabel = new JLabel("Points: 0");
	private JButton resetButton = new JButton("reset");
	
	public TableScreen(double posX, double posY) {
		Image image = null;
				
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/blackjackBKG.png"));
		} catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		panel = new TableScreenPanel(image);
		add(panel);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Table");
		setSize(891, 400);
		setLocation(new Point((int) posX - 445, (int) posY - 200));
		
		pointsLabel.setLocation((getWidth() - pointsLabel.getWidth())/2, (getHeight() - pointsLabel.getHeight())/2);
		pointsLabel.setForeground(Color.white);
		panel.add(pointsLabel);
				
		createChipButton(1);
		createChipButton(5);
		createChipButton(10);
		createChipButton(20);
		createChipButton(50);
		createChipButton(100);
	}
	
	public void setListeners(GameController controller) {
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.reset();
			}
		});
	}
	
	public void createChipButton(int value) {
		Image image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/ficha_" + value + ".png"));
		} catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		JButton btn = new JButton(new ImageIcon(image));
		btn.addActionListener(new ChipButtonListener(value));
		panel.add(btn);		
	}
	
	public void updateLabel(int points) {
		pointsLabel.setText("Points: " + String.valueOf(points));
	}
	
	public void showResetOption() {
		panel.add(resetButton);
		validate();
	}
	
	public void drawUpsideDownCard() {
		panel.drawCard("deck1");
	}
	
	public void removeUpsideDownCard() {
		panel.cardsImages.remove(0);
		panel.repaint();
	}
	
	public void clear() {
		updateLabel(0);
		panel.cardsImages.clear();
		panel.remove(resetButton);
		
		repaint();
	}
}
