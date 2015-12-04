package STM;

import java.util.concurrent.atomic.AtomicLong;

public class VersionClock {
	
	static AtomicLong global = new AtomicLong();
	static ThreadLocal<Long> local = new ThreadLocal<Long>() {
		protected Long initialValue() {
			return 0L;
		}
	};
	
	public static void setReadStamp() {
		local.set(global.get());
	}
	
	public static long getReadStamp() {
		return local.get();
	}
	
	public static void setWriteStamp() {
		local.set(global.incrementAndGet());
	}
	
	public static long getWriteStamp() {
		return local.get();
	}

	public static long getGlobalStamp() {
		return global.get();
	}
}
