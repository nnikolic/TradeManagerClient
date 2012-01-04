package actions.generic.textfieldValidators;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DoubleValidator extends PlainDocument{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1372851973902841906L;
	
	private boolean hasDecLimiter = false;
	
	@Override
	public void insertString(int arg0, String arg1, AttributeSet arg2)
			throws BadLocationException {
		try{
			if(arg1.contains(",")||arg1.contains(".")){
				if(getLength()==0){
					if(arg1.startsWith(",") || arg1.startsWith(".")){
						return;
					}
				}
				if(hasDecLimiter){
					return;
				}
				if(charCount(arg1, ',')+charCount(arg1, '.')>1){
					return;
				}
				hasDecLimiter = true;
			}else{
				arg1 = arg1.replace(",", ".");
				Double.parseDouble(arg1);
			}
		}catch (Exception e) {
			return;
		}
		super.insertString(arg0, arg1, arg2);
	}
	
	private int charCount(String str, char c){
		int count = 0;
		for(int i=0; i<str.length(); i++){
			if(c==str.charAt(i)){
				count++;
			}
		}
		return count;
	}

	@Override
	public void remove(int offs, int len) throws BadLocationException {
		if(getText(offs, len).contains(",") || getText(offs, len).contains(".")){
			hasDecLimiter = false;
		}
		super.remove(offs, len);
	}
}
