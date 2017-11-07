package Model;

public class Card {
	public int number;
	public Suit suit;
	
	public Card(Suit suit, int number) {
		this.number = number;
		this.suit = suit;
	}
	
	public String imageString() {
		String imageName = "";
		
		switch(number) {
		case 1:
			imageName += "a";
			break;
			
		case 10:
			imageName += "t";
			break;
			
		case 11:
			imageName += "j";
			break;
			
		case 12:
			imageName += "q";
			break;
			
		case 13:
			imageName += "k";
			break;
			
		default:
			imageName += Integer.toString(number);
		}
		
		switch(suit) { 
		case Clubs:
			imageName += "c";
			break;
			
		case Diamonds:
			imageName += "d";
			break;
			
		case Hearts:
			imageName += "h";
			break;
			
		case Spades:
			imageName += "s";
			break;
		}
		
		return imageName;
	}
}
