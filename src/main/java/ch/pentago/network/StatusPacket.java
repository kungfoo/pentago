package ch.pentago.network;

import org.jdom.Document;
import org.jdom.Element;

public class StatusPacket {
	private StatusPacket(){
		
	}
	
	public static Document getDocument(){
		Document result = new Document();
		result.setRootElement(new Element("packet"));
		Element content = new Element("status");
		result.getRootElement().addContent(content);
		return result;
	}
}
