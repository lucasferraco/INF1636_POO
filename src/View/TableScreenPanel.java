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

public class TableScreenPanel extends JPanel {
	private int[] chipsXPositions = new int[6];
	private int chipWidth, chipHeight;
	
	public ArrayList<Image> cardsImages = new ArrayList<Image>();
	private Image background = null;
	
	public TableScreenPanel(Image image) {
		background = image;
		
		int nextChipXPosition = background.getWidth(null) / 2 - 175;
		for(int i = 0; i < 6; i++) {
			chipsXPositions[i] = nextChipXPosition;
			nextChipXPosition += 70;
		}
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
	
	private void drawChips(Graphics g, int value, int chipIndex) {
		Image image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/ficha_" + value + ".png"));
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		chipWidth = image.getWidth(null) + 20;
		chipHeight = image.getHeight(null) + 20;
		
		int posY = getSize().height - 50;
		g.drawImage(image, chipsXPositions[chipIndex], posY, 50, 50, null);
	}
	
	public int findChip(int x, int y) {
		int index = -1;
		int chipsY = getSize().height - 50;
		
		if (!(y >= chipsY && y <= chipsY + chipHeight)) {
			return -1;
		}
		
		for(int i = 0; i < chipsXPositions.length; i++) {
			if(x >= chipsXPositions[i] && x <= chipsXPositions[i] + chipWidth) {
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background, 0, 0, this);
		
		drawChips(g, 1, 0);
		drawChips(g, 5, 1);
		drawChips(g, 10, 2);
		drawChips(g, 20, 3);
		drawChips(g, 50, 4);
		drawChips(g, 100, 5);
		
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
