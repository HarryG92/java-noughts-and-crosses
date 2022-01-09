package noughts_and_crosses;
import java.util.HashMap;
import java.util.ArrayList;

public class ReinforcementPlayer extends Player {
	private HashMap<GameState, MoveSelector> moveSelectors = new HashMap<GameState, MoveSelector>();
	private ArrayList<GameState> seenStates;
	private ArrayList<Move> movesPlayed;
	private int numMoves = 0;
	
	@Override public void startGame() {
		this.movesPlayed = new ArrayList<Move>();
		this.seenStates = new ArrayList<GameState>();
	}
	
	@Override
	public Move getMove(Board board) {
		boolean hasSeenBefore = false;
		GameState keyState = board.board;
		for (GameState seenState : moveSelectors.keySet()) {
			if (seenState.isEqual(board.board)) {
				keyState = seenState;
				hasSeenBefore = true;
				System.out.println("old");
				break;
			}
		}
		
		if (!hasSeenBefore) {
			MoveSelector selector = new MoveSelector(board);
			System.out.println("new");
			this.moveSelectors.put(board.board, selector);
		}
		
		this.seenStates.add(keyState);
		MoveSelector selector = this.moveSelectors.get(keyState);
		Move chosenMove = selector.selectMove();
		this.movesPlayed.add(chosenMove);
		this.numMoves += 1;
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
		GameState finalState = this.getFinalState();
		Move finalMove = this.getFinalMove();
		MoveSelector selector = this.moveSelectors.get(finalState);
		selector.zeroOdds(finalMove);
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
		GameState finalState = this.getFinalState();
		Move finalMove = this.getFinalMove();
		MoveSelector selector = this.moveSelectors.get(finalState);
		selector.increaseOdds(finalMove, 2);
		return numWins;
	}

	@Override
	public int lose() {
		this.numLosses += 1;
		GameState finalState = this.getFinalState();
		Move finalMove = this.getFinalMove();
		MoveSelector selector = this.moveSelectors.get(finalState);
		selector.decreaseOdds(finalMove, 2);
		return numLosses;
	}

	@Override
	public int draw() {
		this.numDraws += 1;
		return numDraws;
	}

}
