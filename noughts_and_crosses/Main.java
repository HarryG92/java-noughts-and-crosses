package noughts_and_crosses;

public class Main {

	public static void main(String[] args) {
		
		Board board = new Board();
		
		Move move0 = new Move(0, 0);
		board.makeMove(move0);
		Move move1 = new Move(0, 1);
		board.makeMove(move1);
		Move move2 = new Move(0, 2);
		board.makeMove(move2);
		board.displayBoard();
		
		MoveSelector sel1 = new MoveSelector(board);
		sel1.printOdds();
		
		System.out.println("\n");
		
		Move move3 = sel1.selectMove();
		
		for (int i = 0; i < 10; i++) {
			sel1.increaseOdds(move3, 16);
			sel1.printOdds();
			Move testMove = sel1.selectMove();
			System.out.print(testMove.row);
			System.out.print(testMove.col);
			System.out.print("\n\n");
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
