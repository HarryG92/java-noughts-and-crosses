package noughts_and_crosses;
import java.util.ArrayList;
import java.util.ListIterator;
//graphing packages
import java.awt.*;  
import javax.swing.*;  
import java.awt.geom.*;  

/**
 * A PlayerResults object stores the aggregated results of a game or series of games
 * for an object implementing PlayerInterface. It stores number of wins, losses,
 * and draws, but not game-level results. PlayerResults objects can be compared, where
 * first they are compared by number of losses (fewer losses means greater Result), and
 * ties are resolved by number of wins (more wins means greater Result). The
 * comparison are done by raw numbers, not proportions, so two Results objects should
 * only be compared if they record results for the same total number of games
 * @author H Gulliver
 *
 */
public class PlayerResults implements Comparable<PlayerResults> {
	Player player;
	int wins;
	int draws;
	int losses;
	int total;
	ArrayList<Character> results;
	
	public PlayerResults(Player player) {
		this(player, 0, 0, 0);
	}
	
	public PlayerResults(Player player, int wins, int draws, int losses) {
		this.player = player;
		this.wins = wins;
		this.draws = draws;
		this.losses = losses;
		this.total = wins + draws + losses;
		this.results = new ArrayList<Character>();
	}
	
	/**
	 * adds a single result onto the record
	 * @param result a char, one of 'W', 'L', or 'D',
	 *               for win, loss, or draw respectively
	 */
	void addResult(char result) {
		if (result == 'W') {
			this.wins += 1;
			this.results.add('W');
		} else if (result == 'L') {
			this.losses += 1;
			this.results.add('L');
		} else if (result == 'D') {
			this.draws += 1;
			this.results.add('D');
		} else {
			throw new IllegalArgumentException("result must be one of W, L, or D");
		}
		this.total += 1;
	}

	/**
	 * adds a single result onto the record, assuming this
	 * player was noughts for the game being added
	 * @param result a char; one of 'O', 'X', or 'D', for
	 *        "noughts won", "crosses won", or "draw"
	 */
	void addResultAsNoughts(char result) {
		if (result == 'O') {
			this.wins += 1;
			this.results.add('W');
		} else if (result == 'X') {
			this.losses += 1;
			this.results.add('L');
		} else if (result == 'D') {
			this.draws += 1;
			this.results.add('D');
		} else {
			throw new IllegalArgumentException("result must be one of X, O, D");
		}
		this.total += 1;
	}
	
	/**
	 * adds a single result onto the record, assuming this
	 * player was crosses for the game being added
	 * @param result a char; one of 'O', 'X', or 'D', for
	 *        "noughts won", "crosses won", or "draw"
	 */
	void addResultAsCrosses(char result) {
		if (result == 'O') {
			this.losses += 1;
			this.results.add('L');
		} else if (result == 'X') {
			this.wins += 1;
			this.results.add('W');
		} else if (result == 'D') {
			this.draws += 1;
			this.results.add('D');
		} else {
			throw new IllegalArgumentException("result must be one of X, O, D");
		}
		this.total += 1;
	}
	
	@Override
	public int compareTo(PlayerResults that) {
		if (this.losses == that.losses) {
			return this.wins - that.wins;
		} else {
			return that.losses - this.losses;
		}
	}
	
	/**
	 * prints the number of wins, draws, and losses in this results object
	 */
	public void printResults() {
		String results = String.format("Wins: %d, draws: %d, losses: %d", this.wins, this.draws, this.losses);
		System.out.println(results);
	}
	
	public void add (PlayerResults that) {
		if (this.player == that.player) {
			this.wins += that.wins;
			this.losses += that.losses;
			this.draws += that.draws;
			this.total = this.wins + this.losses + this.draws;
			this.results.addAll(that.results);
		} else {
			throw new IllegalArgumentException("Cannot add PlayerResults for two separate players!");
		}
	}
	
	/**
	 * finds the proportion of entries in an ArrayList of Characters up to each
	 * index which are a particular char
	 * @param list   an ArrayList<Character> in which we want to find the changing
	 *               proportions of a given char
	 * @param symbol the char whose proportions we want to study
	 * @return an array of doubles, where index i records the proportion of entries
	 *         of list up to and including the ith which are equal to symbol
	 */
	private double[] cumulativeProportion(ArrayList<Character> list, char symbol) {
		int length = list.size();
		System.out.println(length);
		double[] cumProp = new double[length];
		double count = 0.0;
		ListIterator<Character> iterator = list.listIterator();
		cumProp[0] = 0; // to avoid a 0/0 issue
		for (int i = 1; i < length; i++) {
			if (iterator.next() == symbol) {
				count += 1.0;
			}
			cumProp[i] = count / (double) i;
		}
		return cumProp;
	}
	
	/**
	 * gives the proportion of wins/losses/draws after each game played
	 * @param result a char; one of 'W', 'L', 'D'. The result whose
	 *               proportions are to be returned
	 * @return an array of doubles; the ith entry records the proportion
	 *         of games ending in result up to and including the ith game
	 */
	private double[] cumulativeProportionResult(char result) {
		return this.cumulativeProportion(this.results, result);
	}
	
