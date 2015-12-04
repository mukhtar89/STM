package STM.ContentionManagers;

import STM.Transaction;

/**
 * Created by Mukhtar on 12/2/2015.
 */
public class TimidManager extends ContentionManager {
    @Override
    public void resolve(Transaction me, Transaction other) {
        me.abort();
    }
}
