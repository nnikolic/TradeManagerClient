package generic.tablerenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class StringRenderer extends JLabel implements TableCellRenderer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2216620410499180769L;
	public StringRenderer(){
		super();
		setOpaque(true);
		setHorizontalAlignment(JLabel.LEFT);
		Border paddingBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));

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
				setFont(table.getFont());
			}
			if(isSelected){
				setBackground(table.getSelectionBackground());
			}
			if(hasFocus){
			}
		}
		if(value instanceof String){
			setText(value.toString());
		}
		return this;
	}
}
