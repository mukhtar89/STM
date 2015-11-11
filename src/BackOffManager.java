import java.util.Random;

public class BackOffManager extends ContentionManager {

	private static final int MIN_DELAY = Integer.MIN_VALUE;
	private static final int MAX_DELAY = Integer.MAX_VALUE;
	Random random = new Random();
	Transaction previous = null;
	int delay = MIN_DELAY;
	
	public void resolve(Transaction me, Transaction other) {
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
			other.abort();
			delay = MIN_DELAY;
		}
	}
}
