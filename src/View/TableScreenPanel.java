package View;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class TableScreenPanel extends JPanel {
	private Image i = null;
	
	public TableScreenPanel(Image i) {
		this.i = i;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(i, 0, 0, null);
	}
}
