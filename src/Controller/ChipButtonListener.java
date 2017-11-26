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
		GameController gameManager = GameController.getInstance();
		GamblerController currentGamblerManager = gameManager.gamblersControllers.get(gameManager.currentPlayer);
		
		currentGamblerManager.bet(chipValue);
	}

}
