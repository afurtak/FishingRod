import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Adam FurtakIOException
 *
 * Flashcard representation
 */
public class Flashcard {

    private final String obverse;
    private final String reverse;

    private int timesOfRepetition;

    public static String pathToFile = System.getProperty("user.home") + "/.FishingRood/flashcard.fish";

    /**
     * Saves flashcard to file.
     * @param pathToFile path to file where will be written flashcard.
     */
    public void saveFlashcard(String pathToFile) throws Exception {
        File file = new File(pathToFile);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(this.obverse + "_" + this.reverse + "_" + this.timesOfRepetition + "\n");
        } catch (Exception e) {
            throw new Exception("Something wrong with opening file! :c \n");
        }
    }

    /**
     * Saves flashcards given by ArrayList object.
     * Replace previous file.
     */
    public static void saveFlashcards(ArrayList<Flashcard> flashcards, String pathToFile) {
        File file = new File(pathToFile);
        for (Flashcard flashcard : flashcards) {
            try {
                flashcard.saveFlashcard(pathToFile);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get saved flashcards from file given by path.
     *
     * @param pathToFile path to file where are written flashcards.
     * @return ArrayList of flashcards written in file.
     * @throws FileNotFoundException if file given pathToFile doesn't exit.
     */
    public static ArrayList<Flashcard> readFlashcard(String pathToFile) throws FileNotFoundException {
        ArrayList<Flashcard> flashcards = new ArrayList<>();

        File file = new File(pathToFile);
        Scanner input = new Scanner(new File(pathToFile));

        Flashcard flashcard = null;
        while (input.hasNextLine()) {
            String[] line = input.nextLine().split("_");
            flashcard = new Flashcard(line[0], line[1], Integer.parseInt(line[2]));
            flashcards.add(flashcard);
        }

        return flashcards;
    }

    /**
     * @param obverse obverse of flashcard.
     * @param reverse reverse of flashcard.
     * @param timesOfRepetition times of repetition of this flashcard.
     *                          the bigger, the lower probability to repeat it again.
     */
    public Flashcard(String obverse, String reverse, int timesOfRepetition) {
        this.obverse = obverse;
        this.reverse = reverse;
        this.timesOfRepetition = timesOfRepetition;
    }

    public String getObverse() {
        return obverse;
    }

    public String getReverse() {
        return reverse;
    }

    public int getTimesOfRepetition() {
        return timesOfRepetition;
    }

    public void incrementTimesOfRepetition() {
        timesOfRepetition++;
    }

    @Override
    public String toString() {
        return "[" + obverse + " " + reverse + " " + timesOfRepetition + "]";
    }

    public static void testFlashcard() {
        String obverse = "obverse";
        String reverse = "reverse";
        for (int i = 0; i < 100; i++) {
            Flashcard flashcard = new Flashcard(obverse + i, reverse + i, 0);
            try {
                flashcard.saveFlashcard(pathToFile);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        Flashcard[] flashcards = new Flashcard[]{};
        try {
            flashcards = Flashcard.readFlashcard(pathToFile).toArray(new Flashcard[0]);
        }
        catch (IOException e) {
            System.out.println("something wrong with open file");
            System.exit(1);
        }

        for (Flashcard flashcard : flashcards)
            System.out.println(flashcard);
    }

}
