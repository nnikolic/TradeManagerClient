package generic.tools;

public class MessageObject {
	public static int INFO = 0;
	public static int WARN = 1;
	public static int ERROR = 2;
	public static int NONE = -1;
	
	private String messageCode, message;
	
	private int severity;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	public int getSeverity() {
		return severity;
	}
	public void setSeverity(int severity) {
		this.severity = severity;
	}
}
