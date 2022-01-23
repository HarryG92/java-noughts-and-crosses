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
public class MoveSelector implements java.io.Serializable{
	final int BOARD_SIZE;
	final GameState gameState;
	final HashMap<Move, Double> moveOdds;
	private int numMoves = 0;
	private Move[] moveArray;
	private double[] oddsArray;
	
	public MoveSelector(Board board) {
		this.gameState = new GameState(board.board);
		this.BOARD_SIZE = this.gameState.BOARD_SIZE;
		this.moveOdds = this.listLegalMoves();
		this.moveArray = new Move[numMoves];
		this.oddsArray = new double[numMoves];
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
			double odds = this.moveOdds.get(move);
			if (odds == 0.0) {
				// to distinguish between actually 0 and rounded to 0 for display
				System.out.println(0);
			} else {
				System.out.println(String.format("%.2f", odds));
			}
		}
		System.out.print("\n");
	}
	
	/**
	 * lists all legal moves in the current game state,
	 * and assigns an equal probability to each of them
	 * @return a HashMap with Move objects as keys, one for each
	 *         legal move in the game, and 1 as all values,
	 *         representing equal odds of all moves
	 */
	private HashMap<Move, Double> listLegalMoves() {
		HashMap<Move, Double> legalMoves = new HashMap<Move, Double>();
		for (int row = 0; row < this.BOARD_SIZE; row++) {
			for (int col = 0; col < this.BOARD_SIZE; col++) {
				Move move = new Move(row, col);
				if (this.gameState.isMoveLegal(move)) {
					legalMoves.put(move, 1.0);
					this.numMoves += 1;
				}
			}
		}
		return legalMoves;
	}

	/**
	 * finds the largest value occurring as an odds component
	 * convenience method
	 * @return the highest odds component
	 */
	private double findMaxOdds() {
		double maxOdds = 0.0;
		for (double odds : this.moveOdds.values()) {
			maxOdds = Math.max(odds, maxOdds);
		}
		return maxOdds;
	}

	/**
	 * simplifies the odds ratio by dividing through by a
	 * constant (with integral division - i.e., quotient)
	 * @param factor the integer to divide through the odds
	 *               ratio. E.g., if the odds ratio is
	 *               3:5:10 and the factor is 3, the new
	 *               odds ratio will be 1:1:3
	 */
	private void simplifyOdds(double factor) {
		for (Move move : this.moveOdds.keySet()) {
			double oldOdds = this.moveOdds.get(move);
			double newOdds = oldOdds / factor;
			this.moveOdds.put(move, newOdds);
		}
	}

	/**
	 * multiplies the odds of a particular move by a given double
	 * @param move        the move whose odds are to be changed
	 * @param multiplier  a double; the odds of playing the given move
	 *                    are multiplied by this
	 */
	public void multiplyOdds(Move move, double multiplier) {
		if (multiplier <= 0) {
			throw new IllegalArgumentException("The multiplier must be positive");
		}
		
		if (!this.gameState.isMoveLegal(move)) {
			throw new IllegalArgumentException("The move is not legal in this game state");
		}
		double currentValue = this.moveOdds.get(move);
		double newValue = currentValue * multiplier;
		this.moveOdds.put(move, newValue);
		
		// divide through to simplify the odds if some are getting large
		double maxOdds = this.findMaxOdds();
		if (maxOdds > Math.pow(2, 20)) { // entirely arbitrary point at which to simplify
			// orginally just always simplified, but because of integer division that
			// ended up setting things to 1 that needed to be higher.
			this.simplifyOdds(2.0);
		}
		
	}

	/**
	 * resets odds to equal
	 */
	private void resetOdds() {
		for (Move move : this.moveOdds.keySet()) {
			this.moveOdds.put(move, 1.0);
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
		this.moveOdds.put(move, 0.0);
		// don't want a situation where all odds are 0
		boolean isNonZero = false;
		for (double odds : this.moveOdds.values()) {
			isNonZero |= (odds != 0.0);
		}
		if (!isNonZero) {
			// reset all odds to equal
			this.resetOdds();
		}
	}
	
	/**
	 * calculates the cumulative sum of an array of ints
	 * uses Math.addExact to guard against integer overflow,
	 * so could throw an ArithmeticException
	 * @param in an Array of ints
	 * @return an Array of ints whose ith entry is the
	 *         sum of the first i entries of in
	 */
	private double[] cumulativeSum(double[] in) {
		double[] out = new double[in.length];
		double total = 0;
		for (int i = 0; i < in.length; i++) {
			total += in[i];
			out[i] = total;
		}
		return out;
	}
	
	/**
	 * chooses an index of an int array with odds given by the differences
	 * of the values in it. E.g., if called with {1,2,4,8},
	 * the odds of returning 0, 1, 2, 3 respectively will be
	 * 1:1:2:4. If the array is all zeroes, will simply
	 * return any index at random
	 * @param accumulated an array of ints, where each
	 *                    entry is at least as big as the last;
	 *                    this.cumulativeSum will return such an array
	 * @return an int corresponding to an index of the input array
	 */
	private int chooseRandomIndex(double[] accumulated) {
		Random random = new Random();
		double max = accumulated[accumulated.length - 1];
		if (max == 0) {
			// if all odds are zero, just pick anything!
			return random.nextInt(accumulated.length);
		}
		double choice = random.nextDouble(max);
		for (int i = 0; i < accumulated.length; i++) {
			if (accumulated[i] > choice) {
				return i;
			}
		}
		// should never reach this line, but needed to convince Eclipse that
		// this method really does return! If we do get here, just return
		// any valid index at random
		return random.nextInt(accumulated.length);
	}
	
	/**
	 * makes a particular move certain, so it will always
	 * be played
	 * @param move the Move object to be made certain
	 */
	public void makeCertain(Move move) {
		for (Move otherMove : this.moveOdds.keySet()) {
			if (move != otherMove) {
				this.zeroOdds(otherMove);
			} else {
				this.moveOdds.put(otherMove, 1.0);
			}
		}
	}
	
	/**
	 * chooses a move to play. Does this by putting the
	 * current odds in an array, choosing a random int between 0 and
	 * the sum of that array, and choosing the index where the cumulative
	 * sum first exceeds the random int; then returns the corresponding Move
	 * @return a Move object representing the chosen move
	 */
	public Move selectMove() {
		// set up odds array to current odds values
		for (int i = 0; i < this.numMoves; i++) {
			Move move = this.moveArray[i];
			double odds = this.moveOdds.get(move);
			this.oddsArray[i] = odds;
		}
		double[] cumulativeOdds;
		try {
			cumulativeOdds = this.cumulativeSum(this.oddsArray);
		} catch (ArithmeticException e) { // if integer overflow
			this.simplifyOdds(2);
			return this.selectMove();
		}
		
		int chosenIndex = this.chooseRandomIndex(cumulativeOdds);
		Move chosenMove = this.moveArray[chosenIndex];
		return chosenMove;
	}
	
	/**
	 * chooses a move to play, with the option of displaying working.
	 * Does this by putting the current odds in an array, choosing a random
	 * int between 0 and the sum of that array, and choosing the index where
	 * the cumulative sum first exceeds the random int; then returns the
	 * corresponding Move
	 * @param verbose a boolean; if true, prints odds of each move first
	 * @return a Move object representing the chosen move
	 */
	public Move selectMove(boolean verbose) {
		if (verbose) {
			this.printOdds();
		}
		return this.selectMove();
	}

	/**
	 * sets the odds of each move to a random amount
	 */
	public void randomiseOdds() {
		for (Move move : this.moveOdds.keySet()) {
			Random random = new Random();
			double odds = random.nextDouble();
			this.moveOdds.put(move, odds);
		}
	}
}
