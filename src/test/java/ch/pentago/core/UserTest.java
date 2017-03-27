package ch.pentago.core;

import junit.framework.TestCase;

/**
 * Testclass for the class User
 * @author sidi
 *
 */
public class UserTest extends TestCase {
	
	private User user;
	
	public void testUserStringString() {
		User user1 = new User("Moritz", "asdf");
		assertEquals(user1.getUserName(), "Moritz");
	}

	public void testGetUserName() {
		assertEquals(user.getUserName(), "Max");
	}

	public void testUserString() {
		User user1 = new User("Moritz");
		assertEquals(user1.getUserName(), "Moritz");
	}

	public void testGetSessionId() {
		assertEquals(user.getSessionId(), "1234");
	}

	public void testSetSessionId() {
		user.setSessionId("neueSession");
		assertEquals(user.getSessionId(),"neueSession");
	}

	@Override
	protected void setUp() throws Exception {
		user = new User("Max", "1234");
		super.setUp();
	}
}
