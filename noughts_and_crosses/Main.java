package noughts_and_crosses;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		int numRounds = 100;
		int numPlayers = 150;
		
		PlayerInterface[] players = new PlayerInterface[numPlayers];
		// set up one third ReinforcementPlayers, one third SymmetrisedReinforcementPlayers,
		// one third RandomPlayers
		for (int player = 0; player < numPlayers; player++) {
			if (player % 3 == 0) {
				players[player] = new ReinforcementPlayer();
			} else if (player % 3 == 1) {
				players[player] = new SymmetrisedReinforcementPlayer();
			} else {
				players[player] = new RandomPlayer();
			}
		}
		
		PlayerInterface[] currentPlayers = new PlayerInterface[2];
		
		long startTime = System.currentTimeMillis();
		
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
		
		System.out.println("And now a symmetrised player");
		
		currentPlayers[0] = players[1];
		currentPlayers[1] = me;
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
		
		
		System.out.println("Change roles");
		
		currentPlayers[0] = me;
		currentPlayers[1] = players[1];
		
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
