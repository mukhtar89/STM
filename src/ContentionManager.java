
public abstract class ContentionManager {

	static ThreadLocal<ContentionManager> local = new ThreadLocal<ContentionManager>() {
		protected ContentionManager initialValue() throws Exception {
			try {
				return (ContentionManager) Defaults.
			} catch (Exception e) {
				throw new PanicException(e);
			}
		}
	};
	
	public abstract void resolve(Transaction me, Transaction other);
	
	public static ContentionManager getLocal() {
		return local.get();
	}
	
	public static void setLocal(ContentionManager m) {
		local.set(m);
	}
}
