package noughts_and_crosses;
import java.util.Arrays;

/**
 * A Tournament object runs a specified number of games between a given collection of players,
 * and optionally records the results to provide stats and a ranking.
 * A Tournament without results recorded is suitable for training rounds, and then a second
 * Tournament with results for determining the strongest players
 * @author H Gulliver
 *
 */
public class Tournament {
	Player[] players;
	int numRounds;
	RoundResults[] results;
	int numPlayers;
	final boolean recordResults;
	
	public Tournament(Player[] players, int numRounds) {
		this(players, numRounds, true);
	}
	
	public Tournament(Player[] players, int numRounds, boolean recordResults) {
		this.recordResults = recordResults;
		this.numPlayers = players.length;
		this.numRounds = numRounds;
		this.players = players;
		if (recordResults) {
			this.results = new RoundResults[numRounds];
		}
		this.runTournament();
	}
	
	/**
	 * a RoundResults objects stores the results of an individual round of the
	 * Tournament - the outcome of the game between each pair of players
	 * @author H Gulliver
	 *
	 */
	class RoundResults {
		int roundNumber;
		char[][] results;
		
		RoundResults(int roundNumber) {
			this.roundNumber = roundNumber;
			int numPlayers = Tournament.this.numPlayers;
			this.results = new char[numPlayers][numPlayers];
		}
		
		/**
		 * records the result of a single game in the round
		 * @param player1 an int, the index of the first player
		 * @param player2 an int, the index of the second player
		 * @param result  a char, 'O' if first player one, 'X' if
		 *                second player one, 'D' if draw
		 */
		void recordResult(int player1, int player2, char result) {
			this.results[player1][player2] = result;
		}
		
		/**
		 * returns all the results of games played by a specified player
		 * in this round
		 * @param player an int, the index of the player whose results are wanted
		 * @return an array of ints, recording wins, losses, and draws
		 */
		PlayerResults reportResultsByPlayer(int player) {
			PlayerResults results = new PlayerResults(Tournament.this.players[player]);
			char noughtsResult;
			char crossesResult;
			for (int opponent = 0; opponent < Tournament.this.numPlayers; opponent++) {
				if (player == opponent) {
					continue;
				}
				noughtsResult = this.results[player][opponent];
				results.addResultAsNoughts(noughtsResult);
				crossesResult = this.results[opponent][player];
				results.addResultAsCrosses(crossesResult);
			}
			return results;
		}
	}
	
	/**
	 * runs a sequence of games, in numRounds rounds. In each round,
	 * every player plays every other player (not itself) twice - once
	 * as noughts (first player), once as crosses (second player)
	 */
	public void runTournament() {
		Player[] currentPlayers = new Player[2];
		for (int round = 0; round < this.numRounds; round++) {
			if (this.recordResults) {
				this.results[round] = new RoundResults(round);
			}
			for (int i = 0; i < this.numPlayers; i++) {
				currentPlayers[0] = this.players[i];
				for (int j = 0; j < this.numPlayers; j++) {
					if (i == j) {
						continue;
					}
					currentPlayers[1] = this.players[j];
					try {
						Game game = new Game(currentPlayers);
						char result = game.runGame();
						if (this.recordResults) {
							this.results[round].recordResult(i, j, result);
						}
					} catch (PlayerNumberException e) {
						String msg = String.format("Error in round %d, with players %d and %d", round, i, j);
						System.out.println(msg);
					}
					
				}
			}
			System.out.println(round);
		}
	}

	/**
	 * gets the results of a particular player in the tournament
	 * @param player an int, the index of the player whose results should be given
	 * @return a PlayerResults object recording the player's results
	 */
	public PlayerResults reportPlayerResults(int playerNumber) {
		Player player = this.players[playerNumber];
		PlayerResults results = new PlayerResults(player);
		for (int round = 0; round < this.numRounds; round++) {
			RoundResults roundResults = this.results[round];
			PlayerResults current = roundResults.reportResultsByPlayer(playerNumber);
			results.add(roundResults.reportResultsByPlayer(playerNumber));
		}
		return results;
	}

	/**
	 * Ranks the players based on their performance in the tournament.
	 * The ordering used for the ranking is number of wins (more wins means
	 * higher ranking), with ties resolved by number of losses (more losses
	 * means lower ranking). When two players have the same number of wins
	 * and losses, this method may rank them in either order relative to
	 * each other
	 * @return an array of PlayerResults, one for each player in this.players,
	 *         in order of highest ranked first to lowest ranked last
	 */
	public PlayerResults[] getRankedResults() {
		PlayerResults[] results = new PlayerResults[this.numPlayers];
		for (int player = 0; player < this.numPlayers; player++) {
			results[player] = this.reportPlayerResults(player);
		}
		Arrays.sort(results);
		return results;
	}
	
	/**
	 * Ranks the players based on their performance in the tournament.
	 * The ordering used for the ranking is number of wins (more wins means
	 * higher ranking), with ties resolved by number of losses (more losses
	 * means lower ranking). When two players have the same number of wins
	 * and losses, this method may rank them in either order relative to
	 * each other
	 * @return an array of PlayerInterfaces, one for each player in this.players,
	 *         in order of highest ranked first to lowest ranked last
	 */
	Player[] getPlayerRankings() {
		PlayerResults[] results = this.getRankedResults();
		Player[] rankings = new Player[this.numPlayers];
		for (int player = 0; player < this.numPlayers; player++) {
			rankings[player] = results[player].player;
		}
		return rankings;
	}
}
