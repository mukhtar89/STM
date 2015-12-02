package STM.Atomic;

import STM.ContentionManagers.ContentionManager;
import STM.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WriteSet {
	
	static Map<LockObject<?>, Object> dataSet = new HashMap<LockObject<?>, Object>();
	static ThreadLocal<Map<LockObject<?>, Object>> map = new ThreadLocal<Map<LockObject<?>, Object>>() {
		protected synchronized Map<LockObject<?>, Object> initialValue() {
			return new HashMap<>();
		}
	};
	static ThreadLocal<WriteSet> local = new ThreadLocal<WriteSet>() {
		protected WriteSet initialValue() {
			return new WriteSet();
		}
	};
	
	public Object get(LockObject<?> x) {
		return map.get().get(x);
	}
	
	public void put(LockObject<?> x, Object y) {
		map.get().put(x, y);
	}
	public boolean tryLock(long timeout, TimeUnit timeUnit) {
		for (LockObject<?> x : map.get().keySet()) {
			while (!x.tryLock(timeout, timeUnit)) {
				ContentionManager.getLocal().resolve(Transaction.getLocal(), x.creator);
				Thread.yield();
			}
		}
		return true;
	}
	public void unlock() {
		for (LockObject<?> x : map.get().keySet()) {
			x.unlock();
		}
	}
	
	public static WriteSet getLocal() {
		return local.get();
	}
	
	public static void setLocal(WriteSet writeSet) {
		local.set(writeSet);
	}
	
	public Set<Map.Entry<LockObject<?>, Object>>  getList() {
		return dataSet.entrySet();
	}

	public void clear() {
		dataSet.clear();
	}
}
