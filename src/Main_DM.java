package STM;

import STM.DataStructure.TLinkedList;
import STM.DataStructure.TransactionalLinkedList;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/* STM basic test */

public class Main_DM {
	
	public static TransactionalLinkedList<Integer> linkedList = new TransactionalLinkedList<>(Integer.MIN_VALUE, Integer.MAX_VALUE);
	//public static TLinkedList<Integer> linkedList = new TLinkedList<>();
	private static Logger LOGGER = Logger.getLogger(Main_DM.class.getName());

	public static class Produce<T> implements Callable<T> {
		
		private T value;
		
		public Produce(T value) {
			LOGGER.info("Adding DataStructure.Node with value : " + value);
			this.value = value;
		}

		public void setValue(T value) {
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

		public void setValue(T value) {
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
		TThread T1, T2, T3;

		Produce<Integer> p1 = new Produce<>(1);
		Consume<Integer> c1 = new Consume<>(1);

		T1 = new TThread();
		T1.doIt(p1);

		T2 = new TThread();
		T2.doIt(p1);

		p1.setValue(2);
		T3 = new TThread();
		T3.doIt(p1);

		c1.setValue(2);
		T1.doIt(c1);

		T2.doIt(p1);

		T1.join();
		T2.join();
		T3.join();

		linkedList.printAll();
    }

	/*
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
	*/
}
