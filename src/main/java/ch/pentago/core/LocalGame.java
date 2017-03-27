package ch.pentago.core;

import ch.pentago.client.ClientState;
import ch.pentago.client.GameMessageSender;

public class LocalGame {

	/**
	 * The Squares hold the game state
	 */
	private Square[] squares = new Square[5];

	/**
	 * The winner of the game
	 */
	private String winner;

	/**
	 * Next Turn. After initialized, no User has turn. needs to be set by setTurn()
	 */
	public static final int LOCAL = 1;
	public static final int REMOTE = 2;
	volatile private int turn = 0;
	
	
	/**
	 * Next action. Always alternating.
	 */
	public static final int PLACING = 1;
	public static final int ROTATING = 2;
	public static final int FINISHED = 3;
	volatile private int action = PLACING;

	public LocalGame(User opponent) {
		for (int i = 1; i < squares.length; i++) {
			squares[i] = new Square();
		}
		ClientState.inGame.set(true);
	}

	public Square[] getSquares() { return squares; }
	
	public int getTurn() { return turn; }
	
	public int getAction() { return action; }
	
	public boolean isFinished() { return turn == FINISHED; }
	
	public String getWinner() { return winner; }
	
	public void finish(String winner) { 
		turn = FINISHED; 
		this.winner = winner;
	}

	public void setTurn(String sessionid) {
		if (turn == 0) {
			if (ClientState.currentUser.getSessionId().equals(sessionid)) {
				turn = LOCAL;
			} else {
				turn = REMOTE;
			}
			
			//Oooooooh, chinese!
			squares[1].placeMarble(0, 0, 0);
		}
	}

	/**
	 * This method gets called by user when rotating a square
	 */
	public void doRotation(int square, int direction) {
		if (turn == LOCAL && action == ROTATING) {
			turn = REMOTE;
			GameMessageSender.sendRotation(square, direction);
			rotateSquare(square, direction);
			
		}
	}

	/**
	 * This Method is called by user to place a marble
	 */
	public void doPlacement(int square, int x, int y) {
		if (turn == LOCAL && action == PLACING) {
			GameMessageSender.sendPlacement(square, x, y);
			placeMarble(square, x, y);
		}
	}

	/**
	 * When a rotation message is received, this method gets called
	 */
	public void receiveRotation(int square, int direction) {
		if (turn == REMOTE && action == ROTATING) {
			turn = LOCAL;
			rotateSquare(square, direction);
		}
	}

	/**
	 * When a placment message is received, this method gets called
	 */
	public void receivePlacement(int square, int x, int y) {
		if (turn == REMOTE && action == PLACING) {
			placeMarble(square, x, y);
		}
	}

	public void rotateSquare(int square, int direction) {
		assert (1 <= square && square <= 4) : "Square index out of range";
		assert (direction == 1 || direction == -1) : "Direction must be 1 or -1";
		
		action = PLACING;
		
		if (direction == 1) {
			squares[square].rotateRight();
		} else {
			squares[square].rotateLeft();
		}
		
		
	}

	public void placeMarble(int square, int x, int y) {
		assert (1 <= square && square <= 4) : "Square index out of range";
		assert (0 <= x && x <= 3) : "Field index out of range";
		assert (0 <= y && y <= 3) : "Field index out of range";

		action = ROTATING;
		
		squares[square].placeMarble(x, y, turn);
	}

}
