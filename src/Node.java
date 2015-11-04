/**
 * Created by Mukhtar on 11/3/2015.
 */
public interface Node<T> {
    T getItem();
    void setItem(T value);
    AtomicArray<Node<T>> getNext();
    void setNext(AtomicArray<Node<T>> value);
}
