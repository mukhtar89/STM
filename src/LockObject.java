import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public class LockObject<T extends Copyable<T>> extends AtomicObject<T> {
	
	protected ReentrantLock lock;
	protected volatile long stamp;
	private volatile T version;
	protected Transaction creator;
	private static Logger LOGGER = Logger.getLogger(LockObject.class.getName());

    public LockObject(T init) {
        super(init);
		version = internalInit;
		lock = new ReentrantLock();
		creator = Transaction.getLocal();
    }

    @SuppressWarnings("unchecked")
	@Override
    public T openRead() throws AbortedException, PanicException {
		Transaction me = Transaction.getLocal();
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
					ContentionManager.getLocal().resolve(me, creator);
					Thread.yield();
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
    	switch(Transaction.getLocal().getStatus()) {
    	case COMMITTED:
			LOGGER.info("In Open Write COMMITTED");
    		return version;
    	case ACTIVE:
			LOGGER.info("In Open Write ACTIVE");
    		WriteSet writeSet = WriteSet.getLocal();
    		T scratch = (T) writeSet.get(this);
    		if (scratch == null) {
    			if (lock.isLocked()) 
    				throw new AbortedException();
				//try {
					//scratch = (T) internalClass.getDeclaredConstructor().newInstance(version);
					scratch = (T) new SNode<>(internalInit);
					version.copyTo(scratch);
					WriteSet.put(this, scratch);
				/*} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
					e.printStackTrace();
				}*/
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
		if (lock.isLocked() && creator == Transaction.getLocal())
			lock.unlock();
	}
	
	public boolean tryLock(long timeout, TimeUnit timeUnit) {
		//TODO Definition of lock
		boolean retValue = false;
		try {
			retValue = lock.tryLock(timeout, timeUnit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return retValue;
	}
}
