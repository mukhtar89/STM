import java.util.concurrent.Callable;


public class TThread<T> extends java.lang.Thread {

	private Runnable onAbort;
	private Runnable onCommit;
	private Callable<Boolean> onValidate;

	public TThread(AtomicObject<Node<T>> object) {
		onAbort = object.onAbort();
		onCommit = object.onCommit();
		onValidate = object.onValidate();
	}

	public <T> T doIt(Callable<T> xaction) throws Exception {
		T result = null;
		while (true) {
			Transaction me = new Transaction();
			Transaction.setLocal(me);
			try {
				result = xaction.call();
			} catch (AbortedException e) {
			} catch (Exception e) {
				throw new PanicException(e);
			}
			if (onValidate.call()) {
				if (me.commit()) {
					onCommit.run();
					return result;
				}
			}
			me.abort();
			onAbort.run();
		}
	}
}
