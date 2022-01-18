package noughts_and_crosses;

/**
 * A PlayerResults object stores the aggregated results of a game or series of games
 * for an object implementing PlayerInterface. It stores number of wins, losses,
 * and draws, but not game-level results. PlayerResults objects can be compared, where
 * first they are compared by number of wins (more wins means greater Result), and
 * ties are resolved by number of losses (fewer losses means greater Result). The
 * comparison are done by raw numbers, not proportions, so two Results objects should
 * only be compared if they record results for the same total number of games
 * @author H Gulliver
 *
 */
public class PlayerResults implements Comparable<PlayerResults> {
	PlayerInterface player;
	int wins;
	int draws;
	int losses;
	int total;
	
	public PlayerResults(PlayerInterface player, int wins, int draws, int losses) {
		this.player = player;
		this.wins = wins;
		this.draws = draws;
		this.losses = losses;
		this.total = wins + draws + losses;
	}
	
	@Override
	public int compareTo(PlayerResults that) {
		if (this.wins == that.wins) {
			return that.losses - this.losses;
		} else {
			return this.wins - that.wins;
		}
	}
	
}
