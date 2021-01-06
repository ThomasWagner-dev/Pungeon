import greenfoot.*;

import java.util.*;
import java.io.*;

public class FileWork {
    public static Tag endTag = new Tag(Tag.Type.TAG_End, null, null);

    private static InputStream readFile(String location) {
        return FileWork.class.getResourceAsStream(location);
    }

    public static void savePlayer(int slot, DungeonWorld world) {
        try {
            Player p = world.getObjects(Player.class).get(0);

            Writer wr = new FileWriter(new File("./data/saves/{}/player.sav".replace("{}", slot + "")));

            wr.write("[location]\n");
            wr.write("pos={x},{y}\n".replace("{x}", p.getX() + "").replace("{y}", p.getY() + ""));
            wr.write("screen={}\n".replace("{}", world.activeScreen.name));
            wr.write("[cosmetics]\n");
            wr.write("skin={}\n".replace("{}", p.skin));
            wr.write("[inventory]\n");
            wr.write("weapon={}\n".replace("{}", p.selectedWeapon.name));
            wr.write("[status]\n");
            wr.write("hp={}\n".replace("{}", p.hp + ""));
            wr.write("maxhp={}\n".replace("{}", p.maxhp + ""));
            wr.close();
        } catch (Exception e) {
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
            Scanner sc = new Scanner(readFile("./data/saves/{}/player.sav".replace("{}", slot + "")));
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
                            pos = new int[]{Integer.parseInt(line[0]), Integer.parseInt(line[1])};
                            break;
                        case "skin":
                            player.skin = line[1];
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
        } catch (Exception e) {
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
            while (sc.hasNextLine()) {
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
        } catch (Exception e) {
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
                weapons.put(child.getName().replaceAll("\\.\\w+$", ""), loadWeapon(child));
            }
        }
        return weapons;
    }

