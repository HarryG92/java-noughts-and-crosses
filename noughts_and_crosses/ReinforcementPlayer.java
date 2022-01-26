package noughts_and_crosses;
import java.util.ArrayList;

public class ReinforcementPlayer extends RandomPlayer {
	// for tracking current game, for learning at the end
	protected double learningRate;
	ArrayList<MovePlayed> movesPlayed;
	
	public ReinforcementPlayer(String id) {
		this(id, 1.05);
	}
	
	public ReinforcementPlayer(String id, double rate) {
		super(id);
		this.learningRate = rate;
	}
	
	@Override
	public void startGame() {
		this.movesPlayed = new ArrayList<MovePlayed>();
	}

	private class MovePlayed implements java.io.Serializable {
		final GameState state;
		final Move move;
		
		private MovePlayed(GameState state, Move move) {
			this.state = state;
			this.move = move;
		}
	}
	
	@Override
	public Move getMove(Board board, boolean verbose) {
		GameState keyState = this.findKeyState(board);
		Move chosenMove = super.getMove(board, verbose);
		MovePlayed movePlayed = new MovePlayed(keyState, chosenMove);
		this.movesPlayed.add(movePlayed);
		return chosenMove;
	}
	
	@Override
 	public int forfeit() {
		this.numForfeits += 1;
		int numMovesPlayed = this.movesPlayed.size();
		MovePlayed lastMove = this.movesPlayed.get(numMovesPlayed - 1);
		MoveSelector selector = this.moveSelectors.get(lastMove.state);
		selector.zeroOdds(lastMove.move);
		return numForfeits;
	}

	@Override
	public int win(int playerNum) {
		this.numWins += 1;
		int numMovesPlayed = this.movesPlayed.size();
		for (int moveNum = 0; moveNum < numMovesPlayed - 1; moveNum++) {
			MovePlayed movePlayed = this.movesPlayed.get(moveNum);
			double reward = Math.pow(this.learningRate, movePlayed.state.turn + 1);
			MoveSelector selector = this.moveSelectors.get(movePlayed.state);
			selector.multiplyOdds(movePlayed.move, reward);
		}
		MovePlayed lastMove = this.movesPlayed.get(numMovesPlayed - 1);
		MoveSelector selector = this.moveSelectors.get(lastMove.state);
		selector.makeCertain(lastMove.move);
		return numWins;
	}

	@Override
	public int lose(int playerNum) {
		this.numLosses += 1;
		int numMovesPlayed = this.movesPlayed.size();
		for (int moveNum = 0; moveNum < numMovesPlayed - 1; moveNum++) {
			MovePlayed movePlayed = this.movesPlayed.get(moveNum);
			double reward = Math.pow(this.learningRate, -(movePlayed.state.turn + 1));
			MoveSelector selector = this.moveSelectors.get(movePlayed.state);
			selector.multiplyOdds(movePlayed.move, reward);
		}
		MovePlayed lastMove = this.movesPlayed.get(numMovesPlayed - 1);
		MoveSelector selector = this.moveSelectors.get(lastMove.state);
		selector.zeroOdds(lastMove.move);
		return numLosses;
	}
	
	@Override
	public int draw(int playerNum) {
		this.numDraws += 1;
		// if crosses player, draw is the best you can expect, so reward slightly
		if (playerNum == 1) {
			// draws are rewarded at an intermediate rate
			double reducedRate = ((this.learningRate - 1) / 2) + 1;
			for (MovePlayed movePlayed : this.movesPlayed) {
				MoveSelector selector = this.moveSelectors.get(movePlayed.state);
				selector.multiplyOdds(movePlayed.move, reducedRate);
			}
		}
		// do not reward or penalise draws for noughts player
		return numDraws;
	}

}
