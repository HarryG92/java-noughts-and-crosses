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
		
		int numPlayers = 10;
		PlayerInterface[] players = new PlayerInterface[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			players[i] = new ReinforcementPlayer();
		}
		
		int numCurrentPlayers = 2;
		int numRounds = 100;
		
		while (numCurrentPlayers <= numPlayers) {
			for (int round = 0; round < numRounds; round++) {
				// one complete round of training
				for (int i = 0; i < numCurrentPlayers; i++) {
					for (int j = 0; j < numCurrentPlayers; j++) {
						PlayerInterface currentPlayers[] = {players[i], players[j]};
						try {
							Game game = new Game(currentPlayers);
							game.runGame();
						} catch (PlayerNumberException e) {
							System.out.println("wtf? that's not meant to happen!");
						}
					}
				}
			}
			// add 2 new players for the next round
			numCurrentPlayers += 2;
			System.out.println(numCurrentPlayers);
		}
		
		// once initial staggered rounds are done, run several full round
		numCurrentPlayers = numPlayers;
		numRounds = 1000;
		for (int round = 0; round < numRounds; round++) {
			// one complete round of training
			for (int i = 0; i < numCurrentPlayers; i++) {
				for (int j = 0; j < numCurrentPlayers; j++) {
					PlayerInterface currentPlayers[] = {players[i], players[j]};
					try {
						Game game = new Game(currentPlayers);
						game.runGame();
					} catch (PlayerNumberException e) {
						System.out.println("wtf? that's not meant to happen!");
					}
				}
			}
		}
		
		
		
		
//		for (int i = 0; i < 100; i++) {
//			try {
//				Game game = new Game(players);
//				game.runGame();
//			} catch (PlayerNumberException e) {
//				System.out.println("wtf? that's not meant to happen!");
//			}
//			
//		}
		
		HumanPlayer me = new HumanPlayer("H");
		PlayerInterface[] currentPlayers = {players[numPlayers - 1], me};
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
		currentPlayers[1] = players[numPlayers - 1];
		
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
