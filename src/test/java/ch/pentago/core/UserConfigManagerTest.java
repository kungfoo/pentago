package ch.pentago.core;

import ch.pentago.xml.UserConfigManager;
import junit.framework.TestCase;

/**
 * Testclass for the class UserConfigManager
 * @author sidi
 *
 */
public class UserConfigManagerTest extends TestCase {
	
	public void testSetUserConfigItem() {
		UserConfigManager.setUserConfigItem("client", "theme", "wood");
		assertEquals("wood", UserConfigManager.getUserConfigItem("client", "theme"));
	}
	
	public void testSetLastLogin() {
		UserConfigManager.setLastLogin("Max");
		assertEquals(UserConfigManager.getLastLogin(), "Max");
	}
}
