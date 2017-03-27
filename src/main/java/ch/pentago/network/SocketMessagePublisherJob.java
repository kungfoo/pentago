package ch.pentago.network;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jdom.Document;

import ch.pentago.core.User;
import ch.pentago.server.ServerMainThread;
import ch.pentago.server.ServerState;
/**
 * listens for messages on a socket
 * @author kungfoo
 *
 */
public class SocketMessagePublisherJob implements Runnable{
	// keeps track of all the subsribed listeners
	private Map<String, ConcurrentLinkedQueue<MessageReceiver>> listeners = new HashMap<String, ConcurrentLinkedQueue<MessageReceiver>>();
	private User user;
	public SocketMessagePublisherJob(User user){
		this.user = user;
		user.setPublisher(this);
	}
	
	public SocketMessagePublisherJob(User user, ServerMainThread server){
		this.user = user;
	}
	
	private void notifyListeners(Message message){
		try{
			String type = message.getType();
			// get all subscribed listeners
			ConcurrentLinkedQueue<MessageReceiver> subscribers = listeners.get(type);
			if(subscribers != null){
				for (MessageReceiver listener : subscribers) {
					listener.receive(message);
				}
			}
			else{
				System.out.println(this.getClass()+" unlistened message of type ["+type+"] received!");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * main loop
	 * will block until a message is received
	 */
	public void run() {
		while(true){
			try {
				Document packet = user.getStream().getPacket();
				Message message = new Message(packet);
				notifyListeners(message);
			} catch (NetworkException e){
				ServerState.removeUser(user.getSessionId());
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * subscribe a MessageReceiver to a message typ
	 * @param subscriber The Subscriber
	 * @param type String with the type of messages
	 */
	public void subscribe(MessageReceiver subscriber, String type){
		try{
			ConcurrentLinkedQueue<MessageReceiver> list = listeners.get(type);
			list.add(subscriber);
		}
		catch(NullPointerException e){
			ConcurrentLinkedQueue<MessageReceiver> list = new ConcurrentLinkedQueue<MessageReceiver>();
			list.add(subscriber);
			listeners.put(type, list);
		}
	}
	
	/**
	 * unsubscibe for a type of messages
	 * @param subscriber 
	 * @param type
	 */
	public void unsubscribe(MessageReceiver subscriber, String type){
		try{
			ConcurrentLinkedQueue<MessageReceiver> list = listeners.get(type);
			list.remove(subscriber);
		}
		catch(NullPointerException e){
			// nobody was subscribed
			System.out.println("nobody subscribed");
		}
	}

	public void unsubscribeAll(){
		// erase old list
		listeners = new HashMap<String, ConcurrentLinkedQueue<MessageReceiver>>();
	}
}
