package STM.Atomic;

import STM.Exceptions.AbortedException;
import STM.Exceptions.PanicException;

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
    public abstract T openRead() throws AbortedException, PanicException;
    public abstract T openWrite() throws AbortedException, PanicException;
    public abstract boolean validate();
}
