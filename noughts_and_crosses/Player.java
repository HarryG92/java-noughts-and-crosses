package noughts_and_crosses;

public abstract class Player implements PlayerInterface {
	int numWins = 0;
	int numDraws = 0;
	int numLosses = 0;
	int numForfeits = 0; // all forfeits are losses, so numForfeits <= numLosses always
	String playerID;
	
	public abstract Move getMove(Board board);
	
	public abstract int forfeit();
	
	public abstract int winForfeit();
	
	public abstract int win();
	
	public abstract int lose();
	
	public abstract int draw();
	
	public String getPlayerID() {
		return this.playerID;
	}
	
	@Override
	public int getNumWins() {
		return this.numWins;
	}
	
	@Override
	public int getNumDraws() {
		return this.numDraws;
	}
	
	@Override
	public int getNumLosses() {
		return this.numLosses;
	}
	
	@Override
	public int getNumForfeits() {
		return this.numForfeits;
	}
}
