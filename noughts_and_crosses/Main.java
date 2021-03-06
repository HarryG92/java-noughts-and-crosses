package noughts_and_crosses;
import java.util.Scanner;
import java.io.*;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		int trainingRounds = 10;
		int competitionRounds = 10;
		int numPlayers = 10;
		// need number of different rates to be coprime with number of types of player, or else
		// more cunning setup logic to make sure each type of player gets each learning rate
		double[] learningRates = {1.001, 1.002, 1.005, 1.01, 1.02};
		//double[] learningRates = {1.01, 1.02, 1.05, 1.1, 1.2};
		
		Player[] players = new Player[numPlayers];
		
		// set up half ReinforcementPlayers, half SymmetrisedReinforcementPlayers,
		// with 9 different learning rates - 5 of each at each rate
		for (int player = 0; player < numPlayers; player++) {
			int rateType = player % learningRates.length;
			double rate = learningRates[rateType];
			if (player % 2 == 0) {
				players[player] = new ReinforcementPlayer(Integer.toString(player), rate);
			} else {
				players[player] = new SymmetrisedReinforcementPlayer(Integer.toString(player), rate);
			}
		}
		
		Player[] currentPlayers = new Player[2];
		
		long startTime = System.currentTimeMillis();
		
		// training
		Tournament training = new Tournament(players, trainingRounds, false);
		
		long trainingEndTime = System.currentTimeMillis();
		double duration = (double)(trainingEndTime - startTime) / 1000.0;
		String strDuration = String.format("%.2f", duration);
		System.out.println("Training took " + strDuration + "s");
		
		// competition
		Tournament competition = new Tournament(players, competitionRounds);
		
		long competitionEndTime = System.currentTimeMillis();

		duration = (double)(competitionEndTime - trainingEndTime) / 1000.0;
		strDuration = String.format("%.2f", duration);
		System.out.println("Competition took " + strDuration + "s");
		
		Player[] rankings = competition.getPlayerRankings();
		// print IDs of top 10 ranked players
		for (int rank = 0; rank < 10; rank++) {
			int playerID = Integer.parseInt(rankings[rank].playerID);
			// print playerID, learning rate, player type (0 = Reinforcement, 1 = Symmetrised)
			String msg = String.format("%d: %f, %d", playerID, learningRates[playerID % learningRates.length], playerID % 2);
			System.out.println(msg);
		}
		competition.reportPlayerResults(0).plotResults();
//		rankings[0].serialize();
//		String filename = String.format("%s.ser", rankings[0].playerID);
//		
//		Player serializedPlayer = Player.deserialize(filename);
//		
//		HumanPlayer me = new HumanPlayer("H");
//		currentPlayers[0] = serializedPlayer;
//		currentPlayers[1] = me;
//		boolean cont = true;
//		while (cont) {
//			try {
//				Game game = new Game(currentPlayers);
//				game.runGame(true);
//			} catch (PlayerNumberException e) {
//				System.out.println("wtf? that's not meant to happen!");
//			}
//			System.out.println("Again? y/n ");
//			String confirmation = scanner.next().toLowerCase();
//			if (!confirmation.equals("y")) {
//				cont = false;
//			}
//		}
//		
//		
//		System.out.println("Change roles");
//		
//		currentPlayers[0] = me;
//		currentPlayers[1] = serializedPlayer;
//		
//		cont = true;
//		while (cont) {
//			try {
//				Game game = new Game(currentPlayers);
//				game.runGame(true);
//			} catch (PlayerNumberException e) {
//				System.out.println("wtf? that's not meant to happen!");
//			}
//			System.out.println("Again? y/n ");
//			String confirmation = scanner.next().toLowerCase();
//			if (!confirmation.equals("y")) {
//				cont = false;
//			}
//		}

	}

}
