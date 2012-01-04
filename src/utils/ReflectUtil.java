package utils;

import java.lang.reflect.Method;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import model.metadata.FieldTypeEnum;

import com.toedter.calendar.JDateChooser;

public class ReflectUtil {
	public static Method getMethod(Object obj, String methodName){
		Method[] methods = obj.getClass().getMethods();
		for(Method m: methods){
			if(m.getName().equals(methodName)){
				return m;
			}
		}
		return null;
	}
	
	public static Object getRealValue(String typeStr, Object component){
		if(typeStr.equals(FieldTypeEnum.STRING)){
			return ((JTextField)component).getText();
		}else if(typeStr.equals(FieldTypeEnum.INTEGER)){
			try {
				return Integer.parseInt(((JTextField)component).getText());
			} catch (Exception e) {
				return null;
			}
		}else if(typeStr.equals(FieldTypeEnum.BOOLEAN)){
			((JCheckBox)component).isSelected();
		}else if(typeStr.equals(FieldTypeEnum.DATE)){
			return ((JDateChooser)component).getDate();
		}else if(typeStr.equals(FieldTypeEnum.DOUBLE) || typeStr.equals(FieldTypeEnum.PRICE)){
			try {
				return Double.parseDouble(((JTextField)component).getText());
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
}
