package ch.pentago.server;


import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ch.pentago.core.User;
import ch.pentago.server.jobs.AuthorizationJob;
import ch.pentago.server.jobs.AuthorizationResult;
import ch.pentago.server.jobs.UserListUpdateJob;

/**
 * the current state of the server.
 * 
 * @author kungfoo
 *
 */
public final class ServerState {
	private static HashMap<String, User> connectedUsers = new HashMap<String, User>();
	// use a queued thread pool to ensure FIFO for messages
	private static ExecutorService workerPool = Executors.newCachedThreadPool();
	public ServerState(){
		
	}
	
	/**k
	 * submit a Runnable to the Servers ThreadPool
	 * @param r the Runnable to execute
	 */
	public static void submitToPool(Runnable r){
		workerPool.submit(r);
	}
	
	/**
	 * submit a Callable to the thread pool
	 * @param job the Runnable to execute
	 * @return a Futute<AuthorizationResult>
	 */
	public static Future<AuthorizationResult> submitToPool(AuthorizationJob job){
		return workerPool.submit(job);
	}
	
	/**
	 * returns a collection of the currently connected users on the server
	 * @return all the connected users
	 */
	public static Collection<User> getUserList(){
		return connectedUsers.values();
	}
	/**
	 * add a new user to the currently connected users
	 * @param user the user to add
	 */
	public static void addUser(User user){
		connectedUsers.put(user.getSessionId(), user);
	}
	
	/**
	 * returns the user with the given sessionid
	 * @param sessionid
	 * @return the User
	 */
	public static User getUser(String sessionid){
		return connectedUsers.get(sessionid);
	}
	/**
	 * remove a user from the connected user pool
	 * @param sessionid
	 */
	public static void removeUser(String sessionid){
		// unsubscribe user from all message types
		getUser(sessionid).getPublisher().unsubscribeAll();
		
		connectedUsers.remove(sessionid);
		workerPool.submit(new UserListUpdateJob());
	}
	/**
	 * return the users nickname
	 * @param sessionid
	 * @return the users nickname
	 */
	public static String resolveUserName(String sessionid){
		try{
			return connectedUsers.get(sessionid).getUserName();
		}
		catch(NullPointerException e){
			return null;
		}
	}
}
