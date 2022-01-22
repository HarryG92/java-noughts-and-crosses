package noughts_and_crosses;

public class SymmetrisedReinforcementPlayer extends ReinforcementPlayer {
	private GameState[] rotations;
	private GameState[] reflections;
	private int rotation;
	private boolean reflection;
	
	public SymmetrisedReinforcementPlayer(String id) {
		super(id);
	}
	
	public SymmetrisedReinforcementPlayer(String id, double rate) {
		super(id, rate);
	}
	
	/**
	 * rotates a (copy of a) GameState by 90 degrees clockwise
	 * @param state the GameState to be rotated
	 * @return a GameState which is a copy of the input GameState,
	 *         but rotated by 90 degrees clockwise
	 */
  	private GameState rotateState(GameState state) {
		char[][] oldState = state.state;
		char[][] newState = new char[state.BOARD_SIZE][state.BOARD_SIZE];
		for (int row = 0; row < state.BOARD_SIZE; row++) {
			for (int col = 0; col < state.BOARD_SIZE; col++) {
				int newRow = col;
				int newCol = state.BOARD_SIZE - row - 1;
				newState[newRow][newCol] = oldState[row][col];
			}
		}
		return new GameState(newState);
	}
  	
	/**
	 * reflects a (copy of a) GameState in the vertical axis
	 * @param state the GameState to be reflected
	 * @return a GameState which is a copy of the input
	 *         state, but reflected in the vertical axis
	 */
	private GameState reflectState(GameState state) {
		char[][] oldState = state.state;
		char[][] newState = new char[state.BOARD_SIZE][state.BOARD_SIZE];
		for (int row = 0; row < state.BOARD_SIZE; row++) {
			for (int col = 0; col < state.BOARD_SIZE; col++) {
				int newCol = state.BOARD_SIZE - col - 1;
				newState[row][newCol] = oldState[row][col];
			}
		}
		return new GameState(newState); 
	}

	/**
	 * rotates a Move by 90 degrees ANTIclockwise
	 * note: rotateState works clockwise, and rotateMove
	 * anticlockwise so that applying a move on a rotated
	 * state is the same as applying the rotated move on
	 * the state
	 * @param move      the Move to be rotated
	 * @param boardSize the size of the board 
	 * @return a new Move, the rotation of the input Move
	 */
	private Move rotateMove(Move move, int boardSize) {
		int newRow = boardSize - move.col - 1;
		int newCol = move.row;
		return new Move(newRow, newCol);
	}
	
	/**
	 * reflects a move in the vertical axis
	 * @param move      the Move to be reflected
	 * @param boardSize the size of the board
	 * @return a new Move, the reflection of the input
	 */
	private Move reflectMove(Move move, int boardSize) {
		int newRow = move.row;
		int newCol = boardSize - move.col - 1;
		return new Move(newRow, newCol);
	}
	
	@Override
	protected GameState findKeyState(Board board) {
		// need to take a copy of the game state to avoid passing by reference
		GameState keyState = new GameState(board.board);
		
		// find the orbit of the board's GameState under the symmetry group
		GameState rotatedState = keyState;
		this.rotations = new GameState[4];
		this.reflections = new GameState[4];
		for (int rotation = 0; rotation < 4; rotation++) {
			GameState reflectedState = this.reflectState(rotatedState);
			this.rotations[rotation] = rotatedState;
			this.reflections[rotation] = reflectedState;
			rotatedState = this.rotateState(rotatedState);
		}
		
		boolean hasSeenBefore = false;
		outerLoop:
		for (GameState seenState : moveSelectors.keySet()) {
			for (int rotation = 0; rotation < 4; rotation++) {
				if (seenState.isEqual(this.rotations[rotation])) {
					this.rotation = rotation;
					this.reflection = false;
					keyState = seenState;
					hasSeenBefore = true;
					break outerLoop;
				} else if (seenState.isEqual(this.reflections[rotation])) {
					this.rotation = rotation;
					keyState = seenState;
					this.reflection = true;
					hasSeenBefore = true;
					break outerLoop;
				}
			}
		}
		
		if (!hasSeenBefore) {
			MoveSelector selector = new MoveSelector(board);
			this.moveSelectors.put(keyState, selector);
			this.rotation = 0;
			this.reflection = false;
		}
		
		return keyState;
	}
	
	@Override
	public Move getMove(Board board, boolean verbose) {
		Move move = super.getMove(board, verbose);
		if (verbose) {
			System.out.println(this.rotation);
			System.out.println(this.reflection);
		}
		// board is rotated then reflected, so move needs to
		// be reflected, then rotated to invert
		if (this.reflection) {
			move = this.reflectMove(move, board.BOARD_SIZE);
		}
		for (int rotation = 0; rotation < this.rotation; rotation++) {
			move = this.rotateMove(move, board.BOARD_SIZE);
		}
		return move;
	}
}
