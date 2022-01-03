package noughts_and_crosses;

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
	
	public MoveSelector(Board board) {
		final Board gameState = new Board(board);
	}
	
	private 
}
