import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class TLinkedList<T> {
	
	private AtomicReference<Node<T>> head;
	private AtomicReference<Node<T>> tail;
	public HashMap<T, Node<T>> nodeMap;
	
	public TLinkedList () {
		head = null;
		tail = null;
		nodeMap = new HashMap<>();
	}
	
	public void add(Node<T> value) {
		nodeMap.put(value.getItem(), value);
		if (head == null) {
			head.set(value);
			tail = head;
		}
		else {
			tail.get().getNext().set(value);
			tail.set(value);
		}
	}
	
	public boolean remove(Node<T> value) {
		if (head == tail)
			return false;
		AtomicReference<Node<T>> runner = head;
		if (head.get().getItem() == value.getItem()) {
			head.get().setNext(head.get().getNext());
			return true;
		}
		while (runner.get().getNext().get() != null) {
			if (runner.get().getNext().get() == value.getItem()) {
				runner.get().getNext().set(runner.get().getNext().get().getNext().get());
				return true;
			}
		}
		return false;
	}

}
