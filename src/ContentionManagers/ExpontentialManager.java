package STM.ContentionManagers;

import STM.Transaction;

/**
 * Created by Mukhtar on 12/1/2015.
 */
public class ExpontentialManager extends ContentionManager {
    @Override
    public void resolve(Transaction me, Transaction other) {
        other.abort();
    }
}
