import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
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
	
	public static void put(LockObject<?> x, Object y) {
		map.get().put(x, y);
	}
	
	public boolean tryLock(long timeout, TimeUnit timeUnit) throws AbortedException {
		Stack<LockObject<?>> stack = new Stack<LockObject<?>>();
		for (LockObject<?> x : map.get().keySet()) {
			if (!x.tryLock(timeout, timeUnit)) {
				for (LockObject<?> y : stack) {
					y.unlock();
				}
				//throw new AbortedException();
				return false;
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
	
	public static Set<Map.Entry<LockObject<?>, Object>>  getList() {
		return dataSet.entrySet();
	}

	public void clear() {
		dataSet.clear();
	}
}
