package app;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import layouts.RiverLayout;


public class PopupProgressBar extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8634750155253698723L;

	private JProgressBar progressBar = null;
	private JLabel titleLabel = null;
	
	private JPanel mainPanel = null;
	
	public PopupProgressBar(){
		initComponents();
		addComponents();
	}
	
	private void initComponents(){
		setUndecorated(true);
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setSize(230, 40);

		mainPanel = new JPanel();
		
		titleLabel = new javax.swing.JLabel();
		
		setSize(300, 100);
		
		mainPanel.setLayout(new RiverLayout());
		
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}

	public void setText(String text){
		titleLabel.setText(text);	
	}
	
	private void addComponents(){
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		
		
		mainPanel.add("tab",titleLabel);
		mainPanel.add("br br br hfill vfill", progressBar);
		
		
		setContentPane(mainPanel);
	}
	
	
}
