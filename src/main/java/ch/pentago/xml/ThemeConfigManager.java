package ch.pentago.xml;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import ch.pentago.core.Theme;

public class ThemeConfigManager {
	
	@SuppressWarnings("unchecked")
	public static List<Theme> getThemeList() {
		SAXBuilder builder = new SAXBuilder();
		List<Theme> themelist = new LinkedList<Theme>();

		try {
			Document config = builder.build("themes/themes.xml");
		
			Element root = config.getRootElement();
			List<Element> list = root.getChildren();
			for (Element element : list) {
				themelist.add(new Theme(element.getAttributeValue("name"),
						element.getChild("name").getAttributeValue("value")));
			}
		} catch (JDOMException e) {
			System.out.println("themes.xml malformed");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("themes.xml not found");
			e.printStackTrace();
		}
		return themelist;
	}

}
