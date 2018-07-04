import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Adam Furtak 2018-03-20
 */
public class FishingRodApp {

    public static void main(String[] args) {

        FishCreatorWindow.getInstance();    //creates instance of FishCreatorWindow.FishCreatorWindow.

        createSystemTray();
        addGlobalHotkeyHandle();
    }

    /**
     * Adds global hotkey handle using JNativeHook library.
     *
     * link to library on github: https://github.com/kwhat/jnativehook
     */
    private static void addGlobalHotkeyHandle() {
        try {
            GlobalScreen.registerNativeHook();
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.WARNING);

            HotkeyListener hotkeyListenerOpenClose = () -> {
                if (!FishCreatorWindow.isWindowOpen)
                    FishCreatorWindow.openWindow();
                else
                    FishCreatorWindow.closeWindow();
            };

            HotkeyListener hotkeyListenerClose = () -> {
                if (FishCreatorWindow.isWindowOpen)
                    FishCreatorWindow.closeWindow();
            };

            GlobalScreen.addNativeKeyListener(
                    new GlobalHotkeyHandler(
                            new int[][]{{NativeKeyEvent.VC_A, NativeKeyEvent.VC_ALT}, {NativeKeyEvent.VC_ESCAPE}},
                            new HotkeyListener[] {hotkeyListenerOpenClose, hotkeyListenerClose}
                    )
            );
        }
        catch (NativeHookException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates system tray.
     */
    private static void createSystemTray() {
        try {
            SystemTrayManager.createSystemTrayIcon();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
