package ch.pentago.server.jobs;

import java.util.Collection;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.core.User;
import ch.pentago.server.ServerState;

/**
 * this class sends the complete userlist to all connected clients.
 * it will not block because of a single user blocking his stream.
 * @author kungfoo
 *
 */
public class UserListUpdateJob implements Runnable{
	
	public UserListUpdateJob(){
		
	}
	
	public void run() {
		Document packet = getDocument();
		Collection<User> userlist = ServerState.getUserList();
		for (User user : userlist) {
			ServerState.submitToPool(new SendMessageJob(packet,user));
		}
		return;
	}
	
	private Document getDocument(){
		Document packet = new Document(new Element("packet"));
		Element doc = new Element("userlist");
		Collection<User> userlist = ServerState.getUserList();
		for(User user:userlist){
			Element userel = new Element("user");
			userel.setAttribute("sessionid", user.getSessionId());
			userel.setAttribute("username", user.getUserName());
			doc.addContent(userel);
		}
		packet.getRootElement().addContent(doc);
		return packet;
	}
}
