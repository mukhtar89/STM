package STM;

import STM.Atomic.TThread;
import STM.DataStructure.TLinkedList;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/* STM basic test */

public class Main {
	
	public static TLinkedList<Integer> linkedList = new TLinkedList<>();
	private static Logger LOGGER = Logger.getLogger(Main.class.getName());

	private static Integer NUM_THREADS = 100;
	
	public static class Produce<T> implements Callable<T> {
		
		private T value;
		
		public Produce(T value) {
			LOGGER.info("Adding DataStructure.Node with value : " + value);
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
			LOGGER.info("Deleting DataStructure.Node with value : " + value);
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
		ExecutorService executor = Executors.newFixedThreadPool(8);
        Random random = new Random();
        for (int i=0; i<NUM_THREADS; i++) {
        	int inserted = random.nextInt(i+1);
			executor.execute(new WorkerThread(new Produce<>(inserted)));
        	if (i > 5) {
				executor.execute(new WorkerThread(new Consume<>(random.nextInt(i-5))));
        	}
        }
		executor.shutdown();
		linkedList.printAll();
    }

	public static class WorkerThread implements Runnable {

		private Callable<Integer> action;

		public WorkerThread(Callable<Integer> action) {
			this.action = action;
		}

		@Override
		public void run() {
			try {
				new TThread().doIt(action);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
