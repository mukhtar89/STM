package STM.ContentionManagers;

import STM.Transaction;

import java.util.Random;
import java.util.logging.Logger;

public class PoliteManager extends ContentionManager {

    private static final int MIN_DELAY = 1;
    private static final int MAX_DELAY = 400;
    Transaction previous = null;
    private int delay = MIN_DELAY;
    private int iter = 1;
    private static Logger LOGGER = Logger.getLogger(PoliteManager.class.getName());

    public void resolve(Transaction me, Transaction other) {
        //LOGGER.info("IN CONTENTION MANAGER............... YAAAAYY...!!!!!!!!!!");
        if (other != previous) {
            LOGGER.info(me.toString() + "............... Other != previous..!!!!!!!!!!");
            previous = other;
            delay = MIN_DELAY;
        }
        if (delay < MAX_DELAY && iter < 22) {
            try {
                Thread.sleep(delay);
                delay *= (int) Math.exp(iter++);
                LOGGER.info(me.toString() + "............... Delay < MAX && iter=" + iter + "..!!!!!!!!!!");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            LOGGER.info(me.toString() + "............... Sorry, ABORTING");
            other.abort();
            iter = 1;
            delay = MIN_DELAY;
        }
    }
}
