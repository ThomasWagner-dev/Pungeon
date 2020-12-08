import greenfoot.*; 

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

    public static ArrayList<ArrayList<String>> loadWorldFile(String screenName) {
        ArrayList<ArrayList<String>> worldList = new ArrayList<>();
        try {
            // Read the file.
            System.out.println(new File("./data/worlds/" + screenName + ".world").exists());
            Scanner sc = new Scanner(new File("./data/worlds/" + screenName + ".world"));
            // Initalize indices.
            String[] lineValues;
            ArrayList<String> temp;
            // Check if the line contains Values.
            int i = 0;
            do
            {
                // Read the next line.
                lineValues = sc.nextLine().split(",");
                // Add each Value from the line to the ArrayList. 
                temp = new ArrayList<>();
                for (String type : lineValues) {
                    temp.add(type);
                }   
                // Add the line to the worldList.
                worldList.add(temp); 
                i++;
            } while (i < 5);
        }
        catch(Exception e) {
            System.err.println("Error while loading WorldFile");
            e.printStackTrace();
        }  
        return worldList;
    }

    /**
     * returns a hashmap of all blocks listed in ./data/blocks/
     */
    public static HashMap<String, Block> loadAllBlocks() {
          HashMap<String, Block> blocks = new HashMap<>();
          File dir = new File("./data/blocks/"); //creates a directory file of the blocks folder
          File[] directoryListing = dir.listFiles(); //retrieves all files present in the folder
          String[] splittedName;
          String name;
          if (directoryListing != null) { //see else
              for (File child : directoryListing) {//loops though all files in the folder
                  if (!child.getName().endsWith(".block")) continue; //skips all files, which aint blocks. for easier disableing using the .disabled ending.
                  splittedName = child.getName().split("\\.");//splits name, to only get the name, without ending (this case .block)
                  name = String.join(".", Arrays.copyOfRange(splittedName, 0, splittedName.length-1));
                  blocks.put(name, loadBlock(child)); //puts the block into the hashmap using its name as a key and a loaded Block Object as the value.
              }
          } else {
              // Handle the case where dir is not really a directory.
              // Checking dir.isDirectory() above would not be sufficient
              // to avoid race conditions with another process that deletes
              // directories.
          }
          return blocks;
    }
    
    /**
     * Only used when loading manually.
     * @see loadBlock(File f)
     */
    public static Block loadBlock(String id) {
        return loadBlock(new File("./data/blocks/" + id + ".block"));
    }
    
    /**
     * loads a block from a file.
     */
    public static Block loadBlock(File f) {
        Block block = null;
        try {
            //Read the file.
            Scanner sc = new Scanner(f);
            //Determine the type of Block.
            String line = sc.nextLine();
            
            switch (line) {
                case "tile":
                    block = new Tile();//TODO think about data structure for saving tiles. 
                    break;
                case "wall":
                    //Read Breakability.
                    boolean breakable = Boolean.parseBoolean(sc.nextLine());
                    block = new Wall(breakable);
                    break;
                case "trap":
                    block = new Tile();
                    break;
            }
            //Read image.
            GreenfootImage img = new GreenfootImage("./images/" + sc.nextLine());
            img.scale(DungeonWorld.pixelSize, DungeonWorld.pixelSize);
            block.setImage(img);
        }
        catch(Exception e) {
            System.err.println("Error while loading Block File");
            e.printStackTrace();
        }  
        return block;
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
