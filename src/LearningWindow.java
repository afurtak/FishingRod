import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class LearningWindow extends JFrame implements FlashCardsListLoader {

    private final int WIDTH = 323;
    private final int HEIGHT = 200;
    private Dimension screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
    private final int X_POS = (screenResolution.width - WIDTH) / 2;
    private final int Y_POS = (screenResolution.height - HEIGHT) / 2;

    private ChooseSetPanel chooseSetPanel;
    private FlashcardsDisplay flashcardsDisplay;
    private Flashcard[] flashcardsList;
    private NativeKeyListener localWindowKeyListener;

    public LearningWindow() {
        createWindow();
        createAndAddChooseSetPanel();
        createLocalWindowKeyListener();
        addWindowListener();
    }

    private void createLocalWindowKeyListener() {
        localWindowKeyListener = new NativeKeyListener() {
            @Override
            public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) { }

            @Override
            public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) { }

            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
                if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_SPACE) {
                    if (areFlashcardsLoaded()) {
                        flashcardsDisplay.showNext();
                    } else {
                        chooseSetPanel.loadFlashcards(LearningWindow.this);
                    }
                }
            }
        };
    }

    private void addWindowListener() {
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }

            @Override
            public void windowClosing(WindowEvent e) { }

            @Override
            public void windowClosed(WindowEvent e) { }

            @Override
            public void windowIconified(WindowEvent e) { }

            @Override
            public void windowDeiconified(WindowEvent e) { }

            @Override
            public void windowActivated(WindowEvent e) {
                addKeyListener();
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                removeKeyListener();
            }
        });
    }

    private void removeKeyListener() {
        GlobalScreen.removeNativeKeyListener(localWindowKeyListener);
    }

    private void addKeyListener() {
        GlobalScreen.addNativeKeyListener(localWindowKeyListener);
    }

    private boolean areFlashcardsLoaded() {
        return flashcardsList != null;
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

        createAndAddFlashcardsDisplay();
    }

    private void createAndAddFlashcardsDisplay() {
        System.out.println("LearningWindow.createAndAddFlashcardsDisplay");
        flashcardsDisplay = new FlashcardsDisplay(this, flashcardsList);
        add(flashcardsDisplay);
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
        startButton.addActionListener((actionEvent) -> loadFlashcards(loader));
        add(startButton, BorderLayout.PAGE_END);
    }

    public void loadFlashcards(FlashCardsListLoader loader) {
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
        else {
            System.out.println("ChooseSetPanel.loadFlashcards");
        }
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

class FlashcardsDisplay extends JPanel {
    private Flashcard[] flashcards;
    private int margin = 50;
    private JButton nextButton;
    private JLabel flashcardLabel;
    private int currentFlashcardIndex = 0;
    private boolean isCurrentDisplayObverse = true;

    public FlashcardsDisplay(JFrame parent, Flashcard[] flashcards) {
        this.flashcards = flashcards;
        locatePanel(parent);
        createFlashcardLabel();
        createNextButton();
    }

    private void createNextButton() {
        nextButton = new JButton("Next");
        nextButton.addActionListener((actionEvent) -> showNext());
        add(nextButton, BorderLayout.PAGE_END);
    }

    private void createFlashcardLabel() {
        flashcardLabel = new JLabel(flashcards[0].getObverse());
        add(flashcardLabel, BorderLayout.PAGE_START);
    }

    private void locatePanel(JFrame parent) {
        setSize(parent.getWidth() - 2 * margin, parent.getHeight() - 2 * margin);
        setLocation(margin, margin);
        setLayout(new BorderLayout());
    }

    void showNext() {
        if (isCurrentDisplayObverse) {
            flashcardLabel.setText(flashcards[currentFlashcardIndex].getReverse());
        }
        else {
            if (currentFlashcardIndex + 1 < flashcards.length)
                flashcardLabel.setText(flashcards[++currentFlashcardIndex].getObverse());
            else {
                flashcardLabel.setText("End of flashcards set.");
                currentFlashcardIndex = -1;
                return;
            }
        }
        isCurrentDisplayObverse = !isCurrentDisplayObverse;
    }
}
