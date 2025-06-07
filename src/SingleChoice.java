import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SingleChoice extends ObjPlane {
    private final int[] C = {-1};
    private int answers;
    private boolean canc = true;

    private JButton button1 = new JButton();
    private JButton button2 = new JButton();
    private JButton button3 = new JButton();
    private JButton button4 = new JButton();

    private JLabel Qs;

    public SingleChoice(String[] choice, String Q, int answers) {
        setBorder(new TitledBorder("单选题"));
        this.answers = answers;
        setLayout(new BorderLayout());

        JPanel cs = new JPanel(new GridLayout(4,1));
        add(cs,BorderLayout.CENTER);

        Qs = new JLabel(Q);
        Qs.setFont(new Font("宋体", Font.BOLD,15));
        cs.add(Qs);

        button1.setText(choice[0]);
        button2.setText(choice[1]);
        button3.setText(choice[2]);
        button1.setFont(new Font("宋体", Font.PLAIN,15));
        button2.setFont(new Font("宋体", Font.PLAIN,15));
        button3.setFont(new Font("宋体", Font.PLAIN,15));
        button4.setFont(new Font("宋体", Font.PLAIN,15));

        cs.add(button1);
        cs.add(button2);
        cs.add(button3);

        button1.setBackground(Color.white);
        button2.setBackground(Color.white);
        button3.setBackground(Color.white);
        button4.setBackground(Color.white);

        if (choice.length == 4) {
            cs.setLayout(new GridLayout(5,1));
            button4.setText(choice[3]);
            cs.add(button4);
        }
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
                    button1.setBackground(new Color(0xAAAA00));
                    button2.setBackground(Color.white);
                    button3.setBackground(Color.white);
                    button4.setBackground(Color.white);
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
                    button2.setBackground(new Color(0xAAAA00));
                    button3.setBackground(Color.white);
                    button4.setBackground(Color.white);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        button3.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (canc) {
                    C[0] = 3;
                    button1.setBackground(Color.white);
                    button2.setBackground(Color.white);
                    button3.setBackground(new Color(0xAAAA00));
                    button4.setBackground(Color.white);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        button4.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (canc) {
                    C[0] = 4;
                    button1.setBackground(Color.white);
                    button2.setBackground(Color.white);
                    button3.setBackground(Color.white);
                    button4.setBackground(new Color(0xAAAA00));
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
                case 3 -> button3.setBackground(new Color(0xFF7070));
                case 4 -> button4.setBackground(new Color(0xFF7070));
                case -1 -> {
                    button1.setBackground(Color.gray);
                    button2.setBackground(Color.gray);
                    button3.setBackground(Color.gray);
                    button4.setBackground(Color.gray);
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
            case 3 -> button3.setBackground(new Color(0x80FF80));
            case 4 -> button4.setBackground(new Color(0x80FF80));
        }
        canc = false;
    }

    public boolean check() {
        return C[0] == answers;
    }
}
