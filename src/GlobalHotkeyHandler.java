import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * @author Adam Furtak 21-03-2018
 *
 * Class implements NativeKeyListener.
 * Detect and handle pressed hotkey.
 */
public class GlobalHotkeyHandler implements NativeKeyListener {
    private int[][] hotkey;
    private boolean[][] pressedKeys;
    private int[] counters;
    private HotkeyListener[] hotkeyListener;

    /**
     * @param hotKey array of integers, where each element is a code of key in hotkey.
     *               The codes of keys are saved in NativeKeyEvent class.
     *               e.g. NativeKeyEvent.VC_E is 'e' key.
     */
    public GlobalHotkeyHandler(int[] hotKey, HotkeyListener hotkeyListener) {
        this.hotkey = new int[][] { hotKey };
        this.hotkeyListener = new HotkeyListener[] { hotkeyListener };
        pressedKeys = new boolean[][] { new boolean[hotKey.length] };
        counters = new int[1];

        for (int i = 0; i < pressedKeys[0].length; i++)
            pressedKeys[0][i] = false;
    }

    /**
     * @param hotKey array of integer arrays, where each single array corresponds to single hotkey to handle.
     *               Each element of this array is a code of key in hotkey.
     *
     *               The codes of keys are saved in NativeKeyEvent class.
     *               e.g. NativeKeyEvent.VC_E is 'e' key.
     *
     * @param hotkeyListener array of objects implements after HotkeyListener. The hotkeyPressed method from each
     *                      element is called when corresponding hotkey is pressed.
     */
    public GlobalHotkeyHandler(int[][] hotKey, HotkeyListener[] hotkeyListener) {
        this.hotkey = hotKey;
        this.hotkeyListener = hotkeyListener;
        pressedKeys = new boolean[hotKey.length][];
        counters = new int[hotKey.length];

        for (int i = 0; i < pressedKeys.length; i++) {
            pressedKeys[i] = new boolean[hotKey[i].length];
            for (int j = 0; j < hotKey[i].length; j++)
                pressedKeys[i][j] = false;
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        for (int i = 0; i < hotkey.length; i++)
            for (int j = 0; j < hotkey[i].length; j++) {
                if (e.getKeyCode() == hotkey[i][j]) {
                    pressedKeys[i][j] = true;
                    counters[i]++;
                }
            }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        for (int i = 0; i < hotkey.length; i++)
            if (isHotkeyPressed(i))
                hotkeyListener[i].hotkeyPressed();

        for (int i = 0; i < hotkey.length; i++)
            for (int j = 0; j < hotkey[i].length; j++) {
                if (e.getKeyCode() == hotkey[i][j]) {
                    pressedKeys[i][j] = false;
                    counters[i]--;
                }
            }
    }

    /**
     * @return true if all of keys in defined hotkey are being pressed.
     *         Otherwise returns false.
     */
    private boolean isHotkeyPressed(int i) {
        return counters[i] == hotkey[i].length;
    }
}
