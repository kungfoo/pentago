package ch.pentago.network;

import org.jdom.Document;
import org.jdom.Element;

public class GamePacket {
	private GamePacket(){
		
	}
	
	public static Document getDocument(){
		Document result = new Document();
		result.setRootElement(new Element("packet"));
		Element content = new Element("game");
		result.getRootElement().addContent(content);
		return result;
	}
}
