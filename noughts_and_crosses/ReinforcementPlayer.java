package noughts_and_crosses;
import java.util.HashMap;

public class ReinforcementPlayer extends Player {
	protected HashMap<GameState, MoveSelector> moveSelectors;
	
	// for tracking current game, for learning at the end
	protected HashMap<GameState, Move> movesPlayed;
	protected GameState lastState;
	protected Move lastMove;
	protected double learningRate;
	
	public ReinforcementPlayer() {
		this(1.05);
	}
	
	public ReinforcementPlayer(double rate) {
		this.learningRate = rate;
		 this.moveSelectors = new HashMap<GameState, MoveSelector>();
	}
	
	@Override public void startGame() {
		this.movesPlayed = new HashMap<GameState, Move>();
	}
	
	@Override
	public Move getMove(Board board, boolean verbose) {
		GameState keyState = this.findKeyState(board);
		MoveSelector selector = this.moveSelectors.get(keyState);
		Move chosenMove = selector.selectMove(verbose);
		this.movesPlayed.put(keyState, chosenMove);
		this.lastMove = chosenMove;
		this.lastState = keyState;
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
 	public int forfeit() {
		System.out.println("Forfeited!");
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
	public int win(int playerNum) {
		this.numWins += 1;
		for (GameState state : this.movesPlayed.keySet()) {
			if (state.turn % 2 != playerNum) {
				continue;
			} else {
				MoveSelector selector = this.moveSelectors.get(state);
				Move move = this.movesPlayed.get(state);
				if (state == this.lastState) {
					selector.makeCertain(move);
				} else {
					double reward = Math.pow(this.learningRate, state.turn + 1);
					selector.multiplyOdds(move, reward);
				}
			}
			
		}
		return numWins;
	}

	@Override
	public int lose(int playerNum) {
		this.numLosses += 1;
		for (GameState state : this.movesPlayed.keySet()) {
			if (state.turn % 2 != playerNum) {
				continue;
			} else {
				MoveSelector selector = this.moveSelectors.get(state);
				Move move = this.movesPlayed.get(state);
				double penalty = Math.pow(this.learningRate, -(state.turn + 1));
				selector.multiplyOdds(move, penalty);
			}
		}
		return numLosses;
	}

	@Override
	public int draw(int playerNum) {
		this.numDraws += 1;
		return numDraws;
	}

}
