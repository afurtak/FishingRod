import java.io.File;
import java.sql.*;
import java.util.ArrayList;

/**
 * Singleton class, using to communication with data base.
 */
public class FlashCardsManager {

    private static FlashCardsManager instance;

    private static String pathToDataBase;
    private static String dataBaseUrl;

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

    private String getSqlQueryAddingFlashCard(Flashcard flashcard, String flashcardsSet) {
        return "INSERT INTO " + flashcardsSet + " (obverse, reverse, timesOfRepetition) " +
                "VALUES ('" + flashcard.getObverse() + "', '" + flashcard.getReverse() +
                "', " + flashcard.getTimesOfRepetition() + ");";
    }

    /**
     * Saves given flashcard in database.
     * If table with flashcards does not exist, create it and make recursive call again.
     *
     * @param flashcard object to save.
     * @throws SQLException
     */
    public void saveFlashCard(Flashcard flashcard, String flashcardsSet) throws SQLException {
        if (existsTableWithFlashcards(flashcardsSet)) {
            String sqlQuery = getSqlQueryAddingFlashCard(flashcard, flashcardsSet);
            statement.executeUpdate(sqlQuery);
        }
        else {
            createNewFlashcardsSet(flashcardsSet);
            saveFlashCard(flashcard, flashcardsSet);
        }
    }

    /**
     * Creates new table in database represents new flashcards set.
     *
     * @param flashcardsSet name of new flashcards set.
     * @throws SQLException
     */
    public void createNewFlashcardsSet(String flashcardsSet) throws SQLException {
        statement.executeUpdate(
                "CREATE TABLE " + flashcardsSet + " (" +
                        "id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "obverse TEXT," +
                        "reverse TEXT," +
                        "timesOfRepetition INTEGER);");
    }

    /**
     * @return ArrayList of fleshcards stored in database.
     * @throws SQLException
     */
    public ArrayList<Flashcard> readFlashcards(String flashcardsSet) throws SQLException {
        ArrayList<Flashcard> flashcards = new ArrayList<>();

        if (existsTableWithFlashcards(flashcardsSet)) {
            ResultSet sqlResult = statement.executeQuery("SELECT * FROM " + flashcardsSet);

            while (sqlResult.next()) {
                Flashcard nextFlashcard = new Flashcard(
                    sqlResult.getInt(1),
                    sqlResult.getString(2),
                    sqlResult.getString(3),
                    sqlResult.getInt(4)
                );

                flashcards.add(nextFlashcard);
            }
        }

        return flashcards;
    }



    /**
     * @return ArrayList names of every tables included to database.
     * @throws SQLException
     */
    public ArrayList<String> getAllFlashcardsSets() throws SQLException {
        ResultSet tables = connection.getMetaData().getTables(null, null, "%", null);
        ArrayList<String> result = new ArrayList<>();
        while (tables.next()) {
            result.add(tables.getString(3));
        }
        return result;
    }

    private boolean existsTableWithFlashcards(String flashcardsSet) throws SQLException {
        ResultSet tables = connection.getMetaData().getTables(null, null, flashcardsSet, null);
        return tables.next();
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
