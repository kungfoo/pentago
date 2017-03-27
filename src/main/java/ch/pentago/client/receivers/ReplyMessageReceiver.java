package ch.pentago.client.receivers;

import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.client.ClientState;
import ch.pentago.core.User;
import ch.pentago.network.Message;
import ch.pentago.network.MessageReceiver;
import ch.pentago.ui.GameWindow;

/**
 * Receives challange replys from challanged user
 * @author guetux
 *
 */
public class ReplyMessageReceiver implements MessageReceiver {
	
	public void receive(Message message) {
		Document packet = message.getPacket();
		Element reply = packet.getRootElement().getChild("reply");
		ClientState.inGame.set(false);
		if (reply.getAttributeValue("ack").equals("deny")) {
			String user = ClientState.getUserName(reply.getAttributeValue("source"));
			JOptionPane.showMessageDialog(null, "User " + user + " denied your challenge!", "Challenge denied", JOptionPane.WARNING_MESSAGE );
		} else if (reply.getAttributeValue("ack").equals("accept")) {
			System.out.println("Got accpet reply, starting new game");
			User opponent = ClientState.getUser(reply.getAttributeValue("source"));
			ClientState.currentGame = new GameWindow(opponent);
			GameMessageReceiver gmr = new GameMessageReceiver(ClientState.currentGame.getGame());
			ClientState.currentUser.getPublisher().subscribe(gmr, "game");
			ClientState.currentGame.setVisible(true);
		}
	}

}
