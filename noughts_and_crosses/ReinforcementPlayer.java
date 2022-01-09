package noughts_and_crosses;
import java.util.HashMap;
import java.util.ArrayList;

public class ReinforcementPlayer extends Player {
	private HashMap<Board, MoveSelector> moveSelectors = new HashMap<Board, MoveSelector>();
	private ArrayList<Board> boardStates;
	private ArrayList<Move> movesPlayed;
	private int numMoves = 0;
	
	@Override public void startGame() {
		this.movesPlayed = new ArrayList<Move>();
		this.boardStates = new ArrayList<Board>();
	}
	
	@Override
	public Move getMove(Board board) {
		boolean hasSeenBefore = false;
		Board keyBoard = board;
		for (Board seenBoard : moveSelectors.keySet()) {
			if (seenBoard.isEqual(board)) {
				keyBoard = seenBoard;
				hasSeenBefore = true;
				break;
			}
		}
		
		if (!hasSeenBefore) {
			MoveSelector selector = new MoveSelector(board);
			this.moveSelectors.put(board, selector);
		}
		
		this.boardStates.add(keyBoard);
		MoveSelector selector = this.moveSelectors.get(keyBoard);
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
	private Board getFinalBoard() {
		return this.boardStates.get(this.numMoves - 1);
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
		Board finalBoard = this.getFinalBoard();
		Move finalMove = this.getFinalMove();
		MoveSelector selector = this.moveSelectors.get(finalBoard);
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
		Board finalBoard = this.getFinalBoard();
		Move finalMove = this.getFinalMove();
		MoveSelector selector = this.moveSelectors.get(finalBoard);
		selector.increaseOdds(finalMove, 2);
		return numWins;
	}

	@Override
	public int lose() {
		this.numLosses += 1;
		Board finalBoard = this.getFinalBoard();
		Move finalMove = this.getFinalMove();
		MoveSelector selector = this.moveSelectors.get(finalBoard);
		selector.decreaseOdds(finalMove, 2);
		return numLosses;
	}

	@Override
	public int draw() {
		this.numDraws += 1;
		return numDraws;
	}

}
