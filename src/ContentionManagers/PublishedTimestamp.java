package STM.ContentionManagers;

import STM.Transaction;
import STM.VersionClock;

import java.util.logging.Logger;

/**
 * Created by Mukhtar on 12/5/2015.
 */
public class PublishedTimestamp extends ContentionManager {

    private static Logger LOGGER = Logger.getLogger(PublishedTimestamp.class.getName());

    @Override
    public void resolve(Transaction me, Transaction other) {
        LOGGER.info("Me Start: " + me.recency.get() + ", Other Start: " + me.recency.get());
        if (me.recency.get() - VersionClock.getReadStamp() > Transaction.getLocal().threshold || me.recency.get() <= me.recency.get()) {
            LOGGER.info("Me: " + me.recency.get() + " < Other: " + me.recency.get());
            me.abort();
        }
        else if (other.recency.get() - VersionClock.getReadStamp() > Transaction.getLocal().threshold || me.recency.get() > me.recency.get()) {
            LOGGER.info("Me: " + me.recency.get() + " > Other: " + me.recency.get());
            me.abort();
        }
    }
}
