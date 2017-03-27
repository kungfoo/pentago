package ch.pentago.server.jobs;

import org.jdom.Document;

import ch.pentago.core.User;
import ch.pentago.network.NetworkException;
import ch.pentago.server.ServerState;

/**
 * send a message to user with sessionid on run()
 * @author kungfoo
 *
 */
public class SendMessageJob implements Runnable{
	private Document message;
	private User user;
	
	private SendMessageJob(){}
	public SendMessageJob(Document message, User user){
		assert(message != null && user != null):"user and message may not be null";
		this.message = message;
		this.user = user;
	}
	
	public void run() {
		try{
			user.getStream().sendPacket(message);
		
		}
		catch(NetworkException e){
			System.out.println(this.getClass()+".run(): could not send over stream, removing client");
			ServerState.removeUser(user.getSessionId());
			System.out.println("new clients:");
		}
		return;
	}
}
