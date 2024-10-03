import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * A subclass extending from the Character parent class and implementing the Shootable interface, reprenting
 * the attributes of a player entity.
 */
public class Player extends Character implements Shootable{
    private int score, verticalSpeed;
    private boolean onPlatform, bossActivated;
    private static final int INITIAL_VERTICAL_SPEED = -20;
    private static final int JUMP_SPEED = 1;
    private static final int PLATFORM_TOLERANCE = 50;
    private static final int FALL_SPEED = 2; // Fall speed for loss condition
    private List<Fireball> fireballs;
    private final Properties gameProps;

    /**
     * Constructor for player object.
     * @param game_props: Properties object to access files and attribute values.
     * @param playerPosition: int array of initial position.
     */
    public Player(Properties game_props, int[] playerPosition) {
        super(game_props, "player", playerPosition);

        /**
         * Sets all parameters to initial value.
         */
        score = 0;
        gameProps = game_props;
        health = Double.parseDouble(gameProps.getProperty("gameObjects.player.health"));
        verticalSpeed = 0;
        onPlatform = true;
        bossActivated = false;
        this.fireballs = new ArrayList<>();
    }

    /**
     * Player implementation of move method relative to user keyboard input.
     * @param input: User's keyboard input.
     * @param flyingPlatforms: array of flying platform objects to check for collisions.
     * @param boss: enemy boss object to check activation radius.
     */
    public void move(Input input, FlyingPlatform[] flyingPlatforms, EnemyBoss boss){

        /**
         * Handle movement when lose condition reached (health is zero or less).
         */
        if (getHealth() <= 0) {
            /**
             * Player moves down until not visible in window.
             */
            verticalSpeed = FALL_SPEED;
            if (getPosition().y < (Integer.parseInt(gameProps.getProperty("windowHeight"))) + getObjectImage().getHeight()) {
                updatePosition(new Point(getPosition().x, getPosition().y + verticalSpeed));
            }
        } else {
            /**
             * Handle input, update fireballs, and check for jumping motion.
             */
            handleInput(input, boss);
            updateFireballs();
            jump(input, flyingPlatforms);
            updatePosition(new Point(getPosition().x, getPosition().y + verticalSpeed));

        }
    }

    private void handleInput(Input input, EnemyBoss boss) {
        if (input.isDown(Keys.LEFT)) {
            setObjectImage(gameProps.getProperty("gameObjects.player.imageLeft"));
        }
        if (input.isDown(Keys.RIGHT)) {
            setObjectImage(gameProps.getProperty("gameObjects.player.imageRight"));
        }
        if (input.wasPressed(Keys.S)){
            if (bossActivated) shootFireball(boss);
        }

    }


    /**
     * A jump method to allow player's vertical movement.
     * @param input: User's keyboard input.
     * @param flyingPlatforms: array of flying platform objects.
     */
    private void jump(Input input, FlyingPlatform[] flyingPlatforms){

        if (input.wasPressed(Keys.UP)) {
            /**
             * Only allow jump if player has platform support.
             */
            if (onMainPlatform() || onFlyingPlatform(flyingPlatforms)) {
                verticalSpeed = INITIAL_VERTICAL_SPEED;
                onPlatform = false;
            }
        }

        /**
         * Falling effect when mid-jump.
         */
        if (getPosition().y < getINITIAL_POSITION().y && !onPlatform) {
            verticalSpeed += JUMP_SPEED;
        }

        /**
         * Constantly check for platform support to allow falling if reached end of flying platform.
         */
        if (!onMainPlatform() && !onFlyingPlatform(flyingPlatforms)) {
            onPlatform = false;
        }

        /**
         * Finishing jump.
         */
        if (verticalSpeed > 0) {
           checkLanding(flyingPlatforms);
        }

    }


    /**
     * Setting variables to initial state when finished jumping.
     * @param platformY: double value of Y position of landed platform.
     */
    private void landOnPlatform(double platformY) {
        verticalSpeed = 0;
        onPlatform = true;
        updatePosition(new Point(getPosition().x, platformY));
    }


