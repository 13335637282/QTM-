import java.util.ArrayList;
import java.util.HashMap;

public class Test {
    private HashMap<String,String> property = new HashMap<>();
    private ArrayList<HashMap<String,String>> main = new ArrayList<>();


    public void User(HashMap<String,String> property, ArrayList<HashMap<String,String>> main) {
        this.property = property;
        this.main = main;
    }
}
