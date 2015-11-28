import java.util.concurrent.Callable;
import java.util.logging.Logger;


public class TThread<T> extends java.lang.Thread {

	private Runnable onAbort;
	private Runnable onCommit;
	private Callable<Boolean> onValidate;
	private AtomicObject<Node<T>> object;
	private static final Logger LOGGER = Logger.getLogger(TThread.class.getName());

	public TThread(AtomicObject<Node<T>> object) {
		this.object = object;
		onAbort = object.onAbort();
		onCommit = object.onCommit();
	}

	public <T> T doIt(Callable<T> xaction) throws Exception {
		T result = null;
		LOGGER.info("Do it function called with obj: " + object.internalInit.getItem());
		while (true) {
			Transaction me = new Transaction();
			Transaction.setLocal(me);
			onValidate = object.onValidate(me);
			ContentionManager contentionManager = new BackOffManager();
			ContentionManager.setLocal(contentionManager);
			try {
				result = xaction.call();
				LOGGER.info("XACTION call is made by :" + object.internalInit.getItem());
			} catch (AbortedException e) {
			} catch (Exception e) {
				throw new PanicException(e);
			}
			if (onValidate.call()) {
				LOGGER.info("OnValidate funcation true: " + object.internalInit.getItem());
				if (me.commit()) {
					onCommit.run();
					LOGGER.info("COMMITTED successful: " + object.internalInit.getItem());
					return result;
				}
			}
			me.abort();
			onAbort.run();
			LOGGER.info("Transaction ABORTED: " + object.internalInit.getItem());
		}
	}
}
