
public class AbortedException extends Exception {

	public AbortedException (String message) {
		super(message);
	}
	
	public AbortedException (String message, Throwable t) {
		super(message, t);
	}
	
	public AbortedException (Throwable t) {
		super(t);
	}
	
	public AbortedException() {
		super();
	}
}
