package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;


@SuppressWarnings("serial")
public class LoadingFrame extends JFrame {
	public static LoadingFrame instance = null;
	private JPanel mainPanel = null, logoPanel = null;
	private Task task;
	private JProgressBar progressBar;
	

	public static LoadingFrame getInstance() {
		if (instance == null)
			instance = new LoadingFrame();
		return instance;
	}

	public LoadingFrame() {
		//ImageIcon icon = new ImageIcon(getClass().getResource("/icons/icon.png"));
		//setIconImage(icon.getImage());
		setTitle("");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);
//		logoPanel = new JPanel(new BorderLayout()) {
//			public void paint(Graphics g) {
//				super.paint(g);
//				ImageIcon img = new ImageIcon(getClass().getResource(
//						"/icons/zebra logo.jpg"));
//				g.drawImage(img.getImage(), getWidth() / 4, getHeight() / 6,this);
//			}
//		};
		logoPanel.setBackground(Color.WHITE);
		logoPanel.setPreferredSize(new Dimension(608, 435));
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setIndeterminate(true);
		task = new Task();
		task.execute();
		mainPanel.add(logoPanel, BorderLayout.CENTER);
		mainPanel.add(progressBar, BorderLayout.SOUTH);
		setContentPane(mainPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	class Task extends SwingWorker<Void, Void> {
		public boolean connected = false;
		
		protected Void doInBackground() throws Exception {
			Random random = new Random();
			int progress = 0;
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			setProgress(0);
//			if (DataBase.getDataBase().openConnection())
//				connected = true;
//			else 
//				connected = false;
			while (progress < 100) {
				progress += random.nextInt(10);
				setProgress(Math.min(progress, 100));
			}
			return null;
		}
		protected void done() {
			setCursor(null);
			if (connected){
				dispose();
				instance = null;
				//Application.getInstance();
			}
			else{
				setVisible(false);
				JOptionPane.showMessageDialog(null, "Konekcija nije uspesna", "Greska", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}
}
