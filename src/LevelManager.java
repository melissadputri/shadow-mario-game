import bagel.Input;
import bagel.Window;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Properties;

/**
 * A class to handle level intialising and customising specific level behaviours including constructing and updating
 * game objects due to the difference in game objects in each level.
 */
public class LevelManager {
    private final Properties gameProps;
    private final double ROUND = 100.0;
    private int currentLevel;
    private boolean gameWon;
    private Player player;
    private Platform platform;
    private EnemyBoss enemyBoss;
    private EndFlag endFlag;
    private FlyingPlatform[] flyingPlatforms;
    private Enemy[] enemies;
    private Power[] invinciblePowers;
    private Power[] doubleScores;
    private Coin[] coins;

    /**
     * Constructor for LevelManager.
     * @param gameProps: takes in Properties object to pass onto object constructors.
     */
    public LevelManager(Properties gameProps) {
        this.gameProps = gameProps;
        this.gameWon = false;
    }

    /**
     * @param level: game level selected.
     * @param objectPositions: CSVData object passing initial positions of objects.
     */
    public void loadLevel(int level, CSVData objectPositions) {
        this.currentLevel = level;

        /**
         * Initialize objects based on CSV data
         */
        this.player = new Player(gameProps, objectPositions.getPlayer());
        this.platform = new Platform(gameProps, "platform", objectPositions.getPlatform());
        this.endFlag = new EndFlag(gameProps, objectPositions.getEndFlag());

        this.enemies = initializeObjects(gameProps, "enemy", objectPositions.getEnemies(), Enemy.class);
        this.coins = initializeObjects(gameProps, "coin", objectPositions.getCoins(), Coin.class);

        if (level > 1) {
            /**
             * Level 2 and 3 contains flying platforms, invincible powers, and double score powers.
             */
            flyingPlatforms = initializeObjects(gameProps, "flyingPlatform", objectPositions.getFlyingPlatforms(), FlyingPlatform.class);
            invinciblePowers = initializeObjects(gameProps, "invinciblePower", objectPositions.getInvinciblePower(), Power.class);
            doubleScores = initializeObjects(gameProps, "doubleScore", objectPositions.getDoubleScore(), Power.class);
        }

        if (level == 3) {
            /**
             * Enemy boss constructed, only present in level 3.
             */
            this.enemyBoss = new EnemyBoss(gameProps, objectPositions.getEnemyBoss());
        }
    }

    private <T> T[] initializeObjects(Properties props, String objectType, int[][] objectPos, Class<T> clazz) {
        T[] array = (T[]) Array.newInstance(clazz, objectPos.length);
        for (int i = 0; i < objectPos.length; i++) {
            if (objectPos[i] != null) {
                try {
                    array[i] = clazz.getDeclaredConstructor(Properties.class, String.class, int[].class)
                            .newInstance(props, objectType, objectPos[i]);
                } catch (NoSuchMethodException e) {
                    /**
                     * Handles exception where constructor is not found.
                     */
                    System.err.println("Constructor with specified parameters not found in " + clazz.getName());
                } catch (IllegalAccessException e) {
                    /**
                     * Handles illegal access to constructor.
                     */
                    System.err.println("Cannot access the constructor of " + clazz.getName());
                } catch (InstantiationException e) {
                    /**
                     * Handles error in instantiating object.
                     */
                    System.err.println("Cannot instantiate " + clazz.getName() + "; ensure it is not abstract and has a visible constructor");
                } catch (InvocationTargetException e) {
                    /**
                     * Handles invoked method or constructor.
                     */
                    System.err.println("Constructor threw an exception for " + clazz.getName() + ": " + e.getCause());
                }
            }
        }
        return array;
    }

    /**
     * Method to call the move method on all game objects according to the current game level.
     * @param input: player keyboard input.
     */
    public void moveObjects(Input input) {
        player.move(input, flyingPlatforms, enemyBoss);
        platform.move(input);
        endFlag.move(input);
        moveGameObjects(enemies, input);
        moveGameObjects(coins, input);

        if (currentLevel != 1){
            moveGameObjects(flyingPlatforms, input);
            moveGameObjects(invinciblePowers, input);
            moveGameObjects(doubleScores, input);

            if (currentLevel == 3){
                enemyBoss.move(input, player);
            }
        }

    }

    /**
     * Helper function to move game objects with multiple instances.
     * @param objects: array of GameObjects.
     * @param input: user's keyboard input.
     */
    private void moveGameObjects(GameObject[] objects, Input input) {
        for (GameObject obj : objects) {
            if (obj != null) {
                obj.move(input);
            }
        }
    }


