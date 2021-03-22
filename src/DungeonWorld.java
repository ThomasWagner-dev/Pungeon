

import java.util.*;
import java.util.List;

/**
 * This is the world...
 */
public class DungeonWorld extends World {
    public final HashMap<String, HashMap<String, Double>> dmgMultiplier;
    public final HashMap<String, Weapon> weapons;
    public final HashMap<String, Block> blocks;
    public final HashMap<String, Enemy> enemies;
    public final HashMap<String, Screen> screens;
    public final HashMap<String, RawScreen> rawscreens;
    public final HashMap<String, Item> items;
    public final HashMap<String, Loottable> loottables;
    public final HashMap<String, java.awt.Font> awtfonts;
    public final String runningOS;
    public static final int pixelSize = 64, globalScale = pixelSize / 16, height = 13, width = 22;
    public final Tag data, mobdrops, generationNoises;
    public MapGenerator mg;
    public Screen tickiveScreen;
    public String selectedSave;
    public MusicBox musichandler;
    public Counter hp_counter;
    public final ImageGenerator imgG;
    public Menuscreen menuscrn;
    public final Inputs inp;

    /**
     * Simple constructor to create the lobby containing Dungeon selection etc.
     */
    public DungeonWorld() {
        // Create a new world with 1408x832 cells with a cell size of 1x1 pixels.
        super(width * pixelSize, height * pixelSize, 1);
        long inittime = System.currentTimeMillis();
        hardEdge = true;
        //collect OS Information
        System.out.println("Fetching os name...");
        runningOS = FileWork.getOS();
        System.out.println("Current os is: "+runningOS);
        System.out.println();
        // Inform the player of the loading process.
        System.out.println("Starting world generation...");
        System.out.println();
        // Generate blackscreen for information and menu screen
        // Load data.nbt.
        System.out.println("Loading data...");
        data = FileWork.getData();
        System.out.println("Loaded data: ");
        data.print();
        System.out.println();
        // Create random.
        System.out.println("Generating random number generator...");
        random = new Random(data.hashCode());
        System.out.println();
        // Load damage Multipliers.
        System.out.println("Loading damage multipliers...");
        dmgMultiplier = FileWork.getDmgMultiplier(data.findNextTag("dmgMultipliers"));
        System.out.println("Loaded types: " + dmgMultiplier.keySet());
        System.out.println();
        //Load mobdrops.
        System.out.println("Loading mobdrops...");
        mobdrops = data.findNextTag("mobdrops");
        mobdrops.print();
        System.out.println();
        //Load loottables.
        System.out.println("Loading loottables...");
        loottables = FileWork.loadAllLoottables(data.findNextTag("loottables"));
        System.out.println("Loaded loottables: " + loottables.keySet());
        System.out.println();
        // Load generation noises
        System.out.println("Loading generationnoises...");
        generationNoises = data.findNextTag("generationNoises");
        generationNoises.print();
        System.out.println();
        // Load weapons.
        System.out.println("Loading weapons...");
        weapons = FileWork.loadAllWeapons(data.findNextTag("weapons"));
        System.out.println("Loaded Weapons: " + weapons.keySet());
        System.out.println();
        // Load all blocks available for map generation.
        System.out.println("Loading blocks...");
        blocks = FileWork.loadAllBlocks(data.findNextTag("blocks"), loottables);
        System.out.println("Loaded Blocks: " + blocks.keySet());
        System.out.println();
        // Load items.
        System.out.println("Loading items...");
        items = FileWork.loadAllItems(data.findNextTag("items"));
        System.out.println("Loaded items: " + items.keySet());
        System.out.println();
        // merge weapons into items
        System.out.println("merging weapons...");
        items.putAll(weapons);
        System.out.println("All droppable items: " + items.keySet());
        System.out.println();
        // Load enemies.
        System.out.println("Loading enemies...");
        enemies = FileWork.loadAllEnemies(data.findNextTag("enemies"), weapons);
        System.out.println("Loaded enemies: " + enemies.keySet());
        System.out.println();
        // Load Image generator.
        System.out.println("Loading Imagegenerator...");
        imgG = new ImageGenerator();
        //imgG.GenerationTest(this);
        System.out.println();
        // Load screens.
        System.out.println("Loading all Screens...");
        screens = FileWork.loadAllScreens(data.findNextTag("screens"), blocks, enemies, imgG, this);
        System.out.println("Loaded screens: " + screens.keySet());
        System.out.println();
        rawscreens = FileWork.loadAllRawScreens(data.findNextTag("screens"));
        //Creating map generator
        System.out.println("Creating map generator...");
        mg = new MapGenerator(random, new ArrayList<>(rawscreens.values()), this, 1);
        System.out.println();

        // Load music.
        System.out.println("Loading musichandler...");
        musichandler = new MusicBox(this);
        System.out.println();
        //Generating key listener
        System.out.println("Loading keylistener...");
        inp = new Inputs(new KeyLayout(), null);
        System.out.println();
        // Load fonts
        System.out.println("Loading fonts...");
        awtfonts = FileWork.loadAwtFonts();
        System.out.println("Loaded fonts: " + awtfonts.keySet());
        System.out.println();
        // Change paint order.
        System.out.println("Changing paintorder");
        setPaintOrder(Counter.class, Projectile.class, Player.class, Enemy.class, Item.class, DeadBody.class, Wall.class, Trap.class, Tile.class);
        System.out.println();

        // Inform the player of the end of the loading process.
        System.out.println("Finished loading.");
        System.out.println("Built world in " + (System.currentTimeMillis()- inittime)/1000.0 +" seconds");
        setTps(100);
        Titlescreen.showTitle(this);
        menuscrn = new Menuscreen(this, inp);
    }

