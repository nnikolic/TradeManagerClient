package generic.form.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import app.Appliction;

public class MenuMetadata {
	private List<MenuMetadata> submenus = null;
	private Map<String, String[]> menuItemList;
	private String menuLabel="";
	
	public MenuMetadata(String menuLbl, Node node){
		submenus = new ArrayList<MenuMetadata>();
		menuLabel = menuLbl;
		menuItemList = new HashMap<String,String[]>();
		
		NodeList items = ((Element)node).getChildNodes();
		Node item = null;
		int type = Appliction.getInstance().getLoggedUser().getType().intValue();
		for (int j = 0; j < items.getLength(); j++) {
			item = items.item(j);
			if (item.getNodeName().equals("menuitem") && type <= Integer.parseInt(((Element) item).getAttribute("licence_type"))) {
				addMenuItem(((Element) item).getAttribute("label"),
						((Element) item).getAttribute("entity"),
						((Element) item).getAttribute("icon"));
			}
		}
		
		initChildren(node);
	}
	
	private void initChildren(Node node){
		Node n = null, n1 = null;
		NodeList childNodes = ((Element)node).getChildNodes();
		int type = Appliction.getInstance().getLoggedUser().getType().intValue();
		for(int i=0; i<childNodes.getLength(); i++){
			n = childNodes.item(i);
			if (n.getNodeName().equals("menu")) {
				int menuType = Integer.parseInt(((Element) n).getAttribute("licence_type"));
				if(type <= menuType){
					MenuMetadata mm = new MenuMetadata(((Element) n).getAttribute("label"), n);
					for (int j = 0; j < n.getChildNodes().getLength(); j++) {
						n1 = n.getChildNodes().item(j);
						if (!n1.getNodeName().equals("#text")) {
							menuType = Integer.parseInt(((Element) n1).getAttribute("licence_type"));
							if(type<=menuType){
								mm.addMenuItem(
										((Element) n1).getAttribute("label"),
										((Element) n1).getAttribute("entity"),
										((Element) n1).getAttribute("icon"));
							}
						}
					}
					submenus.add(mm);
				}
			}
		}
	}
	
	public List<MenuMetadata> getSubmenus() {
		return submenus;
	}
	
	public Map<String, String[]> getMenuItemList() {
		return menuItemList;
	}
	
	public void addMenuItem(String menuItemLbl, String entityName, String imgFileName){
		menuItemList.put(menuItemLbl, new String[]{entityName, imgFileName});
	}
	
	public String getMenuLabel() {
		return menuLabel;
	}
}