    /**
     * Method to render all game objects according to selected game level.
     */
    public void renderObjects() {
        platform.render();
        endFlag.render();
        renderGameObjects(enemies);
        renderGameObjects(coins);

        if (currentLevel != 1){
            renderGameObjects(flyingPlatforms);
            renderGameObjects(invinciblePowers);
            renderGameObjects(doubleScores);

            if (currentLevel == 3){
                enemyBoss.render();
                for (Fireball fireball : enemyBoss.getFireballs()) {
                    fireball.render();
                }
                for (Fireball fireball : player.getFireballs()) {
                    fireball.render();
                }
            }
        }
        player.render();

    }

    /**
     * Helper function to render objects with multiple instances.
     * @param objects: array of GameObject objects.
     */
    private void renderGameObjects(GameObject[] objects) {
        for (GameObject obj : objects) {
            if (obj != null) {
                obj.render();
            }
        }
    }


    /**
     * Method to check for collisions in all game objects in every state update.
     */
    public void checkCollisions() {

        for (Enemy enemy: enemies) {
            if (player.checkCollision(enemy)){
                /**
                 * If a player doesn't have the invincible power active, inflict damage
                 */
                if (!checkPowers(invinciblePowers)) {
                    double newHealth = player.getHealth() - enemy.getDamageSize();
                    double roundedHealth = Math.round(newHealth * ROUND) / ROUND;
                    player.setHealth(roundedHealth);
                }
                /**
                 * Set enemy to zero damage to flag collision
                 */
                enemy.zeroDamage();
            }
        }

        for (Coin coin: coins) {
            if (player.checkCollision(coin)) {
                /**
                 * Double the score if any double score power is active.
                 */
                if (checkPowers(doubleScores)) {
                    player.setScore(player.getScore() + (2 * coin.getCoinValue()));
                } else {
                    player.setScore(player.getScore() + coin.getCoinValue());
                }
                /**
                 * Set coin value to zero to flag collision/
                 */
                coin.zeroValue();
            }
        }

        /**
         * Check for any collision with powers, activate if collided.
         */
        activatePower(invinciblePowers);
        activatePower(doubleScores);


        /**
         * If on level 3, check for fireball collisions.
         */
        if (currentLevel == 3) {
            /**
             * Checks for fireballs shot by enemy boss.
             */
            for (Fireball fireball : enemyBoss.getFireballs()) {
                if (player.checkCollision(fireball)){
                    fireball.collide();
                    /**
                     * If no invincible powers are active, inflict damage.
                     */
                    if (!checkPowers(invinciblePowers)) {
                        double newHealth = player.getHealth() - fireball.getDamageSize();
                        player.setHealth(newHealth);
                    }
                }
            }

            /**
             * Checks for fireballs shot by player.
             */
            for (Fireball fireball : player.getFireballs()) {
                if (enemyBoss.checkCollision(fireball)){
                    fireball.collide();
                    double newHealth = enemyBoss.getHealth() - fireball.getDamageSize();
                    enemyBoss.setHealth(newHealth);
                }
            }
        }

    }

    private boolean checkPowers(Power[] powers){
        if (powers != null) {
            for (Power power: powers) {
                if (power.isActive()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void activatePower(Power[] powers) {
        if (powers != null) {
            for (Power power: powers) {
                if (player.checkCollision(power)) {
                    power.activate();
                }
            }
        }
    }


    /**
     * Checks win and lose conditions.
     * Wins if player collides with end flag.
     * Loses if player's health is equal to or less than zero.
     * If in level 3, the enemy boss must have health of 0 or below to win.
     * @return boolean value
     */
    public boolean checkWinLose() {
        if (player.getHealth() <= 0) {
            if (player.getPosition().y >= Window.getHeight()) { // Waits until player is out of the window
                gameWon = false;
                return true;
            }

        }

        if (currentLevel == 3) {
            if (player.checkCollision(endFlag) && enemyBoss.getHealth() <= 0) {
                gameWon = true;
                return true;
            }
        }
        if (currentLevel != 3) {
            if (player.checkCollision(endFlag)){
                gameWon = true;
                return true;
            }
        }

        return false;
    }

    /**
     * Resets game attributes before restarting game.
     * Sets all GameObject attributes to null, and restarts gameWon flag.
     */
    public void reset() {
        gameWon = false;

        this.enemyBoss = null;

        if (currentLevel != 1) {
            Arrays.fill(flyingPlatforms, null);
            Arrays.fill(invinciblePowers, null);
            Arrays.fill(doubleScores, null);
            flyingPlatforms = null;
            invinciblePowers = null;
            doubleScores = null;

        }
    }

    /**
     * Getter for gameWon flag
     * @return gameWon flag representing game win/lose state
     */
    public boolean gameWon() {
        return gameWon;
    }

    /**
     * Getter for Player object
     * @return Player object to give access to all Player attributes
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter for EnemyBoss object
     * @return EnemyBoss object to give access to EnemyBoss attributes
     */
    public EnemyBoss getEnemyBoss() {
        return enemyBoss;
    }
}
