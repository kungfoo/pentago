package ch.pentago.server.receivers;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.core.Game;
import ch.pentago.network.Message;
import ch.pentago.network.MessageReceiver;
import ch.pentago.server.ServerState;
import ch.pentago.server.jobs.SendMessageJob;

public class ReplyMessageReceiver implements MessageReceiver{
	public void receive(Message message) {
		Document packet = message.getPacket();
		Element reply = packet.getRootElement().getChild("reply");
		String source = reply.getAttributeValue("source");
		if(reply.getAttributeValue("ack").equals("accept")){
			String destination = reply.getAttributeValue("destination");
			
			
			ServerState.submitToPool(new SendMessageJob(packet,ServerState.getUser(destination)));
			
			new Game(ServerState.getUser(source),ServerState.getUser(destination));
		}
		else if(reply.getAttributeValue("ack").equals("deny")){
			String destination = reply.getAttributeValue("destination");
			ServerState.submitToPool(new SendMessageJob(packet,ServerState.getUser(destination)));
		}
		// unsubscribe from the publisher
		ServerState.getUser(source).getPublisher().unsubscribe(this, "reply");
	}
}
