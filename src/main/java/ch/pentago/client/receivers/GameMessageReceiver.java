package ch.pentago.client.receivers;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.client.WinningMarbleVisualizer;
import ch.pentago.core.LocalGame;
import ch.pentago.network.Message;
import ch.pentago.network.MessageReceiver;


/**
 * Receives game messages and forwards them to LocalGame
 * @author guetux
 *
 */
public class GameMessageReceiver implements MessageReceiver {
	
	/**
	* The local game to forward messages
	*/
	private LocalGame game;
	
	public GameMessageReceiver(LocalGame game) {
		this.game = game;
	}

	public void receive(Message message) {
		Document packet = message.getPacket();
		Element gameMessage = packet.getRootElement().getChild("game");
		if(gameMessage.getAttributeValue("type").equals("placement")) {
			
			parsePlacement(gameMessage);
			
		} else if (gameMessage.getAttributeValue("type").equals("rotation")) {
			
			parseRotation(gameMessage);
			
		} else if(gameMessage.getAttributeValue("type").equals("turn")) {
			
			parseTurn(gameMessage);
			
		} else if(gameMessage.getAttributeValue("type").equals("win")) {
			
			String winner = gameMessage.getAttributeValue("sessionid");
			game.finish(winner);
			new Thread(new WinningMarbleVisualizer(game, gameMessage)).start();
			
		} else if(gameMessage.getAttributeValue("type").equals("draw")) {
			
			game.finish("draw");
			
			// Replace marble to force repaint.
			int field = game.getSquares()[1].getField(0, 0);
			game.getSquares()[1].placeMarble(0, 0, field);
			
		}
	}

	private void parseTurn(Element gameMessage) {
		Element player = gameMessage.getChild("player");
		String sessionid = player.getAttributeValue("sessionid");
		game.setTurn(sessionid);
	}

	private void parseRotation(Element gameMessage) {
		Element square = gameMessage.getChild("square");
		int squareNumber = Integer.parseInt(square.getAttributeValue("number"));
		int direction = Integer.parseInt(square.getAttributeValue("direction"));
		game.receiveRotation(squareNumber, direction);
	}

	private void parsePlacement(Element gameMessage) {
		Element square = gameMessage.getChild("square");
		Element marble = gameMessage.getChild("marble");
		int squareNumber = Integer.parseInt(square.getAttributeValue("number"));
		int x = Integer.parseInt(marble.getAttributeValue("x"));
		int y = Integer.parseInt(marble.getAttributeValue("y"));
		game.receivePlacement(squareNumber, x, y);
	}

}
