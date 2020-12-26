import greenfoot.*; 

import java.util.*;
import java.io.*;
public class FileWork
{
    private static InputStream readFile(String location) {
        return FileWork.class.getResourceAsStream(location);
    }
    
    
    public static void savePlayer(int slot, DungeonWorld world) {
        try {
            Player p = world.getObjects(Player.class).get(0);
            
            Writer wr = new FileWriter(new File("./data/saves/{}/player.sav".replace("{}", slot+"")));
            
            wr.write("[location]\n");
            wr.write("pos={x},{y}\n".replace("{x}", p.getX()+"").replace("{y}", p.getY()+""));
            wr.write("screen={}\n".replace("{}",world.activeScreen));
            wr.write("[cosmetics]\n");
            wr.write("skin={}\n".replace("{}", p.skin));
            wr.write("[inventory]\n");
            wr.write("weapon={}\n".replace("{}", p.selectedWeapon.name));
            wr.write("[status]\n");
            wr.write("hp={}\n".replace("{}", p.hp+""));
            wr.write("maxhp={}\n".replace("{}", p.maxhp+""));
            wr.close();
        }
        catch (Exception e) {
            System.err.println("Error while saving player");
            e.printStackTrace();
        }
    }
    
    
    /**
     * load the player into the world.
     */
    public static void loadPlayer(int slot, DungeonWorld world) {
        Player player = new Player();
        try {
            Scanner sc = new Scanner(readFile("./data/saves/{}/player.sav".replace("{}",slot+"")));
            String[] line;
            int[] pos = new int[2];
            while (sc.hasNextLine()) {
                line = sc.nextLine().split("=");
                if (!line[0].startsWith("[")) {
                    switch (line[0]) {
                        case "screen":
                            world.screens.get(line[1]).load(world);
                            break;
                        case "pos":
                            line = line[1].split(",");
                            pos = new int[] {Integer.parseInt(line[0]), Integer.parseInt(line[1])};
                            break;
                        case "skin":
                            player.skin=line[1];
                            break;
                        case "weapon":
                            player.selectWeapon(world.weapons.get(line[1]).clone());
                            break;
                        case "hp":
                            player.hp = Integer.parseInt(line[1]);
                            break;
                        case "maxhp":
                            player.maxhp = Integer.parseInt(line[1]);
                            break;
                        //add further save stuff here
                    }
                }
            }
            world.addObject(player, pos[0], pos[1]);
        }
        catch(Exception e) {
            System.err.println("Error while loading player into world");
            e.printStackTrace();
        }
    }
    

