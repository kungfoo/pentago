package ch.pentago.server.receivers;

import ch.pentago.core.User;
import ch.pentago.network.Message;
import ch.pentago.network.MessageReceiver;
import ch.pentago.server.ServerState;
import ch.pentago.server.jobs.ChatMessageToAllJob;
import ch.pentago.server.jobs.SendMessageJob;
/**
 * this class receives all the "chat" messages on the server
 * @author kungfoo
 *
 */
public class ChatMessageReceiver implements MessageReceiver{
public void receive(Message msg) {
		try{
			if(msg.getType().equals("chat")){
				if(msg.isChatMessageToAll()){
					ChatMessageToAllJob job = new ChatMessageToAllJob(msg.getPacket());
					ServerState.submitToPool(job);
				}
				else{
					String session1 = msg.getPacket().getRootElement().getChild("chat").getAttributeValue("destination");
					String session2 = msg.getPacket().getRootElement().getChild("chat").getAttributeValue("source");
					
					User user1 = ServerState.getUser(session1);
					User user2 = ServerState.getUser(session2);
					ServerState.submitToPool(new SendMessageJob(msg.getPacket(),user1));
					ServerState.submitToPool(new SendMessageJob(msg.getPacket(),user2));
				}
			}
		}
		catch(NullPointerException e){
			System.out.println(getClass()+".receive(): user null");
		}
	}
}
