package ch.pentago.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.core.User;
import ch.pentago.cram.Cram;
import ch.pentago.network.NetworkException;
import ch.pentago.network.PacketFactory;
import ch.pentago.network.XMLStream;
import ch.pentago.ui.LoginPanel;
import ch.pentago.xml.UserConfigManager;

/**
 * Provide the necessary logic to login to a cram server
 * @author guetux
 *
 */
public class AuthenticationHandler {
	
	/**
	 * SystemMessages will be placed relative to this panel is not null
	 */
	private LoginPanel panel;
	
	/**
	 * Stores the result of the authenticatin process for later retrievement
	 */
	public boolean result = false;
	
	/**
	 * Creates new AuthenticationHandler.
	 * @param p Messages get placed relative to this panel. Can be null
	 */
	public AuthenticationHandler(LoginPanel p) {
		panel = p;
	}

	/**
	 * Try to authenticate the given user return result
	 * @param username The username to authenticate
	 * @param password The password in plaintext
	 * @return
	 */
	public boolean authorizeUser(String username, String password) {
		try {
			Document packet;
			Socket socket;
			XMLStream stream;
			
			String sessionid;

			socket = new Socket(InetAddress.getByName(UserConfigManager
					.getUserConfigItem("server", "host")), Integer
					.parseInt(UserConfigManager.getUserConfigItem("server",
							"port")));
			stream = new XMLStream(socket);

			// send credentials
			packet = PacketFactory.createPacket(PacketFactory.REQUEST);
			Element request = packet.getRootElement().getChild("request");
			request.setAttribute("type", "challenge");
			request.setAttribute("user", username);
			stream.sendPacket(packet);

			packet = stream.getPacket();
			Element reply = packet.getRootElement().getChild("reply");
			String challenge = reply.getAttributeValue("value");
			String response = Cram.getSignature(challenge, password);
			packet = PacketFactory.createPacket(PacketFactory.REPLY);
			reply = packet.getRootElement().getChild("reply");
			reply.setAttribute("type", "response");
			reply.setAttribute("value", response);
			stream.sendPacket(packet);
			// get ack or nak
			packet = stream.getPacket();
			reply = packet.getRootElement().getChild("reply");
			result = reply.getAttributeValue("type").equals("ACK");
			if (result) {
				sessionid = reply.getAttributeValue("sessionid");
				ClientState.currentUser = new User(stream, username);
				ClientState.currentUser.setSessionId(sessionid);
			} else {
				JOptionPane.showMessageDialog(panel,"User or password is wrong.","Authentication Error",JOptionPane.ERROR_MESSAGE);
			}
		} catch (NetworkException e) {
			JOptionPane.showMessageDialog(panel,"Network stack error occured!","Network Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(panel,"Server not found!","Network Error",JOptionPane.ERROR_MESSAGE);
		}
		
		return result;
	}

}
