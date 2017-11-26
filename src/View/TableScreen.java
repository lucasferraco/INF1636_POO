package View;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Controller.ChipButtonListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import View.TableScreenPanel;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;

public class TableScreen extends JFrame {
	public TableScreenPanel panel;
	private JLabel pointsLabel = new JLabel("Points: 0");
	
	private JButton chip1Button;
	private JButton chip5Button;
	private JButton chip10Button;
	private JButton chip20Button;
	private JButton chip50Button;
	private JButton chip100Button;
	
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
		
		Color tableGreen = new Color(44, 128, 65);
		
		chip1Button = createChipButton(1);
		chip5Button = createChipButton(5);
		chip10Button = createChipButton(10);
		chip20Button = createChipButton(20);
		chip50Button = createChipButton(50);
		chip100Button = createChipButton(100);
	}
	
	public JButton createChipButton(int value) {
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
		
		return btn;
	}
	
	public void updateLabel(int points) {
		pointsLabel.setText("Points: " + String.valueOf(points));
	}
	
	public void drawUpsideDownCard() {
		panel.drawCard("deck1");
	}
	
	public void removeUpsideDownCard() {
		panel.cardsImages.remove(0);
		panel.repaint();
	}
}
