package noughts_and_crosses;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
//		Board board = new Board();
//		MoveSelector selector = new MoveSelector(board);
//		selector.printOdds();
//		Move move = selector.selectMove();
//		selector.adjustOdds(move, 3, 2);
//		selector.printOdds();
		
		ReinforcementPlayer noughts, crosses;
		noughts = new ReinforcementPlayer();
		crosses = new ReinforcementPlayer();
		PlayerInterface[] players = {noughts, crosses};
		
		for (int i = 0; i < 10000; i++) {
			try {
				Game game = new Game(players);
				game.runGame();
			} catch (PlayerNumberException e) {
				System.out.println("wtf? that's not meant to happen!");
			}
			
		}
		
		HumanPlayer me = new HumanPlayer("H");
		players[1] = me;
		boolean cont = true;
		while (cont) {
			try {
				Game game = new Game(players);
				game.runGame();
			} catch (PlayerNumberException e) {
				System.out.println("wtf? that's not meant to happen!");
			}
			System.out.println("Again? y/n ");
			String confirmation = scanner.next().toLowerCase();
			if (!confirmation.equals("y")) {
				cont = false;
			}
		}
		
		
		
		
		System.out.println("Change roles");
		
		players[1] = crosses;
		players[0] = me;
		
		cont = true;
		while (cont) {
			try {
				Game game = new Game(players);
				game.runGame();
			} catch (PlayerNumberException e) {
				System.out.println("wtf? that's not meant to happen!");
			}
			System.out.println("Again? y/n ");
			String confirmation = scanner.next().toLowerCase();
			if (!confirmation.equals("y")) {
				cont = false;
			}
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
