package STM.Atomic;

import STM.ContentionManagers.ContentionManager;
import STM.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WriteSet {

	private Map<LockObject<?>, Object> map = new HashMap<>();
	static ThreadLocal<WriteSet> local = new ThreadLocal<WriteSet>() {
		protected WriteSet initialValue() {
			return new WriteSet();
		}
	};

	public Object get(LockObject<?> x) {
		return map.get(x);
	}

	public void put(LockObject<?> x, Object y) {
		map.put(x, y);
	}

	public boolean tryLock(long timeout, TimeUnit timeUnit) {
		for (LockObject<?> x : map.keySet()) {
			while (!x.tryLock(timeout, timeUnit)) {
				ContentionManager.getLocal().resolve(Transaction.getLocal(), x.locker);
				Thread.yield();
			}
		}
		return true;
	}

	public void unlock() {
		for (LockObject<?> x : map.keySet()) {
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
		return map.entrySet();
	}

	public void clear() {
		map.clear();
	}
}
