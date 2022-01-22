package noughts_and_crosses;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		int trainingRounds = 1000;
		int competitionRounds = 1000;
		int numPlayers = 2;
		// need number of different rates to be coprime with number of types of player, or else
		// more cunning setup logic to make sure each type of player gets each learning rate
		//double[] learningRates = {1.001, 1.002, 1.005, 1.01, 1.02, 1.05, 1.1, 1.2, 1.5};
		double[] learningRates = {1.2, 1.2};
		
		Player[] players = new Player[numPlayers];
		
		// set up half ReinforcementPlayers, half SymmetrisedReinforcementPlayers,
		// with 9 different learning rates - 5 of each at each rate
		for (int player = 0; player < numPlayers; player++) {
			int rateType = player % 9;
			double rate = learningRates[rateType];
			if (player % 2 == 0) {
				players[player] = new ReinforcementPlayer(Integer.toString(player), rate);
			} else {
				players[player] = new ReinforcementPlayer(Integer.toString(player), rate);
			}
		}
		
		Player[] currentPlayers = new Player[2];
		
		long startTime = System.currentTimeMillis();
		
		//training
		Tournament training = new Tournament(players, trainingRounds, false);
		
		long trainingEndTime = System.currentTimeMillis();
		double duration = (double)(trainingEndTime - startTime) / 1000.0;
		String strDuration = String.format("%.2f", duration);
		System.out.println("Training took " + strDuration + "s");
		
		Tournament competition = new Tournament(players, competitionRounds);
		
		long competitionEndTime = System.currentTimeMillis();

		duration = (double)(competitionEndTime - trainingEndTime) / 1000.0;
		strDuration = String.format("%.2f", duration);
		System.out.println("Competition took " + strDuration + "s");
		
		Player[] rankings = competition.getPlayerRankings();
		// print IDs of top 10 ranked players
		for (int rank = 0; rank < 2; rank++) {
			int playerID = Integer.parseInt(rankings[rank].playerID);
			// print playerID, learning rate, player type (0 = Reinforcement, 1 = Symmetrised)
			String msg = String.format("%d: %f, %d", playerID, learningRates[playerID % 9], playerID % 2);
			System.out.println(msg);
		}
		
		
		
		HumanPlayer me = new HumanPlayer("H");
		currentPlayers[0] = rankings[0];
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
		currentPlayers[1] = rankings[0];
		
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
