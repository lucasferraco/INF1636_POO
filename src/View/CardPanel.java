package View;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

public class CardPanel extends JPanel {	
	public ArrayList<Image> cardsImages = new ArrayList<Image>();
	
	public void drawCard(String cardImageStr) {
		Image image = null;
				
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/" + cardImageStr + ".gif"));
		} catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		cardsImages.add(image);
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int panelSize = getSize().width;
		int posX = -50;
		int posY = 70;
		
		for(Image img: cardsImages) {
			if (panelSize < posX + img.getWidth(null)){
				posX = 10;
				posY += 60;
			}
			else {
				posX += 60;
			}
			
			g.drawImage(img, posX, posY, 50, 50, null);
		}
	}
}
