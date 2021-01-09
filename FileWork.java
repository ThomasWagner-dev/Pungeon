import greenfoot.*;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class FileWork {
    public static Tag endTag = new Tag(Tag.Type.TAG_End, null, null);

    /**
     * returns an Inputstream from the file location, which works in both, folders and jar files
     * @param location locatoion/file the stream is generated from
     * @return an input stream of the given file locatoin
     */
    private static InputStream readFile(String location) {
        return FileWork.class.getResourceAsStream(location);
    }

    /**
     * saves the player to the save file
     * @param slot the save slot
     * @param world the world the player is from
     */
    public static void savePlayer(int slot, DungeonWorld world) {
        try {
            Player p = world.getObjects(Player.class).get(0);

            Writer wr = new FileWriter(new File("./data/saves/{}/player.sav".replace("{}", slot + "")));

            wr.write("[location]\n");
            wr.write("pos={x},{y}\n".replace("{x}", p.getX() + "").replace("{y}", p.getY() + ""));
            wr.write("screen={}\n".replace("{}", world.activeScreen.name));
            wr.write("[cosmetics]\n");
            wr.write("name={}\n".replace("{}", p.displayname));
            wr.write("gender={gnd};{pro}\n".replace("{gnd}", p.gender.s).replace("{pro}", String.join(",", p.gender.pronouns)));
            wr.write("skin={}\n".replace("{}", p.skin));
            wr.write("[inventory]\n");
            wr.write("inv_weapons={}\n".replace("{}", String.join(",", p.inv_weapons.stream().map(w -> w.name).collect(Collectors.toList()))));
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
    public static void loadPlayer(int slot, DungeonWorld world, Player player) {
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
                            for (Weapon wpn : player.inv_weapons) {
                                if (wpn.name.equals(line[1])) {
                                    player.selectWeapon(wpn);
                                    break;
                                }
                            }
                            if (player.selectedWeapon == null) {
                                player.selectWeapon(player.inv_weapons.get(0));
                            }
                            break;
                        case "inv_weapons":
                            System.out.println(line[1]);
                            for (String wpn : line[1].split(",")) {
                                player.inv_weapons.add(world.weapons.get(wpn).clone());
                            }
                            System.out.println(player.inv_weapons);
                            break;
                        case "hp":
                            player.hp = Integer.parseInt(line[1]);
                            break;
                        case "maxhp":
                            player.maxhp = Integer.parseInt(line[1]);
                            break;
                        case "gender":
                            String[] g = line[1].split(";");
                            player.gender = Gender.from(g[0]);
                            player.gender.pronouns = g[1].split(",");
                            break;
                        case "name":
                            player.displayname = line[1];
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

    /**
     * Loads all weapons into a hashmap of name weapon from the given weapons tag
     * @param weapons the tag the weapons are stored in
     * @return a hashmap of weapons as Weaponname -> Weapon
     */
    public static HashMap<String, Weapon> loadAllWeapons(Tag weapons) {
        HashMap<String, Weapon> wpns = new HashMap<>();
        for (Tag wpn : (Tag[]) weapons.getValue()) {
            if (wpn.getName() == null) continue;
            wpns.put(wpn.getName(), loadWeapon(wpn));
        }
        return wpns;
    }

    /**
     * Loads a single weapon from a given tag
     * @param t the tag the weapon get generated off
     * @return a weapon
     */
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
            return new Weapon(t.getName().replace(".wpn", ""), name, descr, range, dmg, type, speed, cooldown, img, scale, hitbox, isMelee, angle);
        } catch (Exception e) {
            System.err.println("Error while loading weapon from tag {}".replace("{}", t.getName()));
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads all blocks into a hashmap of name block from the given blocks tag
     * @param blocks the tag the blocks are stored in
     * @return a hashmap of blocks as Blockname -> Block
     */
    public static HashMap<String, Block> loadAllBlocks(Tag blocks) {
        HashMap<String, Block> blks = new HashMap<>();
        for (Tag blk : (Tag[]) blocks.getValue()) {
            if (blk.getName() == null) continue;
            blks.put(blk.getName(), loadBlock(blk));
        }
        return blks;
    }

    /**
     * Loads a single block from a given tag
     * @param blk the tag the block gets generated off
     * @return a block
     */
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

    /**
     * Generates the damage Multipliers from the given tag.
     * @param dmgMultipliers the tag the hashmap gets generated from
     * @return the hashmap of damage multipliers
     */
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

    /**
     * Generates a hashmap of screens (the world) as screenname -> screen from the screens tag.
     * @param screens the tag the screens are located in
     * @param blocks all blocks available, and able to be placed in a screen
     * @param enemies all enemies available, and able to be present in a screen
     * @param imgGen an image generator for connected textures.
     * @param world the world. Needed for noise generation
     * @return returns a hashmap of screens as screenname -> screen
     */
    public static HashMap<String, Screen> loadAllScreens(Tag screens, HashMap<String, Block> blocks, HashMap<String, Enemy> enemies, ImageGenerator imgGen, DungeonWorld world) {
        HashMap<String, Screen> scrs = new HashMap<>();
        for (Tag scr : (Tag[]) screens.getValue()) {
            if (scr.getName() == null) continue;
            scrs.put(scr.getName(), loadScreen(scr, blocks, enemies, imgGen, world));
        }
        return scrs;
    }

    /**
     * generates a single screen from a tag.
     * @param scr the tag the screen is generated from
     * @param blocks all available blocks
     * @param enemies all available enemies
     * @param imgGen image generator for connected textures
     * @param world the world, needed for noise generation
     * @return returns a brand new screen
     */
    public static Screen loadScreen(Tag scr, HashMap<String, Block> blocks, HashMap<String, Enemy> enemies, ImageGenerator imgGen, DungeonWorld world) {
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
            return new Screen(scr.getName(), rawMap, enemymap, background, blocks, adjScreens, imgGen, world);
        } catch (Exception e) {
            System.out.println("error");
            System.err.println("Error while loading screen {}:".replace("{}", scr.getName()));
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads all enemies from the tag.
     * @param enemies the tag, the enemies are generated from
     * @param weapons all available weapons the enemies can have.
     * @return returns a hashmap of enemies as name -> enemy
     */
    public static HashMap<String, Enemy> loadAllEnemies(Tag enemies, HashMap<String, Weapon> weapons) {
        HashMap<String, Enemy> enes = new HashMap<>();
        for (Tag en : (Tag[]) enemies.getValue()) {
            if (en.getName() == null) continue;
            enes.put(en.getName(), loadEnemy(en, weapons));
        }
        return enes;
    }

    /**
     * generates a single enemy from a tag
     * @param t the tag the enemy gets generated from
     * @param weapons all available weapons the enemy might have
     * @return a fabric new Enemy
     */
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

    /**
     * generates all items from the items tag.
     * @param items the tag items are stored in
     * @return a hashmap of items as name -> item
     */
    public static HashMap<String, Item> loadAllItems(Tag items) {
        HashMap<String, Item> itms = new HashMap<>();
        for (Tag itm : (Tag[]) items.getValue()) {
            if (itm.getName() == null) continue;
            itms.put(itm.getName(), loadItem(itm));
        }
        return itms;
    }

    /**
     * loads a single item from a tag.
     * @param t the tag the item gets generated from
     * @return an unused item
     */
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

    /**
     * loads all sounds from the sounds folder.
     * @return a hashmap of sounds as name -> sound
     */
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

    /**
     * loads all music from the sounds folder
     * @return a hashmap of music as name -> music
     */
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

    /**
     * returns the data tag from the data.nbt file
     * @return the tag of data.
     */
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

    /**
     * Loads all fonts from the assets folder as Greenfoot.Font
     * @return Returns an hashmap of fonts as name -> font
     */
    public static HashMap<String, Font> loadAllFonts() {
        HashMap<String, Font> fonts = new HashMap<>();
        File dir = new File("./assets");
        File[] dirFiles = dir.listFiles();
        String name;
        if (dirFiles != null) {
            for (File child : dirFiles) {
                name = child.getName();
                if (!name.endsWith(".ttf")) continue;
                try {
                    fonts.put(name.replaceAll("\\.\\w+$", ""), FontLoader.loadFont(child, 24));
                }
                catch (Exception e) {
                    System.err.println("Error while loading font {}".replace("{}", name));
                    e.printStackTrace();
                }
            }
        }
        return fonts;
    }

    /**
     * Loads all fonts from the assets folder as java.awt.Font
     * This is needed as Greenfoot sucks and one requies a huge script using awt fonts to center some text
     * @return an hashmap of fonts as name -> font
     */
    public static HashMap<String, java.awt.Font> loadAwtFonts() {
        HashMap<String, java.awt.Font> fonts = new HashMap<>();
        File dir = new File("./assets");
        File[] dirFiles = dir.listFiles();
        String name;
        if (dirFiles != null) {
            for (File child : dirFiles) {
                name = child.getName();
                if (!name.endsWith(".ttf")) continue;
                try {
                    fonts.put(name.replaceAll("\\.\\w+$", ""), java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, child));
                }
                catch (Exception e) {
                    System.err.println("Error while loading font {}".replace("{}", name));
                    e.printStackTrace();
                }
            }
        }
        return fonts;
    }
}
