package noughts_and_crosses;

public class PlayerNumberException extends Exception {
	private static final long serialVersionUID = 1L;
	String errorMessage;
	
	public PlayerNumberException(String msg) {
		errorMessage = msg;
	}
	
	public String toString() {
		return "Number of players must be exactly two: " + this.errorMessage;
	}
}
