/**
 * Created by Mukhtar on 11/3/2015.
 */
public class SNode<T> implements Node<T>, Copyable<SNode<T>> {

    AtomicArray<Node<T>> next;
    T item;

    public SNode() {}
    public SNode(T myItem) {
        item = myItem;
    }

    @Override
    public T getItem() {
        return item;
    }

    @Override
    public void setItem(T value) { item = value;  }

    @Override
    public AtomicArray<Node<T>> getNext() {
        return next;
    }

    @Override
    public void setNext(AtomicArray<Node<T>> value) { next = value;   }

    @Override
    public void copyTo(SNode<T> target) {
        target.next = next;
        target.item = item;
    }
}
