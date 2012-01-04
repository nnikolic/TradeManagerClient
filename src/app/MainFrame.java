package app;

import generic.form.FormsHolder;
import generic.form.GenericForm;
import generic.form.model.MenuBarMetadata;
import generic.form.model.MenuMetadata;
import helpers.ButtonTabComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import localization.Local;
import remotes.RemotesManager;
import util.ServerResponse;
import actions.ActionManager;
import actions.generic.OpenEntityAction;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1729233823098776690L;
	private JMenuBar menuBar;
	private JMenu toolMenu;
	private JMenu skinMenu;
	private JTabbedPane tabbedPane = null;
	private JLabel stanjeLabela = new JLabel("Opcija:");
	private JPanel contentPanel = null, statusPanel = null;
	private JToolBar toolBar = null;
	private MenuBarMetadata menuMeta = null;
	private ArrayList<JDialog> zoomNextDialogs = null;

	public MainFrame() {
		zoomNextDialogs = new ArrayList<JDialog>();
		contentPanel = new JPanel(new BorderLayout());
		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(Appliction.getInstance().getCurrentForm() != null)
					Appliction.getInstance().getCurrentForm().getGenericViewForm().getToolbar().setEditability();
				int si = tabbedPane.getSelectedIndex();
				String title = "";
				if(si >= 0){
					title = tabbedPane.getTitleAt(si);
				}
				stanjeLabela.setText("Opcija: "+title);
			}
		});
		statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		statusPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		statusPanel.add(stanjeLabela);
		
		FormsHolder.getInstance();

		toolBar = new JToolBar();

		contentPanel.add(tabbedPane, BorderLayout.CENTER);
		contentPanel.add(statusPanel, BorderLayout.SOUTH);

		Appliction.getInstance().getPopupProgressBar().setVisible(false);
		
		setContentPane(contentPanel);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(1024, 768));
		initMenuBar();
		setJMenuBar(menuBar);
		setTitle(Local.getString("APPLICATION.TITLE"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
		setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH);
	}

	public void addZoomNextDialog(JDialog d){
		zoomNextDialogs.add(d);
	}
	
	public void removeLastZoomNextDialog(boolean closeDialog){
		if(zoomNextDialogs.size()>0){
			JDialog d = zoomNextDialogs.get(zoomNextDialogs.size()-1);
			if(closeDialog && d.isVisible()){
				d.setVisible(false);
				d.dispose();
			}
			zoomNextDialogs.remove(zoomNextDialogs.size()-1);
		}
	}
	
	private void initTabComponent(int i) {
        tabbedPane.setTabComponentAt(i,
                 new ButtonTabComponent(tabbedPane));
    }
	
	public void addNewTab(String tabName, GenericForm tab){
		if(zoomNextDialogs.size()>0){
			addZoomNextDialog(tab, tabName);
		}else if(tabbedPane.indexOfComponent(tab)==-1){
			tabbedPane.addTab(tabName, tab);
			int index = tabbedPane.indexOfComponent(tab);
			initTabComponent(index);
			tabbedPane.setSelectedIndex(index);
		}else{
			tabbedPane.setSelectedComponent(tab);
		}
		tab.requestFocus();
		stanjeLabela.setText("Opcija: "+tabName);
	}
	
	public GenericForm getPenultimateForm(){
		if(zoomNextDialogs.size()>1){
			return (GenericForm) zoomNextDialogs.get(zoomNextDialogs.size()-2).getContentPane();
		}
		if(tabbedPane.getSelectedComponent()!=null){
			return (GenericForm) tabbedPane.getSelectedComponent();
		}
		return null;
	}
	
	public GenericForm getCurrentForm(){
		if(zoomNextDialogs.size()>0){
			return (GenericForm) zoomNextDialogs.get(zoomNextDialogs.size()-1).getContentPane();
		}
		if(tabbedPane.getSelectedComponent()!=null){
			return (GenericForm) tabbedPane.getSelectedComponent();
		}
		return null;
	}

	public JPanel getContentPanel() {
		return contentPanel;
	}

	public JLabel getStanjeLabela() {
		return stanjeLabela;
	}
	
	public void addZoomNextDialog(GenericForm gf, String title){
		JDialog lookupDialog = new JDialog();
		lookupDialog.setTitle(title);
		lookupDialog.setSize(800, 600);
		lookupDialog.setContentPane(gf);
		lookupDialog.setModal(true);
		lookupDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		lookupDialog.setLocationRelativeTo(this);
		if(zoomNextDialogs.size()>0){
			int x = zoomNextDialogs.get(zoomNextDialogs.size()-1).getLocation().x;
			int y = zoomNextDialogs.get(zoomNextDialogs.size()-1).getLocation().y;
			lookupDialog.setLocation(x+20, y+20);
		}
		
		lookupDialog.addWindowListener(new  WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				removeLastZoomNextDialog(false);
				super.windowClosing(e);
			}
		});
		
		addZoomNextDialog(lookupDialog);

		lookupDialog.setVisible(true);
		gf.requestFocus();
	}

	private void addSubmanu(MenuMetadata mm, JMenu parentMenu){
		for(String k: mm.getMenuItemList().keySet()){
			parentMenu.add(new OpenEntityAction(k, mm.getMenuItemList().get(k)[0], mm.getMenuItemList().get(k)[1]));
		}
		for(MenuMetadata m: mm.getSubmenus()){
			JMenu newMenu = new JMenu(Local.getString(m.getMenuLabel()));
			addSubmanu(m, newMenu);
			parentMenu.add(newMenu);
		}
	}
	
	private void initMenuBar() {
		menuBar = new JMenuBar();
		ServerResponse sr = RemotesManager.getInstance().getMetadataRemote().getMenuXml();
		
		menuMeta = new MenuBarMetadata((String)sr.getData());
		for(MenuMetadata mm : menuMeta.getMenuList()){
			JMenu m = new JMenu(Local.getString(mm.getMenuLabel()));
			addSubmanu(mm, m);
			menuBar.add(m);
		}
		JMenu helpMenu = new JMenu(Local.getString("HELP"));
		
		helpMenu.add(ActionManager.getInstance().getAboutAction());
		
		menuBar.add(helpMenu);
		
//		toolMenu = new JMenu(Local.getString("MENU.TOOLS"));
//		skinMenu = SkinChosser.getSkinMenu();
//		toolMenu.add(skinMenu);
//		radniciMenu = new JMenu(Local.getString("MENU.ENTITETI"));
//		radniciMenu
//				.add(ActionManager.getInstance().getDodavenjeRadnikaAction());
//		radniciMenu.add(ActionManager.getInstance().getPregledRadnikaAction());
//		menuBar.add(radniciMenu);
//		menuBar.add(toolMenu);
	}
}
