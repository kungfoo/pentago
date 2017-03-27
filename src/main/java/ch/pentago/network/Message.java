package ch.pentago.network;

import org.jdom.Document;
import org.jdom.Element;

/**
 * a message received by a socketMessagePublisher and the broadcast to all its subscribers
 * @author kungfoo
 *
 */
public class Message {
	protected String type = null;
	protected Document packet;
	
	
	public Document getPacket(){
		return packet;
	}
	
	public String getType(){
		return type;
	}
	
	public boolean isChatMessageToAll(){
		return type.equals("chat")&&packet.getRootElement().getChild("chat").getAttributeValue("destination").equals("all");
	}
	
	public Message(Document packet){
		this.packet = packet;
		Element root = packet.getRootElement();
		Element t = (Element)root.getChildren().get(0);
		type = t.getQualifiedName();
	}
}
