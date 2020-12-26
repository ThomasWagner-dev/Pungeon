import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * This is the world...
 * 
 * @author Commentator
 */
public class DungeonWorld extends World
{
    public final HashMap<String, HashMap<String, Double>> dmgMultiplier;
    public final HashMap<String, Weapon> weapons;
    public final HashMap<String, Block> blocks;
    public final HashMap<String, Enemy> enemies;
    public final HashMap<String, Screen> screens;
    public static final int pixelSize = 64, globalScale=pixelSize/16, height = 13, width=22;
    public String activeScreen;
    public int selectedSave;
    public MusicHandler musichandler;
    /**
     * Simple constructor to create the lobby containing Dungeon selection etc.
     */
    public DungeonWorld()
    {    
        // Create a new world with 1425x850 cells with a cell size of 1x1 pixels.
        super(width*pixelSize, height*pixelSize, 1);
        //TODO: put save selection here
        selectedSave = 0;
        
        // Inform the player of the loading process.
        System.out.println("Starting world generation...");
        // Load damage Multipliers.
        System.out.println("Loading damage multipliers...");
        dmgMultiplier = FileWork.getDmgMultiplier();
        System.out.println("Loaded types: "+dmgMultiplier.keySet());
        System.out.println();
        // Load weapons
        System.out.println("Loading weapons...");
        weapons = FileWork.loadAllWeapons();
        System.out.println("Loaded Weapons: "+weapons.keySet());
        System.out.println();
        // Load all blocks availabel for map generation.
        System.out.println("Loading blocks...");
        blocks = FileWork.loadAllBlocks();
        System.out.println("Loaded Blocks: "+blocks.keySet());
        System.out.println();
        // Load enemies
        System.out.println("Loading enemies...");
        enemies = FileWork.loadAllEnemies(weapons);
        System.out.println("Loaded enemies: "+enemies.keySet());
        System.out.println();
        // Load screens
        System.out.println("Loading all Screens");
        screens = FileWork.loadAllScreens(blocks, enemies);
        System.out.println("Loaded screens: "+screens.keySet());
        System.out.println();
        // Load the world.
        System.out.println("Loading save...");
        FileWork.loadPlayer(selectedSave, this);
        System.out.println();
        // Load music
        System.out.println("Loading musichandler...");
        musichandler = new MusicHandler(this);
        System.out.println();
        //Change paint order
        System.out.println("Changing paintorder");
        setPaintOrder(Projectile.class, Player.class, Enemy.class, Wall.class, Trap.class, Tile.class);
        System.out.println();
        // Inform the player of the end of the loading process.
        System.out.println("Finished loading.");
    }
    
    /**
     * Gets the closest Object of a specific class to the calling Actor.
     * 
     * @param cls The class elegible for the clostest object. Use null to make all classes elegible.
     * @param me The calling Actor.
     * 
     * @return The closest Object of the class "cls" to the calling Actor "me".
     * 
     * TODO Get intet of Method
     */
    public Actor getClosestObject(Class cls, Actor caller) {
        // Retrieve all objects of the class "cls" currently present in the world and save them to a list.
        List<Actor> actors = getObjects(cls);
        // Remove the calling actor "me" from the list to avoid using distance to self.
        actors.remove(caller); 
        // Save the x and y coordinates of the calling Actor for computational efficiency.
        int x = caller.getX();
        int y = caller.getY();
        // Assume the first actor is the closest one till proofen otherwise.
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
     * Calculates the distance between two points using the Pythagorean equation
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
    * returns the distance betweeen 2 actors. see method above for further information
    */
    public static double getDistance(Actor a, Actor b) {
        return Math.sqrt(Math.pow(a.getX()-b.getX(),2)+Math.pow(a.getY()-b.getY(),2));
    }
    
    /**
    * saves the player to the given save slot
    */
    public void save(int slot) {
        FileWork.savePlayer(slot, this);
    }
    
    /**
    * scales the given image to fit the world
    * @param img Image to be scaled
    * @param scale The scale it should get scaled to (in addition to the worlds global scale)
    */
    public static GreenfootImage scaleImage(GreenfootImage img, double scale) {
        img.scale((int) (img.getWidth()*scale*globalScale), (int) (img.getHeight()*scale*globalScale));
        return img;
    }
    
    /**
    * gets the angel an Actor has to be rotated to, to fit the movement vector
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
    
    public static void compilePosition(int[] pos) {
        for (int i = 0; i < pos.length; i++) {
            pos[i] = pos[i] * pixelSize + pixelSize/2;
        }
    }
}