package ch.pentago.server.receivers;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.core.Game;
import ch.pentago.core.Placement;
import ch.pentago.core.Rotation;
import ch.pentago.network.Message;
import ch.pentago.network.MessageReceiver;
import ch.pentago.server.ServerState;
import ch.pentago.server.jobs.SendMessageJob;

/**
 * class receiveing all the "game" messages from two player connected to the server
 * @author kungfoo
 *
 */
public class GameMessageReceiver implements MessageReceiver{
	private Game game;
	
	public GameMessageReceiver(Game game){
		this.game = game;
	}
	
	public void receive(Message message) {
		Element gamemsg = message.getPacket().getRootElement().getChild("game");
		// see if the player being in turn sent the message
		String sessionid = gamemsg.getAttributeValue("sessionid");
		if(sessionid.equals(game.getInTurn().getSessionId())){
			if(gamemsg.getAttributeValue("type").equals("placement") && game.getState() == Game.PLACING){
				Element marble = gamemsg.getChild("marble");
				Element square = gamemsg.getChild("square");
				int x = Integer.parseInt(marble.getAttributeValue("x"));
				int y = Integer.parseInt(marble.getAttributeValue("y"));
				Placement placement = new Placement(game.getSquare(Integer.parseInt(square.getAttributeValue("number"))),x,y);
				placement.place(game.resolveColor(sessionid));
				System.out.println("player ["+ game.getInTurn().getUserName() +"] placed a marble at ["+x+","+y+"]");
				// send update and ack messages
				sendAckUpdate(message);
				
				// now do a state transition on the game
				game.doStateTransition();
			}
			else if(gamemsg.getAttributeValue("type").equals("rotation") && game.getState() == Game.ROTATING){
				// rotate square and set turn to other player, go to placement state
				Element square = gamemsg.getChild("square");
				Rotation rotation = new Rotation(game.getSquare(Integer.parseInt(square.getAttributeValue("number"))),
						Integer.parseInt(square.getAttributeValue("direction")));
				rotation.rotate();
				System.out.println("player ["+game.getInTurn()+"] rotated square ["+Integer.parseInt(square.getAttributeValue("number"))+"], direction: ["+Integer.parseInt(square.getAttributeValue("direction"))+"]");
				sendAckUpdate(message);
				// now do state transition on game
				game.doStateTransition();
			}
			else{
				System.out.println(this.getClass()+".receive(): player ["
					+ServerState.resolveUserName(gamemsg.getAttributeValue("sessionid"))
					+"] conflicted with the rules");
			}
		}
		else{
			System.out.println(this.getClass()+".receive(): player ["
					+ServerState.resolveUserName(gamemsg.getAttributeValue("sessionid"))
					+"] sent message, even though not in turn");
		}
	}
	
	private void sendAckUpdate(Message message){
		Document packet = new Document(new Element("packet"));
		Element gameElement = new Element("game");
		gameElement.setAttribute("type", "ack");
		Element reply = new Element("reply");
		reply.setAttribute("ack", "accept");
		gameElement.addContent(reply);
		packet.getRootElement().addContent(gameElement);
		ServerState.submitToPool(new SendMessageJob(packet,game.getInTurn()));
		
		packet = message.getPacket();
		ServerState.submitToPool(new SendMessageJob(packet,game.getNotInTurn()));
	}
}
