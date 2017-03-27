package ch.pentago.core;

import java.util.Random;

import ch.pentago.network.SocketMessagePublisherJob;
import ch.pentago.network.XMLStream;

public class User {
	private String sessionId;
	private XMLStream stream;
	private String username;
	private SocketMessagePublisherJob publisher;
	
	public User(String username, String sessionid){
		assert (username != null && sessionid != null): "no null values please";
		this.username = username;
		this.sessionId = sessionid;
	}
	public User(XMLStream stream, String username){
		assert (username != null && stream != null): "stream cannot be null";
		this.stream = stream;
		this.username = username;
		sessionId = generateSessionId();
	}
	
	public String getUserName(){
		return username;
	}
	
	protected User(String username){
		this.username = username;
	}
	
	public String getSessionId(){
		return sessionId;
	}
	
	public XMLStream getStream(){
		return stream;
	}
	
	
	public void setPublisher(SocketMessagePublisherJob publisher) {
		this.publisher = publisher;
	}
	
	public SocketMessagePublisherJob getPublisher() {
		return publisher;
	}
	
	private String generateSessionId(){
		String result = "";
		Random rand = new Random(System.currentTimeMillis());
		for(int i = 0; i < 8; i++){
			result += (char)((rand.nextInt(101)%25)+97);
			result += (int)rand.nextInt(10);
		}
		return result;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
