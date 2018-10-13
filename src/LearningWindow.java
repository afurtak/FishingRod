import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LearningWindow extends JFrame implements FlashCardsListLoader {

    private final int WIDTH = 323;
    private final int HEIGHT = 200;
    private Dimension screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
    private final int X_POS = (screenResolution.width - WIDTH) / 2;
    private final int Y_POS = (screenResolution.height - HEIGHT) / 2;

    private ChooseSetPanel chooseSetPanel;
    private Flashcard[] flashcardsList;

    public LearningWindow() {
        createWindow();
        createAndAddChooseSetPanel();
    }

    private void createAndAddChooseSetPanel() {
        chooseSetPanel = new ChooseSetPanel(this, this);
        add(chooseSetPanel);
    }

    private void createWindow() {
        setLayout(null);
        setLocation(new Point(X_POS, Y_POS));
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
    }

    @Override
    public void load(Flashcard[] flashcardsList) {
        chooseSetPanel.setVisible(false);
        this.flashcardsList = flashcardsList;

        for (Flashcard flashcard : flashcardsList) {
            System.out.println(flashcard);
        }
    }
}

interface FlashCardsListLoader {
    void load(Flashcard[] flashcardsList);
}

class ChooseSetPanel extends JPanel {

    private JComboBox flashcardSetComboBox;

    public ChooseSetPanel(JFrame parent, FlashCardsListLoader loader) {
        locatePanel(parent);
        createAndAddFlashcardsComboBox();
        createAndAddStartButton(loader);
    }

    private void createAndAddStartButton(FlashCardsListLoader loader) {
        JButton startButton = new JButton("START");
        startButton.addActionListener((actionEvent) -> {
            String selectedSet = (String) flashcardSetComboBox.getSelectedItem();
            FlashCardsManager flashcardsManager = FlashCardsManager.getInstance();
            Flashcard[] flashcards = null;

            try {
                flashcards = flashcardsManager.readFlashcards(selectedSet);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            if (flashcards != null)
                loader.load(flashcards);

        });
        add(startButton, BorderLayout.PAGE_END);
    }

    private void createAndAddFlashcardsComboBox() {
        try {
            FlashCardsManager flashCardsManager = FlashCardsManager.getInstance();
            Object[] flashcardsSets = flashCardsManager.getAllFlashcardsSetsNames().toArray();
            flashcardSetComboBox = new JComboBox<>(flashcardsSets);
            add(flashcardSetComboBox, BorderLayout.PAGE_START);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void locatePanel(JFrame parent) {
        int margin = 50;
        setLayout(new BorderLayout());
        setSize(parent.getWidth() - margin * 2, parent.getHeight() - margin * 2);
        setLocation(margin, margin);
    }

}
