package noughts_and_crosses;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * A PlayerResults object stores the aggregated results of a game or series of games
 * for an object implementing PlayerInterface. It stores number of wins, losses,
 * and draws, but not game-level results. PlayerResults objects can be compared, where
 * first they are compared by number of losses (fewer losses means greater Result), and
 * ties are resolved by number of wins (more wins means greater Result). The
 * comparison are done by raw numbers, not proportions, so two Results objects should
 * only be compared if they record results for the same total number of games
 * @author H Gulliver
 *
 */
public class PlayerResults implements Comparable<PlayerResults> {
	Player player;
	int wins;
	int draws;
	int losses;
	int total;
	ArrayList<Character> results;
	
	public PlayerResults(Player player) {
		this(player, 0, 0, 0);
	}
	
	public PlayerResults(Player player, int wins, int draws, int losses) {
		this.player = player;
		this.wins = wins;
		this.draws = draws;
		this.losses = losses;
		this.total = wins + draws + losses;
		this.results = new ArrayList<Character>();
	}
	
	/**
	 * adds a single result onto the record
	 * @param result a char, one of 'W', 'L', or 'D',
	 *               for win, loss, or draw respectively
	 */
	void addResult(char result) {
		if (result == 'W') {
			this.wins += 1;
			this.results.add('W');
		} else if (result == 'L') {
			this.losses += 1;
			this.results.add('L');
		} else if (result == 'D') {
			this.draws += 1;
			this.results.add('D');
		} else {
			throw new IllegalArgumentException("result must be one of W, L, or D");
		}
		this.total += 1;
	}

	/**
	 * adds a single result onto the record, assuming this
	 * player was noughts for the game being added
	 * @param result a char; one of 'O', 'X', or 'D', for
	 *        "noughts won", "crosses won", or "draw"
	 */
	void addResultAsNoughts(char result) {
		if (result == 'O') {
			this.wins += 1;
			this.results.add('W');
		} else if (result == 'X') {
			this.losses += 1;
			this.results.add('L');
		} else if (result == 'D') {
			this.draws += 1;
			this.results.add('D');
		} else {
			throw new IllegalArgumentException("result must be one of X, O, D");
		}
		this.total += 1;
	}
	
	/**
	 * adds a single result onto the record, assuming this
	 * player was crosses for the game being added
	 * @param result a char; one of 'O', 'X', or 'D', for
	 *        "noughts won", "crosses won", or "draw"
	 */
	void addResultAsCrosses(char result) {
		if (result == 'O') {
			this.losses += 1;
			this.results.add('L');
		} else if (result == 'X') {
			this.wins += 1;
			this.results.add('W');
		} else if (result == 'D') {
			this.draws += 1;
			this.results.add('D');
		} else {
			throw new IllegalArgumentException("result must be one of X, O, D");
		}
		this.total += 1;
	}
	
	@Override
	public int compareTo(PlayerResults that) {
		if (this.losses == that.losses) {
			return this.wins - that.wins;
		} else {
			return that.losses - this.losses;
		}
	}
	
	public void add (PlayerResults that) {
		if (this.player == that.player) {
			this.wins += that.wins;
			this.losses += that.losses;
			this.draws += that.draws;
			this.total = this.wins + this.losses + this.draws;
		} else {
			throw new IllegalArgumentException("Cannot add PlayerResults for two separate players!");
		}
	}
	
	/**
	 * finds the proportion of entries in an ArrayList of Characters up to each
	 * index which are a particular char
	 * @param list   an ArrayList<Character> in which we want to find the changing
	 *               proportions of a given char
	 * @param symbol the char whose proportions we want to study
	 * @return an array of doubles, where index i records the proportion of entries
	 *         of list up to and including the ith which are equal to symbol
	 */
	private double[] cumulativeProportion(ArrayList<Character> list, char symbol) {
		int length = list.size();
		double[] cumProp = new double[length];
		double count = 0.0;
		ListIterator<Character> iterator = list.listIterator();
		for (int i = 0; i < length; i++) {
			if (iterator.next() == symbol) {
				count += 1.0;
			}
			cumProp[i] = count / (double) i;
		}
		return cumProp;
	}
	
	/**
	 * gives the proportion of wins/losses/draws after each game played
	 * @param result a char; one of 'W', 'L', 'D'. The result whose
	 *               proportions are to be returned
	 * @return an array of doubles; the ith entry records the proportion
	 *         of games ending in result up to and including the ith game
	 */
	public double[] cumulativeProportionResult(char result) {
		return this.cumulativeProportion(this.results, result);
	}
}
