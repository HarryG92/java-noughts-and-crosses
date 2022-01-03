package noughts_and_crosses;

/**
 * The PlayerInterface declares all the methods
 * needed by the Game class to interact with the
 * player
 * @author H Gulliver
 *
 */
public interface PlayerInterface {
	
	/**
	 * chooses the next move to make 
	 * @param board the Board object with its current
	 *              game state
	 * @return a Move object representing the chosen
	 *         move
	 */
	public Move getMove(Board board);
	
	/**
	 * tells this player that they forfeit the
	 * game
	 * @return the integer count of number
	 *         of forfeits that player has
	 *         made so far 
	 */
	public int forfeit();
	
	/**
	 * tells this player that their opponent has
	 * forefeited and so this player wins
	 * @return the integer count of number
	 *         of forfeits that player has
	 *         won so far 
	 */
	public int winForfeit();
	
	/**
	 * tells this player that they have won the
	 * game
	 * @return the integer count of number
	 *         of wins that player has
	 *         made so far 
	 */
	public int win();
	
	/**
	 * tells this player that they have lost the
	 * game
	 * @return the integer count of number
	 *         of losses this player has
	 *         made so far 
	 */
	public int lose();
	
	/**
	 * tells this player that the game is a draw
	 * @return the integer count of number
	 *         of draws this player has
	 *         made so far 
	 */
	public int draw();

}
