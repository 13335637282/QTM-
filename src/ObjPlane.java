import javax.swing.*;
import java.awt.*;

public class ObjPlane extends JPanel {
    static final int RIGHT = 1;
    static final int WRONG = 2;
    static final int OTHER = 3;

    public ObjPlane(LayoutManager layout, boolean isDoubleBuffered) {
        setLayout(layout);
        setDoubleBuffered(isDoubleBuffered);
        updateUI();
    }

    public ObjPlane(LayoutManager layout) {
        this(layout, true);
    }

    public ObjPlane(boolean isDoubleBuffered) {
        this(new FlowLayout(), isDoubleBuffered);
    }

    public ObjPlane() {
        this(true);
    }

    public void show_answers() {
    }
    public boolean check() {
        return false;
    }
}
