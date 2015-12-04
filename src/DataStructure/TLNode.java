package STM.DataStructure;

import STM.Atomic.AtomicObject;
import STM.Atomic.LockObject;
import STM.Exceptions.AbortedException;
import java.util.logging.Logger;

/**
 * Created by Mukhtar on 11/3/2015.
 */
public class TLNode<T> implements LNode<T> {
    AtomicObject<SLNode<T>> atomic;
    public TLNode() {}
    public TLNode(T myItem) {
        atomic = new LockObject<>(new SLNode<>(myItem));
    }
    private static Logger LOGGER = Logger.getLogger(TLNode.class.getName());

    @Override
    public T getItem() {
        T item = null;
        try {
            item = atomic.openRead().getItem();
        }
        catch (AbortedException e) {
            LOGGER.info("getItem openRead throwing AbortedException");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }
    @Override
    public void setItem(T value) {
        try {
            atomic.openWrite().setItem(value);
        }
        catch (AbortedException e) {
            LOGGER.info("setItem openWrite throwing AbortedException");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public LNode<T> getNext() {
        LNode<T> retNode = null;
        try {
            retNode = atomic.openRead().getNext();
            if (!atomic.validate())
                throw new AbortedException();
        }
        catch (AbortedException e) {
            LOGGER.info("getNext openRead throwing AbortedException");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return retNode;
    }
    @Override
    public LNode<T> getPrev() {
        LNode<T> retNode = null;
        try {
            retNode = atomic.openRead().getPrev();
            if (!atomic.validate())
                throw new AbortedException();
        }
        catch (AbortedException e) {
            LOGGER.info("getPrev openRead throwing AbortedException");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return retNode;
    }
    @Override
    public void setNext(LNode<T> value) {
        try {
            atomic.openWrite().setNext(value);
        }
        catch (AbortedException e) {
            LOGGER.info("setNext openWrite throwing AbortedException");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setPrev(LNode<T> value) {
        try {
            atomic.openWrite().setPrev(value);
        }
        catch (AbortedException e) {
            LOGGER.info("setPrev openWrite throwing AbortedException");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*    //latest additions..
    public SNode<T> get() {
        SNode<T> temp = null;
        try {
            temp = atomic.openRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public void set(SNode<T> temp) {
        try {
            atomic.openWrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}