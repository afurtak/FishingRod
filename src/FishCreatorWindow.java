import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

/**
 * @author Adam Furtak 21-03-2018
 */
public class FishCreatorWindow extends JFrame {

    public static boolean isWindowOpen = false;
    private static FishCreatorWindow instance = null;

    //-----gui components-----
    private  JTextField inputReverse;
    private JTextField inputObverse;
    private JButton addButton;
    private JComboBox<Object> flashcardSetComboBox;

    private FishCreatorWindow() {
        super("Fishing Rood App");
        setLayout(new GridLayout(4, 1));

        setVisible(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        final int WIDTH = 300;
        final int HEIGHT = 400;
        setSize(WIDTH, HEIGHT);

        Dimension screenResolution = Toolkit.getDefaultToolkit().getScreenSize();
        final int X_POS = (screenResolution.width - WIDTH) / 2;
        final int Y_POS = (screenResolution.height - HEIGHT) / 2;
        setLocation(new Point(X_POS, Y_POS));

        setAlwaysOnTop(true);
        setUndecorated(true);

        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                closeWindow();
            }
        });

        createGUI();

        /*
        requests focus on JTextField
         */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                inputObverse.requestFocusInWindow();
            }
        });
    }

    /**
     *
     */
    private void createGUI() {
        createAndAddFlashcardSetComboBox();
        createAndAddInputObverse();
        createAndAddInputReverse();
        createAndAddAddButton();
    }

    private void createAndAddFlashcardSetComboBox() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        try {
            flashcardSetComboBox = new JComboBox<>(FlashCardsManager.getInstance().getAllFlashcardsSetsNames().toArray());
            panel.add(flashcardSetComboBox);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JButton addSetButton = new JButton("+");
        addSetButton.addActionListener((actionEvent) -> openAddNewSetWidnow());
        panel.add(addSetButton);
        add(panel);
    }

    private void openAddNewSetWidnow() {
        new AddNewSetWindow();
    }

    void updateFlashcardsSet() {
        try {
            flashcardSetComboBox.setModel(new DefaultComboBoxModel<>(FlashCardsManager.getInstance().getAllFlashcardsSetsNames().toArray()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createAndAddAddButton() {
        addButton = new JButton("ADD");

        addButton.addActionListener((actionEvent) -> {
            if (!inputObverse.getText().equals("") && !inputReverse.getText().equals("")) {
                addFlashcard();
            }
        });

        add(addButton);
    }

    public void addFlashcard() {
        Flashcard flashcard = new Flashcard(inputObverse.getText(), inputReverse.getText(), 0);

        try {
            FlashCardsManager.getInstance().saveFlashCard(flashcard, (String) flashcardSetComboBox.getSelectedItem());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        inputReverse.setText("");
        inputObverse.setText("");
    }

    private void createAndAddInputObverse() {
        inputObverse = new JTextField();
        inputObverse.setPreferredSize(new Dimension(200, 300));
        inputObverse.requestFocus();
        add(inputObverse);
    }

    private void createAndAddInputReverse() {
        inputReverse = new JTextField();
        inputReverse.setPreferredSize(new Dimension(100, 100));
        add(inputReverse);
    }

    /**
     * @return only instance of FishCreatorWindow class.
     *          In case if instance is null (doesn't exist yet), creates it.
     */
    public static FishCreatorWindow getInstance() {
        if (instance == null)
            instance = new FishCreatorWindow();
        return instance;
    }

    /**
     * Makes window (FishCreatorWindow object) visible.
     */
    public static void openWindow() {
        getInstance().setVisible(true);
        getInstance().inputObverse.requestFocusInWindow();
        isWindowOpen = true;
    }

    /**
     * Makes window (FishCreatorWindow object) invisible.
     */
    public static void closeWindow() {
        getInstance().dispose();
        isWindowOpen = false;
    }
}

class AddNewSetWindow extends JFrame {
    JTextField inputNameOfSet;
    JButton createSetButton;

    public AddNewSetWindow() {
        super("Add new set");
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setLayout(new GridLayout(2, 1));
        setSize(150, 200);
        createGUI();
    }

    void createGUI() {
        inputNameOfSet = new JTextField();
        add(inputNameOfSet);

        createSetButton = new JButton("CREATE");
        createSetButton.addActionListener((a) -> {
            if (!inputNameOfSet.equals("")) {
                try {
                    FlashCardsManager.getInstance().createNewFlashcardsSet(inputNameOfSet.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                FishCreatorWindow.getInstance().updateFlashcardsSet();
                FishCreatorWindow.openWindow();
                dispose();
            }
        });
        add(createSetButton);
    }
}