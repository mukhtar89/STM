package STM.DataStructure;

import java.util.HashMap;
import java.util.logging.Logger;

public class TLinkedList<T> {
	
	private Node<T> head;
	private Node<T> tail;
	public HashMap<T, Node<T>> nodeMap;
	private static Logger LOGGER = Logger.getLogger(TLinkedList.class.getName());
	
	public TLinkedList () {
		head = null;
		tail = null;
		nodeMap = new HashMap<>();
	}
	
	public void add(T value) {
		Node<T> temp = new TNode<>(value);
		nodeMap.put(value, temp);
		if (head == null) {
			head = temp;
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
			LOGGER.severe("inside RUNNER*****************************************************: " + runner.getItem());
			if (runner.getNext().getItem() == value) {
				runner.getNext().setNext(runner.getNext().getNext());
				return true;
			}
			runner = runner.getNext();
		}
		return false;
	}

	public void printAll() {
		Node<T> runner = head;
		while (runner.getNext() != null) {
			System.out.print(runner.getItem() + ",");
			runner = runner.getNext();
		}
	}
}
