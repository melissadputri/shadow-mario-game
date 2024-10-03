import java.util.Properties;

/**
 * A class to store the CSV data read, storing the positions of each object type respectively in separate arrays
 * for easy access.
 */

public class CSVData {
    private final int[] platform;
    private final int[] player;
    private final int[] endFlag;
    private final int[] enemyBoss;
    private final int[][] flyingPlatforms;
    private final int[][] invinciblePower;
    private final int[][] doubleScore;
    private final int[][] coins;
    private final int[][] enemies;


    /**
     * Constructor for CSVData object.
     * Takes in String arrays of object positions.
     * @param platform: String array of platform position.
     * @param player: String array of player position.
     * @param endFlag: String array of end flag position.
     * @param enemyBoss: String array of enemy boss position.
     * @param flyingPlatforms: 2D String array of flying platform position.
     * @param invinciblePower: 2D String array of invincible powers position.
     * @param doubleScore: 2D String array of double score power position.
     * @param coins: 2D String array of coins position.
     * @param enemies: 2D String array of enemies position.
     */
    public CSVData(String[] platform, String[] player, String[] endFlag, String[] enemyBoss,
                   String[][] flyingPlatforms, String[][] invinciblePower, String[][] doubleScore,
                   String[][] coins, String[][] enemies) {
        this.platform = convertArray(platform);
        this.player = convertArray(player);
        this.endFlag = convertArray(endFlag);
        this.enemyBoss = convertArray(enemyBoss);
        this.flyingPlatforms = convert2DArray(flyingPlatforms);
        this.invinciblePower = convert2DArray(invinciblePower);
        this.doubleScore = convert2DArray(doubleScore);
        this.coins = convert2DArray(coins);
        this.enemies = convert2DArray(enemies);
    }

    // A helper function to convert the String array to integer array

    /**
     * Helper function to convert String array to int array.
     * @param stringArray: String array consisting of object position.
     * @return: converted String array to int array.
     */
    private static int[] convertArray(String[] stringArray) {
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            try {
                intArray[i] = Integer.parseInt(stringArray[i]);
            } catch (NumberFormatException e) {
                /**
                 * Handle number format exception.
                 */
                System.err.println("Error converting to integer: " + stringArray[i]);
                intArray[i] = 0; // Defaulting to zero, or handle appropriately
            }
        }
        return intArray;
    }

    // A helper function to convert the String array to a 2D integer array, this is used for objects that allow
    // multiplicity (many instances of the object)

    /**
     * A helper function to convert the String array to a 2D integer array.
     * This is used for objects that allow multiplicity (many instances of the object).
     * @param stringArray: 2D String array.
     * @return: 2D int array.
     */
    private static int[][] convert2DArray(String[][] stringArray) {
        int[][] intArray = new int[stringArray.length][];
        for (int i = 0; i < stringArray.length; i++) {
            intArray[i] = new int[stringArray[i].length];
            for (int j = 0; j < stringArray[i].length; j++) {
                try {
                    intArray[i][j] = Integer.parseInt(stringArray[i][j]);
                } catch (NumberFormatException e) {
                    /**
                     * Handle number format exception.
                     */
                    System.err.println("Error converting to integer at [" + i + "][" + j + "]: " + stringArray[i][j]);
                    intArray[i][j] = 0; // Defaulting to zero, or handle appropriately
                }
            }
        }
        return intArray;
    }

    /**
     * Getters for all object int arrays to access their initial positions.
     * All getters used in LevelManager constructor to construct objects.
     * @return int array of corresponding object.
     */

    public int[] getPlatform() {
        return platform;
    }
    public int[] getPlayer() {
        return player;
    }
    public int[] getEndFlag() {
        return endFlag;
    }
    public int[] getEnemyBoss() {
        return enemyBoss;
    }
    public int[][] getFlyingPlatforms() {
        return flyingPlatforms;
    }
    public int[][] getInvinciblePower() {
        return invinciblePower;
    }
    public int[][] getDoubleScore() {
        return doubleScore;
    }
    public int[][] getCoins() {
        return coins;
    }
    public int[][] getEnemies() {
        return enemies;
    }

}
