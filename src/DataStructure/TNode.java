package STM.DataStructure;

import STM.Atomic.AtomicObject;
import STM.Atomic.LockObject;
import STM.Exceptions.AbortedException;
import STM.Exceptions.PanicException;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public class TNode<T> implements Node<T> {

    AtomicObject<SNode<T>> atomic;
    
    public TNode(T myItem) {
        atomic = new LockObject<>(new SNode<>(myItem));
    }

    @Override
    public T getItem() throws AbortedException, PanicException {
        T item = atomic.openRead().getItem();
        if (!atomic.validate())
            throw new AbortedException();
        return item;
    }

    @Override
    public void setItem(T value) throws AbortedException, PanicException {
        atomic.openWrite().setItem(value);
    }

    @Override
    public Node<T> getNext() throws AbortedException, PanicException {
        Node<T> retNode = atomic.openRead().getNext();
        if (!atomic.validate())
            throw new AbortedException();
        return retNode;
    }


    @Override
    public void setNext(Node<T> value) throws AbortedException, PanicException {
        atomic.openWrite().setNext(value);
    }
}
