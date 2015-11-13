import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;


public class TThread<T> extends java.lang.Thread {

	Runnable onAbort;
	Runnable onCommit;
	Callable<Boolean> onValidate;
	
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
