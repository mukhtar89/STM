package STM.Atomic;

import STM.ContentionManagers.ContentionManager;
import STM.Exceptions.AbortedException;
import STM.Exceptions.PanicException;
import STM.Transaction;
import STM.VersionClock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public class LockObject<T extends Copyable<T>> extends AtomicObject<T> {
	
	protected ReentrantLock lock;
	protected volatile long stamp;
	private T version;
	public Transaction locker;
	private static Logger LOGGER = Logger.getLogger(LockObject.class.getName());

    public LockObject(T init) {
        super(init);
		version = internalInit;
		lock = new ReentrantLock();
		stamp = 0L;
    }

    @SuppressWarnings("unchecked")
	@Override
    public T openRead() throws AbortedException, PanicException {
		Transaction.getLocal().incrementKarma();
		Transaction.getLocal().recency.set(VersionClock.getGlobalStamp());
        ReadSet readSet = ReadSet.getLocal();
        switch(Transaction.getLocal().getStatus()) {
        case COMMITTED:
			LOGGER.info("In Open Read COMMITTED");
        	return version;
        case ACTIVE:
			LOGGER.info("In Open Read ACTIVE");
        	WriteSet writeSet = WriteSet.getLocal();
        	if (writeSet.get(this) == null) {
        		if (lock.isLocked()) {
					/*ContentionManager.getLocal().resolve(Transaction.getLocal(), locker);
					Thread.yield();*/
					throw new AbortedException();
        		}
        		readSet.add(this);
				LOGGER.info("In Open Read ACTIVE with value: " + version.toString());
        		return version;
        	}
        	else {
				LOGGER.info("In Open Read ACTIVE with ELSE condition: " + writeSet.get(this).toString());
        		return (T) writeSet.get(this);
        	}
        case ABORTED:
			LOGGER.info("In Open Read ABORTED");
        	throw new AbortedException();
        default:
        	throw new PanicException("Unexpected Transaction state!");	
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public T openWrite() throws AbortedException, PanicException {
		Transaction.getLocal().incrementKarma();
		Transaction.getLocal().recency.set(VersionClock.getGlobalStamp());
    	switch(Transaction.getLocal().getStatus()) {
    	case COMMITTED:
			LOGGER.info("In Open Write COMMITTED");
    		return version;
    	case ACTIVE:
			LOGGER.info("In Open Write ACTIVE");
    		WriteSet writeSet = WriteSet.getLocal();
    		T scratch = (T) writeSet.get(this);
    		if (scratch == null) {
    			if (lock.isLocked()) {
					/*ContentionManager.getLocal().resolve(Transaction.getLocal(), locker);
					Thread.yield();*/
					throw new AbortedException();
				}
				try {
					LOGGER.info("NEW INSTANCE!!!!!!!");
					scratch = (T) internalClass.newInstance();
					version.copyTo(scratch);
					writeSet.put(this, scratch);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
    		return scratch;
    	case ABORTED:
			LOGGER.info("In Open Write ABORTED");
        	throw new AbortedException();
        default:
        	throw new PanicException("Unexpected Transaction state!");	
    	}
    }

    @Override
    public boolean validate() {
    	Transaction.Status status = Transaction.getLocal().getStatus();
    	switch(status) {
    	case COMMITTED:
    		return true;
    	case ACTIVE:
			LOGGER.info("Stamp: " + stamp + " & Version CLOCK: " + VersionClock.getReadStamp());
    		return stamp <= VersionClock.getReadStamp();
    	case ABORTED:
    		return false;
    	default:
    		return false;
    	}
    }

    
    public void lock() {
    	if (!lock.isLocked())
			lock.lock();
    }
    
	public void unlock(){
		if (lock.isLocked() && locker == Transaction.getLocal())
			lock.unlock();
	}
	
	public boolean tryLock(long timeout, TimeUnit timeUnit) {
		//TODO Definition of lock
		boolean retValue = false;
		try {
			retValue = lock.tryLock(timeout, timeUnit);
			if (retValue)
				locker = Transaction.getLocal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return retValue;
	}
}
