package ch.pentago.core;

import java.util.Date;


import junit.framework.TestCase;

/**
 * Testclass for the class ChatMessage
 * @author sidi
 *
 */
public class ChatMessageTest extends TestCase {
	
	private ChatMessage msg;
	private Date datum = new Date();
	private Date datum2= new Date(000001);

	public void testGetMessage() {
		assertEquals(msg.getMessage(), "TestNachricht");
	}

	public void testGetUsername() {
		assertEquals(msg.getUsername(), null);
	}

	public void testSetMessage() {
		msg.setMessage("TestNachricht2");
		assertEquals(msg.getMessage(),"TestNachricht2");
	}

	public void testGetTimestamp() {
		assertEquals(msg.getTimestamp(), datum);
	}

	public void testSetTimestamp() {
		msg.setTimestamp(datum2);
		assertEquals(msg.getTimestamp(), datum2);
	}

	public void testGetSessionid() {
		assertEquals(msg.getSessionid(), "1234");
	}

	public void testSetUser() {
		msg.setUser("asdf");
		assertEquals(msg.getUsername(), null);
	}

	@Override
	protected void setUp() throws Exception {
		msg = new ChatMessage("1234", datum, "TestNachricht");
		
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
