import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * @author Adam Furtak 21-03-2018
 *
 * Class implements NativeKeyListener.
 * Detect and handle pressed hotkey.
 */
public class GlobalHotkeyHandler implements NativeKeyListener {
    private int[] hotkey;
    private boolean[] pressedKeys;
    private HotkeyListener hotkeyListener;

    /**
     * @param hotKey array of integers, where each element is a code of key in hotkey.
     *               The codes of keys are saved in NativeKeyEvent class.
     *               e.g. NativeKeyEvent.VC_E is 'e' key.
     */
    public GlobalHotkeyHandler(int[] hotKey, HotkeyListener hotkeyListener) {
        this.hotkey = hotKey;
        this.hotkeyListener = hotkeyListener;
        pressedKeys = new boolean[hotKey.length];
        for (int i = 0; i < pressedKeys.length; i++)
            pressedKeys[i] = false;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        if (isHotkeyPressed()) {
            hotkeyListener.hotkeyPressed();
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        for (int i = 0; i < hotkey.length; i++)
            if (e.getKeyCode() == hotkey[i])
                pressedKeys[i] = true;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        for (int i = 0; i < hotkey.length; i++)
            if (e.getKeyCode() == hotkey[i])
                pressedKeys[i] = false;
    }

    /**
     * @return true if all of keys in defined hotkey are being pressed.
     *         Otherwise returns false.
     */
    private boolean isHotkeyPressed() {
        for (boolean pressedKey : pressedKeys)
            if (!pressedKey)
                return false;
        return true;
    }
}
