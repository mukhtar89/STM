import java.util.concurrent.Callable;
import java.util.logging.Logger;


public class TThread<T> extends java.lang.Thread {

	private Runnable onAbort;
	private Runnable onCommit;
	private Callable<Boolean> onValidate;
	private TNode<T> node;
	private static final Logger LOGGER = Logger.getLogger(TThread.class.getName());

	public TThread(TNode<T> node) {
		this.node = node;
		onValidate = node.atomic.onValidate();
		onAbort = node.atomic.onAbort();
		onCommit = node.atomic.onCommit();
	}

	public <T> T doIt(Callable<T> xaction) throws Exception {
		T result = null;
		LOGGER.info("Do it function called with obj: " + node.getItem());
		while (true) {
			Transaction me = new Transaction();
			Transaction.setLocal(me);
			ContentionManager.setLocal(new BackOffManager());
			try {
				result = xaction.call();
				LOGGER.info("XACTION call is made by :" + node.getItem());
			} catch (AbortedException e) {
			} catch (Exception e) {
				throw new PanicException(e);
			}
			if (onValidate.call()) {
				LOGGER.info("OnValidate funcation true: " + node.getItem());
				if (me.commit()) {
					onCommit.run();
					LOGGER.info("COMMITTED successful: " + node.getItem());
					return result;
				}
			}
			me.abort();
			onAbort.run();
			LOGGER.info("Transaction ABORTED: " + node.getItem());
		}
	}
}
