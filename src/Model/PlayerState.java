package Model;

public enum PlayerState {
	Playing(3), Waiting(2), Won(1), Lost(-1), Draw(0);
	
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
