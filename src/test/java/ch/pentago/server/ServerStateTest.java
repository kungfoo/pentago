package ch.pentago.server;

import junit.framework.TestCase;
import ch.pentago.core.User;

/**
 * Testclass for the Class ServerState
 * @author sidi
 *
 */
public class ServerStateTest extends TestCase {

	private User user1;

	public void testGetUserList() {
		assertEquals(ServerState.getUserList().contains(user1), true);
	}

	public void testGetUser() {
		assertEquals(ServerState.getUser(user1.getSessionId()), user1);
	}

	public void testResolveUserName() {
		assertEquals(ServerState.resolveUserName(user1.getSessionId()), user1.getUserName());
	}
	
	@Override
	protected void setUp() throws Exception {
		user1 = new User("Max", "1234");
		ServerState.addUser(user1);
		super.setUp();
	}
}
