package STM.DataStructure;

import STM.Atomic.AtomicObject;
import STM.Atomic.LockObject;
import STM.Exceptions.AbortedException;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public class TNode<T> implements Node<T> {

    AtomicObject<SNode<T>> atomic;
    
    public TNode(T myItem) {
        atomic = new LockObject<>(new SNode<>(myItem));
    }

    @Override
    public T getItem() {
        T item = null;
        try {
            item = atomic.openRead().getItem();
            if (!atomic.validate())
                throw new AbortedException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public void setItem(T value) {
        try {
            atomic.openWrite().setItem(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Node<T> getNext() {
        Node<T> retNode = null;
        try {
            retNode = atomic.openRead().getNext();
            if (!atomic.validate())
                throw new AbortedException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retNode;
    }


    @Override
    public void setNext(Node<T> value) {
        try {
            atomic.openWrite().setNext(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
