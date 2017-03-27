package ch.pentago.server.jobs;

import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import ch.pentago.core.User;
import ch.pentago.server.ServerState;
/**
 * this class will send a message who's in turn to the two players upon run()
 * @author kungfoo
 *
 */
public class SendInTurnJob implements Runnable{
	private User inTurn;
	private User notInTurn;
	
	public SendInTurnJob(User inTurn, User notInTurn){
		this.inTurn = inTurn;
		this.notInTurn = notInTurn;
	}
	
	public void run() {
		Document packet = new Document(new Element("packet"));
		Element game = new Element("game");
		game.setAttribute("type", "turn");
		Element player = new Element("player");
		player.setAttribute("sessionid", inTurn.getSessionId());
		packet.getRootElement().addContent(game);
		game.addContent(player);
		
		
		try {
			new XMLOutputter().output(packet, System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ServerState.submitToPool(new SendMessageJob(packet,inTurn));
		ServerState.submitToPool(new SendMessageJob(packet,notInTurn));
		return;
	}
}
