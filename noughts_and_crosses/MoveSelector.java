package noughts_and_crosses;
import java.util.HashMap;
import java.util.Random;

// TODO: simply checking a Move is legal when input to a method is
// not sufficient - we need to check it's actually one of the specific
// instances in moveArray

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
	final GameState gameState;
	final HashMap<Move, Integer> moveOdds;
	private int numMoves = 0;
	private Move[] moveArray;
	private int[] oddsArray;
	
	// TODO: needs to be based off game state, not Board! otherwise it doesn't notice the changing state on the board as moves are played!
	public MoveSelector(Board board) {
		this.gameState = new GameState(board.board);
		this.BOARD_SIZE = this.gameState.BOARD_SIZE;
		this.moveOdds = this.listLegalMoves();
		this.moveArray = new Move[numMoves];
		this.oddsArray = new int[numMoves];
		int i = 0;
		for (Move move : this.moveOdds.keySet()) {
			this.moveArray[i++] = move;
		}
	}
	
	/**
	 * prints the available moves and their odds
	 * intended for debugging and inspection only
	 */
	public void printOdds() {
		for (Move move : this.moveOdds.keySet()) {
			System.out.print(move.row);
			System.out.print(move.col);
			System.out.print(" ");
			System.out.println(this.moveOdds.get(move));
		}
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
					this.numMoves += 1;
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
	 * simplifies the odds by dividing through by the lowest
	 * odds component, so as to make the lowest component 1
	 */
	private void simplifyOddsByMinimum() {
		int lowestOdds = (int)Math.pow(2, 30); // almost the biggest int possible
		// need lowestOdds to start high, so minimum-ing it with numbers
		// makes it lower
		for (int odds : this.moveOdds.values()) {
			if (odds != 0) {
				lowestOdds = Math.min(lowestOdds, odds);
			}
		}
		System.out.print("Dividing through by ");
		System.out.println(lowestOdds);
		this.simplifyOdds(lowestOdds);
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
	private void divideOdds(Move move, int factor) {
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
		try {
			// need to guard against integer overflow
			int newValue = Math.multiplyExact(currentValue, multiplier);
			this.moveOdds.put(move, newValue);
		} catch (ArithmeticException e) {
			for (Move otherMove : this.moveOdds.keySet()) {
				if (!otherMove.isEqual(move)) {
					this.divideOdds(otherMove, multiplier);
				}
			}
		}
		
		// divide through to simplify the odds if possible
		this.simplifyOddsByMinimum();
	}

	/**
	 * multiplies the odds components of each other move by a given
	 * int, thereby reducing the odds of the specified move by that int
	 * @param move   the Move object whose odds are to be decreased
	 * @param factor the int factor by which to decrease the odds of move
	 */
	public void decreaseOdds(Move move, int factor) {
		for (Move otherMove : this.moveOdds.keySet()) {
			if (move != otherMove) {
				this.increaseOdds(otherMove, factor);
			}
		}
	}
	
	/**
	 * sets the odds of the given move to 0, so the move cannot be played
	 * @param move the Move object whose odds are to be set to 0
	 */
	public void zeroOdds(Move move) {
		if (!this.gameState.isMoveLegal(move)) {
			throw new IllegalArgumentException("The move is not legal in this game state");
		}
		this.moveOdds.put(move, 0);
	}
	
	/**
	 * calculates the cumulative sum of an array of ints
	 * uses Math.addExact to guard against integer overflow,
	 * so could throw an ArithmeticException
	 * @param in an Array of ints
	 * @return an Array of ints whose ith entry is the
	 *         sum of the first i entries of in
	 */
	private int[] cumulativeSum(int[] in) {
		int[] out = new int[in.length];
		int total = 0;
		for (int i = 0; i < in.length; i++) {
			total = Math.addExact(total, in[i]);
			out[i] = total;
		}
		return out;
	}
	
	/**
	 * chooses an index of an int array with odds given by the differences
	 * of the values in it. E.g., if called with {1,2,4,8},
	 * the odds of returning 0, 1, 2, 3 respectively will be
	 * 1:1:2:4
	 * @param accumulated an array of ints, where each
	 *                    entry is at least as big as the last;
	 *                    this.cumulativeSum will return such an array
	 * @return an int corresponding to an index of the input array
	 */
	private int chooseRandomIndex(int[] accumulated) {
		Random random = new Random();
		int max = accumulated[accumulated.length - 1];
		int choice = random.nextInt(max);
		for (int i = 0; i < accumulated.length; i++) {
			if (accumulated[i] > choice) {
				return i;
			}
		}
		return accumulated.length - 1; // should never reach this,
		// since random.nextInt() is exclusive of upper bound, so
		// for loop should return. This is needed to convince Eclipse
		// that the method really will return an int though
	}
	
	/**
	 * chooses a move to play. Does this by putting the
	 * current odds in an array, choosing a random int between 0 and
	 * the sum of that array, and choosing the index where the cumulative
	 * sum first exceeds the random int; then returns the corresponding Move
	 * @return a Move object representing the chosen move
	 */
	public Move selectMove() {
		this.printOdds();
		// set up odds array to current odds values
//		System.out.println("\n");
		for (int i = 0; i < this.numMoves; i++) {
			Move move = this.moveArray[i];
			int odds = this.moveOdds.get(move);
			this.oddsArray[i] = odds;
//			System.out.print(move.row);
//			System.out.print(move.col);
//			System.out.print(" ");
//			System.out.println(odds);
		}
		int[] cumulativeOdds;
		try {
			cumulativeOdds = this.cumulativeSum(this.oddsArray);
		} catch (ArithmeticException e) { // if integer overflow
			this.simplifyOdds(2);
			return this.selectMove();
		}
		
		int chosenIndex = this.chooseRandomIndex(cumulativeOdds);
		Move chosenMove = this.moveArray[chosenIndex];
//		System.out.println(chosenMove.row);
//		System.out.println(chosenMove.col);
		return chosenMove;
	}
}
