package lassesjoblom.thirty;

import java.util.ArrayList;

/**
 * Created by Lasse on 2017-06-20.
 */

public interface Observer {

    public void update(int currentRound, int rethrow, ArrayList<Dice> diceList);
}
