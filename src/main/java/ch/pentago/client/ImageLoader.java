package ch.pentago.client;

import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;

import ch.pentago.xml.UserConfigManager;

/**
 * A cache for all loaded images.
 * @author guetux
 *
 */
public class ImageLoader {
	
	/**
	 * The cache
	 */
	private static HashMap<String, ImageIcon> imagestore = new HashMap<String, ImageIcon>();
	
	/**
	 * This Method loads and returns an Image
	 * @param filename The filename of the Image
	 * @return Image
	 */
	public static Image get(String filename) {
		ImageIcon icon = getIcon(filename);
		if(icon != null) {
			Image img = icon.getImage();
			return img;
		} else {
			return null;
		}
	}
	
	/**
	 * This Method loads and returns an ImageIcon
	 * @param filename The filename of the Image
	 * @return ImageIcon
	 */
	public static ImageIcon getIcon(String filename) {
		if (!imagestore.containsKey(filename)) {
			String location = "themes/" + UserConfigManager.getUserConfigItem("client", "theme")+"/";
			ImageIcon image = new ImageIcon(location+filename);
			imagestore.put(filename, image);
		}
		return imagestore.get(filename);
	}
	
	/**
	 * Clear the cache and force reloading of images 
	 */
	public static void dropCache() {
		imagestore.clear();
	}
}
