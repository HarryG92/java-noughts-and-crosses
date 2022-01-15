package noughts_and_crosses;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		ReinforcementPlayer aiPlayer = new ReinforcementPlayer();
		PlayerInterface[] players = {aiPlayer, aiPlayer};
		
		int numRounds = 10000;
		
		long startTime = System.currentTimeMillis();
		
		for (int round = 0; round < numRounds; round++) {
			try {
				Game game = new Game(players);
				game.runGame();
			} catch (PlayerNumberException e) {
				System.out.println("wtf? that's not meant to happen!");
			}
		}
		
		long endTime = System.currentTimeMillis();

		double duration = (double)(endTime - startTime) / 1000.0;
		String strDuration = String.format("%.2f", duration);
		System.out.println("Training took " + strDuration + "s");
		
		HumanPlayer me = new HumanPlayer("H");
		players[1] = me;
		boolean cont = true;
		while (cont) {
			try {
				Game game = new Game(players);
				game.runGame(true);
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
		
		players[0] = me;
		players[1] = aiPlayer;
		
		cont = true;
		while (cont) {
			try {
				Game game = new Game(players);
				game.runGame(true);
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
