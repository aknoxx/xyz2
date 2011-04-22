package dst2.ejb.util;

public class UserNotLoggedInException extends Exception {

	private static final long serialVersionUID = 7459658788006325630L;

	public UserNotLoggedInException() {		
	}
	
	public UserNotLoggedInException(String msg) {
		super(msg);
	}
}