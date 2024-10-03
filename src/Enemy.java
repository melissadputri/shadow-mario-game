import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

import java.util.Properties;
import java.util.Random;

/**
 * A subclass extending from Character parent class and implementing the RandomMovable interface to represent the enemy
 * entity. This class stores the attributes for a single enemy.
 */

public class Enemy extends Character implements RandomMovable{
    private double damageSize;
    private int currentDisplacement = 0;
    private final int maxDisplacement;
    private boolean movingRight;
    /**
     * Initializing Random object to implement random effect on random movement.
     */
    private static final Random random = new Random();
    private int randomSpeed;

    /**
     * Constructor for enemy object.
     * @param gameProps: Properties object to access file names and attribute values.
     * @param enemyPos: int array of initial position.
     */
    public Enemy(Properties gameProps, String objectType, int[] enemyPos) {
        super(gameProps, objectType, enemyPos);
        /**
         * Initialize other attributes such as damage size, displacement, and random speed.
         * Retrieve values from gameProps.
         */
        damageSize = Double.parseDouble(gameProps.getProperty("gameObjects.enemy.damageSize"));
        maxDisplacement = Integer.parseInt(gameProps.getProperty("gameObjects.enemy.maxRandomDisplacementX"));
        randomSpeed = Integer.parseInt(gameProps.getProperty("gameObjects.enemy.randomSpeed"));
        this.movingRight = random.nextBoolean();

    }

    /**
     * Enemy implementation of move method to perform state update.
     * @param input: User's keyboard input.
     */
    public void move(Input input) {
        /**
         * Move accordingly relative to input.
         */
        if (input.isDown(Keys.RIGHT)) {
            updatePosition(new Point(getPosition().x - getSPEED(), getPosition().y));
        }
        if (input.isDown(Keys.LEFT)) {
            /**
             * Check if within bounds.
             */
            if (getPosition().x < getINITIAL_POSITION().x) {
                updatePosition(new Point(getPosition().x + getSPEED(), getPosition().y));
            }
        }

        /**
         * Move randomly after responding to user input.
         */
        moveRandomly();
    }

    /**
     * Getter for damage size.
     * Used to inflict damage on player object.
     * @return double value of damage size.
     */
    public double getDamageSize() {
        return damageSize;
    }

    /**
     * Sets damage size to zero.
     * Acts as a flag that an enemy has collided.
     * Enemy can no longer inflict damage on the player.
     */
    public void zeroDamage() {
        this.damageSize = 0;
    }

    /**
     * Implement random movement action.
     */
    public void moveRandomly() {
        /**
         * Reverse direction.
         */
        if (Math.abs(currentDisplacement) >= maxDisplacement) {
            randomSpeed = -randomSpeed; // Reverse direction
        }

        /**
         * Implements movement according to direction.
         */
        if (movingRight) {
            int newX = (int) (getPosition().x + randomSpeed);
            updatePosition(new Point(newX, getPosition().y));
        } else {
            int newX = (int) (getPosition().x - randomSpeed);
            updatePosition(new Point(newX, getPosition().y));
        }

        /**
         * Update displacement by incrementing speed.
         */
        currentDisplacement += randomSpeed;
    }
}
