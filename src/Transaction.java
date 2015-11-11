import java.util.concurrent.atomic.AtomicReference;


public class Transaction {
	
	public enum Status {ABORTED, ACTIVE, COMMITTED};
	public static Transaction COMMITTED = new Transaction(Status.COMMITTED);
	private final AtomicReference<Status> status;
	static ThreadLocal<Transaction> local = new ThreadLocal<Transaction>() {
		protected Transaction initialValue() {
			return new Transaction(Status.COMMITTED);
		}
	};
	
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
		return status.compareAndSet(Status.ACTIVE, Status.ABORTED);
	}
	
	public static Transaction getLocal() {
		return local.get();
	}
	
	public static void setLocal(Transaction transaction) {
		local.set(transaction);
	}

}
