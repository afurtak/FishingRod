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

    private static String setTableName = "Sets";
    private static String flashcardsTableName = "Flashcards";

    static {
        pathToDataBase = System.getProperty("user.home") + "/.FishingRod/fishingrod.db";
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


    private String getSqlQueryAddingFlashCard(Flashcard flashcard, int setId) {
        return "INSERT INTO " + flashcardsTableName + " (obverse, reverse, timesOfRepetition, setId) " +
                "VALUES ('" + flashcard.getObverse() + "', '" + flashcard.getReverse() +
                "', " + flashcard.getTimesOfRepetition() + ", " + Integer.toString(setId) + ");";
    }


    private boolean existsTableWithFlashcards() throws SQLException {
        ResultSet tables = connection.getMetaData().getTables(null, null, flashcardsTableName, null);
        return tables.next();
    }


    private boolean existsTableWithSets() throws SQLException {
        ResultSet tables = connection.getMetaData().getTables(null, null, setTableName, null);
        return tables.next();
    }


    private String getSqlQueryAddingFlashcardSet(String setName) {
        return "INSERT INTO " + setTableName + " (name) " +
                "VALUES ('" + setName + "');";
    }


    private int getSetsId(String flashcardsSet) throws SQLException {
        ResultSet result = statement.executeQuery("SELECT id FROM " + setTableName + " WHERE name LIKE \'" + flashcardsSet +"\'");
        if (result.next())
            return result.getInt(1);
        throw new SQLException();
    }


    private void addSetsTable() throws SQLException {
        statement.executeUpdate(
                "CREATE TABLE Sets (" +
                        "id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT);");
    }

    private void addFlashCardsTable() throws SQLException {
        if (existsTableWithSets()) {
            statement.executeUpdate(
                    "CREATE TABLE FlashCards (" +
                            "id  INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "obverse TEXT," +
                            "reverse TEXT," +
                            "timesOfRepetition INTEGER, " +
                            "setId INTEGER);");
        }
        else {
            addSetsTable();
            addFlashCardsTable();
        }
    }


    /**
     * Saves given flashcard in database.
     * If table with flashcards does not exist, create it and make recursive call again.
     *
     * @param flashcard object to save.
     * @throws SQLException
     */
    public void saveFlashCard(Flashcard flashcard, String flashcardsSet) throws SQLException {
        if (existsTableWithFlashcards()) {
            String sqlQuery = getSqlQueryAddingFlashCard(flashcard, getSetsId(flashcardsSet));
            System.out.println(sqlQuery);
            statement.executeUpdate(sqlQuery);
        }
        else {
            addFlashCardsTable();
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
        if (existsTableWithSets())
            statement.executeUpdate(getSqlQueryAddingFlashcardSet(flashcardsSet));
        else {
            addSetsTable();
            createNewFlashcardsSet(flashcardsSet);
        }
    }

    /**
     * @return ArrayList of fleshcards stored in database.
     * @throws SQLException
     */
    public ArrayList<Flashcard> readFlashcards(String flashcardsSet) throws SQLException {
        ArrayList<Flashcard> flashcards = new ArrayList<>();

        if (existsTableWithFlashcards()) {
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
    public ArrayList<String> getAllFlashcardsSetsNames() throws SQLException {
        ArrayList<String> setsNames = new ArrayList<>();
        if (existsTableWithSets()) {

            ResultSet result = statement.executeQuery("SELECT DISTINCT name FROM " + setTableName);

            while (result.next()) {
                setsNames.add(result.getString(1));
            }
        }
        return setsNames;
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
