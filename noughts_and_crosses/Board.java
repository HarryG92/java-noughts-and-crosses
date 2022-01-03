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
	char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
	final char NOUGHTS = 'O', CROSSES = 'X';
	public final char[] PLAYERS = {NOUGHTS, CROSSES};
	int turn = 0;
	char nextSymbol;
	
	public Board() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				this.board[row][col] = ' ';
			}
		}
		this.updateSymbol();
	}
	
	// constructor allowing fields to be set instead of made from scratch
	public Board(char[][] board, int turn) {
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
		if (this.isMoveLegal(move)) {
			int row = move.row;
			int col = move.col;
			this.board[row][col] = this.nextSymbol;
			this.turn += 1;
			this.updateSymbol();
			return true;
		} else {
			return false;
		}
	}
	
	/** 
	 * checks if a given move is legal 
	 * @param move   the Move to be tested
	 * @return boolean  true if Move is legal in current game state      
	 */
	public boolean isMoveLegal(Move move) {
		int row = move.row;
		int col = move.col;
		try {
			if (this.board[row][col] == ' ') {
				return true;
			} else {
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	/**
	 * prints the board in a human-readable way
	 */
	public void displayBoard() {
		String printableBoard = "-------\n";
		for (int row = 0; row < BOARD_SIZE; row++) {
			printableBoard += "|";
			for (int col = 0; col < BOARD_SIZE; col++) {
				printableBoard += this.board[row][col] + "|";
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
			if (this.hasWon(player)) {
				return player;
			}
		}
		if (this.isBoardFull()) {
			return 'D';
		}
		return ' ';
	}
	
	/**
	 * checks if every cell on the board is full
	 * when this happens, the game is over (if no
	 * player has won, it's a draw)
	 * @return true if every cell is full
	 */
	private boolean isBoardFull() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				if (this.board[row][col] == ' ') {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * checks if a particular player has won
	 * @param playerSymbol  a char, either 'O' or 'X'
	 * @return true if that player has won (by
	 *         having a row, column, or main diagonal
	 *         full with their symbol)
	 */
	private boolean hasWon(char playerSymbol) {
		boolean rowWin, colWin, diagWin;
		rowWin = this.checkRowWin(playerSymbol);
		colWin = this.checkColWin(playerSymbol);
		diagWin = this.checkDiagWin(playerSymbol);
		
		return (rowWin | colWin | diagWin);
	}
	
	/**
	 * transposes the board - so rows and columns
	 * are swapped. This is a convenience method
	 * for checking victory conditions
	 * @return a char[][] array representing the
	 *         transposed state of the board 
	 */
	private char[][] transpose() {
		char [][] transposedBoard = new char[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				transposedBoard[col][row] = this.board[row][col];
			}
		}
		return transposedBoard;
	}
	
	/**
	 * checks if a particular player has filled
	 * a row with their symbol (and hence won)
	 * @param boardState   a char[][] array representing the
	 *                     current state of the board 
	 * @param playerSymbol a char ('O' or 'X') representing
	 *                     the current player
	 * @return true if playerSymbol has filled any row
	 */
	private boolean checkRowWin(char[][] boardState, char playerSymbol) {
		for (int row = 0; row < BOARD_SIZE; row++) {
			boolean rowWin = true;
			for (int col = 0; col < BOARD_SIZE; col++) {
				rowWin &= (boardState[row][col] == playerSymbol);				
			}
			if (rowWin) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * checks if a particular player has filled
	 * a row with their symbol (and hence won)
	 * @param playerSymbol a char ('O' or 'X') representing
	 *                     the current player
	 * @return true if playerSymbol has filled any row
	 */
	private boolean checkRowWin(char playerSymbol) {
		return this.checkRowWin(this.board, playerSymbol);
	}
	
	/**
	 * checks if a particular player has filled
	 * a column with their symbol (and hence won)
	 * @param playerSymbol a char ('O' or 'X') representing
	 *                     the current player
	 * @return true if playerSymbol has filled any column
	 */
	private boolean checkColWin(char playerSymbol) {
		// prioritised ease-of-writing over efficiency
		// might rewrite this to copy checkRowWin for performance improvement
		char[][] transposedBoard = this.transpose();
		return this.checkRowWin(transposedBoard, playerSymbol);
	}
	
	/**
	 * checks if a particular player has filled
	 * a diagonal with their symbol (and hence won)
	 * @param playerSymbol a char ('O' or 'X') representing
	 *                     the current player
	 * @return true if playerSymbol has filled any diagonal
	 */
	private boolean checkDiagWin(char playerSymbol) {
		boolean leadingDiag = true, trailingDiag = true;
		for (int row = 0; row < BOARD_SIZE; row++) {
			leadingDiag &= (this.board[row][row] == playerSymbol);
			int trailingCol = BOARD_SIZE - row - 1;
			trailingDiag &= (this.board[row][trailingCol] == playerSymbol);
		}
		
		return (leadingDiag | trailingDiag);
	}

}
