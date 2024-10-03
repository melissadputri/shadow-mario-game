import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

import java.util.Properties;

/**
 * A class to store attributes of a Power entity.
 * This class extends the Item parent class.
 */
public class Power extends Item{
    private static final int MAX_ACTIVE_FRAMES = 500;
    private static final int VERTICAL_SPEED = -10;
    private final Properties gameProps;
    private int activeFrames = 0;
    private boolean isActive;

    /**
     * Constructor for a single Power object.
     * @param gameProps: Properties object containing file names and initial values of attributes.
     * @param objectPos: int array of initial object position.
     */
    public Power(Properties gameProps, String powerType, int[] objectPos){
        super(gameProps, powerType, objectPos);
        this.gameProps = gameProps;
        isActive = false;
    }

    /**
     * Implementation of move method to perform state update.
     * @param input: User's keyboard entry.
     */
    public void move(Input input) {
        /**
         * Check if power is activated.
         */
        if (isActive) {
            /**
             * Object moves upwards until out of frame after collision.
             */
            if (getPosition().y < (Integer.parseInt(gameProps.getProperty("windowHeight"))) + getObjectImage().getHeight()) {
                Point newPos = new Point(getPosition().x, getPosition().y + VERTICAL_SPEED); // Set vertical speed to move coin upwards
                updatePosition(newPos);
            }

            /**
             * Update activeFrames iterator and check that it is within the maximum active frames.
             * If reached maximum, deactivate.
             */
            activeFrames++;
            if (activeFrames >= MAX_ACTIVE_FRAMES) {
                deactivate();
            }
        }

        /**
         * Respond to user's input moving right or left accordingly.
         */
        if (input.isDown(Keys.RIGHT)) {
            updatePosition(new Point(getPosition().x - getSPEED(), getPosition().y));
        }
        if (input.isDown(Keys.LEFT)) {
            /**
             * Check to not move out of bounds.
             */
            if (getPosition().x < getINITIAL_POSITION().x) {
                updatePosition(new Point(getPosition().x + getSPEED(), getPosition().y));
            }
        }
    }

    /**
     * Method to flag the power is active after collision.
     */
    public void activate() {
        isActive = true;
        activeFrames = 0;
    }

    /**
     * Method to switch off the flag after maximum amount of frames reached.
     */
    public void deactivate() {
        isActive = false;
    }

    /**
     * Getter of the active flag.
     * Used to implement the power accordingly in player's score.
     * @return boolean value of active state.
     */
    public boolean isActive() {
        return isActive;
    }

}
