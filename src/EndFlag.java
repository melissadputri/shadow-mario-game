import java.util.Properties;

/**
 * A class extending the Game Object parent class and implementing the Collidable interface to represent the
 * end flag entity.
 */
public class EndFlag extends GameObject implements  Collidable{
    private final double RADIUS;

    /**
     * Constructor for the end flag object.
     * @param game_props: Properties object containing file names and initial attribute values.
     * @param objectCoord: int array of initial object coordinate.
     */
    public EndFlag(Properties game_props, int[] objectCoord) {
        super(game_props, "endFlag", objectCoord);
        RADIUS = Double.parseDouble(game_props.getProperty("gameObjects.endFlag.radius"));
    }

    /**
     * Getter for radius value.
     * @return double type of radius value.
     */
    @Override
    public double getRadius() {
        return RADIUS;
    }
}
