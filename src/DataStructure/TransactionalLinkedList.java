package STM.DataStructure;

import STM.Exceptions.AbortedException;
import STM.Exceptions.PanicException;

import java.util.HashMap;
import java.util.logging.Logger;

public class TransactionalLinkedList<T> {

    private TLNode<T> head;
    private TLNode<T> tail;
    private static Logger LOGGER = Logger.getLogger(TLinkedList.class.getName());

    public TransactionalLinkedList (T dummy_head, T dummy_tail) {
        head = new TLNode<>(dummy_head);
        tail = new TLNode<>(dummy_tail);
        head.setNext(tail);
        tail.setPrev(head);
    }

    public void add(T value) {
        TLNode<T> temp = new TLNode<>(value);
        LNode<T> last_node = tail.getPrev();

        //Now insert/add at this location
        last_node.setNext(temp);
        temp.setNext(tail);
        temp.setPrev(last_node);
        tail.setPrev(temp);
        LOGGER.info(Thread.currentThread().getName() + "..attempting add request with val:=  " + value + "!!");
    }

    public Boolean remove(T value) {
        LNode<T> last_node = head.getNext();
        Boolean status = false;

        while ( last_node != tail) {
            if(last_node.getItem() == value) {
                status = true;
                break;
            }
            else last_node =(last_node.getNext());
        }
        if(status) {
            LNode<T> before_last_node, after_last_node;
            before_last_node = last_node.getPrev();
            after_last_node = last_node.getNext();
            before_last_node.setNext(after_last_node);
            after_last_node.setPrev(before_last_node);
            LOGGER.severe(Thread.currentThread().getName() + "..Removal attempt successful!");
            //last_node = null;
        }
        else { LOGGER.severe(Thread.currentThread().getName() + "..Removal attempt failed.. :("); }
        return status;
    }

    public void printAll() {
        LNode<T> runner = head.getNext();
        while (runner.getNext() != tail) {
            LOGGER.severe((Integer) runner.getItem() + ", ");
            runner = runner.getNext();
        }
    }
}