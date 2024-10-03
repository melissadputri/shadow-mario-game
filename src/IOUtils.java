import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class that contains methods to read a CSV file and a properties file.
 * You may edit this as you wish.
 */
public class IOUtils {

    /***
     * Method that reads a CSV file and return a 2D String array
     * @param csvFile: the path to the CSV file
     * @return String ArrayList
     */
    public static CSVData readCsv(String csvFile) {
        // Lists to dynamically hold the data
        ArrayList<String> platform = new ArrayList<>();
        ArrayList<String> player = new ArrayList<>();
        ArrayList<String> endFlag = new ArrayList<>();
        ArrayList<String> bossEnemy = new ArrayList<>();
        ArrayList<String[]> flyingPlatforms = new ArrayList<>();
        ArrayList<String[]> invinciblePower = new ArrayList<>();
        ArrayList<String[]> doubleScore = new ArrayList<>();
        ArrayList<String[]> coins = new ArrayList<>();
        ArrayList<String[]> enemies = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Assuming the type identifier is in the first column
                switch (values[0]) {
                    case "PLATFORM":
                        platform.add(values[1]);
                        platform.add(values[2]);
                        break;
                    case "PLAYER":
                        player.add(values[1]);
                        player.add(values[2]);
                        break;
                    case "END_FLAG":
                        endFlag.add(values[1]);
                        endFlag.add(values[2]);
                        break;
                    case "ENEMY_BOSS":
                        bossEnemy.add(values[1]);
                        bossEnemy.add(values[2]);
                        break;
                    case "FLYING_PLATFORM":
                        flyingPlatforms.add(new String[]{values[1], values[2]});
                        break;
                    case "INVINCIBLE_POWER":
                        invinciblePower.add(new String[]{values[1], values[2]});
                        break;
                    case "DOUBLE_SCORE":
                        doubleScore.add(new String[]{values[1], values[2]});
                        break;
                    case "COIN":
                        coins.add(new String[]{values[1], values[2]});
                        break;
                    case "ENEMY":
                        enemies.add(new String[]{values[1], values[2]});
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        /**
         * Convert Lists to arrays before passing to the CSVData constructor
         */
        return new CSVData(
                listToArray(platform),
                listToArray(player),
                listToArray(endFlag),
                listToArray(bossEnemy),
                listTo2DArray(flyingPlatforms),
                listTo2DArray(invinciblePower),
                listTo2DArray(doubleScore),
                listTo2DArray(coins),
                listTo2DArray(enemies)
        );
    }

    /**
     * Helper function to transform dynamic array to String array.
     * @param list: takes in String list.
     * @return String array.
     */
    private static String[] listToArray(List<String> list) {
        return list.toArray(new String[0]);
    }

    /**
     * Helper function to transform dynamic array to 2D String array.
     * @param list: takes in String array list.
     * @return 2D String array.
     */
    private static String[][] listTo2DArray(List<String[]> list) {
        return list.toArray(new String[0][]);
    }

    /***
     * Method that reads a properties file and return a Properties object
     * @param configFile: the path to the properties file
     * @return Properties object
     */
    public static Properties readPropertiesFile(String configFile) {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(configFile));
        } catch(IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return appProps;
    }
}
