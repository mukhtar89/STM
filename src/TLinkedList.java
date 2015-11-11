
public class TLinkedList<T> {
	
	private Node<T> head;
	private Node<T> tail;
	
	public TLinkedList () {
		head = null;
		tail = null;
	}
	
	public void add(T value) {
		if (head == null) {
			head = new TNode<T>(value);
			tail = head;
		}
		else {
			Node<T> node = new TNode<T>(value);
			tail.getNext().set(node);
			tail = node;
		}
	}
	
	public boolean remove(T value) {
		if (head == tail)
			return false;
		Node<T> runner = head;
		while (runner.getNext().get() != null) {
			if (runner.getNext().get().getNext().get() == value) {
				runner.getNext().set(runner.getNext().get().getNext().get());
				return true;
			}
		}
		return false;
	}

}
