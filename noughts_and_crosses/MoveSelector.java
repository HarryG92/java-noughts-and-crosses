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
	 * simplifies the odds ratio by dividing through by a
	 * constant (with integral division - i.e., quotient)
	 * @param factor the integer to divide through the odds
	 *               ratio. E.g., if the odds ratio is
	 *               3:5:10 and the factor is 3, the new
	 *               odds ratio will be 1:1:3
	 */
	private void simplifyOdds(int factor) {
		for (Move move : this.moveOdds.keySet()) {
			int oldOdds = this.moveOdds.get(move);
			int newOdds = oldOdds / factor;
			this.moveOdds.put(move, newOdds);
		}
	}

	/**
	 * divides the odds of the given move by the given factor.
	 * This is integral division (quotient), so may introduce
	 * rounding errors, and may set the odds of the move to 0,
	 * if factor is bigger than the previous odds component
	 * @param move    the Move object whose odds are to be reduced
	 * @param factor  the factor by which to reduce the odds of the
	 *                move
	 */
	private void decreaseOdds(Move move, int factor) {
		if (factor <= 1) {
			throw new IllegalArgumentException("The factor must be positive");
		}
		
		if (!this.gameState.isMoveLegal(move)) {
			throw new IllegalArgumentException("The move is not legal in this game state");
		}
		
		int currentValue = this.moveOdds.get(move);
		int newValue = currentValue / factor;
		this.moveOdds.put(move, newValue);
	}
	
	/**
	 * multiplies the odds of a particular move by a given int,
	 * unless doing so would cause an integer overflow. In this
	 * case, the odds factor of every other move is divided by the
	 * multiplier, which should have the same effect, except that
	 * it's integral division (quotient), so will round, and could
	 * set small odds to 0. 
	 * @param move        the move whose odds are to be increased
	 * @param multiplier  an int; the odds of playing the given move
	 *                    are multiplied by this
	 */
	public void increaseOdds(Move move, int multiplier) {
		if (multiplier <= 0) {
			throw new IllegalArgumentException("The multiplier must be positive");
		}
		
		if (!this.gameState.isMoveLegal(move)) {
			throw new IllegalArgumentException("The move is not legal in this game state");
		}
		
		int currentValue = this.moveOdds.get(move);
		int newValue = currentValue * multiplier;
		
		// need to guard against integer overflow
		if (newValue >= currentValue) {
			this.moveOdds.put(move, newValue);
		} else { // if overflow, decrease odds component of all other moves
			for (Move otherMove : this.moveOdds.keySet()) {
				if (!otherMove.isEqual(move)) {
					this.decreaseOdds(otherMove, multiplier);
				}
			}
		}
		
	}

}
