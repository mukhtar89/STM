package STM.Exceptions;

public class PanicException extends Exception {

	public PanicException (String message) {
		super(message);
	}
	
	public PanicException (String message, Throwable t) {
		super(message, t);
	}
	
	public PanicException (Throwable t) {
		super(t);
	}
}
