package STM.ContentionManagers;

import STM.Transaction;

import java.util.Random;
import java.util.logging.Logger;

public class BackOffManager extends ContentionManager {

	private static final int MIN_DELAY = 1;
	private static final int MAX_DELAY = 4 ;
	Random random = new Random();
	Transaction previous = null;
	int delay = MIN_DELAY;
	private static Logger LOGGER = Logger.getLogger(BackOffManager.class.getName());
	
	public void resolve(Transaction me, Transaction other) {
		//LOGGER.info("IN CONTENTION MANAGER............... YAAAAYY...!!!!!!!!!!");
		if (other != previous) {
			previous = other;
			delay = MIN_DELAY;
		}
		if (delay < MAX_DELAY) {
			try {
				Thread.sleep(random.nextInt(delay));
				delay *= 2;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			//LOGGER.info("IN CONTENTION MANAGER............... Sorry, ABORTING");
			other.abort();
			delay = MIN_DELAY;
		}
	}
}
