package ch.pentago.core;

import java.util.Date;

import ch.pentago.client.ClientState;

public class ChatMessage {
	
	private String 	sessionid;
	private Date 	timestamp;
	private String	message="";
	private String  username;
	
	public ChatMessage(String sessionid, Date timestamp, String message) {
		this.sessionid = sessionid;
		this.timestamp = timestamp;
		this.message   = message;
		this.username = ClientState.getUserName(this.sessionid);
	}
	
	public String getMessage() {
		return message;
	}
	public String getUsername() {
		return username;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setUser(String user) {
		this.sessionid = user;
	}

}
