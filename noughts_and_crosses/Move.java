package noughts_and_crosses;

/**
 * A Move object represents a single move
 * in noughts-and-crosses. It simply stores
 * the row and column number (0-indexed) of the
 * cell to be drawn in. This class is mainly included
 * to allow future extensions to more complicated games,
 * where a move may be more complex than two integers.
 * @author H Gulliver
 *
 */
public class Move {
	int row;
	int col;
	
	public Move(int row, int col) {
		this.row = row;
		this.col = col;
	}
}
