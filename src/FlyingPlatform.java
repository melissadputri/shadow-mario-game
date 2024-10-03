import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Properties;
import java.util.Random;

/**
 * A subclass extending from the Platform parent class and implements the RandomMovable interface.
 * Differs from the main platform class due to its ability to move randomly.
 */

public class FlyingPlatform extends Platform implements RandomMovable {
    private final int HALF_LENGTH, HALF_HEIGHT;
    private int currentDisplacement = 0;
    private final int maxDisplacement;
    private boolean movingRight;
    private static final Random random = new Random();
    private int randomSpeed;


    /**
     * Constructor for flying platform.
     * @param gameProps: Properties object to access files and attribute values.
     * @param platformPosition: int array of initial position.
     */
    public FlyingPlatform(Properties gameProps, String objectType, int[] platformPosition) {
        super(gameProps, objectType, platformPosition);
        HALF_LENGTH = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.halfLength"));
        HALF_HEIGHT = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.halfHeight"));
        maxDisplacement = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.maxRandomDisplacementX"));
        randomSpeed = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.randomSpeed"));

        /**
         * Sets random movement direction randomly.
         */
        this.movingRight = random.nextBoolean();
    }

    /**
     * Implementation of move method for flying platforms.
     * @param input: User's keyboard input.
     */
    public void move(Input input) {
        if (input.isDown(Keys.RIGHT)) {
            updatePosition(new Point(this.getPosition().x - this.getSPEED(), this.getPosition().y));
        }
        if (input.isDown(Keys.LEFT)) {
            /**
             * Check if out of bounds.
             */
            if (getPosition().x < getINITIAL_POSITION().x) {
                updatePosition(new Point(getPosition().x + getSPEED(), getPosition().y));
            }
        }

        /**
         * Call method to move randomly.
         */
        moveRandomly();
    }

    /**
     * Getter for platform half height.
     * Used to detect collision and set new position.
     * @return int value of half height.
     */
    public int getHALF_HEIGHT() {
        return HALF_HEIGHT;
    }

    /**
     * Getter for platform half length.
     * Used to detect collision.
     * @return int value of half length.
     */
    public int getHALF_LENGTH() {
        return HALF_LENGTH;
    }


    /**
     * Implementation of the random movement method.
     */
    public void moveRandomly() {
        /**
         * Reverses direction if reached maximum displacement.
         */
        if (Math.abs(currentDisplacement) >= maxDisplacement) {
            randomSpeed = -randomSpeed;
        }

        /**
         * Move according to direction.
         */
        if (movingRight) {
            int newX = (int) (getPosition().x + randomSpeed);
            updatePosition(new Point(newX, getPosition().y));
        } else {
            int newX = (int) (getPosition().x - randomSpeed);
            updatePosition(new Point(newX, getPosition().y));
        }

        /**
         * Increment speed to displacement.
         */
        currentDisplacement += randomSpeed;
    }

}
