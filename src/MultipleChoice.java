import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MultipleChoice extends ObjPlane {
    private final int[] C = {-1,-1,-1,-1};
    private int answers;
    private boolean canc = true;

    private JButton button1 = new JButton();
    private JButton button2 = new JButton();
    private JButton button3 = new JButton();
    private JButton button4 = new JButton();

    private JLabel Qs;

    public MultipleChoice(String[] choice, String Q, int answers) {
        setBorder(new TitledBorder("多选题"));
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
                if (canc) {
                    if (C[0] == -1){
                        C[0] = 1;
                        button1.setBackground(new Color(0xAAAA00));
                    } else {
                        C[0] = -1;
                        button1.setBackground(new Color(0xFFFFFF));
                    }
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
                    if (C[1] == -1){
                        C[1] = 1;
                        button2.setBackground(new Color(0xAAAA00));
                    } else {
                        C[1] = -1;
                        button2.setBackground(new Color(0xFFFFFF));
                    }
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
                    if (C[2] == -1){
                        C[2] = 1;
                        button3.setBackground(new Color(0xAAAA00));
                    } else {
                        C[2] = -1;
                        button3.setBackground(new Color(0xFFFFFF));
                    }
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
                    if (C[3] == -1){
                        C[3] = 1;
                        button4.setBackground(new Color(0xAAAA00));
                    } else {
                        C[3] = -1;
                        button4.setBackground(new Color(0xFFFFFF));
                    }
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

            if (canc ){
                Qs.setText(Qs.getText() + " ×");
                Qs.setForeground(new Color(0xD07070));
            }
        } else {
            if (canc){
                Qs.setText(Qs.getText() + " √");
                Qs.setForeground(new Color(0x00AA00));
            }
        }
        // 检查是否包含A
        if ((answers & 1) == 1) {
            button1.setBackground(Color.GREEN);
        }
        // 检查是否包含B
        if ((answers & 2) == 2) {
            button2.setBackground(Color.GREEN);
        }
        // 检查是否包含C
        if ((answers & 4) == 4) {
            button3.setBackground(Color.GREEN);
        }
        // 检查是否包含D
        if ((answers & 8) == 8) {
            button4.setBackground(Color.GREEN);
        }
        if (!check()){
            if (C[0] == 1) {
                if (button1.getBackground().equals(Color.GREEN)) {
                    button1.setBackground(new Color(0xFFAF00));
                } else {
                    button1.setBackground(new Color(0xFF0A0A));
                }
            }
            if (C[1] == 1) {
                if (button2.getBackground().equals(Color.GREEN)) {
                    button2.setBackground(new Color(0xFFAF00));
                } else {
                    button2.setBackground(new Color(0xFF0A0A));
                }
            }
            if (C[2] == 1) {
                if (button3.getBackground().equals(Color.GREEN)) {
                    button3.setBackground(new Color(0xFFAF00));
                } else {
                    button3.setBackground(new Color(0xFF0A0A));
                }
            }
            if (C[3] == 1) {
                if (button4.getBackground().equals(Color.GREEN)) {
                    button4.setBackground(new Color(0xFFAF00));
                } else {
                    button4.setBackground(new Color(0xFF0A0A));
                }
            }
        }
        canc = false;
    }

    public boolean check() {
        boolean return_ =  true;
        int[] x = new int[]{-1,-1,-1,-1};

            // 检查是否包含A
        if ((answers & 1) == 1) {
            x[0] = 1;
        }
        // 检查是否包含B
        if ((answers & 2) == 2) {
            x[1] = 1;
        }
        // 检查是否包含C
        if ((answers & 4) == 4) {
            x[2] = 1;
        }
        // 检查是否包含D
        if ((answers & 8) == 8) {
            x[3] = 1;
        }

        for (int i = 0; i < 4; i++) {
            if (!(x[i] == C[i])) {
                return_ =false;
                break;
            }
        }

        return return_;
    }
}
