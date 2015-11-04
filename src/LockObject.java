/**
 * Created by Mukhtar on 11/3/2015.
 */
public class LockObject<T extends Copyable<T>> extends AtomicObject<T> {

    public LockObject(T init) {
        super(init);
    }

    @Override
    public T openRead() {
        return null;
    }

    @Override
    public T openWrite() {
        return null;
    }

    @Override
    public boolean validate() {
        return false;
    }
}
