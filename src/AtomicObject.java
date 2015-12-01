import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.concurrent.Callable;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public abstract class AtomicObject<T extends Copyable<T>> {
    public Class internalClass;
    public  T internalInit;
    public AtomicObject(T init) {
        internalInit = init;
        internalClass = init.getClass();
    }
    public abstract T openRead() throws Exception;
    public abstract T openWrite() throws Exception;
    public abstract boolean validate();
    public abstract Callable<Boolean> onValidate();
    public abstract Runnable onCommit();
    public abstract Runnable onAbort();
}
