package ch.pentago.server.jobs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.core.User;
import ch.pentago.cram.Cram;
import ch.pentago.database.DataBaseConnection;
import ch.pentago.database.DataBaseException;
import ch.pentago.network.NetworkException;
import ch.pentago.network.PacketFactory;
import ch.pentago.network.SocketMessagePublisherJob;
import ch.pentago.network.XMLStream;
import ch.pentago.server.ServerState;
/**
 * this class performs authorization of a user using his socket and the specified protocol
 * @author kungfoo
 *
 */
public class AuthorizationJob implements Callable<AuthorizationResult>{
	private XMLStream stream;
	
	public AuthorizationJob(XMLStream stream){
		assert(stream != null):"stream cannot be null";
		this.stream = stream;
	}
	public AuthorizationResult call() throws Exception {
		try{
			Document packet = stream.getPacket();
			Element root = packet.getRootElement();
			Element request = root.getChild("request");
			if(request.getAttributeValue("type").equals("challenge")){
				String username = request.getAttributeValue("user");
				// create and send challenge for this user
				String challenge = Cram.getChallenge();
				packet = PacketFactory.createPacket(PacketFactory.REPLY);
				Element answer = packet.getRootElement().getChild("reply");
				answer.setAttribute("type", "challenge");
				answer.setAttribute("value", challenge);
				stream.sendPacket(packet);
				String hashedPassword = "";
				try{
					String query = "select password from users where username='"+username+"'";
					ResultSet result = DataBaseConnection.executeQuery(query);
					result.first();
					hashedPassword = result.getString("password");
				}
				catch(DataBaseException e){
					e.printStackTrace();
				}
				catch(SQLException e){
					e.printStackTrace();
				}
				// get answer packet from stream
				String expected = Cram.getExpected(challenge, hashedPassword);
				packet = stream.getPacket();
				root = packet.getRootElement();
				answer = root.getChild("reply");
				if(answer.getAttributeValue("type").equals("response")){
					String response = answer.getAttributeValue("value");
					if(expected.equals(response)){
						User user = new User(stream,username);
						packet = PacketFactory.createPacket(PacketFactory.ACK);
						Element reply = packet.getRootElement().getChild("reply");
						reply.setAttribute("sessionid", user.getSessionId());
						stream.sendPacket(packet);
						ServerState.addUser(user);
						SocketMessagePublisherJob messagePublisherJob = new SocketMessagePublisherJob(user);
						return new AuthorizationResult(true,messagePublisherJob);
					}
					else{
						System.out.println("wrong signature");
						packet = PacketFactory.createPacket(PacketFactory.NAK);
						stream.sendPacket(packet);
						return new AuthorizationResult(false,null);
					}
				}
			}
		}
		catch(NetworkException e){
			System.out.println("something wrong with the network stack...");
			e.printStackTrace();
			return new AuthorizationResult(false,null);
		}
		catch(NullPointerException e){
			System.out.println("kicked user...");
			return new AuthorizationResult(false,null);
		}
		return new AuthorizationResult(false,null);
	}
}
