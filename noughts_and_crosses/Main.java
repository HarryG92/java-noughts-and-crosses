package noughts_and_crosses;

public class Main {

	public static void main(String[] args) {
		
		
		
		
		ReinforcementPlayer noughts, crosses;
		noughts = new ReinforcementPlayer();
		crosses = new ReinforcementPlayer();
		PlayerInterface[] players = {noughts, crosses};
		
		for (int i = 0; i < 20000; i++) {
			try {
				Game game = new Game(players);
				game.runGame();
			} catch (PlayerNumberException e) {
				System.out.println("wtf? that's not meant to happen!");
			}
			
		}
		
		HumanPlayer me = new HumanPlayer("H");
		players[1] = me;
		try {
			Game game = new Game(players);
			game.runGame();
		} catch (PlayerNumberException e) {
			System.out.println("wtf? that's not meant to happen!");
		}
		
		
		
		System.out.println("Change roles");
		
		players[1] = crosses;
		players[0] = me;
		try {
			Game game = new Game(players);
			game.runGame();
		} catch (PlayerNumberException e) {
			System.out.println("wtf? that's not meant to happen!");
		}
		
		
//		HumanPlayer player1, player2;
//		player1 = new HumanPlayer("H");
//		player2 = new HumanPlayer("M");
//		HumanPlayer[] players = {player1, player2};
//		
//		try {
//			Game game = new Game(players);
//			game.runGame();
//		} catch(PlayerNumberException e) {
//			System.out.println(e);
//		}
//		
//		
//		System.out.println("And now the other way around!");
//		players[0] = player2;
//		players[1] = player1;
//		
//		try {
//			Game game = new Game(players);
//			game.runGame();
//		} catch(PlayerNumberException e) {
//			System.out.println(e);
//		}
		
	}

}
