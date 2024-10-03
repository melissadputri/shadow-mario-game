import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

import java.util.Properties;

/**
 * A specific class for coins, extending from the Item parent class.
 * Stores attributes of a single coin entity.
 */

public class Coin extends Item{
    private int coinValue;
    private final static int VERTICAL_SPEED = -10;
    private Properties gameProps;

    /**
     * Constructor for a Coin object.
     * @param gameProps: Properties object to retrieve files and default values.
     * @param objectCoord: int array of object's initial position.
     */
    public Coin(Properties gameProps, String objectType, int[] objectCoord) {
        super(gameProps, objectType, objectCoord);
        this.gameProps = gameProps;
        coinValue = Integer.parseInt(gameProps.getProperty("gameObjects.coin.value"));
    }


    /**
     * The Coin object's implementation of move method.
     * @param input: user's keyboard entry.
     */
    public void move(Input input){

        /**
         * If coin has collided, move upwards.
         */
        if (coinValue == 0) {
            /**
             * Continue to move upwards until out of the window.
             */
            if (getPosition().y < (Integer.parseInt(gameProps.getProperty("windowHeight"))) + getObjectImage().getHeight()) {
                Point newPos = new Point(getPosition().x, getPosition().y + VERTICAL_SPEED); // Set vertical speed to move coin upwards
                updatePosition(newPos);
            }
        }

        /**
         * Move left or right accordingly.
         */
        if (input.isDown(Keys.RIGHT)) {
            updatePosition(new Point(getPosition().x - getSPEED(), getPosition().y));
        }

        if (input.isDown(Keys.LEFT)) {
            /**
             * Check to not move over the bounds.
             */
            if (getPosition().x < getINITIAL_POSITION().x) {
                updatePosition(new Point(getPosition().x + getSPEED(), getPosition().y));
            }
        }

    }

    /**
     * Sets coin value to zero.
     * Acts as a flag if a coin has collided with a player.
     */
    public void zeroValue(){
        coinValue = 0;
    }

    /**
     * Getter for coin value.
     * Useful for updating the Player object's score.
     * @return: int of coin value.
     */
    public int getCoinValue() {
        return coinValue;
    }
}
