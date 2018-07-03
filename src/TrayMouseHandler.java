import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Adam Furtak 2018-03-20
 */
class TrayMouseHandler implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (!FishCreatorWindow.isWindowOpen)
            FishCreatorWindow.openWindow();
        else
            FishCreatorWindow.closeWindow();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}