package ch.pentago.client.receivers;

import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.client.ClientState;
import ch.pentago.core.ChatMessage;
import ch.pentago.core.User;
import ch.pentago.network.Message;
import ch.pentago.network.MessageReceiver;

/**
 * Receives chat messages and adds them to chat list
 * @author guetux
 *
 */
public class ChatMessageReceiver implements MessageReceiver {

	/**
	* The List to append chat messages
	*/
	JList chat_messages;

	public ChatMessageReceiver(JList chat_messages) {
		this.chat_messages = chat_messages;

	}

	@SuppressWarnings("unchecked")
	public void receive(Message message) {
		final DefaultListModel model = (DefaultListModel) chat_messages.getModel();
		if (model != null) {
			if (message.isChatMessageToAll()) {
				Document doc = message.getPacket();
				Element chat = doc.getRootElement().getChild("chat");
				User user = ClientState.getUser(chat.getAttributeValue("source"));
				ChatMessage chatmsg = new ChatMessage(user.getSessionId(), new Date(), chat.getText());
				model.addElement(chatmsg);

				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						chat_messages.ensureIndexIsVisible(model.getSize() - 1);
					}
				});
			}
		}
	}

}
