package dst2.ejb.util;

public class ComputersNotAvailableTemporaryException extends Exception {

	private static final long serialVersionUID = 7459658788006325630L;

	public ComputersNotAvailableTemporaryException() {		
	}
	
	public ComputersNotAvailableTemporaryException(String msg) {
		super(msg);
	}
}