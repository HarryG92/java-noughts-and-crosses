package noughts_and_crosses;

public interface PlayerInterface {
	
	public Move getMove(Board board);
	
	public int forfeit();
	
	public int winForfeit();
	
	public int win();
	
	public int lose();
	
	public int draw();
	
	public int getNumWins();
	
	public int getNumDraws();
	
	public int getNumLosses();
	
	public int getNumForfeits();
}
