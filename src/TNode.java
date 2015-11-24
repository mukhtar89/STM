import java.util.concurrent.atomic.AtomicReference;

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
    public AtomicReference<Node<T>> getNext() {
        AtomicReference<Node<T>> next = null;
        try {
            next =  atomic.openRead().getNext();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return next;
    }

    @Override
    public void setNext(AtomicReference<Node<T>> value) {
        try {
            atomic.openWrite().setNext(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
