This project is to create a reinforcement learning noughts and crosses player.

The idea is for the program to look at the current state of the board and choose a legal move at random.
Initially, the selection will be uniform (all legal moves equally likely). When the game ends, the program will adjust its probabilities, so that in future, when it sees a game state it has seen before, it is more likely to repeat moves that led to victories in the past, and less likely to repeat moves that led to defeats.

Initially, I will have the program ignore draws. Then I might try different approaches to find an optimal training strategy. For example, it might be best to initially reward draws (i.e., the program should be more likely to repeat moves that led to draws in the past, although the reward should be less than for a win), and once the program never loses, it should then start to penalise draws to try to learn to win.

Of course, with perfect play from both players, noughts and crosses always ends in a draw...allowing boards larger than 3x3 might get around this, though it will slow training. Or I might need to extend to a more complicated game.

So the program should be built to be easily extensible to different games. It should also allow players with different learning algorithms, to allow comparison of different approaches (though in basic noughts and crosses, the comparison will essentially come down to how quickly each algorithm learns perfect play).

The general plan is:
- A GameState class, responsible for maintaining the current state of play and updating it when moves are made
- A Board class, responsible for managing the GameState, tracking whose turn it is and displaying the board if needed
- A Move class, which simply stores whatever details are needed for a particular move. For noughts and crosses, this will only store two integers (row and column), but having a separate Move class will make it easier to extend to other games.
- A PlayerInterface, setting up the methods a player needs to interact with the other objects
- A Player abstract class implementing PlayerInterface by adding fields to track statistics and methods to access these, but leaving abstract the methods for playing
- A HumanPlayer class extending Player to allow a human to play (useful for testing and to see how the trained reinforcement learning player behaves in various situations)
- One or more AIPlayer classes extending Player and implementing reinforcement learning
- A MoveSelector class to handle the selection of a Move at random (but with a specified probability)
- A Game class to manage the interactions between Board, Player, and Move classes and run a single game
- A PlayerResults class to store and compare results
- A Tournament class to run multiple games and track results

At present, the AIPlayer classes are sufficiently complex that they are broken down further:
- A MoveSelector class which is responsible for choosing at random a legal Move in a single GameState, and adjusting the odds of picking a given Move up or down
- A RandomPlayer class which maintains a collection of MoveSelectors (one for each GameState it has seen) and uses these to choose its Moves when playing
- A ReinforcementPlayer class extending RandomPlayer; a ReinforcementPlayer tracks each GameState it sees and Move it plays in a given Game, then at the end of the Game goes back to its MoveSelectors and adjusts the odds of those Moves upwards if they led to a win, and downwards if they led to a loss (with no change for a draw)
- A SymmetrisedReinforcementPlayer class; similar to the ReinforcementPlayer, but uses rotations and reflections to compare GameStates, so it views two GameStates as the same if they differ by a rotation or reflection, and uses this when choosing a Move. This means that SymmetrisedReinforcementPlayers end up storing fewer MoveSelectors, and should learn faster. It may also influence the strategies they learn (e.g., corner-first play vs middle-first play).


## TO DO

- GUI
- Graphing of results - particularly as training develops
- BayesianPlayer - have a utility function for outcomes (positive for win, negative for loss, probably 0 for draw) and an estimated probability distribution over outcomes, then play the move that maximises the expected utility
