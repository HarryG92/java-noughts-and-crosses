package noughts_and_crosses;

/**
 * the Player abstract class adds an ID
 * and counters of past results to the
 * PlayerInterface. Although a player only
 * needs to implement the PlayerInterface,
 * it is best if it extends the Player class
 * @author gulli
 *
 */
public abstract class Player implements PlayerInterface {
	public int numWins = 0;
	public int numDraws = 0;
	public int numLosses = 0;
	public int numForfeits = 0; // all forfeits are losses, so numForfeits <= numLosses always
	public String playerID;
	
	public Player(String id) {
		this.playerID = id;
	}
	
	public Move getMove(Board board) {
		return this.getMove(board, false);
	}
//	
//	public abstract int forfeit();
//	
//	public abstract int winForfeit();
//	
//	public abstract int win();
//	
//	public abstract int lose();
//	
//	public abstract int draw();
	
	public String getPlayerID() {
		return this.playerID;
	}
	
}