    public static ArrayList<ArrayList<String>> loadWorldFile(String screenName) {
        ArrayList<ArrayList<String>> worldList = new ArrayList<>();
        try {
            // Read the file.
            Scanner sc = new Scanner(readFile("./data/screens/" + screenName + ".world"));
            // Initalize indices.
            String[] lineValues;
            ArrayList<String> temp;
            // Check if the line contains Values.
            while(sc.hasNextLine())
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
            }
        }
        catch(Exception e) {
            System.err.println("Error while loading WorldFile");
            e.printStackTrace();
        }  
        return worldList;
    }

    /**
     * loads weapons from the ./data/weapons folder
     */
    public static HashMap<String, Weapon> loadAllWeapons() {
        HashMap<String, Weapon> weapons = new HashMap<>();
        File dir = new File("./data/weapons/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (!child.getName().endsWith(".wpn")) continue;
                weapons.put(child.getName().replaceAll("\\.\\w+$",""), loadWeapon(child));
            }
        }
        return weapons;
    }
    
    
    private static Weapon loadWeapon(File f) {
        String name="", descr="", type="", img="", hitbox = "";
        int range=0, dmg=0, speed=0, cooldown=0, angle=0;
        GreenfootImage tmp = new GreenfootImage("Invis.png");
        double scale = 1;
        boolean isMelee = true;
        try {
            Scanner sc = new Scanner(f);
            String[] line;
            while (sc.hasNextLine()) {
                line = sc.nextLine().split("=");
                switch (line[0]) {
                    case "name":
                        name = line[1];
                        break;
                    case "descr":
                        descr = line[1];
                        break;
                    case "range":
                        range = Integer.parseInt(line[1]);
                        break;
                    case "type":
                        type = line[1];
                        break;
                    case "speed":
                        speed = Integer.parseInt(line[1]);
                        break;
                    case "img":
                        img = line[1];
                        break;
                    case "cooldown":
                        cooldown = Integer.parseInt(line[1]);
                        break;
                    case "dmg":
                        dmg = Integer.parseInt(line[1]);
                        break;
                    case "hitbox":
                        hitbox = line[1];
                        tmp = new GreenfootImage(line[1]);
                        try {
                            angle = Integer.parseInt(line[2]);
                        }
                        catch(IndexOutOfBoundsException e) {}
                        break;
                    case "scale":
                        scale = Double.parseDouble(line[1]);
                        break;
                    case "melee":
                        isMelee = line[1].equals("true");
                        break;
                    case "ranged":
                        isMelee = line[1].equals("false");
                }
            }
            return new Weapon(f.getName().replace(".wpn",""), name, descr, range, dmg, type, speed, cooldown, img, scale, hitbox, isMelee, angle);
        }
        catch (Exception e) {
            System.err.println("Error while loading weapon from file {}".replace("{}", f.getPath()));
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * returns a hashmap of all blocks listed in ./data/blocks/
     */
    public static HashMap<String, Block> loadAllBlocks() {
          HashMap<String, Block> blocks = new HashMap<>();
          File dir = new File("./data/blocks/"); //creates a directory file of the blocks folder
          File[] directoryListing = dir.listFiles(); //retrieves all files present in the folder
          if (directoryListing != null) { //see else
              for (File child : directoryListing) {//loops though all files in the folder
                  if (!child.getName().endsWith(".block")) continue; //skips all files, which aint blocks. for easier disableing using the .disabled ending.
                  blocks.put(child.getName().replaceAll("\\.\\w+$",""), loadBlock(child)); //puts the block into the hashmap using its name as a key and a loaded Block Object as the value.
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
            String line = sc.nextLine(), trigger="", activeImg="", inactiveImg="", dmgType="";
            int range=0, dmg=0, cooldown=0;
            String[] sline;
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
                    while(sc.hasNextLine()) {
                        sline = sc.nextLine().split("=");
                        switch (sline[0]) {
                            case "trigger":
                                trigger = sline[1];
                                break;
                            case "activeImg":
                                activeImg = sline[1];
                                break;
                            case "inactiveImg":
                                inactiveImg = sline[1];
                                break;
                            case "range":
                                range = Integer.parseInt(sline[1]);
                                break;
                            case "dmg":
                                dmg = Integer.parseInt(sline[1]);
                                break;
                            case "dmgType":
                                dmgType = sline[1];
                                break;
                            case "cooldown":
                                cooldown = Integer.parseInt(sline[1]);
                                break;
                        }
                    }
                    return new Trap(range, inactiveImg, activeImg, dmg, dmgType, trigger, cooldown);
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
            Scanner sc = new Scanner(readFile("./data/dmgMultipliers.stats"));
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
    
    public static HashMap<String, Screen> loadAllScreens(HashMap<String, Block> blocks, HashMap<String, Enemy> enemies) {
        HashMap<String, Screen> screens = new HashMap<>();
        File dir = new File("./data/screens");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (!child.getName().endsWith(".world")) continue; // skips enemymaps at first, those are read in specific loaded
                screens.put(child.getName().replaceAll("\\.\\w+$",""), loadScreen(child, blocks, enemies));
            }
        }
        return screens;
    }
    
    public static Screen loadScreen(File f, HashMap<String, Block> blocks, HashMap<String, Enemy> enemies) {
        try {
            Scanner sc = new Scanner(f);
            ArrayList<ArrayList<String>> rawMap= new ArrayList<>();
            HashMap<String, String> adjScreens = new HashMap<>();
            Block background = new Tile();
            HashMap<Enemy, int[]> enemymap = loadEnemyMap(f.getName().replaceAll("\\.\\w+$",""), enemies);
            String[] line = sc.nextLine().split(",");
            while (!line[0].equals("###")) {
                rawMap.add(new ArrayList<String>(Arrays.asList(line)));
                line = sc.nextLine().split(",");
            }
            while(sc.hasNextLine()) {
                line = sc.nextLine().split(":");
                switch(line[0]) {
                    case "bg":
                        background = blocks.get(line[1]);
                        break;
                    default:
                        adjScreens.put(line[0],line[1].equals("none")? null : line[1]);
                        break;
                }
            }
            return new Screen(rawMap, enemymap, background, blocks, adjScreens);
        }
        catch(Exception e) {
            System.out.println("error");
            System.err.println("Error while loading screen {}:".replace("{}",f.getName()));
            e.printStackTrace();
            return null;
        }
    }
    
    public static HashMap<Enemy, int[]> loadEnemyMap(String screenName, HashMap<String, Enemy> origin) {
        HashMap<Enemy, int[]> enemies = new HashMap<>();
        System.out.println(screenName);
        try {
            Scanner sc = new Scanner(readFile("./data/screens/{}.enemymap".replace("{}",screenName)));
            String[] line;
            String type;
            while (sc.hasNextLine()) {
                line = sc.nextLine().split("=");
                type = line[0];
                line = line[1].split(",");
                enemies.put(origin.get(type).clone(), new int[] {Integer.parseInt(line[0]), Integer.parseInt(line[1])});
            }
        }
        catch (Exception e) {
            System.err.println("Error while loading enemies from File");
            e.printStackTrace();
        }
        System.out.println(enemies.keySet());
        return enemies;
    }
    
    public static HashMap<String, Enemy> loadAllEnemies(HashMap<String, Weapon> weapons) {
        HashMap<String, Enemy> enemies = new HashMap<>();
        File dir = new File("./data/enemies/"); //creates a directory file of the blocks folder
        File[] directoryListing = dir.listFiles(); //retrieves all files present in the folder
        if (directoryListing != null) { //see else
            for (File child : directoryListing) {//loops though all files in the folder
                if (!child.getName().endsWith(".enemy")) continue; //skips all files, which aint blocks. for easier disableing using the .disabled ending.
                enemies.put(child.getName().replaceAll("\\.\\w+$",""), loadEnemy(child, weapons)); //puts the block into the hashmap using its name as a key and a loaded Block Object as the value.
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
        return enemies;
    }
    
    public static Enemy loadEnemy(File f, HashMap<String, Weapon> weapons) {
        Enemy en = null;
        try {
            Scanner sc = new Scanner(f);
            String[] line = sc.nextLine().split("=");
            switch(line[1]) {
                case "melee":
                    en = new Melee(f.getName().replaceAll("\\.\\w+$",""));
                    break;
                case "ranged":
                    en = null; //new Skeleton();
                    break;
            }
            while(sc.hasNextLine()) {
                line = sc.nextLine().split("=");
                switch(line[0]) {
                    case "img":
                        en.setSprite(line[1]);
                        break;
                    case "weapon":
                        en.weapon = weapons.get(line[1]).clone();
                        break;
                    case "hp":
                        en.maxhp = Integer.parseInt(line[1]);
                        en.hp = en.maxhp;
                        break;
                    case "speed":
                        en.speed = Integer.parseInt(line[1]);
                        break;
                    case "type":
                        en.type = line[1];
                        break;
                }
            }
        }
        catch (Exception e) {
            System.err.println("Error while loading Enemy {}".replace("{}",f.getName()));
            e.printStackTrace();
        }
        return en;
    }
    
    public static HashMap<String, GreenfootSound> loadAllSounds() {
        HashMap<String, GreenfootSound> sounds = new HashMap<>();
        File dir = new File("./sounds/");
        File[] dirFiles = dir.listFiles();
        String name;
        String[] splittedName;
        if (dirFiles != null) {
            for (File child : dirFiles) {
                name = child.getName();
                if (!name.startsWith("snd_")) continue;
                splittedName = name.split("_");
                sounds.put(String.join("_",Arrays.copyOfRange(splittedName, 1, splittedName.length)), new GreenfootSound(name));
            }
        }
        
        return sounds;
    }
    
    public static HashMap<String, GreenfootSound> loadAllMusic() {
        HashMap<String, GreenfootSound> musics = new HashMap<>();
        File dir = new File("./sounds/");
        File[] dirFiles = dir.listFiles();
        String name;
        String[] splittedName;
        if (dirFiles != null) {
            for (File child : dirFiles) {
                name = child.getName();
                if (!name.startsWith("msc_")) continue;
                splittedName = name.split("_");
                musics.put(String.join("_",Arrays.copyOfRange(splittedName, 1, splittedName.length)).replaceAll("\\.\\w+$",""), new GreenfootSound(name));
            }
        }
        return musics;
    }

}
