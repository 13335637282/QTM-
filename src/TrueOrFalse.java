import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TrueOrFalse extends ObjPlane {
    private final int[] C = {-1};
    private int answers;
    private boolean canc = true;

    private JButton button1 = new JButton();
    private JButton button2 = new JButton();

    private JLabel Qs;

    public TrueOrFalse(String Q, int answers) {
        setBorder(new TitledBorder("判断题"));
        this.answers = answers;
        setLayout(new BorderLayout());

        JPanel cs = new JPanel(new GridLayout(1,2));
        add(cs,BorderLayout.EAST);

        Qs = new JLabel(Q);
        Qs.setFont(new Font("宋体", Font.BOLD,15));
        add(Qs,BorderLayout.CENTER);

        button1.setText("√");
        button2.setText("×");
        button1.setForeground(new Color(0x0AF00A));
        button2.setForeground(new Color(0xF00A0A));
        button1.setFont(new Font("宋体", Font.PLAIN,15));
        button2.setFont(new Font("宋体", Font.PLAIN,15));
        cs.add(button1);
        cs.add(button2);

        button1.setBackground(Color.white);
        button2.setBackground(Color.white);

        button1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (canc){
                    C[0] = 1;
                    button1.setBackground(new Color(0x0AB00A));
                    button2.setBackground(Color.white);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        button2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (canc) {
                    C[0] = 2;
                    button1.setBackground(Color.white);
                    button2.setBackground(new Color(0xBF0A0A));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }

    @Override
    public void show_answers() {
        if (!check()) {
            switch (C[0]) {
                case 1 -> button1.setBackground(new Color(0xFF7070));
                case 2 -> button2.setBackground(new Color(0xFF7070));
                case -1 -> {
                    button1.setBackground(Color.gray);
                    button2.setBackground(Color.gray);
                }
            }
            if (canc ){
                Qs.setText(Qs.getText() + " ×");
                Qs.setForeground(new Color(0xD07070));
            }
        } else {
            if (canc ){
                Qs.setText(Qs.getText() + " √");
                Qs.setForeground(new Color(0x00AA00));
            }
        }
        switch (answers) {
            case 1 -> button1.setBackground(new Color(0x80FF80));
            case 2 -> button2.setBackground(new Color(0x80FF80));
        }
        canc = false;
    }

    public boolean check() {
        return C[0] == answers;
    }
}
