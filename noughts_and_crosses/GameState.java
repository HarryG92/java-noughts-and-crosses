package noughts_and_crosses;

public class GameState {
	public final int BOARD_SIZE = 3;
	char[][] state;
	
	public GameState() {
		this.state = new char[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < this.BOARD_SIZE; row++) {
			for (int col = 0; col < this.BOARD_SIZE; col++) {
				this.state[row][col] = ' ';
			}
		}
	}
	
	public GameState(GameState that) {
		this.state = new char[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < this.BOARD_SIZE; row++) {
			for (int col = 0; col < this.BOARD_SIZE; col++) {
				this.state[row][col] = that.state[row][col];
			}
		}
	}
	
	/**
	 * checks if another GameState object represents the same state 
	 * @param that the other GameState object to be compared to this
	 * @return true if that GameState is the same as this
	 */
	public boolean isEqual(GameState that) {
		if (this.BOARD_SIZE != that.BOARD_SIZE) {
			return false;
		}
		
		for (int row = 0; row < this.BOARD_SIZE; row++) {
			for (int col = 0; col < this.BOARD_SIZE; col++) {
				if (this.state[row][col] != that.state[row][col]) {
					return false;
				}
			}
		}
		
		return true;
	}

	/**
	* processes a move of the game
	 * @param move   the Move to be attempted
	 * @return true if move successful
	 */
	public void makeMove(Move move, char symbol) {
		if (this.isMoveLegal(move)) {
			int row = move.row;
			int col = move.col;
			this.state[row][col] = symbol;
		} else {
			throw new ArrayIndexOutOfBoundsException();
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
			if (this.state[row][col] == ' ') {
				return true;
			} else {
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * checks if a particular player has won
	 * @param playerSymbol  a char, either 'O' or 'X'
	 * @return true if that player has won (by
	 *         having a row, column, or main diagonal
	 *         full with their symbol)
	 */
	public boolean hasWon(char playerSymbol) {
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
				transposedBoard[col][row] = this.state[row][col];
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
		return this.checkRowWin(this.state, playerSymbol);
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
			leadingDiag &= (this.state[row][row] == playerSymbol);
			int trailingCol = BOARD_SIZE - row - 1;
			trailingDiag &= (this.state[row][trailingCol] == playerSymbol);
		}
		
		return (leadingDiag | trailingDiag);
	}

	/**
	 * checks if every cell on the board is full.
	 * when this happens, the game is over (if no
	 * player has won, it's a draw)
	 * @return true if every cell is full
	 */
	public boolean isBoardFull() {
		for (int row = 0; row < this.BOARD_SIZE; row++) {
			for (int col = 0; col < this.BOARD_SIZE; col++) {
				if (this.state[row][col] == ' ') {
					return false;
				}
			}
		}
		return true;
	}
}
