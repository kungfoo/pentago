package ch.pentago.server.receivers;

import org.jdom.Element;


import ch.pentago.network.Message;
import ch.pentago.network.MessageReceiver;
import ch.pentago.server.ServerState;
import ch.pentago.server.jobs.SendMessageJob;

/**
 * this class receives all "request" messages on the server and performs according operations,
 * such as disconnecting users, sending out challenge messages....
 * @author kungfoo
 *
 */
public class RequestMessageReceiver implements MessageReceiver{
	public void receive(Message message) {
		try{
			
			if(message.getType().equals("request")){
				Element request = message.getPacket().getRootElement().getChild("request");
				String type = request.getAttributeValue("type");
				if(type.equals("disconnect")){
					String sessionid = request.getAttributeValue("sessionid");
					ServerState.removeUser(sessionid);
				}
				else if(type.equals("challenge")){
					String destination = request.getAttributeValue("destination");
					ServerState.submitToPool(new SendMessageJob(message.getPacket(),ServerState.getUser(destination)));
					ServerState.getUser(destination).getPublisher().subscribe(new ReplyMessageReceiver(), "reply");
				}
			}
		}
		catch(NullPointerException e){
			System.out.println("RequestMessageReceiver.receive()");
			e.printStackTrace();
		}
	}
}
