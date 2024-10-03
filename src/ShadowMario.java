import bagel.*;
import java.util.*;


/**
 * Skeleton Code for SWEN20003 Project 2, Semester 1, 2024
 *
 * Please enter your name below
 * Melissa Putri (1389438)
 */
public class ShadowMario extends AbstractGame {

    private final Image BACKGROUND_IMAGE;
    private final Properties gameProps, messageProps;
    private boolean gameStarted;
    private LevelManager levelManager;

    private DisplayManager display;


    /**
     * The constructor for ShadowMario.
     */
    public ShadowMario(Properties game_props, Properties message_props) {
        super(Integer.parseInt(game_props.getProperty("windowWidth")),
              Integer.parseInt(game_props.getProperty("windowHeight")),
              message_props.getProperty("title"));

        gameProps = IOUtils.readPropertiesFile("res/app.properties");
        messageProps = IOUtils.readPropertiesFile("res/message_en.properties");
        BACKGROUND_IMAGE = new Image(game_props.getProperty("backgroundImage"));
        gameStarted = false;
        display = new DisplayManager(gameProps, messageProps);
        levelManager = new LevelManager(gameProps);


    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowMario game = new ShadowMario(game_props, message_props);
        game.run();
    }

    /**
     * Performs a state update of the selected level.
     * Allows the game to exit when the escape key is pressed.
     * Handle screen navigation between levels and instruction pages here.
     * @param input: User's keyboard entry.
     */
    @Override
    public void update(Input input) {
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        /**
         * State when game has not started, render screen appropriately.
         */
        if (!gameStarted) {
            display.renderStartScreen();

            /**
             * For every level selected, read a different CSV file and construct objects accordingly.
             */
            if (input.wasPressed(Keys.NUM_1)) {
                levelManager.loadLevel(1, IOUtils.readCsv(gameProps.getProperty("level1File")));
                gameStarted = true;
            }

            if (input.wasPressed(Keys.NUM_2)) {
                levelManager.loadLevel(2, IOUtils.readCsv(gameProps.getProperty("level2File")));
                gameStarted = true;
            }

            if (input.wasPressed(Keys.NUM_3)) {
                levelManager.loadLevel(3, IOUtils.readCsv(gameProps.getProperty("level3File")));
                gameStarted = true;
            }
        }


        /**
         * Performing state updates when game is started.
         */
        if (gameStarted) {

            /**
             * If lose/win condition is reached, render the appropriate message screen.
             */
            if (levelManager.checkWinLose()) {
                if (levelManager.gameWon()) {
                    display.renderWinScreen();
                } else {
                    display.renderLoseScreen();
                }

                /**
                 * Restart game by resetting game objects.
                 */
                if (input.wasPressed(Keys.SPACE)){
                    levelManager.reset();
                    gameStarted = false;
                }

            } else {
                /**
                 * Perform state update of the game, including checking for collisions, moving objects, and rendering
                 * objects and the game screen.
                 */
                levelManager.checkCollisions();
                levelManager.getPlayer().checkBossActivated(levelManager.getEnemyBoss());
                levelManager.moveObjects(input);
                display.renderGameScreen(levelManager.getPlayer(), levelManager.getEnemyBoss());
                levelManager.renderObjects();
            }

        }

        /**
         * If Escape key is pressed, exit window.
         */
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

    }


}
