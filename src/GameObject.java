import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

import java.util.Properties;

/**
 * Abstract parent class for game objects.
 * This class is abstract since it is impossible to instantiate a general game object. A game object must be of a
 * specific object type to retrieve images and properties.
 */

public abstract class GameObject {
    private Image objectImage;
    private final Point INITIAL_POSITION;
    private Point position;
    private final int SPEED;

    /**
     * General constructor for GameObjects.
     * @param game_props: Properties object to access file names and attribute values.
     * @param objectType: String of specific object type to access correct attributes.
     * @param objectCoord: int array of initial position.
     */
    public GameObject(Properties game_props, String objectType, int[] objectCoord) {
        String imageName;

        /**
         * Access image name according to object type.
         * Player has a different image name format.
         */
        if ("player".equals(objectType)) {
            imageName = game_props.getProperty("gameObjects." + objectType + ".imageRight");
            SPEED = 0;

        } else {
            imageName = game_props.getProperty("gameObjects." + objectType + ".image");
            SPEED = Integer.parseInt(game_props.getProperty("gameObjects." + objectType + ".speed"));
        }

        /**
         * Set attributes.
         */
        objectImage = new Image(imageName);
        position = new Point(objectCoord[0], objectCoord[1]);
        INITIAL_POSITION = new Point(objectCoord[0], objectCoord[1]);

    }

    /**
     * General implementation of move method to respond to user keyboard input.
     * @param input: user's keyboard input.
     */
    public void move(Input input) {
        if (input.isDown(Keys.RIGHT)) {
            updatePosition(new Point(position.x - SPEED, position.y));
        }
        if (input.isDown(Keys.LEFT)) {
            if (position.x < INITIAL_POSITION.x) {
                updatePosition(new Point(position.x + SPEED, position.y));
            }
        }
    }

    /**
     * Setters and Getters for Game Objects
     */
    public void updatePosition(Point newPos){
        position = new Point(newPos.x, newPos.y);
    }
    public void render() {
        objectImage.draw(position.x, position.y);
    }

    /**
     * Setter for object image.
     * @param newImage: String of path to image.
     */
    public void setObjectImage(String newImage) {
        this.objectImage = new Image(newImage);
    }
    public Image getObjectImage() {
        return objectImage;
    }
    public int getSPEED() {
        return SPEED;
    }
    public Point getPosition() {
        return position;
    }
    public Point getINITIAL_POSITION(){
        return INITIAL_POSITION;
    }
}
