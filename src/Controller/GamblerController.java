package Controller;

import Model.Gambler;
import View.GamblerScreen;
import Model.Card;

public class GamblerController {
	private Gambler gambler;
	private GamblerScreen view;

	public GamblerController(Gambler gambler, GamblerScreen view) {
		this.gambler = gambler;
		this.view = view;
		view.setListeners(this);
	}
	
	public void hit() {
		Card removedCard = GameController.getInstance().popCard();
		
		if (removedCard != null) {
			gambler.addPoint(removedCard.number);
			gambler.cards.add(removedCard);
			
			view.panel.drawCard(removedCard.imageString());
			view.repaint();
		}
	}
}
