package ch.pentago.network;

import org.jdom.Document;
import org.jdom.Element;

public class ReplyPacket {
	private ReplyPacket(){
		
	}
	
	public static Document getDocument(){
		Document result = new Document();
		result.setRootElement(new Element("packet"));
		Element content = new Element("reply");
		result.getRootElement().addContent(content);
		return result;
	}
}
