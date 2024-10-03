import bagel.util.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * An interface to give the ability to shoot fireballs towards another Shootable target.
 */

public interface Shootable {
    /**
     * Method to shoot fireball towards a target by constructing a new fireball object.
     * @param target: Shootable object.
     */
    void shootFireball(Shootable target);

    /**
     * Method to update fireball objects.
     */
    void updateFireballs();

    /**
     * Getter for list of fireballs existing.
     * @return list of fireballs.
     */
    List<Fireball> getFireballs();

}
