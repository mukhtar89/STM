package STM.ContentionManagers;

import STM.Transaction;

import java.util.logging.Logger;

/**
 * Created by Mukhtar on 12/2/2015.
 */
public class TimestampManager extends ContentionManager {

    private static Logger LOGGER = Logger.getLogger(TimestampManager.class.getName());

    @Override
    public void resolve(Transaction me, Transaction other) {
        LOGGER.info("Me Start: " + me.timestampStart + ", Other Start: " + other.timestampStart);
        if (me.timestampStart < other.timestampStart) {
            LOGGER.info("Me: " + me.timestampStart + " < Other: " + other.timestampStart);
            me.abort();
        }
        else other.abort();
    }
}
