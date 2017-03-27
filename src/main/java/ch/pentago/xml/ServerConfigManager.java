package ch.pentago.xml;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Singleton class loading config for the server from config file, handling all the parsing and
 * passing of config to other classes<br>
 * lazy initialization is used
 * @author kungfoo
 *
 */
public final class ServerConfigManager {
	private static boolean initialized = false;
	private static Document config = new Document();
	// default config file name
	private static File configFile = new File("config/serverConfig.xml");
	
	private static HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String,String>>();
	
	// singleton
	private ServerConfigManager(){}
	
	/**
	 * sets up the entire ServerConfigManager object
	 */
	@SuppressWarnings("unchecked")
	private static void init(){
		if(!initialized){
			synchronized(config){
				SAXBuilder builder = new SAXBuilder();
				try{
					config = builder.build(configFile);
				}
				catch(IOException e){
					System.out.println("config file not found");
				}
				catch(JDOMException e){
					System.out.println("config file XML malformed, stack trace follows");
					e.printStackTrace();
				}
				Element root = config.getRootElement();
				List<Element> parts = root.getChildren();
				// setup confog collection
				for (Element part : parts) {
					map.put(part.getQualifiedName(), new HashMap<String, String>());
					List<Element> configItems = part.getChildren();
					for (Element configItem : configItems) {
						map.get(part.getQualifiedName()).put(configItem.getQualifiedName(), configItem.getAttributeValue("value"));
					}
				}
				initialized = true;
			}
		}
	}
	
	/**
	 * return the config items value, that has the name key
	 * @param key item to return
	 * @return the value the config item has been set to
	 */
	private static String getDataBaseConfigItem(String key){
		init();
		return map.get("database").get(key);
	}
	
	/**
	 * return the config items value, that has the name key
	 * @param key item to return
	 * @return the value the config item has been set to
	 */
	private static String getServerConfigItem(String key){
		init();
		return map.get("server").get(key);
	}
	
	/*
	 * database attribute functions
	 */
	public static String getDataBaseName(){
		return getDataBaseConfigItem("name");
	}
	public static int getDataBasePort(){
		return Integer.parseInt(getDataBaseConfigItem("port"));
	}
	public static String getDataBaseUser(){
		return getDataBaseConfigItem("user");
	}
	public static String getDataBaseType(){
		return getDataBaseConfigItem("type");
	}
	public static String getDataBasePassword(){
		return getDataBaseConfigItem("password");
	}
	public static String getDataBaseHost(){
		return getDataBaseConfigItem("host");
	}
	
	/*
	 * server config attribute functions
	 */
	public static int getThreads(){
		return Integer.parseInt(getServerConfigItem("threads"));
	}
	public static int getServerPort(){
		return Integer.parseInt(getServerConfigItem("port"));
	}
	public static int getServerMaxUsers(){
		return Integer.parseInt(getServerConfigItem("maxusers"));
	}
	public static String getServerLocation(){
		return getServerConfigItem("location");
	}
	public static InetAddress getServerListeningAdress() throws UnknownHostException{
		return InetAddress.getByName(getServerConfigItem("listen"));
	}
	public static String getServerLanguage(){
		return getServerConfigItem("language");
	}
	public static String getServerName(){
		return getServerConfigItem("name");
	}
}
