/**
 * @author Adam FurtakIOException
 *
 * Flashcard representation
 */
public class Flashcard {

    private final String obverse;
    private final String reverse;
    private int timesOfRepetition;

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
}
