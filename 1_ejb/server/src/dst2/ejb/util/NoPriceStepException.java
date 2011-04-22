package dst2.ejb.util;

public class NoPriceStepException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoPriceStepException() {		
	}
	
	public NoPriceStepException(String msg) {
		super(msg);
	}
}