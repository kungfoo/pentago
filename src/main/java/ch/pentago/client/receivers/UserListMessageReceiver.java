package ch.pentago.client.receivers;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.client.ClientState;
import ch.pentago.core.User;
import ch.pentago.network.Message;
import ch.pentago.network.MessageReceiver;
import ch.pentago.ui.UserListModel;

/**
 * Receives userlist messages and update local user database
 * @author guetux
 *
 */
public class UserListMessageReceiver implements MessageReceiver {
	
	/**
	* The userlist to update
	*/
	private UserListModel userlist;
	
	public UserListMessageReceiver(UserListModel users) {
		this.userlist = users;
	}
	
	@SuppressWarnings("unchecked")
	public void receive(Message message) {
		Document doc = message.getPacket();
		List<Element> users = doc.getRootElement().getChild("userlist")
				.getChildren();
		
		userlist.clear();
		ClientState.resetUserList();
		
		for (Element element : users) {
			User user = new User(element.getAttributeValue("username"),
					element.getAttributeValue("sessionid"));
			userlist.add(user);
			ClientState.addUser(user);
		}
	}

}
