package noughts_and_crosses;

public class Tournament {
	PlayerInterface[] players;
	int numRounds;
	char[][][] results;
	boolean recordResults;
	int numPlayers;
	
	public Tournament(PlayerInterface[] players, int numRounds) {
		this(players, numRounds, true);
	}
	
	public Tournament(PlayerInterface[] players, int numRounds, boolean recordResults) {
		this.recordResults = recordResults;
		this.numPlayers = players.length;
		this.numRounds = numRounds;
		if (recordResults) {
			this.results = new char[numPlayers][numRounds][numPlayers];
		}
	}
	
	/**
	 * runs a sequence of games, in numRounds rounds. In each round,
	 * every player plays every other player (not itself)
	 */
	public void runTournament() {
		PlayerInterface[] currentPlayers = new PlayerInterface[2];
		for (int round = 0; round < this.numRounds; round++) {
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
							this.recordResults(round, i, j, result);
						}
					} catch (PlayerNumberException e) {
						String msg = String.format("Error in round %d, with players %d and %d", round, i, j);
						System.out.println(msg);
					}
					
				}
			}
		}
	}
	
	/**
	 * records the result of a particular round for each player
	 * @param round   an int, the round of the tournament
	 * @param player1 an int, the index of the noughts player
	 *                in this.players
	 * @param player2 an int, the index of the crosses player
	 *                in this.players
	 * @param result a char; if 'O' or 'X', indicates the winner,
	 *               if 'D', indicates a draw. No other value
	 *               should be passed
	 */
	private void recordResults(int round, int player1, int player2, char result) {
		if (result == 'O') {
			this.results[player1][round][player2] = 'W';
			this.results[player2][round][player1] = 'L';
		} else if (result == 'X') {
			this.results[player1][round][player2] = 'L';
			this.results[player2][round][player1] = 'W';
		} else if (result == 'D') {
			this.results[player1][round][player2] = 'D';
			this.results[player2][round][player1] = 'D';
		}
	}

	/**
	 * reports the results, by round, for the specified player
	 * @param player an int, indexing the chosen player in this.players
	 * @return an int[numRounds][3] array; each row is a round of the tournament,
	 * 		   and records {numWins, numLosses, numDraws} for that round
	 */
	public int[][] reportResultsByPlayer(int player) {
		int[][] results = new int[this.numRounds][3]; // 3 for win, loss, draw
		for (int round = 0; round < this.numRounds; round++) {
			int wins = 0;
			int losses = 0;
			int draws = 0;
			char[] roundResults = this.results[player][round];
			for (int opponent = 0; opponent < this.numPlayers; opponent++) {
				char result = roundResults[opponent]; // note: will be null when opponent == player
				if (result == 'W') {
					wins += 1;
				} else if (result == 'L') {
					losses += 1;
				} else if (result == 'D') {
					draws += 1;
				}
			}
			results[round][0] = wins;
			results[round][1] = losses;
			results[round][2] = draws;
		}
		return results;
	}

	/**
	 * reports the results, by player, for the specified round
	 * @param round the round of the tournament for which results
	 *              should be reported
	 * @return an int[3][numPlayers] array; rows are wins, losses,
	 *         and draws, respectively, and record numbers for each
	 *         player in this.players in turn
	 */
	public int[][] reportResultsByRound(int round) {
		int[][] results = new int[this.numPlayers][3]; // 3 for "win, loss, draw"
		for (int player = 0; player < this.numPlayers; player++) {
			int wins = 0;
			int losses = 0;
			int draws = 0;
			for (int opponent = 0; opponent < this.numPlayers; opponent++) {
				char result = this.results[player][round][opponent]; // will be null when opponent == player
				if (result == 'W') {
					wins += 1;
				} else if (result == 'L') {
					losses += 1;
				} else if (result == 'D') {
					draws += 1;
				}
			}
			results[player][0] = wins;
			results[player][1] = losses;
			results[player][2] = draws;
		}
		return results;
	}

	/**
	 * returns a summary of the wins, losses, and draws for the
	 * specified player
	 * @param player an int; the index in this.players of the player
	 *               whose results should be summarised
	 * @return an int[3] array, recording {numWins, numLosses, numDraws}
	 */
	public int[] reportSummaryByPlayer(int player) {
		int[] summary = {0, 0, 0}; // "wins, losses, draws"
		for (int round = 0; round < this.numRounds; round++) {
			for (int opponent = 0; opponent < this.numPlayers; opponent++) {
				char result = this.results[player][round][opponent];
				if (result == 'W') {
					summary[0] += 1;
				} else if (result == 'L') {
					summary[1] += 1;
				} else if (result == 'D') {
					summary[2] += 1;
				} 
			}
		}
		return summary;
	}
}
