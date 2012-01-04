package app;

import generic.MessagePanel;
import generic.form.GenericForm;
import hibernate.entityBeans.User;
import hibernate.remotes.SessionBeanRemote;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import localization.Local;
import model.metadata.EntityMetadata;
import remotes.RemotesManager;
import skin.SkinChosser;
import util.ServerResponse;

public class Appliction{
	private static Appliction instance = null;
	private MainFrame mainFrame = null;
	private SkinChosser skinChosser = null;
	private Properties settings = new Properties();
	
	private User loggedUser = null;
	
	private PopupProgressBar popupProgressBar;
	
	public static Appliction getInstance() {
		if (instance == null)
			instance = new Appliction();
		return instance;
	}
	
	public SkinChosser getSkinChosser() {
		return skinChosser;
	}
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}
	
	public User getLoggedUser() {
		return loggedUser;
	}
	
	public void setNultiMod(){
		try {
			getMainFrame().getContentPanel().remove(2);
		} catch (Exception e) {}
		getMainFrame().getStanjeLabela().setText("Opcija: ");
		getMainFrame().getContentPanel().updateUI();
	}
	
	public GenericForm getCurrentForm(){
		return getMainFrame().getCurrentForm();
	}
	
	private Appliction() {
		skinChosser = new SkinChosser(0);
		popupProgressBar = new PopupProgressBar();
		
		try {
		    settings.load(new FileInputStream("settings.properties"));
		} catch (IOException e) {}
	}
	
	public Properties getSettings() {
		return settings;
	}
	
	public PopupProgressBar getPopupProgressBar() {
		return popupProgressBar;
	}
	
	public static void setNimbusLookAndFeel() {
		try {
			UIManager.put("nimbusFocus", new Color(249,209,0));
			UIManager.put("nimbusSelectedText", new Color(249,209,0));
			UIManager.put("nimbusSelectionBackground", Color.BLACK);
			UIManager.put("nimbusBlueGrey", new Color(45,45,45));
			UIManager.put("nimbusBase", new Color(51,51,51));
			UIManager.put("control", new Color(51,51,51));
			UIManager.put("text", new Color(249,209,0));
			UIManager.put("nimbusLightBackground", new Color(102,102,102));
			for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(laf.getName()))
					UIManager.setLookAndFeel(laf.getClassName());
			}
		} catch (Exception e) {
			System.out.println("Error setting nimbus LAF: " + e);
		}
	}
	
	public static void main(String[] args) {
		UIManager.put("OptionPane.cancelButtonText", Local.getString("CANCEL"));
		UIManager.put("OptionPane.noButtonText", Local.getString("NO"));
	    UIManager.put("OptionPane.yesButtonText", Local.getString("YES"));
		setNimbusLookAndFeel();
		Appliction.getInstance();
		Appliction.getInstance().initLogin();
	}
	
	public void initLogin() {
		
		SessionBeanRemote sr = RemotesManager.getInstance().getSessionRemote();
		ServerResponse metaResponse = RemotesManager.getInstance().getMetadataRemote().getEntityXml("User.xml");
		
		final JTextField username = new JTextField(20);
		final JPasswordField password = new JPasswordField(20);
		JLabel userLabel = new JLabel(Local.getString("LOGIN.USERNAME") + ":");
		JLabel passLabel = new JLabel(Local.getString("LOGIN.PASSWORD") + ":");
		String user = "";
		String pass = "";
		Object[] dialogObj = {userLabel, username, passLabel, password};
		boolean auth = false;
		username.addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
            	username.requestFocusInWindow();
            }
            public void ancestorMoved(AncestorEvent event) {}
            public void ancestorRemoved(AncestorEvent event) {}
        });
		
		Appliction.getInstance().getPopupProgressBar().setText(Local.getString("LOADING"));
		while (!auth){
			password.setText(null);
			int confirm = JOptionPane.showConfirmDialog(null, dialogObj, Local.getString("LOGIN"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			Appliction.getInstance().getPopupProgressBar().setLocationRelativeTo(null);
			Appliction.getInstance().getPopupProgressBar().setVisible(true);
			if (confirm == JOptionPane.YES_OPTION){
				pass = new String(password.getPassword());
				user = username.getText();
				MessagePanel messPanel = null;
				
				if(metaResponse.getSeverity()!=ServerResponse.INFO){
					messPanel = new MessagePanel(Local.getString(metaResponse.getResponseCode()), metaResponse.getResponseMessage());
					JOptionPane.showMessageDialog(null, messPanel, Local.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
				}else{
					EntityMetadata em = new EntityMetadata((String)metaResponse.getData());
					ServerResponse response = sr.login(user, pass, em);
					if(response.getSeverity()==ServerResponse.NONE){
						auth = true;
						loggedUser = (User) response.getData();
						//skinChosser = new SkinChosser(1);
						skinChosser = new SkinChosser(0);
						mainFrame = new MainFrame();
//						ActionManager.getInstance().setAccelerators();
					}else{
						Appliction.getInstance().getPopupProgressBar().setVisible(false);
						messPanel = new MessagePanel(Local.getString(response.getResponseCode()), response.getResponseMessage());
						if(response.getSeverity()==ServerResponse.WARN){
							JOptionPane.showMessageDialog(null, messPanel, Local.getString("WARN"), JOptionPane.WARNING_MESSAGE);
						}else{
							JOptionPane.showMessageDialog(null, messPanel, Local.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}else
				System.exit(0);
		}
	}
}
