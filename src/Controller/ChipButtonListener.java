package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChipButtonListener implements ActionListener {
	private int chipValue;

	public ChipButtonListener(int chipValue) {
		this.chipValue = chipValue;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("button for " + chipValue + " chip value");
	}

}
