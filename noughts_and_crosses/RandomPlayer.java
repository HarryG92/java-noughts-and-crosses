package noughts_and_crosses;

import java.util.HashMap;

public class RandomPlayer extends Player {

	protected HashMap<GameState, MoveSelector> moveSelectors;

	public RandomPlayer() {
		super();
	}
	
	@Override
	public void startGame() {
		// no setup needed for a basic RandomPlayer
	}

	@Override
	public Move getMove(Board board, boolean verbose) {
		GameState keyState = this.findKeyState(board);
		MoveSelector selector = this.moveSelectors.get(keyState);
		Move chosenMove = selector.selectMove(verbose);
		return chosenMove;
	}

	/**
	 * finds the previous GameState used as a key in this.moveSelectors
	 * and matching the GameState of the Board, if such exists, or creates
	 * a new entry in this.moveSelectors so that the current GameState is
	 * the key 
	 * @param board the Board whose GameState is to be compared with keys of
	 *              this.moveSelectors
	 * @return a GameState which is a key in this.moveSelectors and
	 *         matches currentState (represents the same game)
	 */
	protected GameState findKeyState(Board board) {
		boolean hasSeenBefore = false;
		// need to take a copy of the game state to avoid passing by reference
		GameState keyState = new GameState(board.board);
		for (GameState seenState : moveSelectors.keySet()) {
			if (seenState.isEqual(board.board)) {
				keyState = seenState;
				hasSeenBefore = true;
				break;
			}
		}
		
		if (!hasSeenBefore) {
			MoveSelector selector = new MoveSelector(board);
			this.moveSelectors.put(keyState, selector);
		}
		
		return keyState;
	}

	@Override
	public int winForfeit() {
		this.numWins += 1;
		// if won by forfeit, nothing to reinforce, as the
		// forfeit was down to the other player
		return numWins;
	}

	@Override
	public int draw(int playerNum) {
		this.numDraws += 1;
		return numDraws;
	}

	@Override
	public int win(int playerNum) {
		this.numWins += 1;
		return this.numWins;
	}
	
	@Override
	public int lose(int playerNum) {
		this.numLosses += 1;
		System.out.println("You lost :( Better luck next time!");
		return this.numLosses;
	}

	@Override
	public int forfeit() {
		this.numForfeits += 1;
		this.numLosses += 1;
		return this.numForfeits;
	}
}