	/*
	 * gives the running proportions of each result
	 * @return a 2D array of doubles; each row is the running proportions
	 *         of a particular result after each game, and the rows are
	 *         ordered win, loss, draw. So, e.g., if the sequence of results
	 *         is W,W,L,D,L,D, this method will return
	 *         {{1, 1, 2/3, 2/4, 2/5, 2/6},
	 *          {0, 0, 1/3, 1/4, 2/5, 2/6},
	 *          {0, 0, 0, 1/4, 1/5, 1/6}}
	 */
	private double[][] cumulativeProportionAllResults() {
		double[][] cumProps = new double[3][this.total];
		char[] results = {'W', 'L', 'D'};
		for (int i = 0; i < 3; i++) {
			cumProps[i] = this.cumulativeProportionResult(results[i]);
		}
		return cumProps;
	}

	/**
	 * a ResultsPlotter object allows multiple line graphs to be plotted on the
	 * same axes; this is designed for plotting the running proportions or total
	 * amounts of wins, losses, and draws, to study the performance of a Player
	 * over time
	 * When initialised, the ResultsPlotter takes a 2D array of doubles (where each
	 * row of the array is a sequence of y values to be plotted at equal x-increments)
	 * and an array of colours, whose length should match the number of rows in the
	 * double array (these are the colours the different lines will be plotted in)
	 * @author H Gulliver
	 *
	 */
	private class ResultsPlotter extends JPanel{  
	    //initialize coordinates    
	    int margin = 60;
        int legendWidth = 100;
	    double[][] yVals;
	    Color[] colors; // US spelling to be consistent with the class name

	    /**
	     * create a ResultsPlotter for plotting multiple line graphs
	     * @param yVals  a 2D array of doubles; each row will be plotted as a line
	     *               with equally spaced x-increments
	     * @param colors an array of Color objects, of length matching yVals; each
	     *               row of yVals will be plotted in the corresponding color
	     */
	    public ResultsPlotter(double[][] yVals, Color[] colors) {
	    	this.yVals = yVals;
	    	this.colors = colors;
	    }
	      
	    /**
	     * plots a line graph at regular x intervals of this.yVals
	     * @param grf a Graphics object to plot to
	     */
	    protected void paintComponent(Graphics grf){
	        super.paintComponent(grf);
	        Graphics2D graph = (Graphics2D)grf;
	        
	        //Sets the value of a single preference for the rendering algorithms.
	        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        
	        // get width and height of plotting window
	        int width = getWidth();
	        int height = getHeight();
	        
	        // draw axes
	        graph.draw(new Line2D.Double(margin, margin, margin, height - margin));
	        graph.draw(new Line2D.Double(margin, height - margin, width - legendWidth - margin, height - margin));
	        
	        // difference between consecutive x-values
	        double xDiff = (double)(width - 2 * margin - legendWidth)/(yVals[0].length - 1);
	        
	        // scaling factor for y-values - proportion of total height which is within axes
	        double scale = (double)(height - 2 * margin) / getMax();
	        
	        for (int line = 0; line < this.yVals.length; line++) {
	        	graph.setPaint(this.colors[line]);
		        
		        double x1 = margin;
		        double x2 = margin + xDiff;
		        double y1 = height - margin - scale * this.yVals[line][0];
		        double y2;
		        
		        // plot line graph
		        for (int i = 0; i < yVals[line].length - 1; i++){
		        	y2 = height - margin - scale * this.yVals[line][i + 1];
		        	graph.draw(new Line2D.Double(x1, y1, x2, y2));
		        	// prepare valules for next iteration
		        	y1 = y2;
		        	x1 = x2;
		        	x2 += xDiff;
		        }
		    }
	    }
	        
	     
	    // create getMax() method to find maximum value
	    private double getMax(){
	        double max = -Double.MAX_VALUE;
	        for (int i = 0; i < yVals.length; i++){
	        	for (int j = 0; j < yVals[i].length; j++) {
	        		max = Math.max(max, yVals[i][j]);	        		
	        	}
	        }
	        return max;
	    }
	}
	
	public void plotResults() {
		//create an instance of JFrame class  
        JFrame frame = new JFrame();
        
        // set size, layout and location for frame
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        double[][] resultProportions = this.cumulativeProportionAllResults();
        Color[] colors = new Color[3];
        colors[0] = Color.RED;
        colors[1] = Color.BLUE;
        colors[2] = Color.MAGENTA;
        
        ResultsPlotter plotter = new ResultsPlotter(resultProportions, colors);
        
        char[] results = {'W', 'L', 'D'};
        
        for (int i = 0; i < 3; i++) {
        	String result = Character.toString(results[i]);
        	JLabel label = new JLabel(result);
	        label.setForeground(colors[i]);
	        label.setBounds(400, 100 - 20*i, 200, 200);
	        frame.add(label);
        }
        
        frame.add(new ResultsPlotter(resultProportions, colors));
        frame.setSize(400 + plotter.legendWidth, 400);
        frame.setLocation(200, 200);
        frame.setVisible(true);
	}
}
