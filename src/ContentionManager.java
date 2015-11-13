
public abstract class ContentionManager {

	static ThreadLocal<ContentionManager> local = new ThreadLocal<ContentionManager>() {
		protected ContentionManager initialValue() {
			ContentionManager manager = null;
			try {
				//TODO
				manager = ContentionManager.class.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return manager;
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
