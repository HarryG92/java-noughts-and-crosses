package noughts_and_crosses;

import java.util.HashMap;

/**
 * A RandomPlayer is a Player that randomly chooses moves at each turn.
 * The first time it sees a particular GameState, it lists all legal moves
 * in that state, and assigns each a probability. Every time it sees that
 * GameState again, it looks up the probabilities it has assigned, and
 * selects a move to play according to those probabilities. The probabilities
 * can be assigned uniformly (so each legal move is equally likely), or at
 * random (so in each GameState it has a random but fixed probability
 * distribution of moves).
 * It is mainly intended as a base class for various learning Player classes,
 * but is not an abstract class, to allow non-learning random players - this
 * can provide a baseline for the win/loss/draw rates expected of a random
 * player, to compare learning players against
 * @author H Gulliver
 *
 */
public class RandomPlayer extends Player {
	protected HashMap<GameState, MoveSelector> moveSelectors;
	boolean randomise;

	public RandomPlayer(String id, boolean randomise) {
		super(id);
		this.moveSelectors = new HashMap<GameState, MoveSelector>();
		this.randomise = randomise;
	}
	
	public RandomPlayer(String id) {
		this(id, false);
	}
	
	@Override
	public void startGame() {
		// pass
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
	 * the key. If this.randomise = true, the initial odds of each move
	 * will be random; otherwise, they will be uniform
	 * @param board     the Board whose GameState is to be compared with keys of
	 *                  this.moveSelectors
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
			if (this.randomise) {
				selector.randomiseOdds();
			}
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
		return this.numLosses;
	}

	@Override
	public int forfeit() {
		this.numForfeits += 1;
		this.numLosses += 1;
		return this.numForfeits;
	}
}