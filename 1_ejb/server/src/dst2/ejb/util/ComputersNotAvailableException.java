package dst2.ejb.util;

public class ComputersNotAvailableException extends Exception {

	private static final long serialVersionUID = 7459658788006325630L;

	public ComputersNotAvailableException() {		
	}
	
	public ComputersNotAvailableException(String msg) {
		super(msg);
	}
}