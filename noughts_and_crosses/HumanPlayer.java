package noughts_and_crosses;
import java.util.Scanner;

/**
 * A HumanPlayer object represents a human playing
 * the game. It takes keyboard input when needed to
 * select a move, and reports the current game state
 * and result as needed
 * @author H Gulliver
 *
 */
public class HumanPlayer extends Player {
	
	public HumanPlayer(String id) {
		this.playerID = id;
	}
	
	@Override
	public void startGame() {
		System.out.println("Starting a new game!");
	}
	
	Scanner scanner = new Scanner(System.in);
	
	@Override
	public Move getMove(Board board, boolean verbose) {
		board.displayBoard();
		boolean is_move_confirmed = false;
		int row = -1, col = -1;
		
		while (!is_move_confirmed) {
			String msg = String.format("Turn %d. Player %s to play:", board.board.turn, this.playerID);
			System.out.println(msg);
			System.out.println("Enter the (0-indexed) row you wish to play in: ");
			row = this.scanner.nextInt();
			System.out.println("Enter the (0-indexed) column you wish to play in: ");
			col = this.scanner.nextInt();
			msg = String.format("You will play in row %d, column %d. Confirm? y/n ", row, col);
			System.out.println(msg);
			String confirmation = this.scanner.next().toLowerCase();
			if (confirmation.equals("y")) {
				System.out.println("confirmed");
				is_move_confirmed = true;
			}
		}
		Move move = new Move(row, col);
		return move;
		
	}

	@Override
	public int forfeit() {
		this.numForfeits += 1;
		this.numLosses += 1;
		System.out.println("You lost by forfeit (you played an illegal move!)");
		return this.numForfeits;
	}

	@Override
	public int winForfeit() {
		this.numWins += 1;
		System.out.println("You won by forfeit! (your opponent played an illegal move!)");
		return this.numWins;
	}

	@Override
	public int win(int playerNum) {
		this.numWins += 1;
		System.out.println("You won! Congrats");
		return this.numWins;
	}

	@Override
	public int lose(int playerNum) {
		this.numLosses += 1;
		System.out.println("You lost :( Better luck next time!");
		return this.numLosses;
	}

	@Override
	public int draw(int playerNum) {
		this.numDraws += 1;
		System.out.println("It's a draw!");
		return this.numDraws;
	}
	

}
