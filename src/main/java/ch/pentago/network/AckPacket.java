package ch.pentago.network;

import org.jdom.Document;
import org.jdom.Element;

public class AckPacket {
	private AckPacket() {
	}
	public static Document getDocument(){
		Document result = new Document();
		result.setRootElement(new Element("packet"));
		Element content = new Element("reply");
		content.setAttribute("type", "ACK");
		result.getRootElement().addContent(content);
		return result;
	}
}
