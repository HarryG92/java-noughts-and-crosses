package noughts_and_crosses;

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
		
		this.board = board;
		this.players = players;
	}

	public char runGame() {
		char result = this.board.getResult();
		while (result == ' ') {
			int turn = this.board.turn;
			int playerNumber = turn % NUMBER_PLAYERS;
			int otherPlayerNumber = (turn + 1) % NUMBER_PLAYERS;
			PlayerInterface player = this.players[playerNumber];
			PlayerInterface otherPlayer = this.players[otherPlayerNumber];
			Move attemptedMove = this.requestMove(player);
			int moveSuccess = this.board.makeMove(attemptedMove);
			if (moveSuccess != 0) {
				player.forfeit();
				otherPlayer.winForfeit();
				return board.PLAYERS[otherPlayerNumber];
			} else {
				result = this.board.getResult();
			}
		}
		
		return result;
	}
	
	private Move requestMove(PlayerInterface player) {
		return player.getMove(this.board);
	}
}
