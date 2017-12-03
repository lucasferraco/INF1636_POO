package View;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Controller.ChipButtonListener;

public class TableScreenPanel extends JPanel {
	private ArrayList<Image> chipsImages = new ArrayList<Image>();
	public ArrayList<Image> cardsImages = new ArrayList<Image>();
	private Image background = null;
	
	public TableScreenPanel(Image image) {
		background = image;
	}
	
	public void drawCard(String cardImageStr) {
		Image image = null;
				
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/" + cardImageStr + ".gif"));
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		cardsImages.add(image);
		repaint();
	}
	
	public void createChipButton(int value) {
		Image image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/ficha_" + value + ".png"));
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		chipsImages.add(image);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background, 0, 0, this);
		
		int panelWidth = getSize().width;
		int posX = panelWidth / 3 - 50;
		int posY = getSize().height / 2 - 50;
		
		for(Image img: cardsImages) {
			if (panelWidth < posX + img.getWidth(null)){
				posX = panelWidth;
				posY += 60;
			}
			else {
				posX += 60;
			}
			
			g.drawImage(img, posX, posY, 50, 50, null);
		}
	}
}
