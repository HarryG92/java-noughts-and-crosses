package noughts_and_crosses;
import java.util.HashMap;
import java.util.ArrayList;

public class ReinforcementPlayer extends Player {
	private HashMap<GameState, MoveSelector> moveSelectors = new HashMap<GameState, MoveSelector>();
	
	// for tracking current game, for learning at the end
	private ArrayList<GameState> seenStates;
	private ArrayList<Move> movesPlayed;
	private HashMap<GameState, Move> movesPlayed2;
	private GameState lastState;
	private Move lastMove;
	private int numMoves;
	
	@Override public void startGame() {
		this.movesPlayed = new ArrayList<Move>();
		this.seenStates = new ArrayList<GameState>();
		this.numMoves = 0;
		this.movesPlayed2 = new HashMap<GameState, Move>();
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
		
		this.seenStates.add(keyState);
		MoveSelector selector = this.moveSelectors.get(keyState);
		Move chosenMove = selector.selectMove();
		this.movesPlayed.add(chosenMove);
		this.movesPlayed2.put(keyState, chosenMove);
		this.numMoves += 1;
		this.lastMove = chosenMove;
		this.lastState = keyState;
		return chosenMove;
	}

	/**
	 * finds the last board state seen in the current game
	 * @return the Board object that was the game state
	 *         before the final move this player played
	 */
	private GameState getFinalState() {
		return this.seenStates.get(this.numMoves - 1);
	}
	
	/**
	 * finds the last move played in the current game
	 * @return the Move object that was played last
	 */
	private Move getFinalMove() {
		return this.movesPlayed.get(this.numMoves - 1);
	}
	
	@Override
	public int forfeit() {
		this.numForfeits += 1;
		//GameState finalState = this.getFinalState();
		//Move finalMove = this.getFinalMove();
		MoveSelector selector = this.moveSelectors.get(this.lastState);
		//MoveSelector selector = this.moveSelectors.get(finalState);
		selector.zeroOdds(this.lastMove);
		//selector.zeroOdds(finalMove);
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
		System.out.println("win");
		this.numWins += 1;
		for (GameState state : this.movesPlayed2.keySet()) {
			MoveSelector selector = this.moveSelectors.get(state);
			Move move = this.movesPlayed2.get(state);
			selector.increaseOdds(move, 2);
		}
//		GameState finalState = this.getFinalState();
//		Move finalMove = this.getFinalMove();
//		MoveSelector selector = this.moveSelectors.get(finalState);
//		selector.increaseOdds(finalMove, 2);
		return numWins;
	}

	@Override
	public int lose() {
		this.numLosses += 1;
		for (GameState state : this.movesPlayed2.keySet()) {
			MoveSelector selector = this.moveSelectors.get(state);
			Move move = this.movesPlayed2.get(state);
			selector.decreaseOdds(move, 2);
		}
//		GameState finalState = this.getFinalState();
//		Move finalMove = this.getFinalMove();
//		MoveSelector selector = this.moveSelectors.get(finalState);
//		selector.decreaseOdds(finalMove, 2);
		return numLosses;
	}

	@Override
	public int draw() {
		this.numDraws += 1;
		return numDraws;
	}

}
