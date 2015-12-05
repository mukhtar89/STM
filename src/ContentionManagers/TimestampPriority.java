package STM.ContentionManagers;

import STM.Transaction;

import java.util.logging.Logger;

/**
 * Created by Mukhtar on 12/5/2015.
 */
public class TimestampPriority extends ContentionManager {

    private static Logger LOGGER = Logger.getLogger(TimestampPriority.class.getName());

    @Override
    public void resolve(Transaction me, Transaction other) {
        LOGGER.info("Me Start: " + me.timestampStart + ", Other Start: " + other.timestampStart);
        if (me.timestampStart.get() < other.timestampStart.get() && me.thread.getPriority() < other.thread.getPriority()) {
            LOGGER.info("Me: " + me.timestampStart + " < Other: " + other.timestampStart);
            me.abort();
        }
        else other.abort();
    }
}
