package ch.pentago.client;

import java.util.HashMap;

import ch.pentago.core.ObservableBoolean;
import ch.pentago.core.User;
import ch.pentago.network.SocketMessagePublisherJob;
import ch.pentago.ui.GameWindow;

/**
 * Holds global data used in client environment
 * @author guetux
 *
 */
public class ClientState {
	
	/**
	 * The User who is currently logged in
	 */
	public static User currentUser;
	
	/**
	 * Boolean to restrict maximum concurrent games
	 */
	public static ObservableBoolean inGame = new ObservableBoolean(false);
	
	/**
	 * The window of the current game
	 */
	public static GameWindow currentGame;
	
	/** 
	 * The Publisher of the current network connection 
	 */
	private static SocketMessagePublisherJob publisher;
	
	/**
	 * The list of user currently logged in onto server
	 */
	private static HashMap<String, User> userlist = new HashMap<String, User>();
	
	/**
	 * Cleare the user list
	 *
	 */
	public static void resetUserList(){
		userlist = new HashMap<String, User>();
	}
	
	/**
	 * Retrieve User by sessionid
	 * @param sessionid
	 * @return
	 */
	public static User getUser(String sessionid){
		return userlist.get(sessionid);
	}
	
	/**
	 * Retrieve username by sessionid
	 * @param sessionid
	 * @return
	 */
	public static String getUserName(String sessionid) {
		return !(getUser(sessionid) == null) ? getUser(sessionid).getUserName(): null;
	}
	
	/**
	 * Add user to list
	 * @param user
	 */
	public static void addUser(User user){
		userlist.put(user.getSessionId(), user);
	}

	/**
	 * Getter method
	 */
	public static SocketMessagePublisherJob getPublisher() {
		return publisher;
	}

	/**
	 * Setter method
	 */
	public static void setPublisher(SocketMessagePublisherJob publisher) {
		ClientState.publisher = publisher;
	}
}
