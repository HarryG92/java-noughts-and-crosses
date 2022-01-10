package noughts_and_crosses;

/**
 * A Board object represents a noughts-and-crosses board
 * This is a 3x3 square grid (although the code is written
 * to allow arbitrary square grids) on which two players
 * ("noughts" and "crosses") take turns to draw their symbols
 * ('O' and 'X' respectively). A player wins if they fill
 * every cell in a single row, column, or main diagonal with
 * their symbol. A player forfeits if they try to draw a symbol
 * in a cell that doesn't exist or that has already been filled.
 * If every cell is filled and no player has won or forfeited,
 * the game ends in a draw. 
 * @author H Gulliver
 *
 */
public class Board {
	public final int BOARD_SIZE = 3, NUMBER_PLAYERS = 2;
	GameState board;
	final char NOUGHTS = 'O', CROSSES = 'X';
	public final char[] PLAYERS = {NOUGHTS, CROSSES};
	int turn = 0;
	char nextSymbol;
	
	public Board() {
		this.board = new GameState();
		this.updateSymbol();
	}
	
	// constructor allowing fields to be set instead of made from scratch
	public Board(GameState board, int turn) {
		this.board = board;
		this.turn = turn;
		this.updateSymbol();
	}
	
	// copy constructor
	public Board(Board that) {
		this(that.board, that.turn);
	}
	
	/**
	 * checks who the next player will be from turn number
	 * and updates the nextSymbol accordingly
	 */
	private void updateSymbol() {
		int nextPlayerNumber = this.turn % 2;
		this.nextSymbol = PLAYERS[nextPlayerNumber];
	}
	
	/** 
	 * processes a move of the game
	 * @param move   the Move to be attempted
	 * @return true if move successful
	 */
	public boolean makeMove(Move move) {
		try {
			this.board.makeMove(move, this.nextSymbol);
			this.turn += 1;
			this.updateSymbol();
			//this.displayBoard(); //for debugging only
			return true;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	/**
	 * prints the board in a human-readable way
	 */
	public void displayBoard() {
		String printableBoard = "-------\n";
		for (int row = 0; row < this.board.BOARD_SIZE; row++) {
			printableBoard += "|";
			for (int col = 0; col < this.board.BOARD_SIZE; col++) {
				printableBoard += this.board.state[row][col] + "|";
			}
			printableBoard += "\n-------\n";
		}
		
		System.out.println(printableBoard);
	}
	
	
	/**
	 * determines the result (winner, draw, or unfinished)
	 * of the game
	 * @return char  'O' if noughts has won, 'X' if crosses has won,
	 *               'D' if a draw, ' ' if the game is unfinished 
	 */
	public char getResult() {
		for (char player : PLAYERS) {
			if (this.board.hasWon(player)) {
				return player;
			}
		}
		if (this.board.isBoardFull()) {
			return 'D';
		}
		return ' ';
	}


}
