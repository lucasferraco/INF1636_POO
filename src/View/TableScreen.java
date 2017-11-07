package View;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import View.TableScreenPanel;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

public class TableScreen extends JFrame {
	public TableScreen(double posX, double posY) {
		Image image = null;
				
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/blackjackBKG.png"));
		} catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		add(new TableScreenPanel(image));
		
		setSize(891, 400);
		setLocation(new Point((int) posX - 445, (int) posY - 200));
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Table");
	}
}
