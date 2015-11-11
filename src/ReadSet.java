import java.util.ArrayList;
import java.util.List;

public class ReadSet {

	static List<LockObject<?>> dataSet = new ArrayList<LockObject<?>>();
	static ThreadLocal<ReadSet> local = new ThreadLocal<ReadSet>() {
		protected ReadSet initialValue() {
			return new ReadSet();
		}
	};
	
	public static ReadSet getLocal() {
		return local.get();
	}
	
	public static void setLocal(ReadSet readSet) {
		local.set(readSet);
	}
	
	public void add(LockObject<?> lockObj) {
		local.get().dataSet.add(lockObj);
	}
	
	public List<LockObject<?>> getList() {
		return dataSet;
	}

	public void clear() {
		dataSet.clear();
	}
}
