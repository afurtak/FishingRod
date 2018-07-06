import javax.swing.*;
import java.awt.*;
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

    private FishCreatorWindow() {
        super("Fishing Rood App");
        setLayout(new GridLayout(3, 1));
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

        createGUI();
    }

    /**
     *
     */
    private void createGUI() {
        createAndAddInputObverse();
        createAndAddInputReverse();
        createAndAddAddButton();
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
            FlashCardsManager.getInstance().saveFlashCard(flashcard);
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
        add(inputObverse);
    }

    private void createAndAddInputReverse() {
        inputReverse = new JTextField();
        inputReverse.setPreferredSize(new Dimension(100, 100));
        add(inputReverse);
    }

    /**
     * @return only instance of FishCreatorWindow.FishCreatorWindow class.
     *          In case if instance is null (doesn't exist yet), creates it.
     */
    public static FishCreatorWindow getInstance() {
        if (instance == null)
            instance = new FishCreatorWindow();
        return instance;
    }

    /**
     * Makes window (FishCreatorWindow.FishCreatorWindow object) visible.
     */
    public static void openWindow() {
        getInstance().setVisible(true);
        isWindowOpen = true;
    }

    /**
     * Makes window (FishCreatorWindow.FishCreatorWindow object) invisible.
     */
    public static void closeWindow() {
        getInstance().dispose();
        isWindowOpen = false;
    }

}
