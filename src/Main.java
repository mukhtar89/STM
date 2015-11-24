
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Callable;

/* STM basic test */

public class Main {
	
	public static TLinkedList<Integer> linkedList = new TLinkedList<>();
	private static Integer NUM_THREADS = 100;
	
	public static class Produce<T> implements Callable<T> {
		
		private Node<T> node;
		
		public Produce(Node<T> node) {
			this.node = node;
		}

		@Override
		public T call() throws Exception {
			linkedList.add((Node<Integer>) node);
			return null;
		}
	}
	
	public static class Consume<T> implements Callable<T> {

		private Node<T> node;

		public Consume(Node<T> node) {
			this.node = node;
		}

		@Override
		public T call() throws Exception {
			linkedList.remove((Node<Integer>) node);
			return null;
		}
	}

    public static void main(String[] args) throws Exception {
        TThread[] pro = new TThread[NUM_THREADS], con = new TThread[NUM_THREADS];
        HashSet<Integer> produced = new HashSet<>();
        Integer[] producedArray = new Integer[NUM_THREADS];
        Random random = new Random();
        Callable<Integer> paction = null, caction = null;
        for (int i=0; i<NUM_THREADS; i++) {
        	int inserted = random.nextInt(i+1);
			TNode<Integer> temp = new TNode<>(inserted);
        	paction = new Produce<>(temp);
        	pro[i] = new TThread(temp.atomic);
        	pro[i].doIt(paction);
        	produced.add(inserted);
        	if (produced.size() > 2) {
        		produced.toArray(producedArray);
				int toRemove = producedArray[random.nextInt(i)];
				temp = (TNode<Integer>) linkedList.nodeMap.get(toRemove);
        		caction = new Consume<Integer>(temp);
        		con[i] = new TThread(temp.atomic);
            	con[i].doIt(caction);
        	}
        	pro[i].join();
        	con[i].join();
        }
    }
}
