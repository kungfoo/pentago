package ch.pentago.client;

import java.util.List;

import org.jdom.Element;

import ch.pentago.core.LocalGame;
import ch.pentago.core.User;

/**
 * Does the blinking of marbles when a user wins the game 
 * @author guetux
 *
 */
public class WinningMarbleVisualizer implements Runnable {
	
	/**
	 * The game that has been played
	 */
	private LocalGame game;
	
	/**
	 * The win-message that contains the field to blink
	 */
	private Element winMessage;
	
	private int winner;
	
	/**
	 * Constructor
	 * @param game
	 * @param winMessage
	 */
	public WinningMarbleVisualizer(LocalGame game, Element winMessage) {
		this.game = game;
		this.winMessage = winMessage;
		String sessionid = winMessage.getAttributeValue("sessionid");
		User me = ClientState.currentUser;
		winner = (me.getSessionId().equals(sessionid))? 1 : 2;
	}

	/**
	 * Start the Runnable
	 */
	public void run() {
		for(int blinky = 15; blinky >= 0; blinky--){
			setFields(0);
			try { Thread.sleep(500); } 
			catch (InterruptedException e) {e.printStackTrace();}
			setFields(winner);
			try { Thread.sleep(500); } 
			catch (InterruptedException e) {e.printStackTrace();}
		}
		return;
	}

	/**
	 * Sets all fields in gameMessage to to player
	 * @param player
	 */
	@SuppressWarnings("unchecked")
	private void setFields(int player) {
		List<Element> squares = winMessage.getChild("board").getChildren("square");
		for(Element square : squares) {
			int squareNumber = Integer.parseInt(square.getAttributeValue("number"));
			List<Element> marbles = square.getChildren("marble");
			for(Element marble : marbles) {
				int x = Integer.parseInt(marble.getAttributeValue("x"));
				int y = Integer.parseInt(marble.getAttributeValue("y"));
				game.getSquares()[squareNumber].placeMarble(x, y, player);
			}
		}
	}

}
