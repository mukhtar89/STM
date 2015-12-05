package STM;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class Transaction {
	
	public enum Status {ABORTED, ACTIVE, COMMITTED};
	private final AtomicReference<Status> status;
	private AtomicInteger karma = new AtomicInteger(0);
	private AtomicInteger erupt = new AtomicInteger(0);
	public long threshold = 1L;
	static ThreadLocal<Transaction> local = new ThreadLocal<Transaction>() {
		protected Transaction initialValue() {
			return new Transaction(Status.COMMITTED);
		}
	};

	public AtomicLong timestampStart = new AtomicLong(VersionClock.getGlobalStamp());
	public AtomicLong recency = new AtomicLong(VersionClock.getGlobalStamp());
	public Thread thread = Thread.currentThread();
	
	public Transaction() {
		status = new AtomicReference<Status>(Status.ACTIVE);
	}
	
	private Transaction(Transaction.Status myStatus) {
		status = new AtomicReference<Status>(myStatus);
	}
	
	public Status getStatus() {
		return status.get();
	}
	
	public boolean commit() {
		return status.compareAndSet(Status.ACTIVE, Status.COMMITTED);
	}
	
	public boolean abort() {
		if (threshold < 2000000000)
			threshold++;
		else threshold = 0L;
		return status.compareAndSet(Status.ACTIVE, Status.ABORTED);
	}
	
	public static Transaction getLocal() {
		return local.get();
	}
	
	public static void setLocal(Transaction transaction) {
		local.set(transaction);
	}

	public Integer getKarma() {
		return karma.get();
	}

	public void incrementKarma() {
		karma.getAndIncrement();
	}

	public void clearKarma() {
		karma.set(0);
	}

	public int getErupt() {
		if (erupt.get() == 0)
			erupt.set(karma.get());
		return erupt.get();
	}

	public void setErupt(int value) {
		erupt.set(value);
	}

}