    private static Weapon loadWeapon(File f) {
        String name = "", descr = "", type = "", img = "", hitbox = "";
        int range = 0, dmg = 0, speed = 0, cooldown = 0, angle = 0;
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
                        } catch (IndexOutOfBoundsException e) {
                        }
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
            return new Weapon(f.getName().replace(".wpn", ""), name, descr, range, dmg, type, speed, cooldown, img, scale, hitbox, isMelee, angle);
        } catch (Exception e) {
            System.err.println("Error while loading weapon from file {}".replace("{}", f.getPath()));
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, Weapon> loadAllWeapons(Tag weapons) {
        HashMap<String, Weapon> wpns = new HashMap<>();
        for (Tag wpn : (Tag[]) weapons.getValue()) {
            if (wpn.getName() == null) continue;
            wpns.put(wpn.getName(), loadWeapon(wpn));
        }
        return wpns;
    }

    public static Weapon loadWeapon(Tag t) {
        String name = "", descr = "", type = "", img = "", hitbox = "";
        int range = 0, dmg = 0, speed = 0, cooldown = 0, angle = 0;
        GreenfootImage tmp = new GreenfootImage("Invis.png");
        double scale = 1;
        boolean isMelee = true;
        try {
            for (Tag tag : (Tag[]) t.getValue()) {
                if (tag.getName() == null) continue;
                switch (tag.getName()) {
                    case "name":
                        name = (String) tag.getValue();
                        break;
                    case "descr":
                        descr = (String) tag.getValue();
                        break;
                    case "range":
                        range = (Integer) (tag.getValue());
                        break;
                    case "type":
                        type = (String) tag.getValue();
                        break;
                    case "speed":
                        speed = (Integer) (tag.getValue());
                        break;
                    case "img":
                        img = (String) tag.getValue();
                        break;
                    case "cooldown":
                        cooldown = (Integer) (tag.getValue());
                        break;
                    case "dmg":
                        dmg = (Integer) (tag.getValue());
                        break;
                    case "hitbox":
                        hitbox = (String) tag.getValue();
                        tmp = new GreenfootImage((String) tag.getValue());
                        break;
                    case "angle":
                        angle = (Integer) (tag.getValue());
                        break;
                    case "scale":
                        scale = (Double) (tag.getValue());
                        break;
                    case "melee":
                        isMelee = tag.getValue().equals("true");
                        break;
                    case "ranged":
                        isMelee = tag.getValue().equals("false");
                }
            }
            System.out.println(angle);
            return new Weapon(t.getName().replace(".wpn", ""), name, descr, range, dmg, type, speed, cooldown, img, scale, hitbox, isMelee, angle);
        } catch (Exception e) {
            System.err.println("Error while loading weapon from tag {}".replace("{}", t.getName()));
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
                if (!child.getName().endsWith(".block"))
                    continue; //skips all files, which aint blocks. for easier disableing using the .disabled ending.
                blocks.put(child.getName().replaceAll("\\.\\w+$", ""), loadBlock(child)); //puts the block into the hashmap using its name as a key and a loaded Block Object as the value.
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
     *
     * @see FileWork#loadBlock(File f)
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
            String line = sc.nextLine(), trigger = "", activeImg = "", inactiveImg = "", dmgType = "";
            int range = 0, dmg = 0, cooldown = 0;
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
                    while (sc.hasNextLine()) {
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
            //img.scale(DungeonWorld.pixelSize, DungeonWorld.pixelSize);
            block.setImage(img);
        } catch (Exception e) {
            System.err.println("Error while loading Block File");
            e.printStackTrace();
        }
        return block;
    }

    public static HashMap<String, Block> loadAllBlocks(Tag blocks) {
        HashMap<String, Block> blks = new HashMap<>();
        for (Tag blk : (Tag[]) blocks.getValue()) {
            if (blk.getName() == null) continue;
            blks.put(blk.getName(), loadBlock(blk));
        }
        return blks;
    }

    public static Block loadBlock(Tag blk) {
        Block block = null;
        try {
            //Read the file.
            //Determine the type of Block.
            String trigger = "", activeImg = "", inactiveImg = "", dmgType = "";
            int range = 0, dmg = 0, cooldown = 0;
            switch ((String) blk.findNextTag("type").getValue()) {
                case "tile":
                    block = new Tile();//TODO think about data structure for saving tiles.
                    break;
                case "wall":
                    //Read Breakability.
                    boolean breakable = blk.findNextTag("breakable").equals("true");
                    block = new Wall(breakable);
                    break;
                case "trap":
                    for (Tag t : (Tag[]) blk.getValue()) {
                        if (t.getName() == null) continue;
                        switch (t.getName()) {
                            case "trigger":
                                trigger = (String) t.getValue();
                                break;
                            case "activeImg":
                                activeImg = (String) t.getValue();
                                break;
                            case "inactiveImg":
                                inactiveImg = (String) t.getValue();
                                break;
                            case "range":
                                range = (Integer) t.getValue();
                                break;
                            case "dmg":
                                dmg = (Integer) t.getValue();
                                break;
                            case "dmgType":
                                dmgType = (String) t.getValue();
                                break;
                            case "cooldown":
                                cooldown = (Integer) t.getValue();
                                break;
                        }
                    }
                    return new Trap(range, inactiveImg, activeImg, dmg, dmgType, trigger, cooldown);
            }
            //Read image.
            GreenfootImage img = new GreenfootImage("./images/" + (String) blk.get("img"));
            //img.scale(DungeonWorld.pixelSize, DungeonWorld.pixelSize);
            block.setImage(img);
        } catch (Exception e) {
            System.err.println("Error while loading Block File");
            e.printStackTrace();
        }
        return block;
    }

    public static HashMap<String, HashMap<String, Double>> getDmgMultiplier() {
        HashMap<String, HashMap<String, Double>> dmgMultiplier = new HashMap<>();
        try {
            //Read the file.
            Scanner sc = new Scanner(readFile("./data/stats/dmgMultipliers.stats"));
            //Put all types into a list.
            String[] types = sc.nextLine().split(",");
            String[] line;
            HashMap<String, Double> tmp;
            //Read dmg Multipliers for each type
            for (String type : types) {
                tmp = new HashMap<>();
                line = sc.nextLine().split(",");
                for (int i = 0; i < types.length; i++) {
                    tmp.put(types[i], Double.parseDouble(line[i]));
                }
                dmgMultiplier.put(type, tmp);
            }
        } catch (Exception e) {
            System.err.println("Error while loading Damage Multipliers");
            e.printStackTrace();
        }
        return dmgMultiplier;
    }

    public static HashMap<String, HashMap<String, Double>> getDmgMultiplier(Tag dmgMultipliers) {
        HashMap<String, HashMap<String, Double>> dmgMultiplier = new HashMap<>();
        HashMap<String, Double> mp;
        for (Tag t : (Tag[]) dmgMultipliers.getValue()) {
            if (t.getName() == null) continue;
            mp = new HashMap<>();
            for (Tag multi : (Tag[]) t.getValue()) {
                mp.put(multi.getName(), (Double) multi.getValue());
            }
            dmgMultiplier.put(t.getName(), mp);
        }
        return dmgMultiplier;
    }

    public static HashMap<String, Screen> loadAllScreens(HashMap<String, Block> blocks, HashMap<String, Enemy> enemies, ImageGenerator imgGen) {
        HashMap<String, Screen> screens = new HashMap<>();
        File dir = new File("./data/screens");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (!child.getName().endsWith(".world"))
                    continue; // skips enemymaps at first, those are read in specific loaded
                screens.put(child.getName().replaceAll("\\.\\w+$", ""), loadScreen(child, blocks, enemies, imgGen));
            }
        }
        return screens;
    }

    public static Screen loadScreen(File f, HashMap<String, Block> blocks, HashMap<String, Enemy> enemies, ImageGenerator imgGen) {
        try {
            Scanner sc = new Scanner(f);
            ArrayList<ArrayList<String>> rawMap = new ArrayList<>();
            HashMap<String, String> adjScreens = new HashMap<>();
            Block background = new Tile();
            HashMap<Enemy, int[]> enemymap = loadEnemyMap(f.getName().replaceAll("\\.\\w+$", ""), enemies);
            String[] line = sc.nextLine().split(",");
            while (!line[0].equals("###")) {
                rawMap.add(new ArrayList<String>(Arrays.asList(line)));
                line = sc.nextLine().split(",");
            }
            while (sc.hasNextLine()) {
                line = sc.nextLine().split(":");
                switch (line[0]) {
                    case "bg":
                        background = blocks.get(line[1]);
                        break;
                    default:
                        adjScreens.put(line[0], line[1].equals("none") ? null : line[1]);
                        break;
                }
            }
            return new Screen(f.getName().replaceAll("\\.\\w+$", ""), rawMap, enemymap, background, blocks, adjScreens, imgGen);
        } catch (Exception e) {
            System.out.println("error");
            System.err.println("Error while loading screen {}:".replace("{}", f.getName()));
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, Screen> loadAllScreens(Tag screens, HashMap<String, Block> blocks, HashMap<String, Enemy> enemies, ImageGenerator imgGen) {
        HashMap<String, Screen> scrs = new HashMap<>();
        for (Tag scr : (Tag[]) screens.getValue()) {
            if (scr.getName() == null) continue;
            scrs.put(scr.getName(), loadScreen(scr, blocks, enemies, imgGen));
        }
        return scrs;
    }

    public static Screen loadScreen(Tag scr, HashMap<String, Block> blocks, HashMap<String, Enemy> enemies, ImageGenerator imgGen) {
        try {
            ArrayList<ArrayList<String>> rawMap = new ArrayList<>();
            ArrayList<String> tmp;
            HashMap<String, String> adjScreens = new HashMap<>();
            Block background = new Tile();
            HashMap<Enemy, int[]> enemymap = new HashMap<>();

            for (Tag t : (Tag[]) scr.getValue()) {
                if (t.getName() == null) continue;
                switch (t.getName()) {
                    case "map":
                        for (String l : ((String) scr.get("map")).split("\n")) {
                            tmp = new ArrayList<>();
                            Arrays.asList(l.split(",")).forEach(tmp::add);
                            rawMap.add(tmp);
                        }
                        break;
                    case "bg":
                        background = blocks.get((String) t.getValue());
                        break;
                    case "enemymap":
                        for (Tag x : (Tag[]) t.getValue()) {
                            if (x.getName() == null) continue;
                            enemymap.put(enemies.get((String) x.get("type")).clone(), new int[]{(Integer) x.get("x"), (Integer) x.get("y")});
                        }
                        break;
                    default:
                        adjScreens.put(t.getName(), ((String) t.getValue()).equals("none") ? null : (String) t.getValue());
                        break;
                }
            }
            return new Screen(scr.getName(), rawMap, enemymap, background, blocks, adjScreens, imgGen);
        } catch (Exception e) {
            System.out.println("error");
            System.err.println("Error while loading screen {}:".replace("{}", scr.getName()));
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<Enemy, int[]> loadEnemyMap(String screenName, HashMap<String, Enemy> origin) {
        HashMap<Enemy, int[]> enemies = new HashMap<>();
        try {
            Scanner sc = new Scanner(readFile("./data/screens/{}.enemymap".replace("{}", screenName)));
            String[] line;
            String type;
            while (sc.hasNextLine()) {
                line = sc.nextLine().split("=");
                type = line[0];
                line = line[1].split(",");
                enemies.put(origin.get(type).clone(), new int[]{Integer.parseInt(line[0]), Integer.parseInt(line[1])});
            }
        } catch (Exception e) {
            System.err.println("Error while loading enemies from File");
            e.printStackTrace();
        }
        return enemies;
    }

    public static HashMap<String, Enemy> loadAllEnemies(HashMap<String, Weapon> weapons) {
        HashMap<String, Enemy> enemies = new HashMap<>();
        File dir = new File("./data/enemies/"); //creates a directory file of the blocks folder
        File[] directoryListing = dir.listFiles(); //retrieves all files present in the folder
        if (directoryListing != null) { //see else
            for (File child : directoryListing) {//loops though all files in the folder
                if (!child.getName().endsWith(".enemy"))
                    continue; //skips all files, which aint blocks. for easier disableing using the .disabled ending.
                enemies.put(child.getName().replaceAll("\\.\\w+$", ""), loadEnemy(child, weapons)); //puts the block into the hashmap using its name as a key and a loaded Block Object as the value.
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
            switch (line[1]) {
                case "collider":
                    en = new CollidingEnemy(f.getName().replaceAll("\\.\\w+$", ""));
                    break;
                case "everythingelse":
                default:
                    en = new NonCollidingEnemy(f.getName().replaceAll("\\.\\w+$", ""));
                    ; //new Skeleton();
                    break;
            }
            while (sc.hasNextLine()) {
                line = sc.nextLine().split("=");
                switch (line[0]) {
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
        } catch (Exception e) {
            System.err.println("Error while loading Enemy {}".replace("{}", f.getName()));
            e.printStackTrace();
        }
        return en;
    }

    public static HashMap<String, Enemy> loadAllEnemies(Tag enemies, HashMap<String, Weapon> weapons) {
        HashMap<String, Enemy> enes = new HashMap<>();
        for (Tag en : (Tag[]) enemies.getValue()) {
            if (en.getName() == null) continue;
            enes.put(en.getName(), loadEnemy(en, weapons));
        }
        return enes;
    }

    public static Enemy loadEnemy(Tag t, HashMap<String, Weapon> weapons) {
        Enemy en = null;
        try {
            switch ((String) t.get("col")) {
                case "true":
                    en = new CollidingEnemy(t.getName().replaceAll("\\.\\w+$", ""));
                    break;
                case "everythingelse":
                default:
                    en = new NonCollidingEnemy(t.getName().replaceAll("\\.\\w+$", ""));
                    ; //new Skeleton();
                    break;
            }
            for (Tag tag : (Tag[]) t.getValue()) {
                if (tag.getName() == null) continue;
                switch (tag.getName()) {
                    case "img":
                        en.setSprite((String) tag.getValue());
                        break;
                    case "weapon":
                        en.weapon = weapons.get((String) tag.getValue()).clone();
                        break;
                    case "hp":
                        en.maxhp = (Integer) tag.getValue();
                        en.hp = en.maxhp;
                        break;
                    case "speed":
                        en.speed = (Integer) tag.getValue();
                        break;
                    case "type":
                        en.type = (String) tag.getValue();
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error while loading Enemy {}".replace("{}", t.getName()));
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
                sounds.put(String.join("_", Arrays.copyOfRange(splittedName, 1, splittedName.length)), new GreenfootSound(name));
            }
        }

        return sounds;
    }

    public static HashMap<String, Item> loadAllItems() {
        HashMap<String, Item> items = new HashMap<>();
        File dir = new File("./data/items");
        File[] dirFiles = dir.listFiles();
        String name;
        if (dirFiles != null) {
            for (File child : dirFiles) {
                name = child.getName();
                if (!name.endsWith(".itm")) continue;
                items.put(name.replaceAll("\\.\\w+$", ""), loadItem(child));
            }
        }
        return items;
    }

    public static Item loadItem(File f) {
        try {
            Scanner sc = new Scanner(f);
            return new Item(sc.nextLine(), Boolean.parseBoolean(sc.nextLine()), sc.nextLine(), sc.nextLine(), sc.nextLine());
        } catch (Exception e) {
            System.err.println("Error while loading Item: {}".replace("{}", f.getName()));
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, Item> loadAllItems(Tag items) {
        HashMap<String, Item> itms = new HashMap<>();
        for (Tag itm : (Tag[]) items.getValue()) {
            if (itm.getName() == null) continue;
            itms.put(itm.getName(), loadItem(itm));
        }
        return itms;
    }

    public static Item loadItem(Tag t) {
        try {
            return new Item(
                    (String) t.get("name"),
                    Boolean.parseBoolean((String) t.get("instant")),
                    (String) t.get("changing"),
                    t.get("amount") instanceof String ? (String) t.get("amount") : ((Integer) t.get("amount")) + "",
                    (String) t.get("img")
            );
        } catch (Exception e) {
            System.err.println("Error while loading Item: {}".replace("{}", t.getName()));
            e.printStackTrace();
            return null;
        }
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
                musics.put(String.join("_", Arrays.copyOfRange(splittedName, 1, splittedName.length)).replaceAll("\\.\\w+$", ""), new GreenfootSound(name));
            }
        }
        return musics;
    }

    public static Tag getData() {
        Tag t = null;
        try {
            t = Tag.readFrom(readFile("./data.nbt"));
        } catch (Exception e) {
            System.err.println("Error while loading data from file");
            e.printStackTrace();
        }
        return t;
    }

    public static Tag initData() {
        Tag data = new Tag(Tag.Type.TAG_Compound, "data", new Tag[]{endTag});
        Tag loottables = new Tag("loottables", Tag.Type.TAG_Compound);
        data.addTag(loottables);
        return data;
    }

    public static void dmgToData(Tag data, HashMap<String, HashMap<String, Double>> dmg) {
        HashMap<String, Double> tmp;
        Tag dmgMultipliers = new Tag(Tag.Type.TAG_Compound, "dmgMultipliers", new Tag[]{endTag}), x;
        for (String key : dmg.keySet()) {
            tmp = dmg.get(key);
            x = new Tag(Tag.Type.TAG_Compound, key, new Tag[]{endTag});
            for (String mp : tmp.keySet()) {
                x.addTag(new Tag(Tag.Type.TAG_Double, mp, tmp.get(mp)));
            }
            dmgMultipliers.addTag(x);
        }
        data.addTag(dmgMultipliers);
    }

    public static void writeData(Tag data) {
        try {
            data.writeTo(new FileOutputStream("./data.nbt"));
        } catch (Exception e) {
            System.err.println("Error while writing data to file: ");
            e.printStackTrace();
        }
    }
}
