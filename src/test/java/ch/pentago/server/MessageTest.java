package ch.pentago.server;

import org.jdom.Document;
import org.jdom.Element;

import ch.pentago.network.Message;

import junit.framework.TestCase;

/**
 * Testclass for the Class Message
 * @author sidi
 *
 */
public class MessageTest extends TestCase {
	
	private Message message;
	private Document document;
	private Element element;
	
	public void testA() {
		assertTrue(true);
	}

	public void testGetPacket() {
		assertEquals(message.getPacket(), document);
	}

	public void testGetType() {
		assertEquals(message.getType(), "habba1");
	}

	public void testIsChatMessageToAll() {
		assertFalse(message.isChatMessageToAll());
	}

	
	@Override
	protected void setUp() throws Exception {
		element = new Element("habba1");
		document = new Document(new Element("habba2"));
		document.getRootElement().addContent(element);
		message = new Message(document);
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
