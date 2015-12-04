package STM.ContentionManagers;

import STM.Transaction;

import java.util.Random;

/**
 * Created by Mukhtar on 12/2/2015.
 */
public class RandomManager extends ContentionManager {
    @Override
    public void resolve(Transaction me, Transaction other) {
        Random random = new Random();
        int rand = random.nextInt(2);
        if (rand == 1)
            me.abort();
        else
            other.abort();
    }
}
