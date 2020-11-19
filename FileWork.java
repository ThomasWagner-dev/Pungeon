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

    public static int[][] loadWorldFile(String screenName) {
        int[][] worldList = {};
        try {
            //Read the file.
            Scanner sc = new Scanner(new File("./data/worlds/" + screenName + ".world"));
            //Initalize indices.
            int line = 0;
            int element = 0;
            String[] lineValues = sc.nextLine().split(",");
            //Check if the line contains Values.
            while (lineValues.length != 0) {
                //Add each Value from the line to the 2D-Array.
                for (String type : lineValues) {
                    worldList[line][element] = Integer.parseInt(type);
                    element++;
                }
                element = 0;  
                //Read the next line.
                line++;
                lineValues = sc.nextLine().split(",");
            }
        }
        catch(Exception e) {
            System.err.println("Error while loading WorldFile");
            e.printStackTrace();
        }  
        return worldList;
    }

    public static Object loadBlock(int id) {
        try {
            //Read the file.
            Scanner sc = new Scanner(new File("./data/blocks/" + id + ".block"));
            //Determine the type of Block.
            String line = sc.nextLine();
            if (line == "tile") {
                //TODO think about data structure for saving tiles. 
            }
            else if (line == "wall") {
                //Read Breakability.
                boolean breakable = Boolean.parseBoolean(sc.nextLine());
                Wall wall = new Wall(breakable);
                //Read image.
                wall.setImage("./images/" + sc.nextLine());
                return wall;
            }
        }
        catch(Exception e) {
            System.err.println("Error while loading WorldFile");
            e.printStackTrace();
        }  
        return null;
    }

    public static HashMap<String, HashMap<String, Double>> getDmgMultiplier() {
        HashMap<String, HashMap<String, Double>> dmgMultiplier = new HashMap<>();
        try {
            //Read the file.
            Scanner sc = new Scanner(new File("./data/dmgMultipliers.stats"));
            //Put all types into a list.
            String[] types = sc.nextLine().split(",");
            String[] line;
            HashMap<String, Double> tmp;
            //Read dmg Multipliers for each type
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
