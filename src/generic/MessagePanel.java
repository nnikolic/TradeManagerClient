package generic;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MessagePanel extends JPanel{
	private JLabel messageLbl = null;
	private JButton messDescButton = null;
	private String messString, messDescString;
	public MessagePanel(String mess, String desc){
		messString = mess;
		messDescString = desc==null? "":desc;
		messageLbl = new JLabel(messString);
		messDescButton = new JButton("...");
		messDescButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = new JDialog();
				JTextArea area = new JTextArea(messDescString);
				area.setEditable(false);
				dialog.setContentPane(new JScrollPane(area));
				dialog.setSize(400, 250);
				dialog.setModal(true);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(getParent());
				dialog.setVisible(true);
			}
		});
		
		setLayout(new BorderLayout());
		add(messageLbl, BorderLayout.CENTER);
		add(messDescButton, BorderLayout.SOUTH);
	}
}
