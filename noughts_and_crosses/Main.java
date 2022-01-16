package noughts_and_crosses;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		int numRounds = 200;
		int numPlayers = 100;
		
		PlayerInterface[] players = new PlayerInterface[numPlayers];
		
		// set up one quarter ReinforcementPlayers, one quarter SymmetrisedReinforcementPlayers,
		// one quarter uniform RandomPlayers, and one quarter non-uniform RandomPlayers
		for (int player = 0; player < numPlayers; player++) {
			if (player % 4 == 0) {
				players[player] = new ReinforcementPlayer();
			} else if (player % 4 == 1) {
				players[player] = new RandomPlayer(true);
			} else if (player % 4 == 2) {
				players[player] = new SymmetrisedReinforcementPlayer();
			} else {
				players[player] = new RandomPlayer(false);
			}
		}
		// note: the odd-numbered ones are non-learning, and there's no point playing two non-learning
		// players against each other
		
		PlayerInterface[] currentPlayers = new PlayerInterface[2];
		
		long startTime = System.currentTimeMillis();
		
		for (int round = 0; round < numRounds; round++) {
			for (int i = 0; i < numPlayers; i++) {
				for (int j = 0; j < numPlayers; j++) {
					if (i % 2 == 0 & j % 2 == 0) { // if both players can learn
						currentPlayers[0] = players[i];
						currentPlayers[1] = players[j];
						try {
							Game game = new Game(currentPlayers);
							game.runGame();
						} catch (PlayerNumberException e) {
							System.out.println("wtf? that's not meant to happen!");
						}
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
		
		currentPlayers[0] = players[2];
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
		currentPlayers[1] = players[2];
		
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
