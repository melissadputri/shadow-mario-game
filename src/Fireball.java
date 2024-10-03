import bagel.util.Point;

import java.util.Properties;

/**
 * A subclass extending from the character parent class to represent the fireball entity.
 * Contains the attributes of a single fireball entity.
 */
public class Fireball extends Character {
    private final boolean MOVING_RIGHT;
    private static int screenWidth;
    private static double damageSize;
    private boolean collided = false; // Flag to check if fireball has collided

    /**
     * Constructor for fireball object.
     * @param gameProps: Properties object to access files and attribute values.
     * @param startingPosition: int array of initial position.
     * @param direction: boolean value if fireball should start moving right.
     */
    public Fireball (Properties gameProps, int[] startingPosition, boolean direction) {
        super(gameProps, "fireball", startingPosition);
        MOVING_RIGHT = direction;
        screenWidth = Integer.parseInt(gameProps.getProperty("windowWidth"));
        damageSize = Double.parseDouble(gameProps.getProperty("gameObjects.fireball.damageSize"));
    }

    /**
     * Implementation of move method for fireballs.
     */
    public void move() {
        /**
         * Checks the MOVING_RIGHT value and move accordingly.
         */
        if (MOVING_RIGHT) {
            updatePosition(new Point(getPosition().x + getSPEED(), getPosition().y));
        } else {
            updatePosition(new Point(getPosition().x - getSPEED(), getPosition().y));
        }
    }

    /**
     * Checks if fireball is out of the window.
     * @return boolean value.
     */
    public boolean isOffScreen() {
        return getPosition().x < 0 || getPosition().x > screenWidth;
    }

    /**
     * Getter for fireball damage size to inflict damage on target.
     * @return double value of damage size.
     */
    public double getDamageSize() {
        return damageSize;
    }

    /**
     * Getter for flag to check if fireball has collided.
     * @return boolean value.
     */
    public boolean hasCollided(){
        return collided;
    }

    /**
     * Method to set collide flag as true after collision with target.
     */
    public void collide() {
        collided = true;
    }
}
