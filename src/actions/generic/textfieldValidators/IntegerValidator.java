package actions.generic.textfieldValidators;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class IntegerValidator extends PlainDocument{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8342035052806360369L;
	
	@Override
	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {
		if(str.equals("-")&&getLength()==0){
			
		}else{
			try{
				Integer.parseInt(str);
			}catch(Exception e){
				return;
			}
		}
		super.insertString(offs, str, a);
	}

}
