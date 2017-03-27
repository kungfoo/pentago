package ch.pentago.network;


/**
 * interface to receive messsages from the SocketMessagePublisherJob class
 * @author kungfoo
 *
 */
public interface MessageReceiver {
	public void receive(Message message);
}
