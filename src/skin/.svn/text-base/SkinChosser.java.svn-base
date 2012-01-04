package skin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.PropertyResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import localization.Local;
import app.Appliction;

public class SkinChosser {
	private JRadioButtonMenuItem rbDefault;
	private JRadioButtonMenuItem rbBlackYellow;
	private ButtonGroup buttonGroup;
	private ArrayList<Skin> skinList;
	private int id;
	private static JMenu skinMenu;
	private int selectedIndex;

	public ArrayList<Skin> getSkinList() {
		return skinList;
	}

	public void setSkinList(ArrayList<Skin> skinList) {
		this.skinList = skinList;
	}

	public SkinChosser(int id) {
		this.id = id;
		skinList = new ArrayList<Skin>();
		if (id == 0)
			setNimbusLookAndFeel();
		else if (id == 1)
			setBlackNimbusLookAndFeel();
		loadSkin();
		createMenu();
	}

	public static JMenu getSkinMenu() {
		return skinMenu;
	}
	
	public JMenu createMenu() {
		skinMenu = new JMenu("Skin");
		rbDefault = new JRadioButtonMenuItem("Default");
		if (id == 0)
			rbDefault.setSelected(true);
		rbDefault.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				setNimbusLookAndFeel();
				SwingUtilities.updateComponentTreeUI(Appliction.getInstance().getMainFrame());
			}
		});
		rbBlackYellow = new JRadioButtonMenuItem("Black & Yellow");
		if (id == 1)
			rbBlackYellow.setSelected(true);
		rbBlackYellow.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				setBlackNimbusLookAndFeel();
				SwingUtilities.updateComponentTreeUI(Appliction.getInstance().getMainFrame());
			}
		});
		buttonGroup = new ButtonGroup();
		buttonGroup.add(rbDefault);
		buttonGroup.add(rbBlackYellow);
		skinMenu.add(rbDefault);
		skinMenu.add(rbBlackYellow);
		JRadioButtonMenuItem tmp;
		int brojac = 0;
		for (Skin skin:skinList){
			tmp = new JRadioButtonMenuItem(skin.getName());
			if (skin.getId() == id)
				tmp.setSelected(true);
			tmp.setName(brojac++ + "");
			skinMenu.add(tmp);
			buttonGroup.add(tmp);
			tmp.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					selectedIndex = Integer.parseInt(((JRadioButtonMenuItem)e.getItemSelectable()).getName());
					setCustomNimbusLookAndFeel(skinList.get(selectedIndex));
					SwingUtilities.updateComponentTreeUI(Appliction.getInstance().getMainFrame());
				}
			});
		}
		skinMenu.setVisible(true);
		skinMenu.addSeparator();
		JMenuItem addButton = new JMenuItem(Local.getString("SKIN_ADD") + "...");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addSkin();
			}
		});
		skinMenu.add(addButton);
		return skinMenu;
	}
	
	private Skin newSkin;
	private JDialog addDialog;
	
	public void addSkin (){
		addDialog = new JDialog();
		addDialog.setLayout(new GridLayout(4, 2));
		newSkin = new Skin();
		addDialog.setTitle(Local.getString("SKIN_ADD"));
		addDialog.setPreferredSize(new Dimension(300,200));
		addDialog.setResizable(true);
		addDialog.setModal(true);
		
		addDialog.add(new JLabel("Base color"));
		ColorButton basePickButton = new ColorButton();
		basePickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color base = JColorChooser.showDialog(null, "Izaberi boju", Color.WHITE);
				((ColorButton)e.getSource()).setPaintColor(base);
				newSkin.setNimbusBase(base); 
			}
		 });
		addDialog.add(basePickButton);
		
		addDialog.add(new JLabel("Text color"));
		ColorButton textPickButton = new ColorButton();
		textPickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color text = JColorChooser.showDialog(null, "Izaberi boju", Color.WHITE);
				((ColorButton)e.getSource()).setPaintColor(text);
				newSkin.setText(text); 
				newSkin.setNimbusSelectedText(text.brighter());
			}
		 });
		addDialog.add(textPickButton);
		
		addDialog.add(new JLabel("Control color"));
		ColorButton controlPickButton = new ColorButton();
		controlPickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color control = JColorChooser.showDialog(null, "Izaberi boju", Color.WHITE);
				((ColorButton)e.getSource()).setPaintColor(control);
				newSkin.setControl(control);
			}
		 });
		addDialog.add(controlPickButton);
		addDialog.add(new JLabel());
		JButton preview = new JButton("Preview");
		preview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setCustomNimbusLookAndFeel(newSkin);
				SwingUtilities.updateComponentTreeUI(Appliction.getInstance().getMainFrame());
				SwingUtilities.updateComponentTreeUI(addDialog);
				addDialog.repaint();
			}
		});
		addDialog.add(preview);
		addDialog.pack();
		addDialog.setLocationRelativeTo(Appliction.getInstance().getMainFrame());
		addDialog.setVisible(true);
		// JButton colorPickButton = new JButton("...");
		// colorPickButton.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// JColorChooser.showDialog(null, "Izaberi boju", Color.WHITE);
		// }
		// });
		// add(colorPickButton);
	}

	public void loadSkin (){
		File skinFolder = new File("skin");
		PropertyResourceBundle rb;
		for (File tmp:skinFolder.listFiles()){
			try {
				rb = new PropertyResourceBundle(new FileInputStream(tmp));
				Skin skin = new Skin();
				skin.setId(Integer.parseInt(rb.getString("id")));
				skin.setName(rb.getString("name"));
				skin.setNimbusBase(convertToColor(rb.getString("nimbusBase")));
				skin.setControl(convertToColor(rb.getString("control")));
				skin.setNimbusBlueGrey(convertToColor(rb.getString("nimbusBlueGrey")));
				skin.setNimbusFocus(convertToColor(rb.getString("nimbusFocus")));
				skin.setNimbusLightBackground(convertToColor(rb.getString("nimbusLightBackground")));
				skin.setNimbusSelectedText(convertToColor(rb.getString("nimbusSelectedText")));
				skin.setNimbusSelectionBackground(convertToColor(rb.getString("nimbusSelectionBackground")));
				skin.setText(convertToColor(rb.getString("text")));
				skinList.add(skin);
				if (id == skin.getId())
					setCustomNimbusLookAndFeel(skin);
			} catch (Exception e) {
			}
		}
	}

	public Color convertToColor(String color){
		String [] rgb = color.split(" ");
		try{
		return new Color(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));
		} catch (Exception e) {
			return null;
		}
	}

	public static void setBlackNimbusLookAndFeel() {
		try {
			UIManager.put("nimbusFocus", new Color(249, 209, 0));
			UIManager.put("nimbusSelectedText", new Color(249, 209, 0));
			UIManager.put("nimbusSelectionBackground", Color.BLACK);
			UIManager.put("nimbusBlueGrey", new Color(45, 45, 45));
			UIManager.put("nimbusBase", new Color(51, 51, 51));
			UIManager.put("control", new Color(51, 51, 51));
			UIManager.put("text", new Color(249, 209, 0));
			UIManager.put("nimbusLightBackground", new Color(102, 102, 102));
			for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(laf.getName()))
					UIManager.setLookAndFeel(laf.getClassName());
			}
		} catch (Exception e) {
			System.out.println("Error setting nimbus LAF: " + e);
		}
	}

	public static void setNimbusLookAndFeel() {
		try {
			UIManager.put("nimbusFocus", null);
			UIManager.put("nimbusSelectedText", null);
			UIManager.put("nimbusSelectionBackground", null);
			UIManager.put("nimbusBlueGrey", null);
			UIManager.put("nimbusBase", null);
			UIManager.put("control", null);
			UIManager.put("text", null);
			UIManager.put("nimbusLightBackground", null);
			for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(laf.getName()))
					UIManager.setLookAndFeel(laf.getClassName());
			}
		} catch (Exception e) {
			System.out.println("Error setting nimbus LAF: " + e);
		}
	}

	public static void setCustomNimbusLookAndFeel(Skin skin) {
		try {
			UIManager.put("nimbusFocus", skin.getNimbusFocus());
			UIManager.put("nimbusSelectedText", skin.getNimbusSelectedText());
			UIManager.put("text", skin.getText());
			UIManager.put("nimbusSelectionBackground", skin
					.getNimbusSelectionBackground());
			UIManager.put("nimbusBlueGrey", skin.getNimbusBlueGrey());
			UIManager.put("nimbusBase", skin.getNimbusBase());
			UIManager.put("control", skin.getControl());
			UIManager.put("nimbusLightBackground", skin
					.getNimbusLightBackground());
			for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(laf.getName()))
					UIManager.setLookAndFeel(laf.getClassName());
			}
		} catch (Exception e) {
			System.out.println("Error setting nimbus LAF: " + e);
		}
	}

//	public static void main(String[] args) {
//		setNimbusLookAndFeel();
//		SkinChosser skinC = new SkinChosser(3);
//		JFrame f = new JFrame();
//		f.setVisible(true);
//		f.setPreferredSize(new Dimension(800, 600));
//		f.pack();
//		f.setLocationRelativeTo(null);
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.add(skinC.createMenu());
//		f.repaint();
//	}
}
