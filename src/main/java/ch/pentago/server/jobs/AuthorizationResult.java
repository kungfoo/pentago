package ch.pentago.server.jobs;

import ch.pentago.network.SocketMessagePublisherJob;


/**
 * this class encloses an authorization result. The SocketMessagePublisherJob return value is needed
 * to set up communication with the client.
 * @author kungfoo
 *
 */
public class AuthorizationResult {
	private boolean bool;
	private SocketMessagePublisherJob msgPublisher;
	
	private AuthorizationResult(){	
	}
	
	public AuthorizationResult(boolean bool,SocketMessagePublisherJob job){
		assert(bool == true && job != null)||(bool == false && job == null):"wrong comination of result params, use either (true,not null) or (false,null)";
		this.bool = bool;
		this.msgPublisher = job;
	}
	
	public SocketMessagePublisherJob getMsgPublisher() {
		return msgPublisher;
	}
	
	public boolean isAuthorized() {
		return bool;
	}
}
