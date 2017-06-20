package lassesjoblom.thirty;

/**
 * Created by Lasse on 2017-06-20.
 */

public interface Observable {

    void addObserver(Observer o);
    void notifyObserver();
}
