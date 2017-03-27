package ch.pentago.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Singleton class loading config forclient from config file, handling all the
 * parsing and passing of config to other classes<br>
 * lazy initialization is used
 * 
 * @author kungfoo
 * 
 */
public final class UserConfigManager {
	private static boolean initialized = false;

	private static Document config = new Document();

	// default config file name
	private static File configFile = new File("config/userConfig.xml");

	private static List<String> userList = new ArrayList<String>();

	private static HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();

	private UserConfigManager() {
	}
	
	public static boolean isInitalized() {
		return initialized;
	}

	@SuppressWarnings("unchecked")
	private static void init() {
		if (!initialized) {
			synchronized (config) {
				SAXBuilder builder = new SAXBuilder();
				try {
					config = builder.build(configFile);
				} catch (JDOMException e) {
					System.out.println("user config file malformed");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("user config file not found");
					e.printStackTrace();
				}
				if (config != null) {
					Element root = config.getRootElement();
					Element users = root.getChild("users");
					List<Element> list = users.getChildren();
					for (Element user : list) {
						userList.add(user.getAttributeValue("name"));
						// System.out.println("user: "+
						// user.getChild("name").getAttributeValue("value"));
					}
					Element server = root.getChild("configItems");
					list = server.getChildren();
					for (Element item : list) {
						map.put(item.getQualifiedName(),
								new HashMap<String, String>());
						List<Element> configItems = item.getChildren();
						for (Element configItem : configItems) {
							map.get(item.getQualifiedName()).put(
									configItem.getQualifiedName(),
									configItem.getAttributeValue("value"));
						}
					}
					initialized = true;
				}
			}
		}
	}

	/**
	 * fetch a config Item from part of the config file
	 * 
	 * @param part
	 *            part where the item resides
	 * @param item
	 *            name of the item
	 * @return the value the item has been given
	 */
	public static String getUserConfigItem(String part, String item) {
		init();
		return map.get(part).get(item);
	}
	
	/**
	 * sets a new item in the userConfig.xml file and writes the file
	 * the changes will be reflected to the application, after this method completes
	 * @param part which part to write to
	 * @param item which item to set
	 * @param value the new value fot the config item
	 */
	public static void setUserConfigItem(String part, String item, String value){
		init();
		Element root = config.getRootElement();
		root = root.getChild("configItems");
		root.getChild(part).getChild(item).setAttribute("value", value);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		File file = new File("config/userConfig.xml");
		try {
			FileOutputStream filestream = new FileOutputStream(file);
			outputter.output(config, filestream);
			
			filestream.flush();
			filestream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		initialized = false;
		init();
	}

	public static List<String> getUserNames() {
		init();
		java.util.Collections.sort(userList);
		return userList;
	}
	
	public static void addNewUserName(String username) {
		init();
		Element root = config.getRootElement();
		Element users = root.getChild("users");
		Element user = new Element("user");
		user.setAttribute("name", username);
		users.addContent(user);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		File file = new File("config/userConfig.xml");
		try {
			FileOutputStream filestream = new FileOutputStream(file);
			outputter.output(config, filestream);
			
			filestream.flush();
			filestream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		initialized = false;
		init();
	}
	
	public static String getLastLogin() {
		init();
		Element root = config.getRootElement();
		Element users = root.getChild("users");
		return users.getAttributeValue("lastlogin");
	}
	
	public static void setLastLogin(String username) {
		init();
		Element root = config.getRootElement();
		Element users = root.getChild("users");
		users.setAttribute("lastlogin", username);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		File file = new File("config/userConfig.xml");
		try {
			FileOutputStream filestream = new FileOutputStream(file);
			outputter.output(config, filestream);
			
			filestream.flush();
			filestream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		initialized = false;
		init();
	}
}
