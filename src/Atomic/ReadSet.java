package STM.Atomic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ReadSet {

	private List<LockObject<?>> list = new ArrayList<>();
	static ThreadLocal<ReadSet> local = new ThreadLocal<ReadSet>() {
		protected ReadSet initialValue() {
			return new ReadSet();
		}
	};
	private static final Logger LOGGER = Logger.getLogger(ReadSet.class.getName());
	
	public static ReadSet getLocal() {
		return local.get();
	}
	
	public static void setLocal(ReadSet readSet) {
		local.set(readSet);
	}
	
	public void add(LockObject<?> lockObj) {
		LOGGER.info("Adding object to Atomic.ReadSet");
		if (!list.contains(lockObj))
			list.add(lockObj);
	}

	public List<LockObject<?>> getList() {
		return list;
	}

	public void clear() {
		list.clear();
	}
}
