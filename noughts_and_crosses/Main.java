package noughts_and_crosses;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		int numRounds = 100;
		int numPlayers = 100;
		
		PlayerInterface[] players = new PlayerInterface[numPlayers];
		for (int player = 0; player < numPlayers; player++) {
			players[player] = new ReinforcementPlayer();
		}
		
		PlayerInterface[] currentPlayers = new PlayerInterface[2];
		
		long startTime = System.currentTimeMillis();
		
		// initially each player trains against itself
		for (PlayerInterface player : players) {
			for (int round = 0; round < numRounds; round++) {
				currentPlayers[0] = player;
				currentPlayers[1] = player;
				try {
					Game game = new Game(currentPlayers);
					game.runGame();
				} catch (PlayerNumberException e) {
					System.out.println("wtf? that's not meant to happen!");
				}
			}
		}
		
		// then each player plays each other
		for (int round = 0; round < numRounds; round++) {
			for (PlayerInterface player1 : players) {
				currentPlayers[0] = player1;
				for (PlayerInterface player2: players) {
					currentPlayers[1] = player2;
					try {
						Game game = new Game(currentPlayers);
						game.runGame();
					} catch (PlayerNumberException e) {
						System.out.println("wtf? that's not meant to happen!");
					}
				}
			}
		}
		
		
		long endTime = System.currentTimeMillis();

		double duration = (double)(endTime - startTime) / 1000.0;
		String strDuration = String.format("%.2f", duration);
		System.out.println("Training took " + strDuration + "s");
		
		HumanPlayer me = new HumanPlayer("H");
		currentPlayers[0] = players[0];
		currentPlayers[1] = me;
		boolean cont = true;
		while (cont) {
			try {
				Game game = new Game(currentPlayers);
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
		
		currentPlayers[0] = me;
		currentPlayers[1] = players[0];
		
		cont = true;
		while (cont) {
			try {
				Game game = new Game(currentPlayers);
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
		
	}

}
