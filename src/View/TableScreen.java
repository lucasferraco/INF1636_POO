package View;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Controller.GameController;
import SupportingFiles.*;

import javax.swing.JButton;

import View.TableScreenPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class TableScreen extends JFrame implements Subject, MouseListener {
	public TableScreenPanel panel;
	private JLabel pointsLabel = new JLabel("Points: 0");
	
	private JButton resetButton = new JButton("reset");
	private JButton saveButton = new JButton("save");
	private JButton endButton = new JButton("end");
	
	private int[] chipsValues = {1, 5, 10, 20, 50, 100};
	private ArrayList<Observer> observers;
	
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
		
		pointsLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		pointsLabel.setForeground(Color.white);
		panel.add(pointsLabel);
		
		panel.add(saveButton);
		panel.add(endButton);
		
		addMouseListener(this);
		observers = new ArrayList<Observer>();
	}
	
	public void setListeners(GameController controller) {
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.reset();
			}
		});
		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileScreen saveScreen = new FileScreen();
				saveScreen.showSaveScreen();
			}
		});
		
		endButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.end();
			}
		});
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
	
	public boolean hasCardsImages() {
		return !panel.cardsImages.isEmpty();
	}
	
	public void clear() {
		updateLabel(0);
		panel.cardsImages.clear();
		panel.remove(resetButton);
		
		repaint();
	}

	// Subject Methods
	
	@Override
	public void register(Observer obj) {
		if(obj != null && !observers.contains(obj)) 
			observers.add(obj);
	}

	@Override
	public void unregister(Observer obj) {
		observers.remove(obj);
	}

	@Override
	public void notifyObservers(int value) {
		for(Observer o: observers)
			o.update(value);
	}
	
	// MouseListener Methods
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		int chipIndex = panel.findChip(x, y);
		
		if (chipIndex != -1)
			notifyObservers(chipsValues[chipIndex]);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
}
