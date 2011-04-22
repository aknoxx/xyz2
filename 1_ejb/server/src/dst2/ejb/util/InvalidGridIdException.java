package dst2.ejb.util;

public class InvalidGridIdException extends Exception {

	private static final long serialVersionUID = 7459658788006325630L;

	public InvalidGridIdException() {		
	}
	
	public InvalidGridIdException(String msg) {
		super(msg);
	}
}