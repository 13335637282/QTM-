import javax.swing.border.TitledBorder;
import java.awt.*;

public class FillBlanks extends ObjPlane{
    public FillBlanks(String question,String[] answers) {
        setBorder(new TitledBorder("填空题"));
        for (String x : question.split("{blank}")) {
        }
    }

    @Override
    public boolean check() {
        return true;
    }
}
