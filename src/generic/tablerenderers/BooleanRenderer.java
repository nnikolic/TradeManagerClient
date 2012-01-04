package generic.tablerenderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class BooleanRenderer extends JPanel implements TableCellRenderer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JCheckBox check = null;
	
	public BooleanRenderer(){
		super();
		setOpaque(true);
		setLayout(new FlowLayout(FlowLayout.CENTER, 0 , 0));
		check = new JCheckBox();
		check.setEnabled(false);
		add(check);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (table != null) {
			JTableHeader header = table.getTableHeader();
			if (header != null) {
				if(row%2!=0){
					setBackground(Color.WHITE);
				}else{
					setBackground(table.getBackground());
				}
				setBorder(null);
				setFont(table.getFont());
			}
			if(isSelected){
				setBackground(table.getSelectionBackground());
			}
			if(hasFocus){
			}
		}
		
		check.setSelected((Boolean)value == true);
		
		return this;
	}

}