    /**
     * Checks if the player is within the collision range with flying platforms.
     * @param platform: a single flying platform object.
     * @return boolean value if collided.
     */
    private boolean isColliding(FlyingPlatform platform) {
        double xDistance = Math.abs(getPosition().x - platform.getPosition().x);
        double yDistance = platform.getPosition().y - getPosition().y;  // platform_y - player_y

        return xDistance < platform.getHALF_LENGTH() &&  // half length
                yDistance <= platform.getHALF_HEIGHT() &&  // half height
                yDistance >= platform.getHALF_HEIGHT() - 1;    // half height - 1
    }


    /**
     * Checks if a player landed on any platform
     * @param flyingPlatforms: array of flying platform objects.
     */
    private void checkLanding(FlyingPlatform[] flyingPlatforms) {
        boolean foundPlatform = false;

        /**
         * Checks if flyingPlatforms is not null to accommodate for levels without flying platforms.
         * Iterate through all flying platforms to check for collision.
         */
        if (flyingPlatforms != null) {
            for (FlyingPlatform platform : flyingPlatforms) {
                if (isColliding(platform)) {
                    landOnPlatform(platform.getPosition().y - platform.getHALF_HEIGHT());
                    foundPlatform = true;
                    break;  // Stop checking once a landing is detected
                }
            }
        }

        /**
         * Land on main platform if no flying platform was landed on.
         */
        if (!foundPlatform && onMainPlatform()) {
            landOnPlatform(getINITIAL_POSITION().y);
        }
    }


    /**
     * Checks if player is on any flying platform
     * @param flyingPlatforms: array of flying platform objects
     * @return boolean value
     */
    private boolean onFlyingPlatform(FlyingPlatform[] flyingPlatforms) {
        if (flyingPlatforms != null) {
            for (FlyingPlatform platform : flyingPlatforms) {
                if (this.isColliding(platform)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Method to check if player is on main platform.
     * @return boolean value
     */
    private boolean onMainPlatform(){
        return (getPosition().y >= getINITIAL_POSITION().y) && (getPosition().y <= getINITIAL_POSITION().y + PLATFORM_TOLERANCE);
    }


    /**
     * Method to check if player is within the enemy boss activation radius.
     * Used to enable Shootable.
     * @param boss: EnemyBoss object to check activation radius.
     */
    public void checkBossActivated(EnemyBoss boss) {
        if (boss != null) {
            if ((boss.getPosition().x - getPosition().x) <= boss.getACTIVATION_RADIUS()){
                bossActivated = true;
            }
        }
    }


    /**
     * Implementation of the shoot fireball method to allow player to shoot fireballs towards enemy boss.
     * @param target: Shootable object.
     */
    public void shootFireball(Shootable target) {

        /**
         * Ensure that the Shootable target is also an instance of GameObject
         */
        if (!(target instanceof GameObject)) {
            throw new IllegalArgumentException("Shootable must be GameObject");
        }

        GameObject gameObject = (GameObject) target;
        /**
         * Construct a new fireball to move towards target.
         */
        boolean movingRight = getPosition().x < gameObject.getPosition().x;
        int [] position = {(int) getPosition().x, (int)getPosition().y};
        Fireball newFireball = new Fireball(gameProps, position, movingRight);
        fireballs.add(newFireball);
    }


    /**
     * Method to update fireballs in the list.
     */
    public void updateFireballs() {
        Iterator<Fireball> it = fireballs.iterator();
        while (it.hasNext()) {
            Fireball fireball = it.next();
            fireball.move();
            /**
             * Remove fireball if off screen or has collided with target.
             */
            if (fireball.isOffScreen() || fireball.hasCollided()) {
                it.remove();
            }
        }
    }

    /**
     * Getter for fireball list.
     * Used to render and check for collisions.
     * @return List of fireballs.
     */
    public List<Fireball> getFireballs(){
        return fireballs;
    }

    /**
     * Getter for player score.
     * Used to render on game screen.
     * @return int value of score.
     */
    public int getScore(){
        return this.score;
    }

    /**
     * Setter for player score.
     * Used when collided with Coin object to update.
     * @param newScore: int value of updated score.
     */
    public void setScore(int newScore) {
        this.score = newScore;
    }

    /**
     * Getter for player health.
     * Used to render to game screen and check for lose conditions.
     * @return double value of health.
     */
    public double getHealth() {
        return health;
    }

}