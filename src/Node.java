import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public interface Node<T>  {
    T getItem();
    void setItem(T value);
    AtomicReference<Node<T>> getNext();
    void setNext(AtomicReference<Node<T>> value);
}
