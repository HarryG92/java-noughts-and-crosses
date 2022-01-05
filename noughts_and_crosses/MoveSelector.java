package noughts_and_crosses;
import java.util.HashMap;

/**
 * A MoveSelector object exposes a method for randomly
 * choosing a legal move in the noughts and crosses game.
 * A single instance is fixed to a specific game state,
 * and initially returns any legal move with equal probability.
 * 
 * It also exposes methods to adjust its probabilities.
 * It is **not** responsible for determining how to adjust the
 * probabilities - it does not implement the learning. It
 * simply remembers the current probabilities and handles
 * move selection according to those.
 * 
 * @author H Gulliver
 *
 */
public class MoveSelector {
	final int BOARD_SIZE;
	final Board gameState;
	final HashMap<Move, Integer> moveOdds;
	
	public MoveSelector(Board board) {
		gameState = new Board(board);
		BOARD_SIZE = gameState.BOARD_SIZE;
		moveOdds = this.listLegalMoves();
	}
	
	/**
	 * lists all legal moves in the current game state,
	 * and assigns an equal probability to each of them
	 * @return a HashMap with Move objects as keys, one for each
	 *         legal move in the game, and 1 as all values,
	 *         representing equal odds of all moves
	 */
	private HashMap<Move, Integer> listLegalMoves() {
		HashMap<Move, Integer> legalMoves = new HashMap<Move, Integer>();
		for (int row = 0; row < this.BOARD_SIZE; row++) {
			for (int col = 0; col < this.BOARD_SIZE; col++) {
				Move move = new Move(row, col);
				if (this.gameState.isMoveLegal(move)) {
					legalMoves.put(move, 1);
				}
			}
		}
		return legalMoves;
	}

	/**
	 * multiplies the odds of a particular move by a given int
	 * @param move        the move whose odds are to be increased
	 * @param multiplier  an int; the odds of playing the given move
	 *                    are multiplied by this
	 */
	public void increaseOdds(Move move, int multiplier) {
		if (multiplier <= 0) {
			throw new IllegalArgumentException("The multiplier must be positive");
		}
		
		int currentValue = this.moveOdds.get(move); // TO DO: what if the move passed is not legal, so doesn't arise as a key of moveOdds?
		int newValue = currentValue * multiplier;
		
		// need to guard against integer overflow
		if (newValue >= currentValue) {
			this.moveOdds.put(move, newValue);
		}
		
	}

}
