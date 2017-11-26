package Model;

public enum PlayerState {
	Betting(2), Playing(3), Waiting(4), Surrendered(5), Broke(6),
	Won(1), Lost(-1), Draw(0);
	
	public int stateValue;
	PlayerState(int value) {
		stateValue = value;
    }
	
	private boolean Compare(int i) {
		return stateValue == i;
	}
	
	public static PlayerState getStateWith(int value) {
		PlayerState[] allStates = PlayerState.values();
		
        for(int i = 0; i < allStates.length; i++) {
            if (allStates[i].Compare(value))
                return allStates[i];
        }
        
		return null;
	}
}
