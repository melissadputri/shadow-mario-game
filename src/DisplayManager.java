import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.DrawOptions;

import java.util.Properties;

import static java.lang.Integer.parseInt;

/**
 * A class dedicated to handle rendering game displays, includes starting and end screens, health and score
 * displays. Manages font size, position, and color.
 */

public class DisplayManager {
    private final Font TITLE_FONT, MESSAGE_FONT, INS_FONT, P_HEALTH_FONT, SCORE_FONT, E_HEALTH_FONT;
    private final Image BACKGROUND;
    private final static int TOTAL_HEALTH = 100;
    private final Properties gameProps, messageProps;

    /**
     * Constructor for DisplayManager object.
     * @param gameProps: Properties object to access file names and initial values.
     * @param messageProps: Properties object to access String contents to be displayed.
     */
    public DisplayManager(Properties gameProps, Properties messageProps){
        this.gameProps = gameProps;
        this.messageProps = messageProps;

        String FONT_FILE = gameProps.getProperty("font");
        TITLE_FONT = new Font(FONT_FILE, Integer.parseInt(gameProps.getProperty("title.fontSize")));
        MESSAGE_FONT = new Font(FONT_FILE, Integer.parseInt(gameProps.getProperty("message.fontSize")));
        INS_FONT = new Font(FONT_FILE, Integer.parseInt(gameProps.getProperty("instruction.fontSize")));
        P_HEALTH_FONT = new Font(FONT_FILE, Integer.parseInt(gameProps.getProperty("playerHealth.fontSize")));
        E_HEALTH_FONT = new Font(FONT_FILE, Integer.parseInt(gameProps.getProperty("enemyBossHealth.fontSize")));
        SCORE_FONT = new Font(FONT_FILE, Integer.parseInt(gameProps.getProperty("score.fontSize")));

        BACKGROUND = new Image(gameProps.getProperty("backgroundImage"));


    }

    /**
     * Method to render starting screen displayed prior to game starting.
     */
    public void renderStartScreen() {
        BACKGROUND.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        String title = messageProps.getProperty("title");
        String instruction = messageProps.getProperty("instruction");
        Point titlePos = new Point(parseInt(gameProps.getProperty("title.x")), parseInt(gameProps.getProperty("title.y")));
        Point insPos = new Point((Window.getWidth() - INS_FONT.getWidth(instruction)) / 2, Integer.parseInt(gameProps.getProperty("instruction.y")));


        TITLE_FONT.drawString(title, titlePos.x, titlePos.y);
        INS_FONT.drawString(instruction, insPos.x, insPos.y);
    }

    /**
     * Method to render game screen during game play.
     * Displays player's score and health.
     * In level 3 displays enemy boss health.
     * @param player: Player object to access current score and health values.
     * @param enemyBoss: EnemyBoss object to access current health (if present).
     */
    public void renderGameScreen(Player player, EnemyBoss enemyBoss) {
        BACKGROUND.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        String health = messageProps.getProperty("health");
        String score = messageProps.getProperty("score");
        Point phealthPos = new Point(parseInt(gameProps.getProperty("playerHealth.x")), parseInt(gameProps.getProperty("playerHealth.y")));
        Point scorePos = new Point(parseInt(gameProps.getProperty("score.x")), parseInt(gameProps.getProperty("score.y")));
        Point eHealthPos = new Point(parseInt(gameProps.getProperty("enemyBossHealth.x")), parseInt(gameProps.getProperty("enemyBossHealth.y")));

        /**
         * Mapping Player's health value to view percentage format.
         */
        int healthPercentage = (int) (player.getHealth()*TOTAL_HEALTH); // Mapping health

        P_HEALTH_FONT.drawString(health +  " "  + healthPercentage, phealthPos.x, phealthPos.y);
        SCORE_FONT.drawString(score +  " "  + player.getScore(), scorePos.x, scorePos.y);

        /**
         * Defense code to assert that enemy boss is initialized before displaying its health.
         */
        if (enemyBoss != null) {
            /**
             * Maps enemy boss health to display percentage format.
             */
            int eHealthPercentage = (int) (enemyBoss.getHealth()*TOTAL_HEALTH); // Mapping health
            DrawOptions options = new DrawOptions();
            E_HEALTH_FONT.drawString(health + " " + eHealthPercentage, eHealthPos.x, eHealthPos.y, options.setBlendColour(255,0,0));
        }
    }

    /**
     * Method to render winning message when win condition is fulfilled.
     */
    public void renderWinScreen(){
        String winString = messageProps.getProperty("gameWon");
        int messageY = Integer.parseInt(gameProps.getProperty("message.y"));
        MESSAGE_FONT.drawString(winString, (Window.getWidth() - MESSAGE_FONT.getWidth(winString))/2, messageY);
    }

    /**
     * Method to render lose message when lose conditions reached.
     */
    public void renderLoseScreen(){
        String loseString = messageProps.getProperty("gameOver");
        int messageY = Integer.parseInt(gameProps.getProperty("message.y"));
        MESSAGE_FONT.drawString(loseString, (Window.getWidth() - MESSAGE_FONT.getWidth(loseString))/2, messageY);
    }
}
