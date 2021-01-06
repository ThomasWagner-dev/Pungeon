import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * This is the world...
 */
public class DungeonWorld extends World
{
    //public final HashMap<String, HashMap<String, Double>> dmgMultiplier;
    public final HashMap<String, Weapon> weapons;
    public final HashMap<String, Block> blocks;
    public final HashMap<String, Enemy> enemies;
    public final HashMap<String, Screen> screens;
    public final HashMap<String, Item> items;
    public static final int pixelSize = 64, globalScale=pixelSize/16, height = 13, width=22;
    public final Tag data, dmgMultiplierTag, loottables;
    public Screen activeScreen;
    public int selectedSave;
    public MusicHandler musichandler;
    public Counter hp_counter;
    public final Random random;
    public final ImageGenerator imgG;

    /**
     * Simple constructor to create the lobby containing Dungeon selection etc.
     */
    public DungeonWorld()
    {    
        // Create a new world with 1408x832 cells with a cell size of 1x1 pixels.
        super(width*pixelSize, height*pixelSize, 1);
        //TODO: put save selection here.
        selectedSave = 0;
        
        // Inform the player of the loading process.
        System.out.println("Starting world generation...");
        System.out.println();
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
        dmgMultiplierTag = data.findNextTag("dmgMultipliers");
        dmgMultiplierTag.print();
        //dmgMultiplier = FileWork.getDmgMultiplier();
        //System.out.println("Loaded types: "+dmgMultiplier.keySet());
        System.out.println();
        //Load loottables.
        System.out.println("Loading loottables...");
        loottables = data.findNextTag("loottables");
        loottables.print();
        System.out.println();
        // Load weapons.
        System.out.println("Loading weapons...");
        weapons = FileWork.loadAllWeapons();
        System.out.println("Loaded Weapons: "+weapons.keySet());
        System.out.println();
        // Load all blocks available for map generation.
        System.out.println("Loading blocks...");
        blocks = FileWork.loadAllBlocks();
        System.out.println("Loaded Blocks: "+blocks.keySet());
        System.out.println();
        // Load items.
        System.out.println("Loading items...");
        items = FileWork.loadAllItems();
        System.out.println("Loaded items: "+items.keySet());
        System.out.println();
        // Load enemies.
        System.out.println("Loading enemies...");
        enemies = FileWork.loadAllEnemies(weapons);
        System.out.println("Loaded enemies: "+enemies.keySet());
        System.out.println();
        // Load Image generator.
        System.out.println("Loading Imagegenerator...");
        imgG = new ImageGenerator();
        //imgG.GenerationTest(this);
        System.out.println();
        // Load screens.
        System.out.println("Loading all Screens");
        screens = FileWork.loadAllScreens(blocks, enemies, imgG);
        System.out.println("Loaded screens: "+screens.keySet());
        System.out.println();
        // Load the world.
        System.out.println("Loading save...");
        FileWork.loadPlayer(selectedSave, this);
        System.out.println();
        // Load music.
        System.out.println("Loading musichandler...");
        musichandler = new MusicHandler(this);
        System.out.println();
        // Load counters.
        System.out.println("Loading counters...");
        Counter.load(getObjects(Player.class).get(0), this);
        System.out.println();
        // Change paint order.
        System.out.println("Changing paintorder");
        setPaintOrder(Counter.class, Projectile.class, Player.class, Enemy.class, Item.class, Wall.class, Trap.class, Tile.class);
        System.out.println();
        
        //FileWork.dmgToData(data, dmgMultiplier);
        //FileWork.writeData(data);
        
        // Inform the player of the end of the loading process.
        System.out.println("Finished loading.");
    }
    
    /**
     * Gets the closest Object of a specific class to the calling Actor.
     * 
     * @param cls The class elegible for the clostest object. Use null to make all classes elegible.
     * @param caller The calling Actor.
     * 
     * @return The closest Object of the given class "cls" to the calling Actor.
     */
    public Actor getClosestObject(Class cls, Actor caller) {
        // Retrieve all objects of the class "cls" currently present in the world and save them to a list.
        List<Actor> actors = getObjects(cls);
        // Remove the calling actor "me" from the list to avoid using distance to self.
        actors.remove(caller); 
        // Save the x and y coordinates of the calling Actor for computational efficiency.
        int x = caller.getX();
        int y = caller.getY();
        // Assume the first actor is the closest one till proven otherwise.
        Actor closest = actors.get(0);
        double mindistance = getDistance(x, y, closest.getX(), closest.getY());
        double distance = 0;
        // Loop through all Actors in the list actors to find the closest actor of the class "cls".
        for (Actor a : actors) { 
            // Get the distance between the two Actors.
            distance = getDistance(x, y, a.getX(), a.getY()); 
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
     * 
     * @return The distance of the first Point to the Second point as double.
     */
    private static double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }
    
    /**
    * Returns the distance betweeen 2 actors using the Pythagorean equation.
     *
     * @param a The first Actor.
     * @param b The second Actor.
     *
     * @return The distance between Actor a and Actor b as double.
    */
    public static double getDistance(Actor a, Actor b) {
        return Math.sqrt(Math.pow(a.getX()-b.getX(),2)+Math.pow(a.getY()-b.getY(),2));
    }
    
    /**
    * Saves the player to the given save slot.
     *
     * @param slot The slot to save the player to.
    */
    public void save(int slot) {
        FileWork.savePlayer(slot, this);
    }
    
    /**
    * Scales the given image to fit the world.
     *
    * @param img Image to be scaled.
    * @param scale The scale to scale the image to (in addition to the worlds global scale).
     *
     * @return The scaled image.
    */
    public static GreenfootImage scaleImage(GreenfootImage img, double scale) {
        img.scale((int) (img.getWidth()*scale*globalScale), (int) (img.getHeight()*scale*globalScale));
        return img;
    }
    
    /**
     * Returns the angle fitting the movement Vector of a given Actor.
     *
     * @param dir The movement Vector of the player.
     *
     * @return The fitting angle.
    */
    public static int getRotationAngle(double[] dir) {
        double den = (Math.sqrt(Math.pow(dir[0], 2) + Math.pow(dir[1], 2)));
        double cos = dir[0]/den;
        int ret = (int) Math.round(Math.toDegrees(Math.acos(cos)));
        if (dir[1] < 0)
            return 360-ret;
        else
            return ret;
    }

    /**
     * Compiles a given position.
     *
     * @param pos The position to be compiled.
     *
     * @return The compiled posotion.
     */
    public static int[] compilePosition(int[] pos) {
        int[] ret = new int[pos.length];
        for (int i = 0; i < pos.length; i++) {
            ret[i] = pos[i] * pixelSize + pixelSize/2;
        }
        return ret;
    }

    /**
     * Returns all Objects NOT of the given class.
     *
     * @param cls The class to be excluded
     *
     * @return The List of Objects.
     */
    public List<Actor> getObjectsExclusive(Class cls) {
        // Get a list of all Objects.
        List<Actor> things = getObjects(null);
        // Get a list of all Objects to be removed.
        List<Actor> thingsToRemove = getObjects(cls);
        // Remove all Objects which are to be excluded.
        things.removeAll(thingsToRemove);
        return things;
    }
}