    public void tick() {
    }

    /**
     * Gets the closest Object of a specific class to the calling WorldObj.
     *
     * @param cls    The class elegible for the clostest object. Use null to make all classes elegible.
     * @param caller The calling WorldObj.
     * @return The closest Object of the given class "cls" to the calling WorldObj.
     */
    public <T extends WorldObj> T getClosestObject(Class<T> cls, WorldObj caller) {
        // Retrieve all objects of the class "cls" currently present in the world and save them to a list.
        List<T> worldObjs = objectsOf(cls);
        // Remove the calling WorldObj "me" from the list to avoid using distance to self.
        worldObjs.remove(caller);
        // Save the x and y coordinates of the calling WorldObj for computational efficiency.
        int x = caller.x;
        int y = caller.y;
        // Assume the first WorldObj is the closest one till proven otherwise.
        T closest = worldObjs.get(0);
        double mindistance = getDistance(x, y, closest.x, closest.y);
        double distance = 0;
        // Loop through all WorldObjs in the list WorldObjs to find the closest WorldObj of the class "cls".
        for (T a : worldObjs) {
            // Get the distance between the two WorldObjs.
            distance = getDistance(x, y, a.x, a.y);
            // Check if distance of the points closer than the previously chosen closest one.
            if (distance < mindistance) {
                closest = a;
                mindistance = distance;
            }
        }
        return closest;
    }

