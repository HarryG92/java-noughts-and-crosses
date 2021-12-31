package noughts_and_crosses;

public class Main {

	public static void main(String[] args) {
		HumanPlayer player1, player2;
		player1 = new HumanPlayer("H");
		player2 = new HumanPlayer("M");
		HumanPlayer[] players = {player1, player2};
		
		try {
			Game game = new Game(players);
			game.runGame();
		} catch(PlayerNumberException e) {
			System.out.println(e);
		}
		
		
		System.out.println("And now the other way around!");
		players[0] = player2;
		players[1] = player1;
		
		try {
			Game game = new Game(players);
			game.runGame();
		} catch(PlayerNumberException e) {
			System.out.println(e);
		}
		
	}

}
