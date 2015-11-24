import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public class LockObject<T extends Copyable<T>> extends AtomicObject<T> {
	
	ReentrantLock lock;
	volatile long stamp;
	T version;

    public LockObject(T init) {
        super(init);
		lock = new ReentrantLock();
    }

    @SuppressWarnings("unchecked")
	@Override
    public T openRead() throws AbortedException, PanicException {
        ReadSet readSet = ReadSet.getLocal();
        switch(Transaction.getLocal().getStatus()) {
        case COMMITTED:
        	return version;
        case ACTIVE:
        	WriteSet writeSet = WriteSet.getLocal();
        	if (writeSet.get(this) == null) {
        		if (lock.isLocked()) {
        			throw new AbortedException();
        		}
        		readSet.add(this);
        		return version;
        	}
        	else {
        		return (T) writeSet.get(this);
        	}
        case ABORTED:
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
    		return version;
    	case ACTIVE:
    		WriteSet writeSet = WriteSet.getLocal();
    		T scratch = (T) writeSet.get(this);
    		if (scratch == null) {
    			if (lock.isLocked()) 
    				throw new AbortedException();
    			scratch = new LockObject<T>(null).internalInit;
    			version.copyTo(scratch);
    			WriteSet.put(this, scratch);
    		}
    		return scratch;
    	case ABORTED:
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
    
    public Callable<Boolean> onValidate() {

		final long TIMEOUT = 0;
		return new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				WriteSet writeSet = WriteSet.getLocal();
				ReadSet readSet = ReadSet.getLocal();
				if (!writeSet.tryLock(TIMEOUT, TimeUnit.MILLISECONDS)) {
					return false;
				}
				for (LockObject<?> x : readSet.getList()) {
					if (x.lock.isLocked() && !x.lock.isHeldByCurrentThread())
						return false;
					if (stamp > VersionClock.getReadStamp())
						return false;
				}
				return true;
			}
		};
    }

	@Override
	public Runnable onCommit() {
		return new Runnable() {
			@Override
			public void run() {
				try {
					WriteSet writeSet = WriteSet.getLocal();
					ReadSet readSet = ReadSet.getLocal();
					VersionClock.setWriteStamp();
					long writeVersion = VersionClock.getWriteStamp();
					for (Map.Entry<LockObject<?>,Object> entry : WriteSet.getList()) {
						LockObject<?> key = entry.getKey();
						Copyable<?> destination = key.openRead();
						Copyable<Copyable<?>> source = (Copyable<Copyable<?>>) entry.getValue();
						source.copyTo(destination);
						key.stamp = writeVersion;
					}
					writeSet.unlock();
					writeSet.clear();
					readSet.clear();
				} catch (AbortedException | PanicException e) {
					e.printStackTrace();
				}
			}
		};
	}

	@Override
	public Runnable onAbort() {
		return new Runnable() {
			@Override
			public void run() {
				while(!Transaction.getLocal().abort());
			}
		};
	}
    
    public void lock() {
    	//TODO Definition of lock
    }
    
	public void unlock(){
		//TODO Definition of lock
	}
	
	public boolean tryLock(long timeout, TimeUnit timeUnit) {
		//TODO Definition of lock
		return false;
	}
	
	public boolean tryUnlock(long timeout, TimeUnit timeUnit) {
		//TODO Definition of lock
		return false;
	}
}
