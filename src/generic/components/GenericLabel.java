package generic.components;

import javax.swing.JLabel;

public class GenericLabel extends JLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1067264906288164983L;
	
	private boolean required;
	private String text;
	
	public GenericLabel(String text){
		super(text);
		this.text = text;
	}

	public void setRequired(boolean required) {
		this.required = required;
		setText(text + (required ? " *" : ""));
	}
}
