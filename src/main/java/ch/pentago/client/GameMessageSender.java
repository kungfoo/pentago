package ch.pentago.client;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.core.User;
import ch.pentago.network.NetworkException;
import ch.pentago.network.PacketFactory;

/**
 * The contains the methods to send Game Messages
 * @author guetux
 *
 */
public class GameMessageSender {
	
	private static User me = ClientState.currentUser;
	
	/**
	 * Sends a Placement Message
	 * @param square The square numbered by top left square to bottom right square starting by 1
	 * @param x X Coordinate of field
	 * @param y Y Coordinate of field
	 */
	public static void sendPlacement(int square, int x, int y) {
		try {
			// Create XML Document
			Document packet = new Document(new Element("packet"));

			// Add game element and set type and sessionid
			Element game = new Element("game");
			game.setAttribute("sessionid", me.getSessionId());
			game.setAttribute("type", "placement");

			// Add square number element to game
			Element squarenumber = new Element("square");
			squarenumber.setAttribute("number", "" + square);
			game.addContent(squarenumber);

			// Add marble (field) element to game
			Element marble = new Element("marble");
			marble.setAttribute("x", "" + x);
			marble.setAttribute("y", "" + y);
			game.addContent(marble);

			// Add game element and send packet
			packet.getRootElement().addContent(game);
			ClientState.currentUser.getStream().sendPacket(packet);
			
			

		} catch (NetworkException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a Rotation Message
	 * @param square The square numbered by top left square to bottom right square starting by 1
	 * @param direction The direction in which square is turned: 1 = Right; -1 = Left
	 */
	public static void sendRotation(int square, int direction) {
		try {
			Document packet = new Document(new Element("packet"));
			Element game = new Element("game");
			game.setAttribute("sessionid", me.getSessionId());
			game.setAttribute("type", "rotation");
			Element rotation = new Element("square");
			rotation.setAttribute("number", "" + square);
			rotation.setAttribute("direction", "" + direction);
			game.addContent(rotation);
			packet.getRootElement().addContent(game);
			ClientState.currentUser.getStream().sendPacket(packet);
		} catch (NetworkException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a Chat Message to specific receiver
	 * @param sessionid The receiver of the Message
	 * @param message The message itself
	 */
	public static void sendChatMessage(String sessionid, String message) {
		try {
			Document packet = PacketFactory.createPacket(PacketFactory.CHAT);
			Element chat = packet.getRootElement().getChild("chat");
			chat.setAttribute("destination", sessionid);
			chat.setAttribute("source", ClientState.currentUser.getSessionId());
			chat.setText(message);
			ClientState.currentUser.getStream().sendPacket(packet);
		} catch (NetworkException e) {
			e.printStackTrace();
		}
	}

}
