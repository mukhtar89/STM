package STM.Atomic;

import STM.ContentionManagers.*;
import STM.Exceptions.AbortedException;
import STM.Exceptions.PanicException;
import STM.Transaction;
import STM.VersionClock;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class TThread extends Thread {

	private final int TIMEOUT = 10;
	private static final Logger LOGGER = Logger.getLogger(TThread.class.getName());

	public <T> T doIt(Callable<T> xaction) throws Exception {
		T result = null;
		LOGGER.info("Do it function called");
		while (true) {
			Transaction me = new Transaction();
			Transaction.setLocal(me);
			ContentionManager.setLocal(new PublishedTimestamp());
			try {
				result = xaction.call();
				LOGGER.info("XACTION call is made");
			} catch (AbortedException | PanicException e) {
				me.abort();
				onAbort.run();
				LOGGER.info("Transaction ABORTED from EXCEPTION");
				continue;
			}
			if (onValidate.call()) {
				LOGGER.info("OnValidate function ");
				if (me.commit()) {
					onCommit.run();
					LOGGER.info("COMMITTED successful" );
					return result;
				}
			}
			me.abort();
			onAbort.run();
			LOGGER.info("Transaction ABORTED");
		}
	}

	private Runnable onAbort = new Runnable() {
		@Override
		public void run() {
			LOGGER.info("Transaction Aborting from Atomic.LockObject");
			WriteSet.getLocal().unlock();
			WriteSet.getLocal().clear();
			ReadSet.getLocal().clear();
		}
	};

	private Runnable onCommit = new Runnable() {
		@Override
		public void run() {
			try {
				WriteSet writeSet = WriteSet.getLocal();
				ReadSet readSet = ReadSet.getLocal();
				VersionClock.setWriteStamp();
				long writeVersion = VersionClock.getWriteStamp();
				for (Map.Entry<LockObject<?>,Object> entry : writeSet.getList()) {
					LockObject<?> key = entry.getKey();
					Copyable<?> destination = key.openRead();
					Copyable<Copyable<?>> source = (Copyable<Copyable<?>>) entry.getValue();
					source.copyTo(destination);
					LOGGER.info("WRITING OBJECT VALUE");
					key.stamp = writeVersion;
					Transaction.getLocal().clearKarma();
				}
				writeSet.unlock();
				writeSet.clear();
				readSet.clear();
			} catch (AbortedException | PanicException e) {
				e.printStackTrace();
			}
		}
	};

	private Callable<Boolean> onValidate = new Callable<Boolean>() {
		@Override
		public Boolean call() throws Exception {
			WriteSet writeSet = WriteSet.getLocal();
			ReadSet readSet = ReadSet.getLocal();
			if (!writeSet.tryLock(TIMEOUT, TimeUnit.MILLISECONDS)) {
				LOGGER.info("Atomic.WriteSet Lock TIMEOUT");
				return false;
			}
			for (LockObject<?> x : readSet.getList()) {
				if (x.lock.isLocked() && !x.lock.isHeldByCurrentThread()) {
					LOGGER.info("Object locked and held, ContentionManagers.ContentionManager called");
					/*ContentionManager.getLocal().resolve(Transaction.getLocal(), x.locker);
					Thread.yield();*/
					return false;
				}
				if (x.stamp > VersionClock.getReadStamp()) {
					LOGGER.info("Stamp: " + x.stamp + " > Version CLOCK: " + VersionClock.getReadStamp());
					return false;
				}
			}
			return true;
		}
	};

}
