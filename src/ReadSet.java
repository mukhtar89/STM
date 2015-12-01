import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ReadSet {

	static List<LockObject<?>> dataSet = new ArrayList<>();
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
		LOGGER.info("Adding object to ReadSet");
		if (!dataSet.contains(lockObj))
			dataSet.add(lockObj);
	}

	public List<LockObject<?>> getList() {
		return dataSet;
	}

	public void clear() {
		dataSet.clear();
	}
}
