package Model;

public enum Suit {
	Clubs("Clubs"), Diamonds("Diamonds"), Hearts("Hearts"), Spades("Spades");
	
	public String suitDescription;
	Suit(String description) {
		suitDescription = description;
    }
	
	private boolean Compare(String desc) {
		return suitDescription.compareTo(desc) == 0;
	}
	
	public static Suit getSuitWith(String description) {
		Suit[] allDescs = Suit.values();
		
        for(int i = 0; i < allDescs.length; i++) {
            if (allDescs[i].Compare(description))
                return allDescs[i];
        }
        
		return null;
	}
}
