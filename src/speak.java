import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

public class speak {
    public speak(String s) {
        try {
            FileWriter writer = new FileWriter("speak.vbs", Charset.forName("GBK"));
            writer.write("CreateObject(\"SAPI.SpVoice\").Speak \""+s+"\"");
            writer.close();
            Runtime.getRuntime().exec("cscript //NoLogo speak.vbs");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //new File("speak.vbs").delete();
    }
}