    /**
     * Calculates the distance between two points using the Pythagorean equation.
     *
     * @param x1 The x coordinate of the first point.
     * @param y1 The y coordinate of the first point.
     * @param x2 The x coordinate of the second point.
     * @param y2 The y coordinate of the second point.
     * @return The distance of the first Point to the Second point as double.
     */
    private static double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Returns the distance betweeen 2 WorldObjs using the Pythagorean equation.
     *
     * @param a The first WorldObj.
     * @param b The second WorldObj.
     * @return The distance between WorldObj a and WorldObj b as double.
     */
    public static double getDistance(WorldObj a, WorldObj b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    /**
     * Saves the player to the given save slot.
     *
     * @param slot The slot to save the player to.
     */
    public void save(String slot) {
        FileWork.savePlayer(slot, this);
    }

    /**
     * Scales the given image to fit the world.
     *
     * @param img   Image to be scaled.
     * @param scale The scale to scale the image to (in addition to the worlds global scale).
     * @return The scaled image.
     */
    public static AdvancedImage scaleImage(AdvancedImage img, double scale) {
        img = img.scale((int) (img.getWidth() * scale * globalScale), (int) (img.getHeight() * scale * globalScale));
        return img;
    }

    /**
     * Returns the angle fitting the movement Vector of a given WorldObj.
     *
     * @param dir The movement Vector of the player.
     * @return The fitting angle.
     */
    public static int getRotationAngle(double[] dir) {
        double den = (Math.sqrt(Math.pow(dir[0], 2) + Math.pow(dir[1], 2)));
        double cos = dir[0] / den;
        int ret = (int) Math.round(Math.toDegrees(Math.acos(cos)));
        if (dir[1] < 0)
            return 360 - ret;
        else
            return ret;
    }

    /**
     * Compiles a given position.
     *
     * @param pos The position to be compiled.
     * @return The compiled posotion.
     */
    public static int[] compilePosition(int[] pos) {
        int[] ret = new int[pos.length];
        for (int i = 0; i < pos.length; i++) {
            ret[i] = pos[i] * pixelSize + pixelSize / 2;
        }
        return ret;
    }

    /**
     * Returns all Objects NOT of the given class.
     *
     * @param cls The class to be excluded
     * @return The List of Objects.
     */
    public <T extends WorldObj> List<WorldObj> getObjectsExclusive(Class<T> cls) {
        // Get a list of all Objects.
        List<WorldObj> things = objectsOf(null);
        // Get a list of all Objects to be removed.
        List<T> thingsToRemove = objectsOf(cls);
        // Remove all Objects which are to be excluded.
        things.removeAll(thingsToRemove);
        return things;
    }

    /**
     * Returns the new blockstring, depending on the noises from the data.nbt
     *
     * @param b The block noise is gonna be applied to
     * @return The new block with applied noises
     */
    public String applyGenerationNoise(String b) {
        Tag noise = generationNoises.findNextTag(b);
        if (noise == null) return b;
        for (Tag t : (Tag[]) noise.getValue()) {
            if (t.getName() == null) continue;
            if ((Double) t.getValue() > random.nextDouble()) {
                return t.getName();
            }
        }
        return b;
    }

    /**
     * generates a new Random number from a Gaussian function having a custom mean and standardDeviation
     * @param mean the mean of the function
     * @param standardDeviation the standard deviation of the function
     * @return a random number
     */
    public double nextGaussian(double mean, double standardDeviation) {
        return random.nextGaussian()*standardDeviation + mean ;
    }

    /**
     * Generates a Random number in the defined range, having a mean
     * @param min the smallest outcome of the random number
     * @param mean the average outcome
     * @param max the maximum outcome
     * @return a random number
     */
    public double nextGaussian(double min, double mean, double max) {
        return Math.max(Math.min(nextGaussian(mean, ( max - min + 1 ) * 0.25 ), max), min);
    }

    public void startSave(String slot, World upper) {
        if (slot.equals("New Game")) {
            Titlescreen.showNewSave(this, upper);
            return;
        }
        selectedSave = slot;
        System.out.println("Loading save");
        // Load the world.
        System.out.println("Loading save...");
        FileWork.loadPlayer(slot, this, new Player(inp));
        System.out.println();
        // Load counters.
        System.out.println("Loading counters...");
        Counter.load(objectsOf(Player.class).get(0), this);
        System.out.println();
        menuscrn = new Menuscreen(this, inp);
        switchWorld(this);
    }

    public void keyPressed(char key) {
        inp.p.keyPressed(key);
    }
}