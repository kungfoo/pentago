package ch.pentago.client.receivers;

import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.client.ClientState;
import ch.pentago.core.User;
import ch.pentago.network.Message;
import ch.pentago.network.MessageReceiver;
import ch.pentago.network.NetworkException;
import ch.pentago.ui.GameWindow;

/**
 * Receives challange Message from other players. Starts game if Challange is accepted
 * @author guetux
 *
 */
public class ChallengeMessageReceiver implements MessageReceiver {
	
	public void receive(Message message) {
		Document packet = message.getPacket();
		Element request = packet.getRootElement().getChild("request");
		if (request.getAttributeValue("type").equals("challenge") && !ClientState.inGame.get()) {
			String source = request.getAttributeValue("source");
			String[] options = {"Accept", "Deny"};
			
			int answer = JOptionPane.showOptionDialog(	null, 
														"You have been challenged by " + ClientState.getUserName(source), 
														"Challenge",
														0, 
														JOptionPane.QUESTION_MESSAGE, 
														null, 
														options, 
														options[0]);
			if(answer == 0) {
				//Accept
				Element reply = sendAcceptChallenge(request);
				
				User opponent = ClientState.getUser(reply.getAttributeValue("destination"));
				ClientState.currentGame = new GameWindow(opponent);
				GameMessageReceiver gmr = new GameMessageReceiver(ClientState.currentGame.getGame());
				ClientState.currentUser.getPublisher().subscribe(gmr, "game");
				ClientState.currentGame.setVisible(true);
			} else {
				sendDenyChallenge(request);
			}
			
		} else {
			sendDenyChallenge(request);
		}
	}

	/** 
	 * Sends a deny message to challenger
	 * @param request
	 */
	private void sendDenyChallenge(Element request) {
		Document replypacket = new Document(new Element("packet"));
		Element reply = new Element("reply");
		reply.setAttribute("type", "challenge");
		reply.setAttribute("ack","deny");
		reply.setAttribute("source", ClientState.currentUser.getSessionId());
		reply.setAttribute("destination", request.getAttributeValue("source"));
		replypacket.getRootElement().addContent(reply);
		try {
			ClientState.currentUser.getStream().sendPacket(replypacket);
		} catch (NetworkException e) {
			e.printStackTrace();
		}
	}

	private Element sendAcceptChallenge(Element request) {
		Document replypacket = new Document(new Element("packet"));
		Element reply = new Element("reply");
		reply.setAttribute("type", "challenge");
		reply.setAttribute("ack","accept");
		reply.setAttribute("source", ClientState.currentUser.getSessionId());
		reply.setAttribute("destination", request.getAttributeValue("source"));
		replypacket.getRootElement().addContent(reply);
		try {
			ClientState.currentUser.getStream().sendPacket(replypacket);
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		return reply;
	}

}
