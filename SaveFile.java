import java.util.*;
import java.io.*;
public class SaveFile  
{
    HashMap<Integer, HashMap<String, Object>> saves = new HashMap<>();
    public void load(int slot) {
        try {
            Scanner sc = new Scanner(new File("./saves/save{}.gfg".replace("{}",slot+"")));
            String content = sc.next();
            HashMap<String, Object> saveslot = new HashMap<>();
            String key, value;
            String[] z;
            for (String x : content.split("\n")) {
                z = x.split(":");
                key = z[0];
                value = z[1];
                
            }
        }
        catch (Exception e) {
            System.err.println("Error while loading file");
        }
    }
}
