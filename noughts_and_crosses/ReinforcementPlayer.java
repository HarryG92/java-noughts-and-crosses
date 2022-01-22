package noughts_and_crosses;

public class ReinforcementPlayer extends RandomPlayer {
	// for tracking current game, for learning at the end
	protected double learningRate;
	
	public ReinforcementPlayer(String id) {
		this(id, 1.05);
	}
	
	public ReinforcementPlayer(String id, double rate) {
		super(id);
		this.learningRate = rate;
	}

	
	@Override
 	public int forfeit() {
		this.numForfeits += 1;
		MoveSelector selector = this.moveSelectors.get(this.lastState);
		selector.zeroOdds(this.lastMove);
		return numForfeits;
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
				if (state == this.lastState) {
					selector.zeroOdds(move);
				} else {
					double penalty = Math.pow(this.learningRate, -1);//(state.turn + 1));
					selector.multiplyOdds(move, penalty);
				}
			}
		}
		return numLosses;
	}

}
