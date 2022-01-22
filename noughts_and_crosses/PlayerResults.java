package noughts_and_crosses;

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
	
	public PlayerResults(Player player) {
		this(player, 0, 0, 0);
	}
	
	public PlayerResults(Player player, int wins, int draws, int losses) {
		this.player = player;
		this.wins = wins;
		this.draws = draws;
		this.losses = losses;
		this.total = wins + draws + losses;
	}
	
	/**
	 * adds a single result onto the record
	 * @param result a char, one of 'W', 'L', or 'D',
	 *               for win, loss, or draw respectively
	 */
	void addResult(char result) {
		if (result == 'W') {
			this.wins += 1;
		} else if (result == 'L') {
			this.losses += 1;
		} else if (result == 'D') {
			this.draws += 1;
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
		} else if (result == 'X') {
			this.losses += 1;
		} else if (result == 'D') {
			this.draws += 1;
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
		} else if (result == 'X') {
			this.wins += 1;
		} else if (result == 'D') {
			this.draws += 1;
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
	
}
