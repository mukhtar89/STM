package STM.DataStructure;

import java.util.HashMap;
import java.util.logging.Logger;

public class TLinkedList<T> {
	
	private Node<T> head;
	public HashMap<T, Node<T>> nodeMap;
	private static Logger LOGGER = Logger.getLogger(TLinkedList.class.getName());
	
	public TLinkedList () {
		head = new TNode<>(null);
		nodeMap = new HashMap<>();
	}
	
	public void add(T value) {
		Node<T> temp = new TNode<>(value);
		nodeMap.put(value, temp);
		if (head.getNext() == null)
			head.setNext(temp);
		else {
			Node<T> runner = head;
			while (runner.getNext() != null)
				runner = runner.getNext();
			runner.setNext(temp);
		}
	}
	
	public boolean remove(T value) {
		if (head.getNext() == null)
			return false;
		Node<T> runner = head;
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
		LOGGER.info("NOW PRINTING.......................................!!!!!!!");
		Node<T> runner = head;
		while (runner.getNext() != null) {
			System.out.print(runner.getItem() + ",");
			runner = runner.getNext();
		}
	}
}
