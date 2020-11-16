import java.util.*;
import java.io.*;
public class FileWork
{
    public static HashMap<Integer, HashMap<String, Object>> saves = new HashMap<>();
    public static void load(int slot) {
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
            e.printStackTrace();
        }
    }
    
    public static HashMap<String, HashMap<String, Double>> getDmgMultiplier() {
        HashMap<String, HashMap<String, Double>> dmgMultiplier = new HashMap<>();
        try {
            Scanner sc = new Scanner(new File("./data/dmgMultipliers.stats"));
            String[] types = sc.nextLine().split(","),
                line;
            HashMap<String, Double> tmp;
            for (String type : types) {
                tmp = new HashMap<>();
                line = sc.nextLine().split(",");
                for (int i = 0; i<types.length; i++) {
                    tmp.put(types[i], Double.parseDouble(line[i]));
                }
                dmgMultiplier.put(type, tmp);
            }
        }
        catch(Exception e) {
            System.err.println("Error while loading Damage Multipliers");
            e.printStackTrace();
        }
        
        return dmgMultiplier;
    }
}
