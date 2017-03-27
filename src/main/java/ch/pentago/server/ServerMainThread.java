package ch.pentago.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ch.pentago.network.NetworkException;
import ch.pentago.network.XMLStream;
import ch.pentago.server.jobs.AuthorizationJob;
import ch.pentago.server.jobs.AuthorizationResult;
import ch.pentago.server.jobs.UserListUpdateJob;
import ch.pentago.server.receivers.ChatMessageReceiver;
import ch.pentago.server.receivers.RequestMessageReceiver;
import ch.pentago.xml.ServerConfigManager;

/**
 * the servers main thread. This class will accept() connections from the socket and start all
 * necessary jobs
 * @author kungfoo
 *
 */
public class ServerMainThread implements Runnable{
	private ServerSocket serverSocket;
	private ChatMessageReceiver chatMessageReceiver;
	private RequestMessageReceiver requestMessageReceiver;
	
	public static void main(String[] args) {
		System.out.println("Starting Main Thread...");
		Thread thread = new Thread(new ServerMainThread());
		thread.start();
	}
	
	
	public ServerMainThread(){
		try{
			serverSocket = new ServerSocket(ServerConfigManager.getServerPort());
			chatMessageReceiver = new ChatMessageReceiver();
			requestMessageReceiver = new RequestMessageReceiver();
		}
		catch(IOException e){
			System.out.println("server socket could not be created");
			e.printStackTrace();
		}
	}
	
	/**
	 * main server loop
	 * accepts from the sockket
	 */
	public void run() {
		for(;;){
			try {
				Socket clientSocket = serverSocket.accept();
				XMLStream stream = new XMLStream(clientSocket);
				Future<AuthorizationResult> future = ServerState.submitToPool(new AuthorizationJob(stream));
				try{
					AuthorizationResult result = future.get(3, TimeUnit.SECONDS);
					if(result.isAuthorized()){
						
						/*
						 * we want to receive all the chat messages, so we subscribe to this "topic"
						 */
						result.getMsgPublisher().subscribe(chatMessageReceiver, "chat");
						result.getMsgPublisher().subscribe(requestMessageReceiver, "request");
						
						ServerState.submitToPool(result.getMsgPublisher());
						// update userlist an all clients
						UserListUpdateJob userlistUpdater = new UserListUpdateJob();
						ServerState.submitToPool(userlistUpdater);
					}
					else{
						// client did not authorize correctly or use correct sequence
						System.out.println("no hit");
						clientSocket.close();
					}
				}
				catch(ExecutionException e){
					System.out.println(this.getClass() +".run(): caught executionException");
				}
				catch(InterruptedException e){
					System.out.println(this.getClass() +".run(): got interrupted");
				}
				catch(TimeoutException e){
					System.out.println(this.getClass() +".run(): time-out hit, cancelling task: " + future.cancel(true));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NetworkException e) {
				System.out.println("could not create client/server xml stream");
				e.printStackTrace();
			}
		}
	}
}
