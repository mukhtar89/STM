import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class TLinkedList<T> {
	
	private AtomicReference<Node<T>> head;
	private AtomicReference<Node<T>> tail;
	public HashMap<T, Node<T>> nodeMap;
	
	public TLinkedList () {
		head = new AtomicReference<>(null);
		tail = new AtomicReference<>(null);
		nodeMap = new HashMap<>();
	}
	
	public void add(Node<T> node) {
		nodeMap.put(node.getItem(), node);
		if (head == null) {
			head.set(node);
			tail.set(node);
		}
		else {
			tail.get().setNext(node);
			tail.set(node);
		}
	}
	
	public boolean remove(Node<T> node) {
		if (head == tail)
			return false;
		Node<T> runner = head.get();
		if (head.get().getItem() == node.getItem()) {
			head.get().setNext(head.get().getNext());
			return true;
		}
		while (runner.getNext() != null) {
			if (runner.getNext() == node.getItem()) {
				runner.getNext().setNext(runner.getNext().getNext());
				return true;
			}
		}
		return false;
	}
}
