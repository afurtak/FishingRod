/**
 * @author Adam FurtakIOException
 *
 * Flashcard representation
 */
public class Flashcard {

    private final String obverse;
    private final String reverse;
    private int timesOfRepetition;
    private int id;

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
        id = 0;
    }

    /**
     * @param obverse obverse of flashcard.
     * @param reverse reverse of flashcard.
     * @param timesOfRepetition times of repetition of this flashcard.
     *                          the bigger, the lower probability to repeat it again.
     * @param id flashcard in database
     */
    public Flashcard(String obverse, String reverse, int timesOfRepetition, int id) {
        this.obverse = obverse;
        this.reverse = reverse;
        this.timesOfRepetition = timesOfRepetition;
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void incrementTimesOfRepetition() {
        timesOfRepetition++;
    }

    @Override
    public String toString() {
        return "[" + obverse + " " + reverse + " " + timesOfRepetition + "]";
    }
}
