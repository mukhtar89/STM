package STM.DataStructure;

import STM.Atomic.Copyable;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public class SLNode<T> implements LNode<T>, Copyable<SLNode<T>> {

    LNode<T> next, prev;
    T item;

    public SLNode(){};

    public SLNode(T myItem) {
        item = (myItem);
        next = null;
        prev = null;
    }

    @Override
    public T getItem() {
        return item;
    }

    @Override
    public void setItem(T value) {
        item = (value);
    }

    @Override
    public LNode<T> getNext() {
        return next;
    }

    @Override
    public LNode<T> getPrev() {
        return prev;
    }

    @Override
    public void setNext(LNode<T> value) {
        next = (value);
    }

    @Override
    public void setPrev(LNode<T> value) {
        prev = (value);
    }

    @Override
    public void copyTo(SLNode<T> target) {
        target.setItem(item);
        target.setNext(next);
        target.setPrev(prev);
    }
}
