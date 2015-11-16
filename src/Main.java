<<<<<<< HEAD
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Callable;
=======
/* STM basic test */
>>>>>>> origin/master

public class Main {
	
	public static TLinkedList<Integer> linkedList = new TLinkedList<>();
	private static Integer NUM_THREADS = 100;
	
	public static class Produce<T> implements Callable<T> {
		
		private T value;
		
		public Produce(T val) {
			this.value = val;
		}

		@Override
		public T call() throws Exception {
			linkedList.add((Integer) value);
			return null;
		}
	}
	
	public static class Consume<T> implements Callable<T> {
		
		private T value;
		
		public Consume(T val) {
			this.value = val;
		}

		@Override
		public T call() throws Exception {
			linkedList.remove((Integer) value);
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
        	paction = new Produce<Integer>(inserted);
        	pro[i] = new TThread();
        	pro[i].doIt(paction);
        	produced.add(inserted);
        	if (produced.size() > 2) {
        		produced.toArray(producedArray);
        		caction = new Consume<Integer>(producedArray[random.nextInt(i)]);
        		con[i] = new TThread();
            	con[i].doIt(caction);
        	}
        	pro[i].join();
        	con[i].join();
        }
    }
}
