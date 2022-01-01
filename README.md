This project is to create a reinforcement learning noughts and crosses player.

The idea is for the program to look at the current state of the board and choose a legal move at random.
Initially, the selection will be uniform (all legal moves equally likely). When the game ends, the program will adjust its probabilities, so that in future, when it sees a game state it has seen before, it is more likely to repeat moves that led to victories in the past, and less likely to repeat moves that led to defeats.

Initially, I will have the program ignore draws. Then I might try different approaches to find an optimal training strategy. For example, it might be best to initially reward draws (i.e., the program should be more likely to repeat moves that led to draws in the past, although the reward should be less than for a win), and once the program never loses, it should then start to penalise draws to try to learn to win.

Of course, with perfect play from both players, noughts and crosses always ends in a draw...allowing boards larger than 3x3 might get around this, though it will slow training. Or I might need to extend to a more complicated game.

So the program should be built to be easily extensible to different games. It should also allow players with different learning algorithms, to allow comparison of different approaches (though in basic noughts and crosses, the comparison will essentially come down to how quickly each algorithm learns perfect play).

The general plan is: 
- A class for the Board, responsible for maintaining the state of play and updating it when moves are made (or returning an error code if the move is illegal).
- A class for a Move, which simply stores whatever details are needed for a particular move. For noughts and crosses, this will only store two integers (row and column), but having a separate Move class will make it easier to extend to other games.
- A PlayerInterface, setting up the methods a player needs to interact with the other objects
- A Player abstract class implementing PlayerInterface by adding fields to track statistics and methods to access these, but leaving abstract the methods for playing
- A HumanPlayer class extending Player to allow a human to play (useful for testing and to see how the trained reinforcement learning player behaves in various situations)
- One or more AIPlayer classes extending Player and implementing reinforcement learning
- A Game class to manage the interactions between Board, Player, and Move classes and run a single game.
- A Match class to run multiple games and track results.
