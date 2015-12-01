import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class TLinkedList<T> {
	
	private Node<T> head;
	private Node<T> tail;
	public HashMap<T, Node<T>> nodeMap;
	
	public TLinkedList () {
		head = new TNode<>(null);
		tail = new TNode<>(null);
		nodeMap = new HashMap<>();
	}
	
	public void add(T value) {
		Node<T> temp = new TNode<>(value);
		nodeMap.put(value, temp);
		if (head == null) {
			head
			tail = head;
		}
		else {
			tail.setNext(temp);
			tail = temp;
		}
	}
	
	public boolean remove(T value) {
		if (head == tail)
			return false;
		Node<T> runner = head;
		if (head.getItem() == value) {
			head.setNext(head.getNext());
			return true;
		}
		while (runner.getNext() != null) {
			if (runner.getNext() == value) {
				runner.getNext().setNext(runner.getNext().getNext());
				return true;
			}
		}
		return false;
	}
}
