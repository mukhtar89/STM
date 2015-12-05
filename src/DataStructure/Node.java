package STM.DataStructure;

import STM.Exceptions.AbortedException;
import STM.Exceptions.PanicException;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public interface Node<T>  {
    T getItem() throws AbortedException, PanicException;
    void setItem(T value) throws AbortedException, PanicException;
    Node<T> getNext() throws AbortedException, PanicException;
    void setNext(Node<T> value) throws AbortedException, PanicException;
}
