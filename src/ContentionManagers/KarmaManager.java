package STM.ContentionManagers;

import STM.Transaction;

import java.util.logging.Logger;

/**
 * Created by Mukhtar on 12/4/2015.
 */
public class KarmaManager extends ContentionManager {

    private static Logger LOGGER = Logger.getLogger(KarmaManager.class.getName());

    @Override
    public void resolve(Transaction me, Transaction other) {
        LOGGER.info("Me: " + me.getFinished() + ", " + other.getFinished());
        if (me.getFinished() < other.getFinished())
            me.abort();
        else other.abort();
    }
}
