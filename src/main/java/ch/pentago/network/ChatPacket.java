package ch.pentago.network;

import org.jdom.Document;
import org.jdom.Element;

public class ChatPacket {
	private ChatPacket(){
		
	}
	
	public static Document getDocument(){
		Document result = new Document();
		result.setRootElement(new Element("packet"));
		Element content = new Element("chat");
		result.getRootElement().addContent(content);
		return result;
	}
}
