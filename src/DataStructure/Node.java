package STM.DataStructure;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public interface Node<T>  {
    T getItem();
    void setItem(T value);
    Node<T> getNext();
    void setNext(Node<T> value);
}
