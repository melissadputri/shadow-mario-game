import java.util.Properties;

/**
 * A parent class for item objects, inheriting from game object parent class. Game objects are classified as items
 * when they are collectable and impacts the player's gameplay. This parent class is also an abstract class since it is
 * impossible to instantiate an item object, it must be of a specific object type to construct.
 */

public abstract class Item extends GameObject implements Collidable{
    private final double RADIUS; // radius is final since it stays constant throughout the game

    /**
     * Constructor for item object.
     * @param game_props: Properties file to access files and attribute values.
     * @param objectType: String of object type.
     * @param objectCoord: int array of object initial position.
     */
    public Item(Properties game_props, String objectType, int[] objectCoord) {
        super(game_props, objectType, objectCoord);
        RADIUS = Double.parseDouble(game_props.getProperty("gameObjects."+objectType+".radius"));
    }

    /**
     * Getter for radius value.
     * Used to check for collisions.
     * @return double value of radius.
     */
    @Override
    public double getRadius() {
        return RADIUS;
    }
}
