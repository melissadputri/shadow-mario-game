import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

import java.util.*;

/**
 * A subclass extending the Character parent class and the Shootable interface, representing the Enemy Boss entity.
 * This class holds the attributes of a single Enemy Boss entity.
 */

public class EnemyBoss extends Character implements Shootable {
    private final int ACTIVATION_RADIUS;
    private final int FRAME_INTERVALS = 100;
    private final int FALL_SPEED = 2;
    private int verticalSpeed;
    Properties gameProps;
    private int frameCounter = 0;
    private final Random random = new Random();
    private List<Fireball> fireballs;

    /**
     * Constructor for enemy boss object.
     * @param gameProps: Properties object to access files and attribute values.
     * @param enemyBosPos: int array of initial position.
     */
    public EnemyBoss(Properties gameProps, int[] enemyBosPos){
        super(gameProps, "enemyBoss", enemyBosPos);
        /**
         * Initialize attributes by accessing from gameProps.
         */
        ACTIVATION_RADIUS = Integer.parseInt(gameProps.getProperty("gameObjects.enemyBoss.activationRadius"));
        this.health = Double.parseDouble(gameProps.getProperty("gameObjects.enemyBoss.health"));
        this.gameProps = gameProps;
        this.fireballs = new ArrayList<>();
        verticalSpeed = 0;
    }

    /**
     * Implementation of move method for enemy boss.
     * @param input: user's keyboard input.
     * @param player: player object to access player's position
     */
    public void move(Input input, Player player) {

        /**
         * If health reaches zero or under, move down until not visible in the window.
         */
        if (getHealth() <= 0) {
            verticalSpeed = FALL_SPEED;
            if (getPosition().y < (Integer.parseInt(gameProps.getProperty("windowHeight"))) + getObjectImage().getHeight()) {
                updatePosition(new Point(getPosition().x, getPosition().y + verticalSpeed));
            }
        }

        /**
         * Move relative to user input.
         */
        if (input.isDown(Keys.RIGHT)) {
            updatePosition(new Point(getPosition().x - getSPEED(), getPosition().y));
        }
        if (input.isDown(Keys.LEFT)) {
            if (getPosition().x < getINITIAL_POSITION().x) {
                updatePosition(new Point(getPosition().x + getSPEED(), getPosition().y));
            }
        }

        /**
         * Increment frame counter to randomly shoot fireballs every 100 frames
         */
        frameCounter++;
        if (frameCounter % FRAME_INTERVALS == 0) {  // Every 100 frames

            /**
             * Random chance to shoot fireball if the player is within activation radius
             */
            if ((distanceToPlayer(player) <= getACTIVATION_RADIUS()) && random.nextBoolean()) {
                shootFireball(player);
            }
        }

        /**
         * Update fireballs and enemy boss position.
         */
        updateFireballs();
        updatePosition(new Point(getPosition().x, getPosition().y + verticalSpeed));

    }

    /**
     * Method to shoot fireball towards Shootable target
     * @param target: a Shootable object
     */
    public void shootFireball(Shootable target) {

        /**
         * Ensure that target is an instance of GameObject
         */
        if (!(target instanceof GameObject)) {
            throw new IllegalArgumentException("Shootable must be GameObject");
        }

        GameObject gameObject = (GameObject) target; // Safe cast after checking

        /**
         * Check for direction of fireball and add a new instance of fireball to the dynamic array.
         */
        boolean movingRight = getPosition().x < gameObject.getPosition().x;
        int [] position = {(int) getPosition().x, (int)getPosition().y};
        Fireball newFireball = new Fireball(gameProps, position, movingRight);
        fireballs.add(newFireball);
    }


    /**
     * Updates the fireballs position to move accordingly towards target
     */
    public void updateFireballs() {
        Iterator<Fireball> it = fireballs.iterator();
        while (it.hasNext()) {
            Fireball fireball = it.next();
            fireball.move();
            /**
             * Remove fireball if outside of player window or has collided with target.
             */
            if (fireball.isOffScreen() || fireball.hasCollided()) {
                it.remove();
            }
        }
    }

    /**
     * Getter for fireball list.
     * @return list of current fireballs shot.
     */
    public List<Fireball> getFireballs(){
        return fireballs;
    }

    /**
     * Method to calculate distance to player object.
     * @param player: player object to access position.
     * @return double value of distance to player.
     */
    public double distanceToPlayer(Player player) {
        return getPosition().x - player.getPosition().x;
    }

    /**
     * Getter for enemy boss health.
     * Used to render the health value on screen and check win/lose condition.
     * @return double value of enemy boss health.
     */
    public double getHealth() {
        return health;
    }

    /**
     * Getter for enemy boss activation radius.
     * Used to check Shootable validity.
     * @return int value of activation radius.
     */
    public int getACTIVATION_RADIUS() {
        return ACTIVATION_RADIUS;
    }
}
