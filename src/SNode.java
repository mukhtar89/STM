import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public class SNode<T> implements Node<T>, Copyable<SNode<T>> {

    AtomicReference<Node<T>> next;
    T item;

    public SNode(T myItem) {
        item = myItem;
        next = new AtomicReference<>(null);
    }

    @Override
    public T getItem() {
        return item;
    }

    @Override
    public void setItem(T value) {
        item = value;
    }

    @Override
    public Node<T> getNext() {
        return next.get();
    }

    @Override
    public void setNext(Node<T> value) {
        next.set(value);
    }

    @Override
    public void copyTo(SNode<T> target) {
        target.next = next;
        target.item = item;
    }
}
