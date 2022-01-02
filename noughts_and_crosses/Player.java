package noughts_and_crosses;

public abstract class Player implements PlayerInterface {
	public int numWins = 0;
	public int numDraws = 0;
	public int numLosses = 0;
	public int numForfeits = 0; // all forfeits are losses, so numForfeits <= numLosses always
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
	
}
