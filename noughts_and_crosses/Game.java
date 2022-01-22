package noughts_and_crosses;

/**
 * A Game object manages a single game of
 * noughts-and-crosses. It interfaces between the
 * players and the board to allow each player to
 * play a move in turn, and reports the result
 * when the game ends.
 * @author H Gulliver
 *
 */
public class Game {
	PlayerInterface[] players;
	Board board;
	final int NUMBER_PLAYERS = 2;
	
	public Game(PlayerInterface[] players) throws PlayerNumberException {
		this(new Board(), players);
	}
	
	public Game(Board board, PlayerInterface[] players) throws PlayerNumberException {
		if (players.length != NUMBER_PLAYERS) {
			String message = String.format("Tried instantiating Game with %d players",players.length);
			throw new PlayerNumberException(message);
		}
		if (players[0] == players[1]) {
			throw new IllegalArgumentException("Players cannot play against themselves");
		}
		
		this.board = board;
		this.players = players;
	}

	/**
	 * runs a complete game, from start to finish.
	 * Asks each player in turn for their move,
	 * and makes those moves on the board
	 * @return a char representing the outcome
	 *         of the game. 'X' or 'O' for the winner,
	 *         or 'D' for a draw
	 */
	public char runGame() {
		return this.runGame(false);
	}
	
	/**
	 * runs a complete game, from start to finish.
	 * Asks each player in turn for their move,
	 * and makes those moves on the board
	 * @param verbose  a boolean; if true, displays
	 *                 some internal working
	 * @return a char representing the outcome
	 *         of the game. 'X' or 'O' for the winner,
	 *         or 'D' for a draw
	 */
	public char runGame(boolean verbose) {
		char result = this.board.getResult();
		for (PlayerInterface player : this.players) {
			player.startGame();
		}
		while (result == ' ') {
			int turn = this.board.board.turn;
			int playerNumber = turn % NUMBER_PLAYERS;
			int otherPlayerNumber = (turn + 1) % NUMBER_PLAYERS;
			PlayerInterface player = this.players[playerNumber];
			PlayerInterface otherPlayer = this.players[otherPlayerNumber];
			Move attemptedMove = this.requestMove(player, verbose);
			boolean moveSuccess = this.board.makeMove(attemptedMove);
			if (moveSuccess) {
				result = this.board.getResult();
				this.reportResult(result);
			} else {
				player.forfeit();
				otherPlayer.winForfeit();
				return board.PLAYERS[otherPlayerNumber];
			}
		}
		return result;
	}
	
	/**
	 * Lets players know the result when the game is over
	 * (for a non-forfeit reason) by calling their .win,
	 * .lose, or .draw methods as appropriate
	 * @param result   the char representing the game result
	 *                 'X' or 'O' for the winner, 'D' for a draw
	 *                 ' ' if the game is ongoing (in which case
	 *                 this method does nothing)
	 */
	private void reportResult(char result) {
		PlayerInterface noughtsPlayer = this.players[0];
		PlayerInterface crossesPlayer = this.players[1];
		if (result == 'O') {
			noughtsPlayer.win(0);
			crossesPlayer.lose(1);
		} else if (result == 'X') {
			noughtsPlayer.lose(0);
			crossesPlayer.win(1);
		} else if (result == 'D') {
			noughtsPlayer.draw(0);
			crossesPlayer.draw(1);
		}
	}
	
	/**
	 * asks the player for their next move choice
	 * @param player an object implementing the 
	 *               PlayerInterface
	 * @param verbose a boolean; if true, displays
	 *                information about internal workings
	 * @return a Move object representing the choice
	 *         of move
	 */
	private Move requestMove(PlayerInterface player, boolean verbose) {
		return player.getMove(this.board, verbose);
	}
}
