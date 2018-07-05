import java.io.File;
import java.sql.*;

/**
 * Singleton class, using to communication with data base.
 */
public class FlashCardsManager {

    private static FlashCardsManager instance;

    private static String pathToDataBase;
    private static String dataBaseUrl;
    private static String tableName = "flashcards";

    static {
        pathToDataBase = System.getProperty("user.home") + "/.FishingRod/flashcards.db";
        File file = new File(System.getProperty("user.home") + "/.FishingRod");
        if (!file.exists())
            file.mkdirs();
        dataBaseUrl = "jdbc:sqlite:" + pathToDataBase;
    }

    private Connection connection;
    private Statement statement;

    private FlashCardsManager() throws SQLException {
        connection = DriverManager.getConnection(dataBaseUrl);
        statement = connection.createStatement();
    }

    private String getSqlQueryAddingFlashCard(Flashcard flashcard) {
        return "INSERT INTO " + tableName + " (id, obverse, reverse, timesOfRepetition) " +
                "VALUES ((SELECT MAX(ID) + 1 FROM " + tableName + "), '" + flashcard.getObverse() + "', '" + flashcard.getReverse() +
                "', " + flashcard.getTimesOfRepetition() + ");";
    }

    /**
     * Saves given flashcard in database.
     * If table with flashcards does not exist, create it and make recursive call again.
     *
     * @param flashcard object to save.
     * @throws SQLException
     */
    public void saveFlashCard(Flashcard flashcard) throws SQLException {
        ResultSet tables = connection.getMetaData().getTables(null, null, tableName, null);
        if (tables.next()) {
            String sqlQuery = getSqlQueryAddingFlashCard(flashcard);
            statement.executeUpdate(sqlQuery);
        }
        else {
            statement.executeUpdate(
                    "CREATE TABLE " + tableName + " (" +
                            "id INT IDENTITY(1, 1)," +
                            "obverse TEXT," +
                            "reverse TEXT," +
                            "timesOfRepetition INT);");
            saveFlashCard(flashcard);
        }
    }

    /**
     * @return only instance of FlashCardsManager. In case the instance doesn't exist, creates one.
     */
    public static FlashCardsManager getInstance() {
        if (instance == null) {
            try {
                instance = new FlashCardsManager();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
