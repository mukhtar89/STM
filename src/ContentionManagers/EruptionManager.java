package STM.ContentionManagers;

import STM.Transaction;

/**
 * Created by Mukhtar on 12/5/2015.
 */
public class EruptionManager extends ContentionManager {

    @Override
    public void resolve(Transaction me, Transaction other) {
        if (me.getErupt() < other.getErupt()) {
            me.abort();
            me.setErupt(me.getErupt() + other.getErupt());
        }
        else {
            other.abort();
            other.setErupt(me.getErupt() + other.getErupt());
        }
    }
}
