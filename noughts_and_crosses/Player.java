package noughts_and_crosses;
import java.io.*;

/**
 * the Player abstract class adds an ID
 * and counters of past results to the
 * PlayerInterface. Although a player only
 * needs to implement the PlayerInterface,
 * it is best if it extends the Player class
 * @author gulli
 *
 */
public abstract class Player implements PlayerInterface, Serializable {
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
	
	public void serialize() {
		try {
			String filename = String.format("/tmp/%s.ser", this.playerID);
	         FileOutputStream fileOut = new FileOutputStream(filename);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(this);
	         out.close();
	         fileOut.close();
	         System.out.printf(String.format("Serialized data is saved in %s", filename));
	      } catch (IOException i) {
	         i.printStackTrace();
	      }
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
