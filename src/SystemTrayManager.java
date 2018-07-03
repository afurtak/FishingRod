import java.awt.*;

/**
 * @author Adam Furtak 2018-03-20
 */
public class SystemTrayManager {
    /**
     * Flag checking if method createSystemTrayIcon was called earlier.
     */
    private static boolean iconExist = false;

    private static String iconPath = "systemTrayIcon.png";

    /**
     * Static method creates system tray icon to communicate with user without start new JFrame object with menu.
     * @throws Exception if SystemTray is not supported or the method was called earlier.
     */
    public static void createSystemTrayIcon() throws Exception {
        if (SystemTray.isSupported() && !iconExist) {
            iconExist = true;
            SystemTray tray = SystemTray.getSystemTray();

            TrayIcon icon = new TrayIcon(loadImage(), "Fishing Rood", createMenu());
            icon.addMouseListener(new TrayMouseHandler());

            try {
                tray.add(icon);
            }
            catch (AWTException e) {
                System.out.println("Unable to add icon to tray.");
                e.printStackTrace();
            }

        }
        else {
            if (!SystemTray.isSupported())
                throw new Exception("Sorry, system tray is not supported.");
            else
                throw new Exception("The method was called earlier and the icon was created.");
        }
    }

    private static PopupMenu createMenu() {
        PopupMenu menu = new PopupMenu();

        MenuItem quit = new MenuItem("Quit");
        quit.addActionListener(actionEvent -> System.exit(0));
        menu.add(quit);

        return menu;
    }

    private static Image loadImage() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.getImage(iconPath);
    }
}

