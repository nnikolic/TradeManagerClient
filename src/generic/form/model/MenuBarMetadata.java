package generic.form.model;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import app.Appliction;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class MenuBarMetadata {
	private String xmlStr;
	private ArrayList<MenuMetadata> menuList = null;

	public MenuBarMetadata(String xmlStr) {
		this.xmlStr = xmlStr;
		menuList = new ArrayList<MenuMetadata>();
		parseXmlString();
	}
	
	public ArrayList<MenuMetadata> getMenuList() {
		return menuList;
	}

	private void parseXmlString() {
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new StringReader(this.xmlStr)));
			Document doc = parser.getDocument();
			Element root = doc.getDocumentElement();

			String menuName = "";
			Node n1;
			NodeList menuNodeList = ((Element)root).getChildNodes();
			
			int type = Appliction.getInstance().getLoggedUser().getType().intValue();
			
			for (int i = 0; i < menuNodeList.getLength(); i++) {
				n1 = menuNodeList.item(i);
				if (n1.getNodeName().equals("menu") && type<=Integer.parseInt(((Element) n1).getAttribute("licence_type"))) {
					menuName = ((Element) n1).getAttribute("label");
					MenuMetadata mm = new MenuMetadata(menuName, n1);
					menuList.add(mm);
				}
			}

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
