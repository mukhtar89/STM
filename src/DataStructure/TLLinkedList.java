package STM.DataStructure;

import STM.Exceptions.AbortedException;
import STM.Exceptions.PanicException;

import java.util.HashMap;
import java.util.logging.Logger;

public class TLLinkedList<T> {

    private TLNode<T> head;
    private TLNode<T> tail;
    //public HashMap<T, TLNode<T>> nodeMap;
    private static Logger LOGGER = Logger.getLogger(TLinkedList.class.getName());

    public TLLinkedList (T dummy_head, T dummy_tail) {
        head = new TLNode<>(dummy_head); //make the dummy_item value to null
        tail = new TLNode<>(dummy_tail);
        head.setNext(tail);
        tail.setPrev(head);
        //nodeMap = new HashMap<>();
    }

    public void add(T value) {
        //nodeMap.put(value, temp);
        TLNode<T> temp = new TLNode<>(value);
        LNode<T> last_node = head;

        while(last_node.getNext() != tail) {
            last_node = (last_node.getNext());
        }

        //Now insert/add at this location
        last_node.setNext(temp);
        temp.setNext(tail);
        temp.setPrev(last_node);
        tail.setPrev(temp);
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
            last_node = null;
        }
        return status;
    }

    public void printAll() {
        LNode<T> runner = head;
        while (runner.getNext() != null) {
            System.out.print(runner.getItem() + ",");
            runner = runner.getNext();
        }
    }
}
