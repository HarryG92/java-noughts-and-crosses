package noughts_and_crosses;
import java.util.HashMap;

public class ReinforcementPlayer extends Player {
	private HashMap<GameState, MoveSelector> moveSelectors = new HashMap<GameState, MoveSelector>();
	
	// for tracking current game, for learning at the end
	private HashMap<GameState, Move> movesPlayed;
	private GameState lastState;
	private Move lastMove;
	
	@Override public void startGame() {
		this.movesPlayed = new HashMap<GameState, Move>();
	}
	
	@Override
	public Move getMove(Board board) {
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
		
		MoveSelector selector = this.moveSelectors.get(keyState);
		Move chosenMove = selector.selectMove();
		this.movesPlayed.put(keyState, chosenMove);
		this.lastMove = chosenMove;
		this.lastState = keyState;
		return chosenMove;
	}
	
	@Override
	public int forfeit() {
		this.numForfeits += 1;
		MoveSelector selector = this.moveSelectors.get(this.lastState);
		selector.zeroOdds(this.lastMove);
		return numForfeits;
	}

	@Override
	public int winForfeit() {
		this.numWins += 1;
		// if won by forfeit, nothing to reinforce, as the
		// forfeit was down to the other player
		return numWins;
	}

	@Override
	public int win() {
		this.numWins += 1;
		for (GameState state : this.movesPlayed.keySet()) {
			MoveSelector selector = this.moveSelectors.get(state);
			Move move = this.movesPlayed.get(state);
			selector.increaseOdds(move, 2);
		}
		return numWins;
	}

	@Override
	public int lose() {
		this.numLosses += 1;
		for (GameState state : this.movesPlayed.keySet()) {
			MoveSelector selector = this.moveSelectors.get(state);
			Move move = this.movesPlayed.get(state);
			selector.decreaseOdds(move, 2);
		}
		return numLosses;
	}

	@Override
	public int draw() {
		this.numDraws += 1;
		return numDraws;
	}

}
