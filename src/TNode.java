/**
 * Created by Mukhtar on 11/3/2015.
 */
public class TNode<T> implements Node<T> {

    AtomicObject<SNode<T>> atomic;

    public TNode(T myItem) {
        atomic = new LockObject<SNode<T>>(new SNode<>(myItem));
    }

    @Override
    public T getItem() {
        return null;
    }

    @Override
    public void setItem(T value) {

    }

    @Override
    public AtomicArray<Node<T>> getNext() {
        return null;
    }

    @Override
    public void setNext(AtomicArray<Node<T>> value) {

    }
}
