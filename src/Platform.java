import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

import java.util.Properties;

/**
 * A subclass extending from GameObject parent class, representing the main platform object
 */

public class Platform extends GameObject {
    private final int X_MIN = 0;

    /**
     * Constructor for platform.
     * @param gameProps: Properties object to access files and attribute values.
     * @param type: String of type of platform.
     * @param platformPosition: int array of initial position.
     */
    public Platform(Properties gameProps, String type, int[] platformPosition){
        super(gameProps, type, platformPosition);
    }


    /**
     * Implementation of move method to ensure platform doesn't move beyond specified range.
     * @param input: user's keyboard input.
     */
    public void move(Input input){
        /**
         * Move right if X position is greater than minimum value.
         */
        if (input.isDown(Keys.RIGHT)) {
            if (getPosition().x > X_MIN) {
                updatePosition(new Point(getPosition().x - this.getSPEED(), getPosition().y));
            }
        }

        /**
         * Move left if X position is less than initial position.
         */
        if (input.isDown(Keys.LEFT)) {
            if (getPosition().x < this.getINITIAL_POSITION().x) {
                updatePosition(new Point(getPosition().x + this.getSPEED(), getPosition().y));
            }
        }
    }
}
