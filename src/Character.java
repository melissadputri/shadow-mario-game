import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * An abstract parent class inheriting from GameObject parent class to represent Character objects. A game object
 * is classified as a character if it has health and/or damage properties. This class is abstract since the object needs
 * to be of a specific type to construct. This class implements the Collidable interface, as all characters are
 * Collidable.
 */

public abstract class Character extends GameObject implements Collidable{
    protected double health = 0;
    private final double RADIUS;

    /**
     * Constructor for Character object.
     * @param game_props: Properties object containing file names and default values.
     * @param objectType: String of object type to retrieve corresponding attributes from Properties file.
     * @param objectCoord: int array of initial object position.
     */
    public Character(Properties game_props, String objectType, int[] objectCoord) {
        super(game_props, objectType, objectCoord);
        RADIUS = Double.parseDouble(game_props.getProperty("gameObjects."+objectType+".radius"));
    }

    /**
     * Getter for radius value. Useful for detecting collisions.
     * @return Character's radius value of double type.
     */
    @Override
    public double getRadius() {
        return RADIUS;
    }

    /**
     * Setter for the Character's health value. Useful for setting new health value after damage.
     * @param newHealth: double of new health value intended to be set.
     */
    public void setHealth(double newHealth) {
        this.health = newHealth;
    }
}
