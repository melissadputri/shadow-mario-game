import bagel.util.Point;

/**
 * An interface to signify that an object is Collidable.
 * A Collidable object will have a radius.
 */

public interface Collidable {
    /**
     * Method to retrieve an object's current position.
     * @return Point value consisting of X and Y coordinate.
     */
    Point getPosition();

    /**
     * A default method to check if an object has collided with another.
     * Can take any other Collidable object as an argument, as long as they are a GameObject.
     * @param object: Any Collidable object.
     * @return boolean type checking if distance is less than or equal to the range.
     */
    default boolean checkCollision(Collidable object) {

        /**
         * Defense handling in case of object not existing due to game level.
         */
        if (object == null) {
            return false;
        }

        /**
         * Asserting that object must be of GameObject.
         */
        if (!(object instanceof GameObject)) {
            throw new IllegalArgumentException("Collidable must be GameObject");
        }

        GameObject gameObject = (GameObject) object;

        double range = this.getRadius() + object.getRadius();

        /**
         * Calculating distance using Euclidian distance.
         */
        double distance = Math.sqrt(
                Math.pow(this.getPosition().x - gameObject.getPosition().x, 2) +
                        Math.pow(this.getPosition().y - gameObject.getPosition().y, 2)
        );
        return distance <= range;
    }

    /**
     * Getter for radius value.
     * Method must be implemented for all Collidable object to check for collision.
     * @return double value of radius.
     */
    double getRadius();

}
