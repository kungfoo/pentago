package ch.pentago.server.jobs;

import java.util.Collection;

import org.jdom.Document;

import ch.pentago.core.User;
import ch.pentago.server.ServerState;
/**
 * this class sends a chat message to all currently connected users.
 * it starts several SendMessageJobs to avoid blocking
 * @author kungfoo
 *
 */
public class ChatMessageToAllJob implements Runnable{
	private Document packet;
	public ChatMessageToAllJob(Document packet){
		this.packet = packet;
	}
	
	public void run() {
		Collection<User> users = ServerState.getUserList();
		for(User user : users){
			ServerState.submitToPool(new SendMessageJob(packet,user));
		}
		return;
	}
}
