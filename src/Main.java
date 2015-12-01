
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/* STM basic test */

public class Main {
	
	public static TLinkedList<Integer> linkedList = new TLinkedList<>();
	private static Logger LOGGER = Logger.getLogger(Main.class.getName());
	private static LoggerFwk logfwk;

	private static Integer NUM_THREADS = 10;
	
	public static class Produce<T> implements Callable<T> {
		
		private T value;
		
		public Produce(T value) {
			LOGGER.info("Adding Node with value : " + value);
			this.value = value;
		}

		@Override
		public T call() throws Exception {
			linkedList.add((Integer) value);
			LOGGER.info("Calling Produce Callable: " + value);
			return null;
		}
	}
	
	public static class Consume<T> implements Callable<T> {

		private T value;

		public Consume(T value) {
			LOGGER.info("Deleting Node with value : " + value);
			this.value = value;
		}

		@Override
		public T call() throws Exception {
			linkedList.remove((Integer) value);
			LOGGER.info("Calling Consume Callable: " + value);
			return null;
		}
	}

    public static void main(String[] args) throws Exception {
        TThread[] pro = new TThread[NUM_THREADS], con = new TThread[NUM_THREADS];
        HashSet<Integer> produced = new HashSet<>();
        Integer[] producedArray = new Integer[NUM_THREADS];
		logfwk = new LoggerFwk();
		LOGGER = logfwk.logHandler(LOGGER);
        Random random = new Random();
        Callable<Integer> paction, caction;
        for (int i=0; i<NUM_THREADS; i++) {
        	int inserted = random.nextInt(i+1);
			TNode<Integer> temp = new TNode<>(inserted);
        	paction = new Produce<>(inserted);
        	pro[i] = new TThread();
        	pro[i].doIt(paction);
        	produced.add(inserted);
        	if (produced.size() > 2) {
        		produced.toArray(producedArray);
				int toRemove = producedArray[random.nextInt(i)];
				temp = (TNode<Integer>) linkedList.nodeMap.get(toRemove);
        		caction = new Consume<>(toRemove);
        		con[i] = new TThread();
            	con[i].doIt(caction);
        	}
        	pro[i].join();
			if (produced.size() > 2)
				con[i].join();
        }
    }
}
