package noughts_and_crosses;
import java.util.HashMap;

public class ReinforcementPlayer extends Player {

	private HashMap<Board, Float[]> moveProbabilities = new HashMap<Board, Float[]>();
	
	@Override
	public Move getMove(Board board) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int forfeit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int winForfeit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int win() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int lose() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int draw() {
		// TODO Auto-generated method stub
		return 0;
	}

}
