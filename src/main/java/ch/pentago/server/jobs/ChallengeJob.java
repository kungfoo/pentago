package ch.pentago.server.jobs;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.core.User;
import ch.pentago.server.ServerState;

public class ChallengeJob implements Runnable{
	private User player1;
	private User player2;
	
	public ChallengeJob(String sessionid1, String sessionid2){
		assert(sessionid1 != "" && sessionid1 != null):"sessionid1 may not be empty or null";
		assert(sessionid2 != "" && sessionid2 != null):"sessionid2 may not be empty or null";
		player1 = ServerState.getUser(sessionid1);
		player2 = ServerState.getUser(sessionid2);
	}
	
	public void run() {
		Document packet = new Document(new Element("packet"));
		Element request = new Element("request");
		packet.getRootElement().addContent(request);
		request.setAttribute("type", "challenge");
		request.setAttribute("source", player1.getSessionId());
		request.setAttribute("destination", player2.getSessionId());
		
		ServerState.submitToPool(new SendMessageJob(packet,player2));
		return;
	}
}
