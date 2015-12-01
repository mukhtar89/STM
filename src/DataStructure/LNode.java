package STM.DataStructure;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public interface LNode<T>  {
    T getItem();
    void setItem(T value);
    LNode<T> getNext();
    LNode<T> getPrev();
    void setNext(LNode<T> value);
    void setPrev(LNode<T> value);
}
