package noughts_and_crosses;

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
	
	private void updateSymbol() {
		int nextPlayerNumber = this.turn % 2;
		this.nextSymbol = PLAYERS[nextPlayerNumber];
	}
	
	public int makeMove(Move move) {
		int row = move.row;
		int col = move.col;
		try {
			if (this.board[row][col] == ' ') {
				this.board[row][col] = this.nextSymbol;
				this.turn += 1;
				this.updateSymbol();
				return 0; // success code
			} else {
				return 1; // cell already full error
			}
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			return 2; // index out of bounds error
		}
		
	}
	
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
	
	public char getResult() {
		// returns one of player symbols (if that player has won),
		// 'D' if a draw, or ' ' if the game is ongoing
		// assumes game is in a valid state:
		// if both players have attained win conditions,
		// this will only return one of them (whichever is listed
		// first in the players array). But this cannot
		// occur in legal play
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
	
	private boolean hasWon(char playerSymbol) {
		boolean rowWin, colWin, diagWin;
		rowWin = this.checkRowWin(playerSymbol);
		colWin = this.checkColWin(playerSymbol);
		diagWin = this.checkDiagWin(playerSymbol);
		
		return (rowWin | colWin | diagWin);
	}
	
	private char[][] transpose() {
		char [][] transposedBoard = new char[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				transposedBoard[col][row] = this.board[row][col];
			}
		}
		return transposedBoard;
	}
	
	private boolean checkRowWin(char[][] boardState, char playerSymbol) {
		// check if player has won by filling a row
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
	
	private boolean checkRowWin(char playerSymbol) {
		return this.checkRowWin(this.board, playerSymbol);
	}
	
	private boolean checkColWin(char playerSymbol) {
		// prioritised ease-of-writing over efficiency
		// might rewrite this to copy checkRowWin for performance improvement
		char[][] transposedBoard = this.transpose();
		return this.checkRowWin(transposedBoard, playerSymbol);
	}
	
	private boolean checkDiagWin(char playerSymbol) {
		// check if either of the main diagonals provides a win
		boolean leadingDiag = true, trailingDiag = true;
		for (int row = 0; row < BOARD_SIZE; row++) {
			leadingDiag &= (this.board[row][row] == playerSymbol);
			int trailingCol = BOARD_SIZE - row - 1;
			trailingDiag &= (this.board[row][trailingCol] == playerSymbol);
		}
		
		return (leadingDiag | trailingDiag);
	}

}
